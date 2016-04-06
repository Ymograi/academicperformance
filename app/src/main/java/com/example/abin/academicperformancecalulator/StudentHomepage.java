package com.example.abin.academicperformancecalulator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class StudentHomepage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_homepage);

        final String name = getIntent().getStringExtra("name");
        final String prog = getIntent().getStringExtra("prog");
        final String year = getIntent().getStringExtra("year");
        final String dept = getIntent().getStringExtra("dept");
        final String roll_no = getIntent().getStringExtra("roll_no");
        final int valid = getIntent().getIntExtra("valid", 0);
        TextView stud_name = (TextView) findViewById(R.id.student_name);
        final EditText cgpa = (EditText)findViewById(R.id.cgpa);
        Button submit_cgpa = (Button)findViewById(R.id.submit_cgpa);

        stud_name.setText("Welcome, "+name);

        if(valid==1){
            ((ViewManager)cgpa.getParent()).removeView(cgpa);
            ((ViewManager)submit_cgpa.getParent()).removeView(submit_cgpa);
            stud_name.setText("Welcome, "+name+".\n\n\nYour CGPA has been successfully verified.");
        }

        else
        {
            submit_cgpa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean valid = true;
                    double cgpaText=-1.0;

                    if (cgpa.getText().toString().equals("")) {
                        valid = false;
                        cgpa.setError("Enter a CPGA between 0 and 10");
                    }
                    else
                        cgpaText= Double.parseDouble(cgpa.getText().toString());

                    if(cgpaText < 0.0 || cgpaText > 10.0) {
                        valid = false;
                        cgpa.setError("Enter a CPGA between 0 and 10");
                    }

                    if (valid) {
                        String rollText = roll_no;

                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(getResources().getString(R.string.URL))
                                .build();

                        EnterCgpa enterCgpa = retrofit.create(EnterCgpa.class);
                        Call<ResponseBody> call = enterCgpa.getData(rollText, cgpaText);

                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                try {
                                    String result = response.body().string();
                                    int end = result.lastIndexOf("}") + 1;
                                    int start = result.indexOf("{");
                                    result = result.substring(start, end);
                                    JSONObject resultJSONStr = new JSONObject(result);
                                    String errorCode = resultJSONStr.getString("error");
                                    Toast.makeText(StudentHomepage.this, result, Toast.LENGTH_SHORT).show();
                                    if(errorCode.equalsIgnoreCase("FALSE")){
                                        String rollResponse = resultJSONStr.getString("roll_no");
                                        double cgpaResponse = resultJSONStr.getDouble("cgpa");

                                        Intent i = new Intent(StudentHomepage.this,CgpaConfirm.class);

                                        i.putExtra("roll_no",rollResponse);
                                        i.putExtra("cgpa",cgpaResponse);
                                        i.putExtra("name",name);

                                        startActivity(i);
                                    }
                                    else{
                                        Toast.makeText(StudentHomepage.this, resultJSONStr.getString("err_msg"), Toast.LENGTH_SHORT).show();
                                    }
                                }
                                catch (Exception e) {
                                    Toast.makeText(StudentHomepage.this, e.toString(), Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(StudentHomepage.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

            });


        }
    }
    public void onBackPressed() {
        this.finish();
        Intent i = new Intent(StudentHomepage.this,MainActivity.class);
        startActivity(i);
        super.onBackPressed();
    }
}
interface EnterCgpa{
    @FormUrlEncoded
    @POST("enter_cgpa.php")
    Call<ResponseBody> getData(@Field("roll_no") String roll_no,@Field("cgpa") double cgpa);
}