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

package org.ppollack.symphony.faqbot.entities;

import java.util.LinkedHashMap;

public class FaqbotConversation {

  private ISymphonyUser user;
  private LinkedHashMap<FaqbotRequest, FaqbotResponse> requestSequence
      = new LinkedHashMap<>();

  private boolean finished;

  public FaqbotConversation(ISymphonyUser user) {
    this.user = user;
  }

  public ISymphonyUser getUser() {
    return user;
  }

  public LinkedHashMap<FaqbotRequest, FaqbotResponse> getRequestSequence() {
    return requestSequence;
  }

  public FaqbotRequest getLastRequest() {
    FaqbotRequest request = null;
    for (FaqbotRequest req : requestSequence.keySet()) {
      request = req;
    }
    return request;
  }

  public FaqbotResponse getLastResponse() {
    return requestSequence.get(getLastRequest());
  }

  public void setFinished(boolean finished) {
    this.finished = finished;
  }

  public boolean isFinished() {
    if (finished) { return true; }

    FaqbotRequest lastRequest = getLastRequest();
    finished = lastRequest != null && lastRequest.getType().isConversationEnder();
    return finished;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("FaqbotConversation{");
    sb.append("user=").append(user);
    sb.append(", requestSequence=").append(requestSequence);
    sb.append(", finished=").append(finished);
    sb.append('}');
    return sb.toString();
  }
}
