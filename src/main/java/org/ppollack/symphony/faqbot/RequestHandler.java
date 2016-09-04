/*
 *
 *
 * Copyright 2016 Symphony Communication Services, LLC
 *
 * Licensed to Symphony Communication Services, LLC under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */


package org.ppollack.symphony.faqbot;

import org.ppollack.symphony.faqbot.configurations.IConfigurationProvider;
import org.ppollack.symphony.faqbot.entities.FaqbotConversation;
import org.ppollack.symphony.faqbot.entities.FaqbotRequest;
import org.ppollack.symphony.faqbot.entities.FaqbotRequestType;
import org.ppollack.symphony.faqbot.entities.FaqbotResponse;
import org.ppollack.symphony.faqbot.entities.ISymphonyUser;
import org.ppollack.symphony.faqbot.processors.AllRequestProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is responsible for processing requests, which are essentially Symphony
 * messages received by faqbot.
 *
 * This is not a stateless implementation.  Conversations may span back and forth exchanges.
 */

@Component
public class RequestHandler {

  private static final Logger LOG = LoggerFactory.getLogger(RequestHandler.class);

  private final IConfigurationProvider configurationProvider;
  private final IRequestClassifier requestClassifier;

  private final AllRequestProcessor requestProcessor;

  private final Map<Long, FaqbotConversation> userIdToConversation;

  @Autowired
  public RequestHandler(IConfigurationProvider configurationProvider,
      IRequestClassifier requestClassifier, AllRequestProcessor requestProcessor) {
    this.configurationProvider = configurationProvider;
    this.requestClassifier = requestClassifier;
    this.requestProcessor = requestProcessor;
    userIdToConversation = new HashMap<>();
  }

  public FaqbotResponse handleRequest(ISymphonyUser user, String messageContent) {

    FaqbotConversation conversation = null;
    FaqbotRequestType type = null;

    // grab an existing conversation with the user if one exists, else create a new one
    synchronized (this) {
      conversation = userIdToConversation.get(user.getUserId());
      if (conversation == null) {
        conversation = initializeNewConversation(user);
      } else {
        LOG.debug("resuming conversation with user " + user + ", previous request was " +
            conversation.getLastRequest());
      }
    }

    FaqbotResponse response;
    synchronized (conversation) {

      if (type == null) {
        // determine the type of the request, within the context of the current conversation
        type = requestClassifier.classifyRequest(conversation, messageContent);
        if (type == null) {
          // faqbot is not able to understand a message as a response from the user, so start a new conversation
          conversation.setFinished(true);
          discardFinishedConversation(user, conversation);
          conversation = initializeNewConversation(user);
          type = FaqbotRequestType.QUESTION_SEARCH;
        }
      }

      FaqbotRequest request = new FaqbotRequest(messageContent, type);

      // process the request and store the response with it, for reference
      LOG.debug("processing request for user " + user + ": " + request);
      try {
        response = requestProcessor.get(type).process(conversation, request);
        conversation.getRequestSequence().put(request, response);
      } catch (RequestProcessingException e) {
        LOG.error("something went wrong while processing a request", e);
        String msg = e.getMessage() == null
            ? "I'm sorry, I didn't understand that.  Please try again."
            : e.getMessage();
        response = new FaqbotResponse(request, msg, null);
      }

      LOG.debug("finished processing request for user " + user + ", response: " + response);

      // if the response finishes the conversation, then discard it
      discardFinishedConversation(user, conversation);
    }

    return response;
  }

  private FaqbotConversation initializeNewConversation(ISymphonyUser user) {
    FaqbotConversation conversation;
    conversation = new FaqbotConversation(user);
    userIdToConversation.put(user.getUserId(), conversation);
    LOG.debug("starting new conversation with user " + user);
    return conversation;
  }

  private boolean discardFinishedConversation(ISymphonyUser user, FaqbotConversation conversation) {
    if (conversation.isFinished()) {
      userIdToConversation.remove(user.getUserId());
      LOG.debug("finished conversation with user " + user + ": " + conversation);
      return true;
    }
    return false;
  }

}
