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


package org.ppollack.symphony.faqbot.processors;

import org.ppollack.symphony.faqbot.RequestProcessingException;
import org.ppollack.symphony.faqbot.entities.FaqbotAnswer;
import org.ppollack.symphony.faqbot.entities.FaqbotConversation;
import org.ppollack.symphony.faqbot.entities.FaqbotQuestion;
import org.ppollack.symphony.faqbot.entities.FaqbotRequest;
import org.ppollack.symphony.faqbot.entities.FaqbotResponse;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class QuestionLoadProcessor implements IRequestProcessor {

  @Override
  public FaqbotResponse process(FaqbotConversation conversation, FaqbotRequest request) {

    int selectedQuestionIndex = ProcessorUtils.extractSelection(request.getQuery());

    FaqbotResponse lastResponse = conversation.getRequestSequence().get(conversation.getLastRequest());

    if (selectedQuestionIndex < 0 || selectedQuestionIndex >= lastResponse.getResponseQuestions().size()) {
      throw new RequestProcessingException("Please select a question by entering its number, from "
          + "1 to " + lastResponse.getResponseQuestions().size());
    }

    FaqbotQuestion selectedQuestion = lastResponse.getResponseQuestions().get(selectedQuestionIndex);
    StringBuilder responseText = new StringBuilder();
    responseText.append(selectedQuestion.getFaqtext());

    if (selectedQuestion.getAnswers() != null && !selectedQuestion.getAnswers().isEmpty()) {
      // there are already some answers to this question, so let's present them to the user
      int i = 1;
      for (FaqbotAnswer answer : selectedQuestion.getAnswers()) {
        responseText.append('\n').append(i++).append(". ").append(format(answer));
      }
    } else {
      // there are no answers to this question yet
      responseText.append("\nThis question has not been answered yet.");
    }

    responseText.append("\nTo add a new answer, respond with \"new answer: \" (without quotes) followed by an answer.");
    responseText.append("\nTo upvote or downvote an existing answer, respond with \"upvote #\" (without quotes), "
        + "where # indicates the number of the answer you're voting on.");

    return new FaqbotResponse(request, responseText.toString(), Collections.singletonList(selectedQuestion));
  }

  private String format(FaqbotAnswer answer) {
    return String.format("%s (%s upvotes / %s downvotes)", answer.getFaqtext(),
        answer.getUpvotes(), answer.getDownvotes());
  }

}
