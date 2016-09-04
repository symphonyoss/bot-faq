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

package org.ppollack.symphony.faqbot.clients;

import com.symphony.api.agent.model.V2Message;
import com.symphony.api.pod.model.Stream;
import com.symphony.api.pod.model.User;
import com.symphony.api.pod.model.V2RoomDetail;

import org.ppollack.symphony.faqbot.formatters.MessageML;
import org.ppollack.symphony.faqbot.entities.ISymphonyMessage;
import org.ppollack.symphony.faqbot.entities.Message;

import java.util.List;

/**
 * Defines the functionality of a Symphony client
 */

public interface ISymphonyClient {

  /** For authentication */
  void authenticate();

  /** For searching rooms */
  List<V2RoomDetail> getRoomsForSearchQuery(String query);
  V2RoomDetail getRoomForSearchQuery(String query);

  /** For sending messages */
  V2Message sendMessage(String roomID, MessageML messageML);
  V2Message sendMessage(String roomID, String text);
  V2Message sendMessage(V2RoomDetail roomDetail, MessageML messageML);

  /** For long polling for messages */
  List<Message> getMessages();
  ISymphonyMessage getSymphonyMessage();
  List<ISymphonyMessage> getSymphonyMessages();

  /** For finding users */
  User getUserForEmailAddress(String emailAddress);

  /** For finding/creating streams related to users */
  Stream getStreamWithUser(User user);
  Stream getStreamWithUsers(List<User> users);
  Stream getStreamWithUsers(User... users);
}