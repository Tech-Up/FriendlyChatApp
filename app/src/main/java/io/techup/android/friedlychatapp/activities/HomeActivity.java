package io.techup.android.friedlychatapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import io.techup.android.friedlychatapp.R;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

  private Button mButtonJoinRoom;
  private TextView mTextViewLogout;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);
    initView();
  }

  private void initView() {
    mButtonJoinRoom = (Button) findViewById(R.id.btn_join_room);
    mTextViewLogout = (TextView) findViewById(R.id.tv_logout);
    mButtonJoinRoom.setOnClickListener(this);
    mTextViewLogout.setOnClickListener(this);
  }

  @Override public void onClick(View view) {
    Intent intent = null;
    switch (view.getId()) {
      case R.id.btn_join_room:
        intent = new Intent(this, ChatRoomActivity.class);
        break;
      case R.id.tv_logout:
        FirebaseAuth.getInstance().signOut();
        intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        break;
    }
    startActivity(intent);
  }

  private void displayLogoutAlert() {

  }
}
