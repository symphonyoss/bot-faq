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
import org.ppollack.symphony.faqbot.persistence.IFaqbotQuestionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AnswerVoteProcessor implements IRequestProcessor {

  @Autowired
  private IFaqbotQuestionDao faqbotQuestionDao;

  @Override
  public FaqbotResponse process(FaqbotConversation conversation, FaqbotRequest request) {

    int selection = ProcessorUtils.extractSelection(request.getQuery());

    FaqbotQuestion question = conversation.getLastResponse().getResponseQuestions().get(0);

    if (selection < 0 || selection >= question.getAnswers().size()) {
      throw new RequestProcessingException("Please select a question by entering its number, from "
          + "1 to " + question.getAnswers().size());
    }

    FaqbotAnswer answer = question.getAnswers().get(selection);
    StringBuilder responseText = new StringBuilder();

    if (request.getQuery().toLowerCase().contains("downvote")) {
      answer.setDownvotes(answer.getDownvotes()+1);
      responseText.append("Downvoted answer: " + answer.getFaqtext());
    } else if (request.getQuery().toLowerCase().contains("upvote")) {
      answer.setUpvotes(answer.getUpvotes()+1);
      responseText.append("Upvoted answer: " + answer.getFaqtext());
    }

    faqbotQuestionDao.save(question);

    FaqbotResponse faqbotResponse = new FaqbotResponse(request, responseText.toString(), null);
    return faqbotResponse;
  }

}
