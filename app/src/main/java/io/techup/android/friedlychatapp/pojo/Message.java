package io.techup.android.friedlychatapp.pojo;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ceosilvajr on 13/06/16.
 */
@IgnoreExtraProperties public class Message {

  private String messageId;

  private String uid;

  private String message;

  private String senderName;

  private String senderPhotoUrl;

  @Exclude private boolean isSender;

  public Message() {
    super();
  }

  public Message(String uid, String message, String senderName, String senderPhotoUrl) {
    super();
    this.uid = uid;
    this.message = message;
    this.senderName = senderName;
    this.senderPhotoUrl = senderPhotoUrl;
  }

  public String getMessageId() {
    return messageId;
  }

  public void setMessageId(String messageId) {
    this.messageId = messageId;
  }

  public String getUid() {
    return uid;
  }

  public void setUid(String uid) {
    this.uid = uid;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getSenderName() {
    return senderName;
  }

  public void setSenderName(String senderName) {
    this.senderName = senderName;
  }

  public String getSenderPhotoUrl() {
    return senderPhotoUrl;
  }

  public void setSenderPhotoUrl(String senderPhotoUrl) {
    this.senderPhotoUrl = senderPhotoUrl;
  }

  public boolean isSender() {
    return isSender;
  }

  public void setSender(boolean sender) {
    isSender = sender;
  }

  @Override public String toString() {
    return "Message{" +
        "messageId='" + messageId + '\'' +
        ", uid='" + uid + '\'' +
        ", message='" + message + '\'' +
        ", senderName='" + senderName + '\'' +
        ", senderPhotoUrl='" + senderPhotoUrl + '\'' +
        ", isSender=" + isSender +
        '}';
  }

  @Exclude public Map<String, Object> toMap() {
    HashMap<String, Object> result = new HashMap<>();
    result.put("messageId", messageId);
    result.put("uid", uid);
    result.put("message", message);
    result.put("senderName", senderName);
    result.put("senderPhotoUrl", senderPhotoUrl);
    return result;
  }
}
