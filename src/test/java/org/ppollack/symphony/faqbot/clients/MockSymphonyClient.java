package org.ppollack.symphony.faqbot.clients;

import com.symphony.api.agent.model.V2Message;
import com.symphony.api.pod.model.Stream;
import com.symphony.api.pod.model.User;
import com.symphony.api.pod.model.V2RoomDetail;

import org.ppollack.symphony.faqbot.formatters.MessageML;
import org.ppollack.symphony.faqbot.entities.ISymphonyMessage;
import org.ppollack.symphony.faqbot.entities.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MockSymphonyClient implements ISymphonyClient {

  private static final Logger LOG = LoggerFactory.getLogger(MockSymphonyClient.class);

  private Map<String, List<String>> sentMessagesRoomToText = new HashMap<>();

  @Override
  public void authenticate() {
    LOG.info("authenticated");
  }

  @Override
  public List<V2RoomDetail> getRoomsForSearchQuery(String query) {
    return null;
  }

  @Override
  public V2RoomDetail getRoomForSearchQuery(String query) {
    return null;
  }

  @Override
  public V2Message sendMessage(String roomID, MessageML messageML) {
    LOG.info("sending to roomId " + roomID + " messageMl " + messageML);
    return null;
  }

  @Override
  public V2Message sendMessage(String roomId, String text) {
    LOG.info("sending to roomId " + roomId + " string " + text);
    if (sentMessagesRoomToText.get(roomId) == null) {
      sentMessagesRoomToText.put(roomId, new ArrayList<>());
    }
    sentMessagesRoomToText.get(roomId).add(text);
    return null;
  }

  @Override
  public V2Message sendMessage(V2RoomDetail roomDetail, MessageML messageML) {
    LOG.info("sending to roomDetail " + roomDetail + " messageMl " + messageML);
    return null;
  }

  @Override
  public List<Message> getMessages() {
    return null;
  }

  @Override
  public ISymphonyMessage getSymphonyMessage() {
    return null;
  }

  @Override
  public List<ISymphonyMessage> getSymphonyMessages() {
    return null;
  }

  @Override
  public User getUserForEmailAddress(String emailAddress) {
    return null;
  }

  @Override
  public Stream getStreamWithUser(User user) {
    return null;
  }

  @Override
  public Stream getStreamWithUsers(List<User> users) {
    return null;
  }

  @Override
  public Stream getStreamWithUsers(User... users) {
    return null;
  }
}
