package io.techup.android.friedlychatapp.services;

import android.util.Log;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by ceosilvajr on 15/08/16.
 */
public class FriendlyChatInstanceIDService extends FirebaseInstanceIdService {

  private static final String TAG = "MyFirebaseIIDService";

  /**
   * Called if InstanceID token is updated. This may occur if the security of
   * the previous token had been compromised. Note that this is called when the InstanceID token
   * is initially generated so this is where you would retrieve the token.
   */
  @Override public void onTokenRefresh() {
    // Get updated InstanceID token.
    String refreshedToken = FirebaseInstanceId.getInstance().getToken();
    Log.d(TAG, "Refreshed token: " + refreshedToken);
    sendRegistrationToServer(refreshedToken);
  }

  /**
   * Persist token to third-party servers.
   *
   * Modify this method to associate the user's FCM InstanceID token with any server-side account
   * maintained by your application.
   *
   * @param token The new token.
   */
  private void sendRegistrationToServer(String token) {
    // TODO: Implement this method to send token to your app server.
  }
}
