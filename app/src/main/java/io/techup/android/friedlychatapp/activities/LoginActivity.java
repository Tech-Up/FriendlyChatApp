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
  private FirebaseAuth mFirebaseAuth;

  /**
   * Activity onCreate
   *
   * @param savedInstanceState Bundle
   */
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    initView();
  }

  /**
   * Initialised views
   */
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
    mFirebaseAuth = FirebaseAuth.getInstance();
  }

  /**
   * Button onClick
   */
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

  /**
   * Sign in using email and password
   *
   * @param email String
   * @param password String
   */
  private void signInUser(String email, String password) {
    // Set progress dialog message
    mProgressDialog.setMessage("Logging in");
    mProgressDialog.show();

    // Perform user login
    mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, this);
  }

  /**
   * Handles facebook access token, responsible for user login with facebook credentials
   *
   * @param token String
   */
  private void handleFacebookAccessToken(AccessToken token) {
    // Get credentials from facebook AccessToken
    AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());

    // Perform login using facebook
    mFirebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, this);
  }

  /**
   * On Facebook success login
   *
   * @param loginResult LoginResult
   */
  @Override public void onSuccess(LoginResult loginResult) {
    handleFacebookAccessToken(loginResult.getAccessToken());
  }

  /**
   * On user canceled facebook login
   */
  @Override public void onCancel() {
    Toast.makeText(LoginActivity.this, "Login canceled", Toast.LENGTH_SHORT).show();
  }

  /**
   * On error occur during user facebook login
   *
   * @param error FacebookException
   */
  @Override public void onError(FacebookException error) {
    Toast.makeText(LoginActivity.this, "Error found: " + error.getMessage(), Toast.LENGTH_SHORT)
        .show();
  }

  /**
   * On complete Firebase login e.g. via email/password or Facebook
   */
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

  /**
   * Handles facebook callbackManager behaviour it triggers onSuccess/onCancel/onError facebook
   * events
   */
  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    mCallbackManager.onActivityResult(requestCode, resultCode, data);
  }

  /**
   * Dismiss progress dialog
   */
  private void dismissProgressDialog() {
    if (mProgressDialog != null && mProgressDialog.isShowing()) {
      mProgressDialog.dismiss();
    }
  }

  /**
   * On Activity destroyed
   */
  @Override protected void onDestroy() {
    super.onDestroy();
    dismissProgressDialog();
  }
}
