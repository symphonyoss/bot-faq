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
import org.ppollack.symphony.faqbot.entities.FaqbotResponse;
import org.ppollack.symphony.faqbot.entities.ISymphonyUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
public class RequestExecutor {

  private final ExecutorService executorService;
  private final RequestHandler requestHandler;

  @Autowired
  public RequestExecutor(IConfigurationProvider configProvider, RequestHandler requestHandler) {
    this.requestHandler = requestHandler;
    executorService = Executors.newFixedThreadPool(configProvider.getNumWorkerThreads());
  }

  public Future<FaqbotResponse> execute(ISymphonyUser user, String messageContent) {
    return executorService.submit(() -> requestHandler.handleRequest(user, messageContent));
  }
}
