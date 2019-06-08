package com.example.soundalert;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service {

    int POLL_INTERVAL = 500;
    long lastRing=0L;
    public int mThreshold;
    public SoundMeter SoundMeterSensor;
    public boolean mRunning = false;


    public Runnable SensorListener = new Runnable() {
        public void run() {
            while (mRunning) {
                double amp = SoundMeterSensor.getAmplitude();
                Log.e("Noise2222", "runnable mPollTask... " + (int) (amp));

                if ((amp > mThreshold)) {
                    Log.e("Noise333", "==== HIGH-NOTE ===");

                    if( (System.currentTimeMillis() - lastRing) > 15000 ) {
                        lastRing = System.currentTimeMillis();
                        WakeUpPhone();
                    }
                }

                try {
                    Thread.sleep(POLL_INTERVAL);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }//WHILE LOOP ends==========================
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SoundMeterSensor = new SoundMeter();
        SoundMeterSensor.start();
        Toast.makeText(this, "Service ON!", Toast.LENGTH_LONG).show();

        initializor();
        Log.e("SERVICE", "==== onSTART ===");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service STOP!", Toast.LENGTH_LONG).show();
        SoundMeterSensor.stop();
        mRunning = false;

        super.onDestroy();
    }

    public void WakeUpPhone() {
        Log.e("HELP!", "=== ASKING TO OPEN ACTIVITY  ===");

        Intent i = new Intent (this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    public void initializor() {
        mThreshold = 3500;
        mRunning = true;
        Thread t1 = new Thread(SensorListener);
        t1.start();
    }
}