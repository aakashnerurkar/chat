package com.example.aakash.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.raweng.built.Built;
import com.raweng.built.BuiltError;
import com.raweng.built.BuiltResultCallBack;
import com.raweng.built.BuiltUser;
import com.raweng.built.utilities.BuiltConstant;

public class RegisterActivity extends AppCompatActivity {

    protected EditText mUsername;
    protected EditText mUserEmail;
    protected EditText mUserPassword;
    protected EditText mUserConfirmpwd;
    protected Button mRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
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
        mUsername = (EditText)findViewById(R.id.usernameRegisterEditText);
        mUserEmail = (EditText)findViewById(R.id.emailRegisterEditText);
        mUserPassword = (EditText)findViewById(R.id.passwordRegisterEditText);
        mUserConfirmpwd = (EditText)findViewById(R.id.confirmpwdRegisterEditText);
        mRegisterButton = (Button)findViewById(R.id.RegisterButton);

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //get username,password,email and convert to string
                String username = mUsername.getText().toString().trim();
                String email = mUserEmail.getText().toString().trim();
                String password = mUserPassword.getText().toString().trim();
                String confirmpwd = mUserConfirmpwd.getText().toString().trim();

                //register in built

                try {
                    BuiltUser userObject = Built.application(getApplicationContext(),"blt8d0f84abe0f3eac8").user();

                    userObject.setUserName(username);
                    userObject.setEmail(email);
                    userObject.setPassword(password);
                    userObject.setConfirmPassword(confirmpwd);
                    userObject.registerInBackground(new BuiltResultCallBack() {
                        @Override
                        public void onCompletion(BuiltConstant.ResponseType responseType, BuiltError builtError) {
                            if(builtError == null){

                                // object is created successfully
                                Toast.makeText(RegisterActivity.this, "Success!", Toast.LENGTH_LONG).show();
                                //take to loginpage
                                Intent taketoLogin = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(taketoLogin);
                            }else{
                                // login failed
                                // refer to the 'error' object for more details
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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
