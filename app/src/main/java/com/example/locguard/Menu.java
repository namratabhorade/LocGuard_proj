package com.example.locguard;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
public class Menu extends AppCompatActivity {


    CardView card1, card2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        card1=(CardView)findViewById(R.id.cardView);
        card2=(CardView)findViewById(R.id.cardView3);

        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View view) {
                Intent int3 = new Intent(Menu.this, Main2Activity.class);
                startActivity(int3);
            }
        });

        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent int4 = new Intent(Menu.this,select_loc.class);
                startActivity(int4);
            }
        });
/*
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent int3 = new Intent(Menu.this, Main2Activity.class);
                        startActivity(int3);
                    }
                });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent int4 = new Intent(Menu.this,select_loc.class);
                startActivity(int4);
            }
        });
 */
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}