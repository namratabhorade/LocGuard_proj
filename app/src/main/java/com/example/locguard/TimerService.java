package com.example.locguard;

import android.app.IntentService;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimerService extends IntentService {
    public String t1;
    public String t2;
    public String t;
    Calendar calendar;
    String time;
    String TAG = TimerService.class.getSimpleName();

    public TimerService(String name) {
        super(name);
    }

    //MainActivity act = new MainActivity();
   // AudioManager am =(AudioManager) this.getSystemService(Context.AUDIO_SERVICE);




    public String getCurrentTime()
    {
        calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        time = format.format(calendar.getTime());
        return time;
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        String time1 = extras.getString("time1");
        String time2 = extras.getString("time2");
        AudioManager am =(AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        while (true) {
            t1 = getCurrentTime();
            if (t1.equalsIgnoreCase(time1)) {
                am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                break;
            }
        }
        while (true) {
            t2 = getCurrentTime();
            if (t2.equalsIgnoreCase(time2)) {
                am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                break;
            }
        }


    }

/*
    @Nullable
    public IBinder onBind(Intent intent)

    {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate()");
        Notification notification = new Notification();
        startForeground(42, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.i(TAG,"onStartCommand()");
        Bundle extras = intent.getExtras();
        String time1 = extras.getString("time1");
        String time2 = extras.getString("time2");
        AudioManager am =(AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        Log.i(TAG,time1);
        Log.i(TAG,time2);






        while (true) {
            t1 = getCurrentTime();
            if (t1.equalsIgnoreCase(time1)) {
                am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                break;
            }
        }
        while (true) {
            t2 = getCurrentTime();
            if (t2.equalsIgnoreCase(time2)) {
                am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                break;
            }
        }


        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.i(TAG,"onTaskRemoved()");



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"onDestroy()");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.i(TAG,"onLowMemory()");

    }*/
}
