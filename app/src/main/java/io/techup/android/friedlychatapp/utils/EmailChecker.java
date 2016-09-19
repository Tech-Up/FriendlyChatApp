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
    boolean result = true;
    final String email = editText.getText().toString();
    if (StringUtils.isEmpty(email)) {
      editText.setError("Email should not be empty.");
      result = false;
    } else if (!EmailValidator.getInstance().isValid(email)) {
      editText.setError("Invalid email.");
      result = false;
    }
    return result;
  }
}
