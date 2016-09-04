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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * This class starts the application that is faqbot.  This application depends on Elasticsearch,
 * which is expected to be run via the Docker Compose file in this project.
 */
public class SymphonyFaqbotApp {

  private static final Logger LOG = LoggerFactory.getLogger(SymphonyFaqbotApp.class);

  public static void main(String[] args) {
    long start = System.currentTimeMillis();
    LOG.info(SymphonyFaqbotApp.class.getSimpleName() + " starting");

    // this will initialize SymphonyFaqbot and all of its dependencies
    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring-appcontext.xml");
    context.getBean(SymphonyFaqbot.class).start();

    LOG.info(SymphonyFaqbotApp.class.getSimpleName()
        + " started in " + (System.currentTimeMillis() - start) + "ms");
  }
}
