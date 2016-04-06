package com.example.abin.academicperformancecalulator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class VerifyCgpa2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_cgpa2);

//        final TextView message = (TextView) findViewById(R.id.message);

        String rolls[] = getIntent().getStringArrayExtra("rolls");
        int valid[] = getIntent().getIntArrayExtra("valid");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.URL))
                .build();
        SendVerify ver2 = retrofit.create(SendVerify.class);
//        Toast.makeText(VerifyCgpa2.this,"Length of rolls is " + rolls.length,Toast.LENGTH_LONG).show();
        Call<ResponseBody> call = ver2.getData(rolls, valid);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try{
                    String result = response.body().string();
//                    Toast.makeText(VerifyCgpa2.this,"This is the result:\n" + result,Toast.LENGTH_LONG).show();
                    int end = result.lastIndexOf("}")+1;
                    int start = result.indexOf("{");
                    result = result.substring(start,end);

                    JSONObject resultJSONStr = new JSONObject(result);
                    String errorCode = resultJSONStr.getString("error");
                    String msg = "";
                    if(errorCode.equalsIgnoreCase("FALSE")){
                        int uCount = resultJSONStr.getInt("update_count");
                        int nCount = resultJSONStr.getInt("no_count");

                        msg = uCount + " students were marked valid and " + nCount + " were marked invalid.";
                    }
                    else{
                        String errorMessage = resultJSONStr.getString("err_msg");

                        if(errorMessage.equalsIgnoreCase("One or more entries could not be updated."))
                        {
                            int uCount = resultJSONStr.getInt("update_count");
                            int nCount = resultJSONStr.getInt("no_count");

                            msg = errorMessage + "\n" + uCount + " student(s) was/were marked valid and " + nCount + " was/were marked invalid.";
                        }
                        else{
                            msg = errorMessage;
                        }
                    }
                    TextView message = (TextView) findViewById(R.id.message);
                    message.setText(" ");
                    message.setText(msg);
                }
                catch (Exception e){
                    Toast.makeText(VerifyCgpa2.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(VerifyCgpa2.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onBackPressed() {
        this.finish();
        final String username = getIntent().getStringExtra("username");
        final String prog = getIntent().getStringExtra("prog");
        final String year = getIntent().getStringExtra("year");
        final String dept = getIntent().getStringExtra("dept");
        Intent i = new Intent(VerifyCgpa2.this,FaHomepage.class);
        i.putExtra("username",username);
        i.putExtra("prog",prog);
        i.putExtra("year",year);
        i.putExtra("dept",dept);
        startActivity(i);
        super.onBackPressed();
    }
}
interface SendVerify{
    @FormUrlEncoded
    @POST("verify_cgpa_2.php")
    Call<ResponseBody> getData(@Field("rolls[]") String rolls[], @Field("valid[]") int validStatuses[]);
}