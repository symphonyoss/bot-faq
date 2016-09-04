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

public class FaqbotAnswer {

  private String answerId;
  private String faqtext;
  private int upvotes, downvotes;

  public FaqbotAnswer() {
  }

  public FaqbotAnswer(String answerId, String text) {
    this.answerId = answerId;
    this.faqtext = text;
  }

  public String getAnswerId() {
    return answerId;
  }

  public String getFaqtext() {
    return faqtext;
  }

  public int getUpvotes() {
    return upvotes;
  }

  public int getDownvotes() {
    return downvotes;
  }

  public void setUpvotes(int upvotes) {
    this.upvotes = upvotes;
  }

  public void setDownvotes(int downvotes) {
    this.downvotes = downvotes;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("FaqbotAnswer{");
    sb.append("answerId='").append(answerId).append('\'');
    sb.append(", faqtext='").append(faqtext).append('\'');
    sb.append(", upvotes=").append(upvotes);
    sb.append(", downvotes=").append(downvotes);
    sb.append('}');
    return sb.toString();
  }
}
