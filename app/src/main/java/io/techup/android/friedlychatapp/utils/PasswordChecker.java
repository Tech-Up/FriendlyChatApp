package io.techup.android.friedlychatapp.utils;

import android.widget.EditText;
import org.apache.commons.lang.StringUtils;

/**
 * Created by ceosilvajr on 10/08/16.
 */
public class PasswordChecker {

  private static PasswordChecker ourInstance = new PasswordChecker();

  private PasswordChecker() {

  }

  public static PasswordChecker getInstance() {
    return ourInstance;
  }

  public boolean isValid(EditText editText) {
    final String password = editText.getText().toString();
    if (StringUtils.length(password) < 6) {
      editText.setError("password length should be at least 6 characters");
      return false;
    }
    return true;
  }
}
