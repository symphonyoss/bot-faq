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
import org.ppollack.symphony.faqbot.persistence.IPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * This processor can generate two types of responses.
 *
 * 1. Please select one of the following (numeric): In this case the user is expected to respond with an
 *    integer that indicates the selection; this leads to either QUESTION_LOAD or QUESTION_ADD_PROMPT
 * 2. No questions found, would you like to add a new one? (yes/no): In this case the user is expected to
 *    respond with yes or no, and thus this leads to QUESTION_ADD_PROMPT
 */
@Component
public class QuestionSearchProcessor implements IRequestProcessor {

  private static final Logger LOG = LoggerFactory.getLogger(QuestionSearchProcessor.class);
  private static final int MAX_QUESTION_LENGTH = 100;

  @Autowired
  private IFaqbotQuestionDao faqbotQuestionDao;

  @Override
  public FaqbotResponse process(FaqbotConversation conversation, FaqbotRequest request) {

    // search for previously asked questions
    IPage<FaqbotQuestion> storedQuestions = faqbotQuestionDao.search(request.getQuery());

    String responseText;
    List<FaqbotQuestion> responseQuestions = null;
    if(storedQuestions != null && storedQuestions.getItems() != null && !storedQuestions.getItems().isEmpty() ) {
      // we found some questions! let's present them to the user
      responseQuestions = storedQuestions.getItems();

      StringBuilder sb = new StringBuilder();

      sb.append("I've found a few previously asked questions that might be what you're looking for.\n")
          .append("Please select one of the following by entering its number:\n");

      int counter = 1;
      for(FaqbotQuestion responseQuestion : responseQuestions) {
        String question = responseQuestion.getFaqtext();

        if(question.length() > MAX_QUESTION_LENGTH) {
          question = question.substring(0, MAX_QUESTION_LENGTH - 1);
        }
        sb.append('\n').append(counter++).append(". ").append(question);
      }

      responseText = sb.toString();
    } else {
      // we weren't able to find any questions
      responseText = "No questions matching that criteria were found.";
    }

    responseText += "\nTo add a new question, respond with \"new question: \" (without quotes) followed by the question.";

    return new FaqbotResponse(request, responseText, responseQuestions);
  }
}
