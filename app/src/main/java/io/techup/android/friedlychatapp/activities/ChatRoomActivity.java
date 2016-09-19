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

  private ListView listViewMessage;
  private List<Message> messagesList;
  private ConversationAdapter conversationAdapter;
  private DatabaseReference databaseReference;
  private Button mButtonSendMessage;
  private EditText mEditTextMessage;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chat_room);
    initFirebaseDatabase();
    initView();
  }

  private void initFirebaseDatabase() {
    databaseReference = FirebaseDatabase.getInstance().getReference();
    databaseReference.addChildEventListener(this);
  }

  private void initView() {
    listViewMessage = (ListView) findViewById(R.id.lv_conversations);
    mEditTextMessage = (EditText) findViewById(R.id.edt_message);
    mButtonSendMessage = (Button) findViewById(R.id.btn_send_message);
    messagesList = new ArrayList<>();
    conversationAdapter = new ConversationAdapter(this, messagesList);
    listViewMessage.setAdapter(conversationAdapter);
    mButtonSendMessage.setOnClickListener(this);
  }

  @Override public void onClick(View view) {
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
    }
  }

  private void sendMessage(String messageData) throws Exception {
    final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    if (firebaseUser == null) {
      throw new Exception("User not found");
    }
    String photoUrl = "";
    final Uri photoUri = firebaseUser.getPhotoUrl();
    if (photoUri != null) {
      photoUrl = photoUri.toString();
    }
    final String key = databaseReference.child("messages").push().getKey();
    final Message message =
        new Message(firebaseUser.getUid(), messageData, firebaseUser.getDisplayName(), photoUrl);
    final Map<String, Object> messageValues = message.toMap();
    final Map<String, Object> childUpdates = new HashMap<>();
    childUpdates.put("/messages/" + key, messageValues);
    databaseReference.updateChildren(childUpdates);
  }

  @Override public void onChildAdded(DataSnapshot dataSnapshot, String s) {

  }

  @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {
    long lastMessage = (dataSnapshot.getChildrenCount() - 1);
    long position = 0;
    for (final DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
      if (position == lastMessage) {
        final Message message = postSnapshot.getValue(Message.class);
        if (message.getUid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
          message.setMe(true);
        }
        messagesList.add(message);
      }
      position++;
    }
    conversationAdapter.notifyDataSetChanged();
  }

  @Override public void onChildRemoved(DataSnapshot dataSnapshot) {

  }

  @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {

  }

  @Override public void onCancelled(DatabaseError databaseError) {
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
