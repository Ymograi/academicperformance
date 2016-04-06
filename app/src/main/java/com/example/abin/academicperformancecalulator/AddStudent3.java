package com.example.abin.academicperformancecalulator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AddStudent3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student3);

        int nos = getIntent().getIntExtra("nos",0);
        int duplicate = getIntent().getIntExtra("duplicate",0);
        String[] duplicateRolls = getIntent().getStringArrayExtra("duplicateRolls");

        String message = "";

        if(nos!=0){
            message += nos +" student(s) have been successfully added to the database.";
        }

        if(duplicate!=0){
            message += "\n There were "+duplicate+" students who were already existing in the database and were not added.\nThey are:\n"+duplicateRolls[0];

            int i;
            for(i=1;i<duplicate;i++){
                message +=" ,"+duplicateRolls[i];
            }
        }

        RelativeLayout root = (RelativeLayout)findViewById(R.id.root);
        TextView result = new TextView(AddStudent3.this);
        result.setText(message);
        root.addView(result);
    }
    @Override
    public void onBackPressed() {
        this.finish();
        Intent i = new Intent(AddStudent3.this,FaHomepage.class);
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
