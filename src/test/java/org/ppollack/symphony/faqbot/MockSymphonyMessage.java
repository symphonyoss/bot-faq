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

import org.ppollack.symphony.faqbot.entities.ISymphonyMessage;
import org.ppollack.symphony.faqbot.entities.ISymphonyUser;
import org.ppollack.symphony.faqbot.entities.Message;
import org.ppollack.symphony.faqbot.entities.SymphonyUser;

import java.util.Date;

public class MockSymphonyMessage implements ISymphonyMessage {

  ISymphonyUser user;
  Message message;

  public MockSymphonyMessage(String username, String message) {
    this.user = new SymphonyUser(1L, username + "@symphony.com", username);
    String base = String.valueOf(System.currentTimeMillis() % 100);
    this.message = new Message(base, "streamId" + base, user.getUserId(), message,
        new Date().toString(), null);
  }

  @Override
  public ISymphonyUser getSymphonyUser() {
    return user;
  }

  @Override
  public Message getMessage() {
    return message;
  }

  @Override
  public String getStreamId() {
    return message.getStreamId();
  }

  @Override
  public String getAuthorName() {
    return user.getDisplayName();
  }

  @Override
  public String getMessageText() {
    return message.getMessage();
  }
}
