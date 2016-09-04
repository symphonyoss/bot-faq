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

import java.util.Collections;
import java.util.List;

// immutable
public class FaqbotResponse {

  private final FaqbotRequest request;
  private final String responseText;
  private final List<FaqbotQuestion> responseQuestions;

  public FaqbotResponse(FaqbotRequest request, String responseText,
      List<FaqbotQuestion> responseQuestions) {
    this.request = request;
    this.responseText = responseText;
    this.responseQuestions = responseQuestions;
  }

  public FaqbotRequest getRequest() {
    return request;
  }

  public String getResponseText() {
    return responseText;
  }

  public List<FaqbotQuestion> getResponseQuestions() {
    return Collections.unmodifiableList(responseQuestions);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("FaqbotResponse{");
    sb.append("request=").append(request);
    sb.append(", responseText='").append(responseText).append('\'');
    sb.append(", responseQuestions=").append(responseQuestions);
    sb.append('}');
    return sb.toString();
  }
}
