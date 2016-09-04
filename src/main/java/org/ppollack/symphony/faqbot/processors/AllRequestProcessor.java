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

import org.ppollack.symphony.faqbot.configurations.IConfigurationProvider;
import org.ppollack.symphony.faqbot.entities.FaqbotConversation;
import org.ppollack.symphony.faqbot.entities.FaqbotRequest;
import org.ppollack.symphony.faqbot.entities.FaqbotRequestType;
import org.ppollack.symphony.faqbot.entities.FaqbotResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AllRequestProcessor {

  private final IConfigurationProvider configurationProvider;
  private final AnswerAddProcessor answerAddProcessor;
  private final AnswerVoteProcessor answerVoteProcessor;
  private final QuestionAddProcessor questionAddProcessor;
  private final QuestionLoadProcessor questionLoadProcessor;
  private final QuestionSearchProcessor questionSearchProcessor;
  private final ScoreboardProcessor scoreboardProcessor;

  @Autowired
  public AllRequestProcessor(
      IConfigurationProvider configurationProvider,
      AnswerAddProcessor answerAddProcessor,
      AnswerVoteProcessor answerVoteProcessor,
      QuestionAddProcessor questionAddProcessor,
      QuestionLoadProcessor questionLoadProcessor,
      QuestionSearchProcessor questionSearchProcessor,
      ScoreboardProcessor scoreboardProcessor) {
    this.configurationProvider = configurationProvider;
    this.answerAddProcessor = answerAddProcessor;
    this.answerVoteProcessor = answerVoteProcessor;
    this.questionAddProcessor = questionAddProcessor;
    this.questionLoadProcessor = questionLoadProcessor;
    this.questionSearchProcessor = questionSearchProcessor;
    this.scoreboardProcessor = scoreboardProcessor;
  }

  public IRequestProcessor get(FaqbotRequestType type) {
    switch (type) {
      case ANSWER_ADD:
        return answerAddProcessor;
      case ANSWER_VOTE:
        return answerVoteProcessor;
      case QUESTION_ADD:
        return questionAddProcessor;
      case QUESTION_LOAD:
        return questionLoadProcessor;
      case QUESTION_SEARCH:
        return questionSearchProcessor;
      case SCOREBOARD:
        return scoreboardProcessor;
      case RESET:
        return (conversation, request) -> new FaqbotResponse(request, "Ok, let's start over.", null);
      case HELP:
        return (conversation, request) -> new FaqbotResponse(request, configurationProvider.getHelpText(), null);
    }

    throw new RuntimeException("no request processor found for type " + type);
  }

}
