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

import org.ppollack.symphony.faqbot.entities.FaqbotConversation;
import org.ppollack.symphony.faqbot.entities.FaqbotRequest;
import org.ppollack.symphony.faqbot.entities.FaqbotRequestType;
import org.ppollack.symphony.faqbot.entities.FaqbotResponse;
import org.ppollack.symphony.faqbot.entities.FaqbotUserResponseType;
import org.ppollack.symphony.faqbot.processors.ProcessorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RequestClassifier implements IRequestClassifier {

  private static final Logger LOG = LoggerFactory.getLogger(RequestClassifier.class);

  @Override
  public FaqbotRequestType classifyRequest(FaqbotConversation conversation, String messageContent) {

    messageContent = messageContent.trim();

    if (messageContent.equalsIgnoreCase("scoreboard")) {
      return FaqbotRequestType.SCOREBOARD;
    }

    if (messageContent.equalsIgnoreCase("reset")) {
      return FaqbotRequestType.RESET;
    }

    if (messageContent.equalsIgnoreCase("help")) {
      return FaqbotRequestType.HELP;
    }

    FaqbotRequestType requestType = null;
    FaqbotRequest lastRequest = conversation.getLastRequest();
    FaqbotResponse lastResponse = conversation.getLastResponse();

    if (lastRequest != null) {
      if (lastRequest.getType().equals(FaqbotRequestType.QUESTION_SEARCH)) {
        requestType = lastRequestWasQuestionSearch(messageContent, requestType, lastResponse);
      }
      else if (lastRequest.getType().equals(FaqbotRequestType.QUESTION_LOAD)) {
        requestType = lastRequestWasQuestionLoad(messageContent, requestType);
      }
    }

    if (requestType == null) {
      LOG.info("failed to classify request type; conversation={}, requestMsg={}", conversation, messageContent);
    }
    return requestType;
  }

  private FaqbotRequestType lastRequestWasQuestionLoad(String messageContent, FaqbotRequestType requestType) {
    if (FaqbotUserResponseType.VOTE.extractResponseOption(messageContent) != null) {
      requestType = FaqbotRequestType.ANSWER_VOTE;
    } else if (FaqbotUserResponseType.NEW_ANSWER.extractResponseOption(messageContent) != null) {
      requestType = FaqbotRequestType.ANSWER_ADD;
    }
    return requestType;
  }

  private FaqbotRequestType lastRequestWasQuestionSearch(String messageContent,
      FaqbotRequestType requestType, FaqbotResponse lastResponse) {
    Integer selection = FaqbotUserResponseType.SELECT.extractResponseOption(messageContent);
    if (selection != null) {
      selection--;  // offset, since we are really 0-based but we present things as 1-based for users
      requestType = FaqbotRequestType.QUESTION_LOAD;
    } else {
      if (FaqbotUserResponseType.NEW_QUESTION.extractResponseOption(messageContent) != null) {
        requestType = FaqbotRequestType.QUESTION_ADD;
      }
    }
    return requestType;
  }

}
