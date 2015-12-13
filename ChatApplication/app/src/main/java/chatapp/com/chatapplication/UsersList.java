package chatapp.com.chatapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SHISHUPAL SHAKYA on 10/11/2015.
 */
public class UsersList extends Activity {

    ListView usersListView ;
    String currentUserId ;
    ArrayList<String> names ;
    ProgressDialog progressDialog;
    BroadcastReceiver receiver ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userslist);


        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

//broadcast receiver to listen for the broadcast
//from MessageService
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Boolean success = intent.getBooleanExtra("success", false);
                progressDialog.dismiss();

                //show a toast message if the Sinch
                //service failed to start
                if (!success) {
                    Toast.makeText(getApplicationContext(), "Messaging service failed to start", Toast.LENGTH_LONG).show();
                }
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("chatapp.com.chatapplication.UsersList"));


        usersListView = (ListView)findViewById(R.id.usersListView);
        currentUserId = ParseUser.getCurrentUser().getObjectId();
        names = new ArrayList<String>();

        ParseQuery<ParseUser> query = ParseUser.getQuery();
//don't include yourself
        query.whereNotEqualTo("objectId", currentUserId);
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> userList, com.parse.ParseException e) {
                if (e == null) {
                    for (int i=0; i<userList.size(); i++) {
                        names.add(userList.get(i).getUsername().toString());
                    }

                    usersListView = (ListView)findViewById(R.id.usersListView);
                    ArrayAdapter  namesArrayAdapter =  new ArrayAdapter<String>(getApplicationContext(),R.layout.user_list_item, names);
                    //ArrayAdapter  namesArrayAdapter =  new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1, names);
                    usersListView.setAdapter(namesArrayAdapter);

                    usersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> a, View v, int i, long l) {
                            openConversation(names, i);
                        }
                    });

                } else {
                    Toast.makeText(getApplicationContext(),"Error loading user list",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void openConversation(ArrayList<String> names, int pos) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", names.get(pos));
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> user, ParseException e) {
                if (e == null) {
                    //start the messaging activity
                    Intent intent = new Intent(getApplicationContext(), MessagingActivity.class);
                    intent.putExtra("RECIPIENT_ID", user.get(0).getObjectId());
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Error finding that user",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
