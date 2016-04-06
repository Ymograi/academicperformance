package com.example.abin.academicperformancecalulator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
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

public class ViewApi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_api);

        final LinearLayout root = (LinearLayout) findViewById(R.id.root_3);
        final String prog = getIntent().getStringExtra("prog");
        final String year = getIntent().getStringExtra("year");
        final String dept = getIntent().getStringExtra("dept");

        final TextView msg = (TextView)findViewById(R.id.message);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.URL))
                .build();
        GetApi getApi = retrofit.create(GetApi.class);

        Call<ResponseBody> call = getApi.getData(dept,prog,year);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
               try{
                   String result = response.body().string();
                   int end = result.lastIndexOf("}")+1;
                   int start = result.indexOf("{");
                   result = result.substring(start,end);
                   JSONObject resultJSONStr = new JSONObject(result);
//                   String errorCode = resultJSONStr.getString("error");
                   double api = resultJSONStr.getDouble("API");
                   double ap = resultJSONStr.getDouble("AP");
                   int valid = resultJSONStr.getInt("VALID");
                   int invalid = resultJSONStr.getInt("INVALID");

                   String message = "";

                   if(valid==0){
                       message = "There are no valid students with a verified CGPA";
                   }

                   else{
                       message = "The Academic Performance Index  of " + year + " " + prog + " of " + dept
                               + " is " + api +" and the AP is " + ap + ".\nThere are currently "
                               + valid +" students with a verified CGPA and " + invalid +
                               " students with a unverified CGPA or unset CGPA.";
                   }
                   msg.setText(message);
               }
               catch (Exception e){
                   Toast.makeText(ViewApi.this, e.toString(), Toast.LENGTH_SHORT).show();
               }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ViewApi.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onBackPressed() {
        this.finish();
        Intent i = new Intent(ViewApi.this,FaHomepage.class);
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
interface GetApi{
    @FormUrlEncoded
    @POST("api.php")
    Call<ResponseBody> getData(@Field("dept") String dept, @Field("prog") String prog, @Field("year") String year);
}
