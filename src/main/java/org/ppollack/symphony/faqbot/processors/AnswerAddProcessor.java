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

import java.util.UUID;

@Component
public class AnswerAddProcessor implements IRequestProcessor {

  private static final String NEW_ANSWER_PREFIX = "new answer:";

  @Autowired
  private IFaqbotQuestionDao faqbotQuestionDao;

  @Override
  public FaqbotResponse process(FaqbotConversation conversation, FaqbotRequest request) {
    String answerId = UUID.randomUUID().toString();
    String answerText = request.getQuery().trim();
    String temp = answerText.toLowerCase();
    if (temp.startsWith(NEW_ANSWER_PREFIX)) {
      answerText = answerText.substring(NEW_ANSWER_PREFIX.length()).trim();
    }

    if (answerText.isEmpty()) {
      throw new RequestProcessingException("Whoops, it looks like you tried to add an empty answer. "
          + "Please enter a non-empty answer.");
    }

    FaqbotAnswer newAnswer = new FaqbotAnswer(answerId, answerText);
    FaqbotQuestion question = conversation.getLastResponse().getResponseQuestions().get(0);
    question.getAnswers().add(newAnswer);
    faqbotQuestionDao.save(question);

    FaqbotResponse faqbotResponse = new FaqbotResponse(request,
        "Added answer: " + newAnswer.getFaqtext(), null);
    return faqbotResponse;
  }

}
