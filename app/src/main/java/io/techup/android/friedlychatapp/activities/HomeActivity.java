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

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);
    initView();
  }

  private void initView() {
    final Button btnJoinRoom = (Button) findViewById(R.id.btn_join_room);
    final TextView tvLogout = (TextView) findViewById(R.id.tv_logout);
    btnJoinRoom.setOnClickListener(this);
    tvLogout.setOnClickListener(this);
  }

  @Override public void onClick(View view) {
    Intent intent = null;
    switch (view.getId()) {
      case R.id.btn_join_room:
        intent = new Intent(this, ChatRoomActivity.class);
        break;
      case R.id.tv_logout:
        intent = new Intent(this, LoginActivity.class);
        signOut(intent);
        break;
    }
    startActivity(intent);
  }

  private void signOut(Intent intent) {
    FirebaseAuth.getInstance().signOut();
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
  }
}
