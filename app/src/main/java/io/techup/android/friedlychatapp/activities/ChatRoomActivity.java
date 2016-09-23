package io.techup.android.friedlychatapp.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import io.techup.android.friedlychatapp.R;
import io.techup.android.friedlychatapp.adapters.ConversationAdapter;
import io.techup.android.friedlychatapp.pojo.Message;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

public class ChatRoomActivity extends AppCompatActivity
    implements ChildEventListener, View.OnClickListener {

  private static final String TAG = "ChatRoomActivity";

  private List<Message> messagesList;
  private ConversationAdapter conversationAdapter;
  private DatabaseReference databaseReference;
  private EditText mEditTextMessage;

  @Override protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chat_room);
    initFirebaseDatabase();
    initView();
  }

  private void initFirebaseDatabase() {
    databaseReference = FirebaseDatabase.getInstance().getReference("messages");
    databaseReference.addChildEventListener(this);
  }

  private void initView() {
    final ListView listViewMessage = (ListView) findViewById(R.id.lv_conversations);
    final Button mButtonSendMessage = (Button) findViewById(R.id.btn_send_message);
    mEditTextMessage = (EditText) findViewById(R.id.edt_message);
    messagesList = new ArrayList<>();
    conversationAdapter = new ConversationAdapter(this, messagesList);
    listViewMessage.setAdapter(conversationAdapter);
    mButtonSendMessage.setOnClickListener(this);
  }

  @Override public void onClick(final View view) {
    switch (view.getId()) {
      case R.id.btn_send_message:
        sendMessageFromEditTextMessage();
        break;
    }
  }

  private void sendMessageFromEditTextMessage() {
    final String message = mEditTextMessage.getText().toString();
    if (StringUtils.isNotBlank(message)) {
      try {
        sendMessage(message);
      } catch (final Exception e) {
        e.printStackTrace();
      }
      mEditTextMessage.setText("");
    }
  }

  private void sendMessage(final String messageData) throws Exception {
    final FirebaseUser firebaseUser = getFirebaseUser();
    final String photoUrl = getFirebasePhotoUri(firebaseUser);
    final String key = generateFirebaseMessageKey();
    final Message message =
        new Message(firebaseUser.getUid(), messageData, firebaseUser.getDisplayName(), photoUrl);
    final Map<String, Object> messageValues = message.toMap();
    final Map<String, Object> childUpdates = new HashMap<>();

    childUpdates.put("/" + key, messageValues);
    databaseReference.updateChildren(childUpdates);
  }

  private String generateFirebaseMessageKey() {
    return databaseReference.child("messages").push().getKey();
  }

  private FirebaseUser getFirebaseUser() throws Exception {
    final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    if (firebaseUser == null) {
      throw new Exception("User not found");
    }
    return firebaseUser;
  }

  private String getFirebasePhotoUri(final FirebaseUser firebaseUser) {
    final Uri photoUri = firebaseUser.getPhotoUrl();
    if (photoUri != null) {
      return photoUri.toString();
    }
    return "";
  }

  @Override public void onChildAdded(final DataSnapshot dataSnapshot, final String s) {
    final Message message = dataSnapshot.getValue(Message.class);
    if (message.getUid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
      message.setMe(true);
    }
    messagesList.add(message);
    conversationAdapter.notifyDataSetChanged();
  }

  @Override public void onChildChanged(final DataSnapshot dataSnapshot, final String s) {

  }

  @Override public void onChildRemoved(final DataSnapshot dataSnapshot) {
    
  }

  @Override public void onChildMoved(final DataSnapshot dataSnapshot, final String s) {

  }

  @Override public void onCancelled(final DatabaseError databaseError) {
    Toast.makeText(ChatRoomActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
    databaseError.toException().printStackTrace();
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    if (databaseReference != null) {
      databaseReference.removeEventListener(this);
    }
  }
}
