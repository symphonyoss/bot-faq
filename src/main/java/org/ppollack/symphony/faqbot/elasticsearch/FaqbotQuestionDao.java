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

import org.elasticsearch.index.query.QueryBuilders;
import org.ppollack.symphony.faqbot.entities.FaqbotQuestion;
import org.ppollack.symphony.faqbot.persistence.IFaqbotQuestionDao;
import org.ppollack.symphony.faqbot.persistence.IPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FaqbotQuestionDao implements IFaqbotQuestionDao {

  private static final Logger LOG = LoggerFactory.getLogger(FaqbotQuestionDao.class);

  private final ElasticsearchTemplate elasticsearchTemplate;
  private final FaqbotQuestionRepository questionRepository;

  @Autowired
  public FaqbotQuestionDao(ElasticsearchTemplate elasticsearchTemplate,
      FaqbotQuestionRepository questionRepository) {
    this.elasticsearchTemplate = elasticsearchTemplate;
    this.questionRepository = questionRepository;
  }

  @Override
  public void save(FaqbotQuestion question) {
    questionRepository.save(new FaqbotQuestionElasticsearch(question));
    LOG.debug("saved " + question);
  }

  @Override
  public FaqbotQuestion getById(String faqbotQuestionId) {
    FaqbotQuestionElasticsearch esQuestion = questionRepository.findOne(faqbotQuestionId);
    return esQuestion == null ? null : esQuestion;
  }

  @Override
  public IPage<FaqbotQuestion> search(String query) {
    SearchQuery searchQuery = new NativeSearchQueryBuilder()
        .withQuery(QueryBuilders.multiMatchQuery(query, "faqtext"))
        .build();
    Page<FaqbotQuestionElasticsearch> page = elasticsearchTemplate.queryForPage(searchQuery,
        FaqbotQuestionElasticsearch.class);

    if (page == null) {
      LOG.warn("spring data elasticsearch returned a null page");
      return null;
    }

    return new FaqbotQuestionSpringDataPage(page);
  }

  private class FaqbotQuestionSpringDataPage implements IPage<FaqbotQuestion> {

    final Page<FaqbotQuestionElasticsearch> page;

    FaqbotQuestionSpringDataPage(Page<FaqbotQuestionElasticsearch> page) {
      this.page = page;
    }

    @Override
    public int getPageNumber() {
      return page.getNumber();
    }

    @Override
    public int getPageSize() {
      return page.getSize();
    }

    @Override
    public long getTotalItems() {
      return page.getTotalElements();
    }

    @Override
    public List<FaqbotQuestion> getItems() {
      List<FaqbotQuestion> items = new ArrayList<>(page.getNumberOfElements());
      for (FaqbotQuestionElasticsearch esQuestion : page.getContent()) {
        items.add(esQuestion);
      }
      return items;
    }
  }
}
