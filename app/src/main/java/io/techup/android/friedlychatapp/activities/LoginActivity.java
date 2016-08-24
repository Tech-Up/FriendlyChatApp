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
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import io.techup.android.friedlychatapp.R;
import io.techup.android.friedlychatapp.utils.EmailChecker;
import io.techup.android.friedlychatapp.utils.PasswordChecker;

public class LoginActivity extends AppCompatActivity
    implements View.OnClickListener, OnCompleteListener<AuthResult>, FacebookCallback<LoginResult> {

  private EditText mEditTextEmail;
  private EditText mEditTextPassword;
  private Button mButtonLogin;
  private Button mButtonRegister;
  private ProgressDialog mProgressDialog;
  private TextView mTextViewForgotPassword;
  private LoginButton mLoginButton;
  private CallbackManager mCallbackManager;
  private FirebaseAuth mAuth;

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

    //Facebook Login
    mCallbackManager = CallbackManager.Factory.create();
    mLoginButton = (LoginButton) findViewById(R.id.login_button);
    mLoginButton.setReadPermissions("email", "public_profile");
    mLoginButton.registerCallback(mCallbackManager, this);

    //Firebase Auth instance
    mAuth = FirebaseAuth.getInstance();
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
    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, this);
  }

  private void handleFacebookAccessToken(AccessToken token) {
    AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
    mAuth.signInWithCredential(credential).addOnCompleteListener(this, this);
  }

  @Override public void onSuccess(LoginResult loginResult) {
    handleFacebookAccessToken(loginResult.getAccessToken());
  }

  @Override public void onCancel() {
    Toast.makeText(LoginActivity.this, "Login canceled", Toast.LENGTH_SHORT).show();
  }

  @Override public void onError(FacebookException error) {
    Toast.makeText(LoginActivity.this, "Error found: " + error.getMessage(), Toast.LENGTH_SHORT)
        .show();
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

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    mCallbackManager.onActivityResult(requestCode, resultCode, data);
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
