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
    if (EmailChecker.getInstance().isValid(mEditTextEmail) && PasswordChecker.getInstance()
        .isValid(mEditTextPassword)) {
      createUser(mEditTextEmail.getText().toString(), mEditTextPassword.getText().toString());
    }
  }

  private void createUser(String email, String password) {
    mProgressDialog.setMessage("Creating new user");
    mProgressDialog.show();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, this);
  }

  @Override public void onComplete(@NonNull Task<AuthResult> task) {
    if (mProgressDialog != null && mProgressDialog.isShowing()) {
      mProgressDialog.dismiss();
    }
    // If sign in fails, display a message to the user. If sign in succeeds
    // the auth state listener will be notified and logic to handle the
    // signed in user can be handled in the listener.
    if (task.isSuccessful()) {
      Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
      startActivity(intent);
    } else {
      Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT)
          .show();
    }
  }
}
