package com.example.locguard;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity{
    Button button;
    TextView txt;
    Switch sw1;
    FloatingActionButton fb;
    DataBaseHelper controllerdb = new DataBaseHelper(this);
    SQLiteDatabase db;
    public ArrayList<String> Id = new ArrayList<String>();
    public ArrayList<String> TIMER1  = new ArrayList<String>();
    public ArrayList<String> TIMER2 = new ArrayList<String>();
    ListView lv;
    String TAG = Main2Activity.class.getSimpleName();

    public Switch getSw()
    {
        return sw1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        lv=findViewById(R.id.listview1);
        //button = findViewById(R.id.button);
        //txt=findViewById(R.id.textView2);
        //sw1=findViewById(R.id.switch1);
        fb = findViewById(R.id.fb);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b=new Bundle();
                b.putStringArrayList("TIMER1",TIMER1);
                b.putStringArrayList("TIMER2",TIMER2);
                Intent int1 = new Intent(Main2Activity.this,MainActivity.class);
                if(TIMER1.size()!=0) {
                    int1.putExtras(b);
                }
                startActivity(int1);

            }
        });
    }
    @Override
    protected void onResume() {
        displayData();
        super.onResume();
    }
    private void displayData() {
        db = controllerdb.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM  UserDetails ORDER BY Time1 ASC",null);
        Id.clear();
        TIMER1.clear();
        TIMER2.clear();
        if (cursor.moveToFirst()) {
            do {
                Id.add(cursor.getString(cursor.getColumnIndex("Id")));
                TIMER1.add(cursor.getString(cursor.getColumnIndex("Time1")));
                TIMER2.add(cursor.getString(cursor.getColumnIndex("Time2")));

            } while (cursor.moveToNext());
        }


        ListAdaptor ca= new ListAdaptor(Main2Activity.this, Id, TIMER1, TIMER2);
        lv.setAdapter(ca);

        //code to set adapter to populate list
        cursor.close();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent int1 = new Intent(Main2Activity.this, Menu.class);
        startActivity(int1);
    }
}


