package chatapp.com.chatapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseUser;

import java.text.ParseException;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseUser user = ParseUser.getCurrentUser();

        if(user != null){
            //Start UsersList
            Intent usersList = new Intent(MainActivity.this,UsersList.class);
            startActivity(usersList);

            //Start MessageService
            final Intent serviceIntent = new Intent(getApplicationContext(), MessageService.class);
            startService(serviceIntent);
        }

    }

 public void onSignIn(View v){


     EditText username = (EditText)findViewById(R.id.enterusername);
     EditText userpass = (EditText)findViewById(R.id.enterpass);

     ParseUser.logInInBackground(username.getText().toString(), userpass.getText().toString(), new LogInCallback() {
         public void done(ParseUser user, com.parse.ParseException e) {
             if (user != null) {

                 //Start UsersList
                 Intent usersList = new Intent(MainActivity.this,UsersList.class);
                 startActivity(usersList);

                 //Start MessageService
                 final Intent serviceIntent = new Intent(getApplicationContext(), MessageService.class);
                 startService(serviceIntent);

             } else {
                 Toast.makeText(getApplicationContext(),
                         "There was an error logging in.",
                         Toast.LENGTH_LONG).show();
             }
         }
     });
 }

    @Override
    public void onDestroy() {
        //Stop MessageService .
        stopService(new Intent(getApplicationContext(), MessageService.class));
        super.onDestroy();
    }
}
