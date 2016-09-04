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

package org.ppollack.symphony.faqbot.entities;

/**
 * This enum is intended to help determine what type of response a user has provided to a
 * message sent by faqbot.
 */
public enum FaqbotUserResponseType {
  YESNO {
    @Override
    public Boolean extractResponseOption(String text) {
      if (text != null && !text.isEmpty()) {
        if (text.toLowerCase().startsWith(YES)) { return true; }
        if (text.toLowerCase().startsWith(NO)) { return false; }
      }
      return null;
    }
  },
  SELECT {
    @Override
    public Integer extractResponseOption(String text) {
      if (text != null && !text.isEmpty()) {
        String numberChoice = extractLeadingNumber(text);
        if (numberChoice != null) {
          try {
            return Integer.parseInt(numberChoice);
          } catch (NumberFormatException e) {}
        }
      }
      return null;
    }
  },
  VOTE {
    @Override
    public Boolean extractResponseOption(String text) {
      if (text != null && !text.isEmpty()) {
        if (text.toLowerCase().startsWith(UPVOTE)) { return true; }
        if (text.toLowerCase().startsWith(DOWNVOTE)) { return false; }
      }
      return null;
    }
  },
  NEW_QUESTION {
    @Override
    public String extractResponseOption(String text) {
      return returnTextIfStartsWithName(this, text);
    }
  },
  NEW_ANSWER {
    @Override
    public String extractResponseOption(String text) {
      return returnTextIfStartsWithName(this, text);
    }
  };

  private static final String YES = "yes";
  private static final String NO = "no";
  private static final String UPVOTE = "upvote";
  private static final String DOWNVOTE = "downvote";

  public abstract <T> T extractResponseOption(String text);

  private static String extractLeadingNumber(String text) {
    StringBuilder sb = new StringBuilder();
    for (char c : text.toCharArray()) {
      if (c >= '0' || c <= '9') {
        sb.append(c);
      } else {
        break;
      }
    }
    return sb.length() == 0 ? null : sb.toString();
  }

  private static String returnTextIfStartsWithName(FaqbotUserResponseType type, String text) {
    String prefix = type.name().toLowerCase().replace('_', ' ');
    if (text != null && !text.isEmpty() && text.trim().toLowerCase().startsWith(prefix)) {
      return text;
    }
    return null;
  }
}
