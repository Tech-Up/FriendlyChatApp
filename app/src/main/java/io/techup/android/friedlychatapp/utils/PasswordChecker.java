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

    // Set string that gets the value of password edit text
    String password = editText.getText().toString();

    // Checks password length
    if (StringUtils.length(password) < 6) {
      // set error
      editText.setError("password length should be at least 6 characters");
      return false;
    }

    return true;
  }
}
