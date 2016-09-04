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

import org.ppollack.symphony.faqbot.entities.FaqbotConversation;
import org.ppollack.symphony.faqbot.entities.FaqbotQuestion;
import org.ppollack.symphony.faqbot.entities.FaqbotRequest;
import org.ppollack.symphony.faqbot.entities.FaqbotResponse;
import org.ppollack.symphony.faqbot.persistence.IFaqbotQuestionDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class QuestionAddProcessor implements IRequestProcessor {

  private static final Logger LOG = LoggerFactory.getLogger(QuestionAddProcessor.class);

  private static final String NEW_QUESTION_PREFIX = "new question:";
  @Autowired
  private IFaqbotQuestionDao faqbotQuestionDao;

  @Override
  public FaqbotResponse process(FaqbotConversation conversation, FaqbotRequest request) {
    String questionId = UUID.randomUUID().toString();

    String questionText = request.getQuery();
    if (questionText.startsWith(NEW_QUESTION_PREFIX)) {
      questionText = questionText.substring(NEW_QUESTION_PREFIX.length()).trim();
    }

    FaqbotQuestion newQuestion = new FaqbotQuestion(questionId, questionText);
    faqbotQuestionDao.save(newQuestion);

    FaqbotResponse faqbotResponse = new FaqbotResponse(request,
        String.format("Added question: %s\nYou will be notified when it is answered",
            newQuestion.getFaqtext()), null);
    return faqbotResponse;
  }
}