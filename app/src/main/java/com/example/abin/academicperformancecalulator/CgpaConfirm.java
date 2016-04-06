package com.example.abin.academicperformancecalulator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class CgpaConfirm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cgpa_confirm);
        setTitle("CGPA Updated");
        final String name = getIntent().getStringExtra("name");
        final String roll_no = getIntent().getStringExtra("roll_no");
        final double cgpa = getIntent().getDoubleExtra("cgpa", -1.0);

        TextView message = (TextView)findViewById(R.id.message);

        if(cgpa >= 0.0 && cgpa <=10){
            message.setText(name+"'s (roll number "+roll_no+") CGPA has been updated to "+cgpa);
        }
        else{
            message.setText(name+" 's (roll number "+roll_no+") CGPA  could not be updated. Try agin later.");
        }
    }
    @Override
    public void onBackPressed() {
        this.finish();
        Intent i = new Intent(CgpaConfirm.this,MainActivity.class);
        startActivity(i);
        super.onBackPressed();
    }
}
