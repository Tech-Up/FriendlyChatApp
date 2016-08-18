package io.techup.android.friedlychatapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import io.techup.android.friedlychatapp.R;
import io.techup.android.friedlychatapp.utils.EmailChecker;
import io.techup.android.friedlychatapp.utils.PasswordChecker;

public class LoginActivity extends AppCompatActivity
    implements View.OnClickListener, OnCompleteListener<AuthResult> {

  private EditText mEditTextEmail;
  private EditText mEditTextPassword;
  private Button mButtonLogin;
  private Button mButtonRegister;
  private ProgressDialog mProgressDialog;
  private TextView mTextViewForgotPassword;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    initView();
  }

  private void initView() {
    mEditTextEmail = (EditText) findViewById(R.id.edt_email);
    mEditTextPassword = (EditText) findViewById(R.id.edt_password);
    mButtonLogin = (Button) findViewById(R.id.btn_login);
    mButtonLogin.setOnClickListener(this);
    mButtonRegister = (Button) findViewById(R.id.btn_register);
    mButtonRegister.setOnClickListener(this);
    mTextViewForgotPassword = (TextView) findViewById(R.id.tv_forgot_password);
    mTextViewForgotPassword.setOnClickListener(this);
    mProgressDialog = new ProgressDialog(this);
    mProgressDialog.setCancelable(false);
  }

  @Override public void onClick(View view) {
    Intent intent = null;
    switch (view.getId()) {
      case R.id.btn_login:
        if (EmailChecker.getInstance().isValid(mEditTextEmail) && PasswordChecker.getInstance()
            .isValid(mEditTextPassword)) {
          signInUser(mEditTextEmail.getText().toString(), mEditTextPassword.getText().toString());
        }
        break;
      case R.id.btn_register:
        intent = new Intent(this, RegisterActivity.class);
        break;
      case R.id.tv_forgot_password:
        intent = new Intent(this, ForgotPasswordActivity.class);
        break;
    }
    if (intent != null) {
      startActivity(intent);
    }
  }

  private void signInUser(String email, String password) {
    mProgressDialog.setMessage("Logging in");
    mProgressDialog.show();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, this);
  }

  @Override public void onComplete(@NonNull Task<AuthResult> task) {
    dismissProgressDialog();
    // If sign in fails, display a message to the user. If sign in succeeds
    // the auth state listener will be notified and logic to handle the
    // signed in user can be handled in the listener.
    if (task.isSuccessful()) {
      Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
      startActivity(intent);
    } else {
      Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT)
          .show();
    }
  }

  private void dismissProgressDialog() {
    if (mProgressDialog != null && mProgressDialog.isShowing()) {
      mProgressDialog.dismiss();
    }
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    dismissProgressDialog();
  }
}
