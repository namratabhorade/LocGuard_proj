package com.example.locguard;

import android.app.IntentService;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {
    TextView textview1,textView,textview2;
    public TimePicker timepicker1, timepicker2;
    public Button button;
    public Calendar calendar;
    public AudioManager am;
    public String time1,time2;
    public String time;
    public Button apply;
    Main2Activity act = new Main2Activity();
    Switch sw1=act.getSw();
    public Intent intent;
    DataBaseHelper db = new DataBaseHelper(this);
    SQLiteDatabase database;
    String TAG = MainActivity.class.getSimpleName();

    public String getTime1 (TimePicker timepicker)
    {
        String currentTime = String.format("%02d:%02d",timepicker.getCurrentHour(),timepicker.getCurrentMinute());
        return currentTime;
    }
    public String getCurrentTime()
    {
        calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        time = format.format(calendar.getTime());
        return time;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && !notificationManager.isNotificationPolicyAccessGranted()) {

            Intent intent = new Intent(
                    android.provider.Settings
                            .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);

            startActivity(intent);
        }
        am = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        textview1=findViewById(R.id.textView1);
        timepicker1=findViewById(R.id.timePicker);
        timepicker2=findViewById(R.id.timePicker2);
        //Uncomment the below line of code for 24 hour view
        timepicker1.setIs24HourView(true);
        timepicker2.setIs24HourView(true);
        apply=findViewById(R.id.button1);
        textView=findViewById(R.id.textView);



        apply.setOnClickListener(
                new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                time1 = getTime1(timepicker1);
                time2 = getTime1(timepicker2);
                ArrayList<String> list1=new ArrayList<>();
                ArrayList<String> list2=new ArrayList<>();

                Intent intent=getIntent();
                Bundle extras=intent.getExtras();
                if(extras!=null) {
                    list1 = extras.getStringArrayList("TIMER1");
                    list2 = extras.getStringArrayList("TIMER2");

                    Log.i(TAG,list1.size()+"");
                }
                int flag=0;
                Iterator i = list1.iterator();
                Iterator j = list2.iterator();
                Log.i(TAG,""+i.hasNext());

                while((i.hasNext()) && (j.hasNext()))
                {
                    String Start=i.next().toString();
                    Log.i(TAG,Start);
                    String End=j.next().toString();
                    Log.i(TAG,End);
                    if( ((time1.compareTo(Start) > 0) && (time1.compareTo(End) <0)) || ((time2.compareTo(Start) > 0) && (time2.compareTo(End) <0)))
                    {
                        Toast.makeText(getApplicationContext(), "Overlapping Time Interval", Toast.LENGTH_SHORT).show();
                        flag=1;
                        break;
                    }
                }
                if(flag==0)
                {
                    Log.i(TAG,"HIIIII");
                    database = db.getWritableDatabase();
                    database.execSQL("INSERT INTO UserDetails(Time1, Time2)VALUES('" + time1 + "','" + time2 + "')");
                /*
                Bundle extras = new Bundle();
                extras.putString("time1",time1);
                extras.putString("time2",time2);
                intent = new Intent(MainActivity.this,TimerService.class );
                intent.putExtras(extras);
                startService(intent);
                 */

                    Test t = new Test();
                    final Thread th = new Thread(t);
                    th.start();
                    Intent int1 = new Intent(MainActivity.this, Main2Activity.class);
                    startActivity(int1);

                }
                /*Intent intent = new Intent(this,MyService.class);
                this.startService(intent);*/

                /*final Intent int2 = new Intent(MainActivity.this,Main2Activity.class);



                sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {



                            th.start();
                            startActivity(int2);

                            // The toggle is enabled

                        } else {
                            // The toggle is disabled
                            th.interrupt();
                            startActivity(int2);
                        }
                    }
                });*/
            }
                });

    }
    @Override protected void onDestroy(){

        //stopService(intent);
        super.onDestroy();
        Log.i(TAG,"onDestroy() of MainActivity");


    }
    class Test implements Runnable{

        public  void run()
        {
            time2 = getTime1(timepicker2);
            time1 = getTime1(timepicker1);
            while (true) {
                time = getCurrentTime();
                if (time.equalsIgnoreCase(time1)) {
                    am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                    break;
                }
            }
            while (true) {
                time = getCurrentTime();
                if (time.equalsIgnoreCase(time2)) {
                    am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                    break;
                }
            }

        }
    }

    /*public class MyService extends IntentService{
        public MyService(String name) {
            super(name);
        }

        @Override
        protected void onHandleIntent(@Nullable Intent intent) {
                time2 = getTime1(timepicker2);
                time1 = getTime1(timepicker1);
                while (true) {
                    time = getCurrentTime();
                    if (time.equalsIgnoreCase(time1)) {
                        am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                        break;
                    }
                }
                while (true) {
                    time = getCurrentTime();
                    if (time.equalsIgnoreCase(time2)) {
                        am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                        break;
                    }



            }

        }
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent int1 = new Intent(MainActivity.this, Main2Activity.class);
        startActivity(int1);
    }
}

