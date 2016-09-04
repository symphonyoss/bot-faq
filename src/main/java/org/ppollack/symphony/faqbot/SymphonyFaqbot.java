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

import org.ppollack.symphony.faqbot.clients.ISymphonyClient;
import org.ppollack.symphony.faqbot.configurations.IConfigurationProvider;
import org.ppollack.symphony.faqbot.entities.FaqbotResponse;
import org.ppollack.symphony.faqbot.entities.ISymphonyMessage;
import org.ppollack.symphony.faqbot.entities.ISymphonyUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * This class listens for incoming Symphony messages and responds to them.
 */
@Component
public class SymphonyFaqbot {

  private static final Logger LOG = LoggerFactory.getLogger(SymphonyFaqbot.class);

  private final IConfigurationProvider configurationProvider;
  private final ISymphonyClient symphonyClient;
  private final RequestExecutor requestExecutor;

  @Autowired
  public SymphonyFaqbot(IConfigurationProvider configurationProvider,
      ISymphonyClient symphonyClient,
      RequestExecutor requestExecutor) {
    this.configurationProvider = configurationProvider;
    this.symphonyClient = symphonyClient;
    this.requestExecutor = requestExecutor;
  }

  public void start() {
    LOG.info("initializing symphony client...");
    initializeSymphonyClient();
    LOG.info("initialized symphony client");

    while (true) {
      LOG.info("sending request for symphony messages...");
      List<ISymphonyMessage> messages = symphonyClient.getSymphonyMessages();
      LOG.info("received {} symphony messages to process", (messages == null ? 0 : messages.size()));
      for (ISymphonyMessage message : messages) {
        handleIncomingMessage(message);
      }
    }
  }

  private void initializeSymphonyClient() {
    symphonyClient.authenticate();
  }

  public void handleIncomingMessage(ISymphonyMessage symphonyMessage) {

    String messageContent = symphonyMessage.getMessageText();
    ISymphonyUser author = symphonyMessage.getSymphonyUser();

    if (messageContent.toLowerCase().contains("hello")) {
      String messageToSend = "Hello " + author.getDisplayName() + "!";
      symphonyClient.sendMessage(symphonyMessage.getStreamId(), messageToSend);
      return;
    }

    LOG.info("processing message [" + messageContent + "] from user [" + author + "]...");
    Future<FaqbotResponse> responseFuture = requestExecutor.execute(author, messageContent);

    try {
      FaqbotResponse response = responseFuture.get(
          configurationProvider.getRequestProcessingTimeout(), TimeUnit.MILLISECONDS);
      sendResponse(symphonyMessage, response);
      LOG.info("sending response to [" + messageContent + "] from user [" + author
          + "], response is [" + response.getResponseText() + "]");

    } catch (Exception e) {
      LOG.error("failed to process message [" + messageContent + "] from user [" + author + "]", e);
      // TODO consider sending a message back to the author
    }
  }

  private void sendResponse(ISymphonyMessage symphonyMessage, FaqbotResponse response) {
    symphonyClient.sendMessage(symphonyMessage.getStreamId(), response.getResponseText());
  }
}