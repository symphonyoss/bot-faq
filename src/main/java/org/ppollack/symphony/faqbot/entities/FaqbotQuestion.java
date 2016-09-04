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

import java.util.ArrayList;
import java.util.List;

public class FaqbotQuestion {

  private String questionId;
  private String faqtext;
  private List<FaqbotAnswer> answers;

  public FaqbotQuestion() {
  }

  public FaqbotQuestion(String questionId, String faqtext) {
    this.questionId = questionId;
    this.faqtext = faqtext;
    answers = new ArrayList<>();
  }

  public String getQuestionId() {
    return questionId;
  }

  public String getFaqtext() {
    return faqtext;
  }

  public List<FaqbotAnswer> getAnswers() {
    return answers;
  }

  public void setQuestionId(String questionId) {
    this.questionId = questionId;
  }

  public void setFaqtext(String faqtext) {
    this.faqtext = faqtext;
  }

  public void setAnswers(List<FaqbotAnswer> answers) {
    this.answers = answers;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("FaqbotQuestion{");
    sb.append("questionId='").append(questionId).append('\'');
    sb.append(", faqtext='").append(faqtext).append('\'');
    sb.append(", answers=").append(answers);
    sb.append('}');
    return sb.toString();
  }
}
