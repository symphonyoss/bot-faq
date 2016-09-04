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

package org.ppollack.symphony.faqbot.elasticsearch;

import org.ppollack.symphony.faqbot.entities.FaqbotQuestion;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;

/**
 * This subclass exists so that the parent class can remain pure of any dependency on Spring.
 */
@Document(indexName = "questions", shards = 1, replicas = 0, type = "question-type")
@Mapping(mappingPath = "/question-index-mappings.json")
public class FaqbotQuestionElasticsearch extends FaqbotQuestion {
  public FaqbotQuestionElasticsearch() {
  }

  public FaqbotQuestionElasticsearch(String id, String text) {
    super(id, text);
  }

  public FaqbotQuestionElasticsearch(FaqbotQuestion question) {
    super(question.getQuestionId(), question.getFaqtext());
    setAnswers(question.getAnswers());
  }

  @Override
  @Id
  public String getQuestionId() {
    return super.getQuestionId();
  }

}
