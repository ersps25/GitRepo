package chatapp.com.chatapplication;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by SHISHUPAL SHAKYA on 10/11/2015.
 */
public class ChatApp extends Application {

    public static final String APPLICATION_ID = "ES5YCyCHWmOHv9aan0OtlAGETUPAyNfUS0RnCmST";
    public static final String CLIENT_KEY = "DCL5BrpkRY1cUWWoZLMOCOhrrRjIQpEH325geuyF";
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);
    }
}
