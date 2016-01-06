package com.example.aakash.chat;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.raweng.built.Built;
import com.raweng.built.BuiltApplication;
import com.raweng.built.BuiltError;
import com.raweng.built.BuiltOnEventCallback;
import com.raweng.built.BuiltRealTimeCallback;
import com.raweng.built.BuiltUser;

import org.json.JSONArray;

/**
 * Created by aakash on 12/4/2015.
 */
public class onService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        BuiltApplication builtApplication = null;
        try {
            builtApplication = Built.application(getApplicationContext(), "blt8d0f84abe0f3eac8");
            builtApplication.enableRealTime(new BuiltRealTimeCallback() {
                @Override
                public void onRealtimeError(BuiltApplication builtApplication, BuiltError builtError) {
                    Log.i("error: ", "" + builtError.getErrorMessage());
                    Log.i("error: ", "" + builtError.getErrorCode());
                    Log.i("error: ", "" + builtError.getErrors());
                }

                @Override
                public void onRealtimeConnect(BuiltApplication builtApplication) {
                    BuiltUser person = builtApplication.getCurrentUser();
                    person.on(Built.EventType.BROADCAST, new BuiltOnEventCallback() {
                        @Override
                        public void onReceive(Object o, BuiltError builtError) {
                            if (builtError == null) {
                                //success
                                //String message = (String) o;
                                Log.i("Success: ", "on()");
                                JSONArray schemaArray = (JSONArray) o;
                                String msg = schemaArray.toString();
                                Log.i("Success: ", msg);
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

        } catch (Exception e) {
            e.printStackTrace();
        }


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
