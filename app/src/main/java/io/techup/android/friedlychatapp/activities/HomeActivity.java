package io.techup.android.friedlychatapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import io.techup.android.friedlychatapp.R;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

  private Button buttonJoinRoom;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);
    initView();
  }

  private void initView() {
    buttonJoinRoom = (Button) findViewById(R.id.btn_join_room);
    buttonJoinRoom.setOnClickListener(this);
  }

  @Override public void onClick(View view) {
    Intent intent = null;
    switch (view.getId()) {
      case R.id.btn_join_room:
        intent = new Intent(this, ChatRoomActivity.class);
        break;
    }
    startActivity(intent);
  }
}
