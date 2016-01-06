package com.example.aakash.chat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.raweng.built.Built;
import com.raweng.built.BuiltApplication;
import com.raweng.built.BuiltBroadCastCallback;
import com.raweng.built.BuiltError;
import com.raweng.built.BuiltObject;
import com.raweng.built.BuiltOnEventCallback;
import com.raweng.built.BuiltRealTimeCallback;
import com.raweng.built.BuiltResultCallBack;
import com.raweng.built.BuiltUser;
import com.raweng.built.utilities.BuiltConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListUsersActivity extends AppCompatActivity {

    protected ListView usersListView;
    protected EditText mmessageEdit;
    protected Button mbroadcastbutton;
    private ChatAdapter adapter;
    private ArrayList<ChatMessage> chatHistory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_users);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        mmessageEdit = (EditText)findViewById(R.id.messageEdit);
        mbroadcastbutton=(Button)findViewById(R.id.broadcastbtn);
        usersListView = (ListView)findViewById(R.id.usersListView);

        try {
            final BuiltApplication builtApplication = Built.application(getApplicationContext(),"blt8d0f84abe0f3eac8");

            builtApplication.enableRealTime(new BuiltRealTimeCallback() {
                @Override
                public void onRealtimeError(BuiltApplication builtApplication, BuiltError builtError) {
                    // refer to the 'error' object for more details
                    Log.i("error: ", "" + builtError.getErrorMessage());
                    Log.i("error: ", "" + builtError.getErrorCode());
                    Log.i("error: ", "" + builtError.getErrors());
                    AlertDialog.Builder builder = new AlertDialog.Builder(ListUsersActivity.this);
                    builder.setTitle("Sorry!");
                    builder.setMessage("error: " + builtError.getErrorMessage());
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            //close dialog
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

                @Override
                public void onRealtimeConnect(BuiltApplication builtApplication) {
                    //realtime enabled
                    Log.i("Success: ", "Realtime enabled");
                    //startService(new Intent(getBaseContext(), onService.class));
                    //Log.i("Success: ", "Service started");
                    BuiltObject person = builtApplication.classWithUid("chatroom").object("bltee5542021ebe6cfa");
                    person.on(Built.EventType.BROADCAST, new BuiltOnEventCallback() {
                        @Override
                        public void onReceive(Object o, BuiltError builtError) {
                            if (builtError == null) {
                                //success
                                String message = (String) o;
                                Log.i("Success: ", "on()");
                                /*JSONArray schemaArray = (JSONArray) o;
                                String msg = schemaArray.toString();*/
                                Log.i("Success: ", message);
                            } else {
                                //fail
                                // refer to the 'error' object for more details
                                Log.i("error: ", "" + builtError.getErrorMessage());
                                Log.i("error: ", "" + builtError.getErrorCode());
                                Log.i("error: ", "" + builtError.getErrors());
                            }
                        }
                    });



                }

                @Override
                public void onRealtimeDisconnect(BuiltApplication builtApplication) {

                }

                @Override
                public void onRealtimeWillReconnect(BuiltApplication builtApplication) {

                }
            });

            adapter = new ChatAdapter(ListUsersActivity.this, new ArrayList<ChatMessage>());
            usersListView.setAdapter(adapter);


            mbroadcastbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String msg = mmessageEdit.getText().toString();
                    BuiltObject person = builtApplication.classWithUid("chatroom").object("bltee5542021ebe6cfa");
                    BuiltUser userObject = builtApplication.getCurrentUser();
                    final String user = userObject.getUserName();

                    JSONObject object = new JSONObject();
                    try {
                        object.put("message", msg);
                        object.put("sender", user);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }

                    String sentmsg = object.toString();



                    /*ChatMessage chatMessage = new ChatMessage();
                    chatMessage.setMessage(msg);
                    chatMessage.setUsername(user);
                    displayMessage(chatMessage);
                    mmessageEdit.setText("");*/

                    person.broadcast( sentmsg , new BuiltBroadCastCallback() {
                        @Override
                        public void onCompletion(BuiltConstant.ResponseType responseType, BuiltError builtError) {
                            if (builtError == null) {
                                //success
                                ChatMessage chatMessage = new ChatMessage();
                                chatMessage.setMessage(msg);
                                chatMessage.setUsername(user);
                                displayMessage(chatMessage);
                                mmessageEdit.setText("");

                                AlertDialog.Builder builder = new AlertDialog.Builder(ListUsersActivity.this);
                                builder.setTitle("Message");
                                builder.setMessage("Broadcasted: "+ msg);
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int which) {
                                        //close dialog
                                        dialogInterface.dismiss();
                                    }
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();

                            } else {
                                // refer to the 'error' object for more details
                                Log.i("error: ", "" + builtError.getErrorMessage());
                                Log.i("error: ", "" + builtError.getErrorCode());
                                Log.i("error: ", "" + builtError.getErrors());
                            }
                        }
                    });

                }
            });



        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void displayMessage(ChatMessage message) {
        adapter.add(message);
        adapter.notifyDataSetChanged();
        scroll();

    }

    private void scroll() {
        usersListView.setSelection(usersListView.getCount() - 1);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_listusers, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case R.id.action_settings:

                break;
            case R.id.logout:
                //logout user
                try {
                    BuiltApplication builtApplication = Built.application(getApplicationContext(),"blt8d0f84abe0f3eac8");
                    BuiltUser userObject = builtApplication.user();
                    //stopService(new Intent(getBaseContext(),onService.class));

                    userObject.logoutInBackground(new BuiltResultCallBack() {
                        @Override
                        public void onCompletion(BuiltConstant.ResponseType responseType, BuiltError builtError) {
                            Log.i("BuiltUser", "Logged-out");
                            Intent takeUsertoLogin = new Intent(ListUsersActivity.this, LoginActivity.class);
                            startActivity(takeUsertoLogin);

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }


                break;
        }



        return super.onOptionsItemSelected(item);
    }

}
