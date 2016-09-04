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

package org.ppollack.symphony.faqbot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ppollack.symphony.faqbot.elasticsearch.FaqbotQuestionElasticsearch;
import org.ppollack.symphony.faqbot.entities.FaqbotQuestion;
import org.ppollack.symphony.faqbot.persistence.IFaqbotQuestionDao;
import org.ppollack.symphony.faqbot.persistence.IPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring-appcontext-test.xml"})
@Configuration
public class AskFirstQuestionTest {

  private static final Logger LOG = LoggerFactory.getLogger(AskFirstQuestionTest.class);

  @Autowired
  private SymphonyFaqbot faqbot;

  @Autowired
  private ElasticsearchTemplate template;

  @Autowired
  private IFaqbotQuestionDao questionDao;

  @Before
  public void setup() {
    template.deleteIndex(FaqbotQuestionElasticsearch.class);
    template.createIndex(FaqbotQuestionElasticsearch.class);
    template.putMapping(FaqbotQuestionElasticsearch.class);
  }

  @Test
  public void test() {
    String username = "jonsnow";
    faqbot.handleIncomingMessage(new MockSymphonyMessage(username, "why do you read so many books?"));
    faqbot.handleIncomingMessage(new MockSymphonyMessage(username, "yes"));

    searchAndAssertNumResults("books", 1);
    searchAndAssertNumResults("wee", 0);
    searchAndAssertNumResults("why read", 1);
    searchAndAssertNumResults("gooks", 0);
  }

  private void searchAndAssertNumResults(String query, int numResults) {
    IPage<FaqbotQuestion> questionsPage = questionDao.search(query);

    assertNotNull(questionsPage);
    assertEquals(numResults, questionsPage.getTotalItems());
    assertEquals(questionsPage.getItems().size(), questionsPage.getTotalItems());
  }

}
