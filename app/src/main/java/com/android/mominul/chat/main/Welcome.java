package com.android.mominul.chat.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.android.mominul.chat.R;

public class Welcome extends AppCompatActivity {

    TextView text;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        text = (TextView) findViewById(R.id.welcome);
        Bundle a = getIntent().getExtras();
        if(a!=null) {
            String b = a.getString("Name");
            text.setText("Welcome!!!\n" + b);
        }


    }

    public void conclicked(View view) {
        Intent i = new Intent(Welcome.this, Status.class);
        Bundle a = getIntent().getExtras();
        if(a!=null) {
            String b = a.getString("Name");
            i.putExtra("N", b);
            startActivity(i);
        }

    }

}
