package com.example.abin.academicperformancecalulator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FaHomepage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fa_homepage);

        final String username = getIntent().getStringExtra("username");
        final String prog = getIntent().getStringExtra("prog");
        final String year = getIntent().getStringExtra("year");
        final String dept = getIntent().getStringExtra("dept");

//        Toast.makeText(FaHomepage.this, "Dept in FaHomepage is " + dept, Toast.LENGTH_LONG).show();
        TextView adminName = (TextView) findViewById(R.id.admin_name);
        adminName.setText("Welcome, " + username);
        setTitle("Faculty Advisor Homepage");

        Button addstud = (Button)findViewById(R.id.add_students);
        addstud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FaHomepage.this, AddStudent1.class);
                i.putExtra("username", username);
                i.putExtra("prog", prog);
                i.putExtra("year", year);
                i.putExtra("dept", dept);
                startActivity(i);
            }
        });
        Button verify_cgpa = (Button)findViewById(R.id.verify_cgpa);
        verify_cgpa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FaHomepage.this, VerifyCgpa1.class);
                i.putExtra("username",username);
                i.putExtra("prog",prog);
                i.putExtra("year",year);
                i.putExtra("dept",dept);
                startActivity(i);
            }
        });
        Button view_api = (Button)findViewById(R.id.view_api);
        view_api.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FaHomepage.this, ViewApi.class);
                i.putExtra("username",username);
                i.putExtra("prog",prog);
                i.putExtra("year",year);
                i.putExtra("dept",dept);
                startActivity(i);
            }
        });
        Button verified_students=(Button)findViewById(R.id.verified);
        verified_students.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FaHomepage.this,VerifiedStudents.class);
                i.putExtra("username",username);
                i.putExtra("prog",prog);
                i.putExtra("year",year);
                i.putExtra("dept",dept);
                startActivity(i);
            }
        });
    }
    public void onBackPressed() {
        this.finish();
        Intent i = new Intent(FaHomepage.this,MainActivity.class);
        startActivity(i);
        super.onBackPressed();
    }
}
