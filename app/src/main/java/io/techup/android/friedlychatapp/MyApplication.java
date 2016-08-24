package io.techup.android.friedlychatapp;

import android.app.Application;
import com.facebook.FacebookSdk;

/**
 * Created by ceosilvajr on 24/08/16.
 */
public class MyApplication extends Application {
  @Override public void onCreate() {
    super.onCreate();
    FacebookSdk.sdkInitialize(this);
  }
}
