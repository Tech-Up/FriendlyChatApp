package io.techup.android.friedlychatapp.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class ChatRoomActivity extends AppCompatActivity implements ChildEventListener {

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
    messagesList = new ArrayList<>();
    conversationAdapter = new ConversationAdapter(this, messagesList);
    listViewMessage.setAdapter(conversationAdapter);
    mEditTextMessage = (EditText) findViewById(R.id.edt_message);
    mButtonSendMessage = (Button) findViewById(R.id.btn_send_message);
    mButtonSendMessage.setOnClickListener(view -> {
      String message = mEditTextMessage.getText().toString();
      if (StringUtils.isNotBlank(message)) {
        try {
          sendMessage(message);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  private void sendMessage(String messageData) throws Exception {
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    if (firebaseUser == null) {
      throw new Exception("");
    }

    String photoUrl = "";
    Uri photoUri = firebaseUser.getPhotoUrl();
    if (photoUri != null) {
      photoUrl = photoUri.toString();
    }

    String key = databaseReference.child("messages").push().getKey();

    Message message =
        new Message(firebaseUser.getUid(), messageData, firebaseUser.getDisplayName(), photoUrl);

    Map<String, Object> messageValues = message.toMap();

    Map<String, Object> childUpdates = new HashMap<>();

    childUpdates.put("/messages/" + key, messageValues);

    databaseReference.updateChildren(childUpdates);
  }

  @Override public void onChildAdded(DataSnapshot dataSnapshot, String s) {
    Message message = dataSnapshot.getValue(Message.class);
    messagesList.add(message);
    conversationAdapter.notifyDataSetChanged();
    Log.d("Message data added ", message.toString());
  }

  @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {
    Message message = dataSnapshot.getValue(Message.class);
    Log.d("Message data changed", dataSnapshot.toString());
    Log.d("Message data changed", message.toString());
  }

  @Override public void onChildRemoved(DataSnapshot dataSnapshot) {
    Message message = dataSnapshot.getValue(Message.class);
    Log.d("Message data removed ", message.toString());
  }

  @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {
    Message message = dataSnapshot.getValue(Message.class);
    Log.d("Message data moved", message.toString());
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
