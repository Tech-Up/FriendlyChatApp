package io.techup.android.friedlychatapp.activities;

import android.content.Intent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import io.techup.android.friedlychatapp.R;
import io.techup.android.friedlychatapp.commons.SplashActivity;

public class MainActivity extends SplashActivity {

  @Override public int setLayout() {
    return R.layout.activity_main;
  }

  @Override public int setTimeInSeconds() {
    return 2;
  }

  @Override public void init() {

  }

  @Override public void done() {
    Intent intent = null;
    if (isUserLoggedIn()) {
      intent = new Intent(this, HomeActivity.class);
    } else {
      intent = new Intent(this, LoginActivity.class);
    }
    startActivity(intent);
    finish();
  }

  public boolean isUserLoggedIn() {
    final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    return firebaseUser != null;
  }
}
