package com.example.soundalert;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    public static final int RECORD_AUDIO = 0;
    Button btnSt,btnEnd;
    int checker=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},RECORD_AUDIO);
        }

        btnSt = (Button) findViewById(R.id.button1);
        btnEnd = (Button) findViewById(R.id.button2);

        checker++;
        Log.e("ACTIVITY", "ON CREATE: " + checker);
    }

    public void StartIt(View v){
        Log.e("MainLast CLICK", "Button clicked to start SERVICE");
        Intent intent=new Intent(this,MyService.class);
        startService(intent);

        this.finish();
    }
    public void StopIt(View v){
        Log.e("MainLast CLICK", "Button clicked to END SERVICE");
        Intent intent=new Intent(this,MyService.class);
        stopService(intent);
    }

    protected void onPause(){
        super.onPause();
        //============================
        if(this.hasWindowFocus()) {
            Log.e("ACTIVTY","onPause()... FINISH");
            finish();
        }
        else{
            Log.e("ACTIVTY","onPause()... NO finish");
        }
        //============================You need this code because if the screen dims with this activity
        //on the top, means it wasnt finished. & hence the new FLAG_ACTIVITY_NEW_TASK wont deliver a new
        //activity, & so the screen never lights up. Cuase it thinks the Activity is still on the top of
        //the stack there is no need to deliver the new intent. hence nothing happens... and if the screen
        //had blanked out. then that is how it will stay. when you activate the screen again & it lights
        // up ... you will see the activity still lying over there. So when dimming you need to still call
        //finish.
        //But when an activity is coming back into the foreground & it executes OnResume.. there is a chance
        // that it finished onResumed before the blank screen is cleared and the phone has woken up,
        // then in such a case onPause & onStop are called cause it detects the darkness as the window is still
        //not in the foreground. So thats why we only call finish() when the windowHasFocus. Or else every
        //time it executes onResume before the wakeLock is cleared, the onPause will be called and it will
        //finish the activity
    }

    protected void onResume(){
        super.onResume();
        Log.e("ACTIVTY","onRESUME... now in wakeDevice(): " + checker);
        wakeDevice();
    }

    protected void onStop(){
        super.onStop();
        Log.e("onStop()", "x-x-x-x--xx-x-x--xx--xx--x-x-x-x-x-x-x-x");
    }

    public void wakeDevice() {
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("TAG");
        keyguardLock.disableKeyguard();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                //| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
    }
}
