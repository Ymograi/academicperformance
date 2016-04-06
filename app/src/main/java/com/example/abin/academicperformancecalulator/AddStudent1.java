package com.example.abin.academicperformancecalulator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddStudent1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student1);

        final EditText nos = (EditText)findViewById(R.id.nos);
        Button addstuds = (Button)findViewById(R.id.addstudents);

        final String username = getIntent().getStringExtra("username");
        final String prog = getIntent().getStringExtra("prog");
        final String year = getIntent().getStringExtra("year");
        final String dept = getIntent().getStringExtra("dept");
//        Toast.makeText(AddStudent1.this, "Dept in AddStud1 is " + dept, Toast.LENGTH_LONG).show();
        addstuds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean valid = true;

                if(nos.getText().toString().equals("")||Integer.parseInt(nos.getText().toString())<=0)
                {
                    valid = false;
                    nos.setError("Enter a number greater than 0");
                }

                if(valid)
                {
                    String nosText = nos.getText().toString();

                    Intent i = new Intent(AddStudent1.this,AddStudent2.class);
                    i.putExtra("nos",Integer.parseInt(nosText));
                    i.putExtra("username",username);
                    i.putExtra("prog",prog);
                    i.putExtra("year",year);
                    i.putExtra("dept",dept);
                    startActivity(i);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        this.finish();
        Intent i = new Intent(AddStudent1.this,FaHomepage.class);
        final String username = getIntent().getStringExtra("username");
        final String prog = getIntent().getStringExtra("prog");
        final String year = getIntent().getStringExtra("year");
        final String dept = getIntent().getStringExtra("dept");
        i.putExtra("username",username);
        i.putExtra("prog",prog);
        i.putExtra("year",year);
        i.putExtra("dept",dept);
        startActivity(i);
        super.onBackPressed();
    }
}
