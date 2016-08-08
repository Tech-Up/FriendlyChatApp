package io.techup.android.friedlychatapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import io.techup.android.friedlychatapp.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

  private EditText editTextEmail;
  private EditText editTextPassword;
  private TextView textViewForgotPassword;
  private Button buttonLogin;
  private Button buttonRegister;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    initView();
  }

  private void initView() {
    editTextEmail = (EditText) findViewById(R.id.edt_email);
    editTextPassword = (EditText) findViewById(R.id.edt_password);
    textViewForgotPassword = (TextView) findViewById(R.id.tv_forgot_password);
    buttonLogin = (Button) findViewById(R.id.btn_login);
    buttonLogin.setOnClickListener(this);
    buttonRegister = (Button) findViewById(R.id.btn_register);
    buttonRegister.setOnClickListener(this);
  }

  @Override public void onClick(View view) {
    Intent intent = null;
    switch (view.getId()) {
      case R.id.btn_login:
        intent = userLogin();
        break;
      case R.id.btn_register:
        intent = new Intent(this, RegisterActivity.class);
        break;
    }
    startActivity(intent);
  }

  private Intent userLogin() {
    Intent intent = new Intent(this, HomeActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    return intent;
  }
}
