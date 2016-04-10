package com.example.abin.academicperformancecalulator;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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
        Button pdfdown=(Button)findViewById(R.id.buttonpdf);
        pdfdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(FaHomepage.this,Pdfmaildown.class);
                i.putExtra("username",username);
                i.putExtra("prog",prog);
                i.putExtra("year",year);
                i.putExtra("dept",dept);
                startActivity(i);
//                Retrofit retrofit = new Retrofit.Builder()
//                        .baseUrl(getResources().getString(R.string.URL))
//                        .build();
//                Pdf pdf = retrofit.create(Pdf.class);
//                Call<ResponseBody> call = pdf.getData(dept,prog,year);
//
//                call.enqueue(new Callback<ResponseBody>() {
//                    @Override
//                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                        try{
//                            String result = response.body().string();
////                                    Toast.makeText(AddStudent2.this, result, Toast.LENGTH_LONG).show();
//                            int end = result.lastIndexOf("}")+1;
//                            int start = result.indexOf("{");
//                            result = result.substring(start,end);
//                            //Toast.makeText(FaHomepage.this, result, Toast.LENGTH_SHORT).show();
//                            JSONObject resultJSONStr = new JSONObject(result);
//                            String errorCode = resultJSONStr.getString("error");
////                                    Toast.makeText(AddStudent2.this, result, Toast.LENGTH_SHORT).show();
//                            if(errorCode.equalsIgnoreCase("FALSE")){
//                                Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.URL)+"/doc.pdf"));
//                                startActivity(browser);
//
//                        }
//                            else{
//                                Toast.makeText(FaHomepage.this,resultJSONStr.getString("error_msg"),Toast.LENGTH_SHORT).show();
//                            }
//                    }
//                        catch (Exception e){
//                            Toast.makeText(FaHomepage.this, e.toString(), Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//                    }
//                });

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

//interface Pdf{
//    @FormUrlEncoded
//    @POST("make_pdf_file.php")
//    Call<ResponseBody> getData(@Field("dept") String dept, @Field("prog") String prog, @Field("year") String year);
//}
