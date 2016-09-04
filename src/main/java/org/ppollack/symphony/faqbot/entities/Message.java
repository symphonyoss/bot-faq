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

import org.json.JSONArray;
import org.json.JSONObject;
import org.ppollack.symphony.faqbot.formatters.MLMessageParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ryan.dsouza on 7/26/16.
 *
 * Message JSON Object response from Symphony --> Must have messageML
 */
public class Message {

  private static final Logger LOG = LoggerFactory.getLogger(Message.class);
  private static final MLMessageParser messageParser = new MLMessageParser();

  private final JSONArray attachments;
  private final String streamId;
  private final Long fromUserId;
  private final String id;
  private final String message;
  private final String timestamp;

  public Message(JSONObject object) {
    this.attachments = object.getJSONArray("attachments");
    this.streamId = object.getString("streamId");
    this.fromUserId = object.getLong("fromUserId");
    this.id = object.getString("id");
    this.timestamp = object.getString("timestamp");

    String messageML = object.getString("message");
    String message = "";
    try {
      messageParser.parseMessage(messageML);
      message = messageParser.getText();
    } catch (Exception exception) {
      LOG.error("Error parsing messageML: " + messageML, exception);
    }
    this.message = message;
  }

  public Message(String id, String streamId, Long fromUserId, String message,
      String timestamp, JSONArray attachments) {
    this.streamId = streamId;
    this.fromUserId = fromUserId;
    this.id = id;
    this.message = message;
    this.timestamp = timestamp;
    this.attachments = attachments;
  }

  public JSONArray getAttachments() {
    return attachments;
  }

  public String getStreamId() {
    return streamId;
  }

  public Long getFromUserId() {
    return fromUserId;
  }

  public String getId() {
    return id;
  }

  public String getMessage() {
    return message;
  }

  public String getTimestamp() {
    return timestamp;
  }

  @Override
  public String toString() {
    return "Message{" +
        "attachments=" + attachments +
        ", streamId='" + streamId + '\'' +
        ", fromUserId=" + fromUserId +
        ", id='" + id + '\'' +
        ", message='" + message + '\'' +
        ", timestamp='" + timestamp + '\'' +
        '}';
  }
}