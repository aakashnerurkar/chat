package com.example.aakash.chat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.raweng.built.Built;
import com.raweng.built.BuiltApplication;
import com.raweng.built.BuiltError;
import com.raweng.built.BuiltResultCallBack;
import com.raweng.built.BuiltUser;
import com.raweng.built.utilities.BuiltConstant;

public class LoginActivity extends AppCompatActivity {

    protected EditText mLoginEmail;
    protected EditText mLoginpassword;
    protected Button mloginButton;
    protected Button mregisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        try {
            BuiltApplication builtApplication = Built.application(getApplicationContext(), "blt8d0f84abe0f3eac8", "chat");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //initialize
        mLoginEmail = (EditText)findViewById(R.id.emailLoginEditText);
        mLoginEmail.setText("test@gmail.com");
        mLoginpassword = (EditText)findViewById(R.id.passwordLoginEditText);
        mLoginpassword.setText("test");
        mloginButton = (Button)findViewById(R.id.loginButton);
        mregisterButton = (Button)findViewById(R.id.registerLoginButton);

        mloginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get username and password
                String email = mLoginEmail.getText().toString().trim();
                String password = mLoginpassword.getText().toString().trim();

                try {
                    final BuiltUser userObject = Built.application(getApplicationContext(),"blt8d0f84abe0f3eac8").user();
                    userObject.loginInBackground(email, password, new BuiltResultCallBack() {
                        @Override
                        public void onCompletion(BuiltConstant.ResponseType responseType, BuiltError builtError) {
                            if(builtError == null){
                                // user has logged in successfully
                                Toast.makeText(LoginActivity.this, "Welcome Back! " + userObject.getUserName(), Toast.LENGTH_LONG).show();
                                //take to userlist
                                Intent takeuserhome = new Intent(LoginActivity.this, ListUsersActivity.class);
                                startActivity(takeuserhome);
                            }else{
                                // login failed
                                // refer to the 'error' object for more details
                                Log.i("error: ", "" + builtError.getErrorMessage());
                                Log.i("error: ", "" + builtError.getErrorCode());
                                Log.i("error: ", "" + builtError.getErrors());
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //BuiltApplication builtApplication = Built.application(getApplicationContext(),"blt8d0f84abe0f3eac8").user();
                /*try {
                    BuiltUser userObject = Built.application(getApplicationContext(),"blt8d0f84abe0f3eac8").user();

                    BuiltSynchronousResponse response = userObject.login(email, password);
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
            }
        });

        mregisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takeUsertoRegister = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(takeUsertoRegister);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
