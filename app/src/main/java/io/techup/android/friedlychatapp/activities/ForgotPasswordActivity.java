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
import com.google.firebase.auth.FirebaseAuth;
import io.techup.android.friedlychatapp.R;
import io.techup.android.friedlychatapp.utils.EmailChecker;

public class ForgotPasswordActivity extends AppCompatActivity
    implements View.OnClickListener, OnCompleteListener<Void> {

  private EditText mEditTextEmail;
  private Button mButtonSubmit;
  private ProgressDialog mProgressDialog;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_forgot_password);
    initView();
  }

  private void initView() {
    mEditTextEmail = (EditText) findViewById(R.id.edt_email);
    mButtonSubmit = (Button) findViewById(R.id.btn_submit);
    mButtonSubmit.setOnClickListener(this);
    mProgressDialog = new ProgressDialog(this);
  }

  @Override public void onClick(View view) {
    switch (view.getId()) {
      case R.id.btn_submit:
        if (EmailChecker.getInstance().isValid(mEditTextEmail)) {
          requestResetPassword(mEditTextEmail.getText().toString());
        }
        break;
    }
  }

  private void requestResetPassword(String email) {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(this);
  }

  @Override public void onComplete(@NonNull Task<Void> task) {
    dismissProgressDialog();
    
    if (task.isSuccessful()) {
      Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
      startActivity(intent);
    } else {
      Toast.makeText(ForgotPasswordActivity.this, task.getException().getMessage(),
          Toast.LENGTH_SHORT).show();
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