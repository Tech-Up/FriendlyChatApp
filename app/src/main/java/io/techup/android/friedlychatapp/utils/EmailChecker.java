package io.techup.android.friedlychatapp.utils;

import android.widget.EditText;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;

/**
 * Created by ceosilvajr on 10/08/16.
 */
public class EmailChecker {

  private static EmailChecker ourInstance = new EmailChecker();

  private EmailChecker() {

  }

  public static EmailChecker getInstance() {
    return ourInstance;
  }

  public boolean isValid(EditText editText) {

    // Set string that gets the value of Email edit text
    String email = editText.getText().toString();

    // Checks if the value is empty
    if (StringUtils.isEmpty(email)) {
      // set error
      editText.setError("Email should not be empty.");
      return false;
    }

    // Validates email
    if (!EmailValidator.getInstance().isValid(email)) {
      // set error
      editText.setError("Invalid email.");
      return false;
    }

    return true;
  }
}
