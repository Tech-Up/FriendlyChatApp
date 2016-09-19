package io.techup.android.friedlychatapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import io.techup.android.friedlychatapp.R;
import io.techup.android.friedlychatapp.utils.EmailChecker;
import io.techup.android.friedlychatapp.utils.PasswordChecker;

public class RegisterActivity extends AppCompatActivity
    implements View.OnClickListener, OnCompleteListener<AuthResult> {

  private EditText mEditTextEmail;
  private EditText mEditTextPassword;
  private Button mButtonSubmit;
  private ProgressDialog mProgressDialog;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_register);
    initView();
  }

  private void initView() {
    mEditTextEmail = (EditText) findViewById(R.id.edt_email);
    mEditTextPassword = (EditText) findViewById(R.id.edt_password);
    mButtonSubmit = (Button) findViewById(R.id.btn_submit);
    mButtonSubmit.setOnClickListener(this);
    mProgressDialog = new ProgressDialog(this);
    mProgressDialog.setCancelable(false);
  }

  @Override public void onClick(View view) {
    boolean isValidatedEmailAndPassword =
        EmailChecker.getInstance().isValid(mEditTextEmail) && PasswordChecker.getInstance()
            .isValid(mEditTextPassword);
    if (isValidatedEmailAndPassword) {
      createNewFirebaseUser();
    }
  }

  private void createNewFirebaseUser() {
    showProgressDialog();
    final String email = mEditTextEmail.getText().toString();
    final String password = mEditTextPassword.getText().toString();
    final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, this);
  }

  /**
   * Firebase create new FirebaseUser callback on complete
   */
  @Override public void onComplete(@NonNull Task<AuthResult> task) {
    dismissProgressDialog();
    if (task.isSuccessful()) {
      final Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
      startActivity(intent);
    } else {
      Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT)
          .show();
    }
  }

  private void showProgressDialog() {
    mProgressDialog.setMessage("Creating new user");
    mProgressDialog.show();
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
