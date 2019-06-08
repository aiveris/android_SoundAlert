package com.example.soundalert;


import android.media.MediaRecorder;
import android.util.Log;

import java.io.IOException;

public class SoundMeter {

    static final private double EMA_FILTER = 0.5;

    private MediaRecorder mRecorder = null;
    private double mEMA = 0.0;

    public void start() {

        if (mRecorder == null) {

            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile("/dev/null");

            try {
                mRecorder.prepare();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            mRecorder.start();
            mEMA = 0.0;
        }
    }

    public void stop() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
    }

    public double getAmplitude() {
        if (mRecorder != null){
            double x =  mRecorder.getMaxAmplitude();
            Log.e("AMP", "MaxAmp: " + (int)x );
            return  x; //(x/2000.0);
        }
        else
            return 0;

    }


}