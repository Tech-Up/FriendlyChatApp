package io.techup.android.friedlychatapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import io.techup.android.friedlychatapp.R;
import io.techup.android.friedlychatapp.adapters.ConversationAdapter;
import io.techup.android.friedlychatapp.pojo.Message;
import java.util.ArrayList;
import java.util.List;

public class ChatRoomActivity extends AppCompatActivity {

  private ListView listViewMessage;
  private List<Message> messagesList;
  private ConversationAdapter conversationAdapter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chat_room);
    initView();
  }

  private void initView() {
    listViewMessage = (ListView) findViewById(R.id.lv_conversations);
    messagesList = new ArrayList<>();
    conversationAdapter = new ConversationAdapter(this, messagesList);
    listViewMessage.setAdapter(conversationAdapter);
  }
}
