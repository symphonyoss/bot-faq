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

public class ProcessorUtils {

  public static int extractSelection(String text) {
    String[] tokens = text.split("\\s+");
    Integer selection = null;
    for (String token : tokens) {
      try {
        selection = Integer.parseInt(token.trim()) - 1;
        break;
      } catch (NumberFormatException e) {
        // consume
      }
    }

    if (selection == null) {
      throw new RequestProcessingException("Please select a question by entering its number.");
    }
    return selection;
  }
}
