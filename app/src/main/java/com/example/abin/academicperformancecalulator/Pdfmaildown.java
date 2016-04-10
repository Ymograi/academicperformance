package com.example.abin.academicperformancecalulator;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class Pdfmaildown extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfmaildown);
        final String user = getIntent().getStringExtra("username");
        final String prog = getIntent().getStringExtra("prog");
        final String year = getIntent().getStringExtra("year");
        final String dept = getIntent().getStringExtra("dept");
        final EditText ema=(EditText)findViewById(R.id.sendemail);
        TextView fa=(TextView)findViewById(R.id.fa);
        fa.setText("Welcome Faculty Advisor, " + user);
        final TextView fin = (TextView) findViewById(R.id.Fin);

        Button send=(Button)findViewById(R.id.button);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pattern pattern = Patterns.EMAIL_ADDRESS;
                final String em=ema.getText().toString();
                if (pattern.matcher(em).matches() || em.equals("")){
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(getResources().getString(R.string.URL))
                        .build();
                Pdf pdf = retrofit.create(Pdf.class);
                Call<ResponseBody> call = pdf.getData(dept, prog, year, em);

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            String result = response.body().string();
//                            Toast.makeText(Pdfmaildown.this, result, Toast.LENGTH_LONG).show();
                            int end = result.lastIndexOf("}") + 1;
                            int start = result.indexOf("{");
                            result = result.substring(start, end);
                            //Toast.makeText(FaHomepage.this, result, Toast.LENGTH_SHORT).show();
                            JSONObject resultJSONStr = new JSONObject(result);
                            String errorCode = resultJSONStr.getString("error");
//                                    Toast.makeText(AddStudent2.this, result, Toast.LENGTH_SHORT).show();
                            if (errorCode.equalsIgnoreCase("FALSE")) {
                                if(!em.equals("")) {
                                    String as = "The email has been sent to " + em + ". Thank you, " + user + ".";
                                    fin.setText(as);
                                }
                                else{
                                    fin.setText("The PDF file has been downloaded.");
                                }
                                Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.URL) + "/doc.pdf"));
                                startActivity(browser);

                            } else {
                                Toast.makeText(Pdfmaildown.this, resultJSONStr.getString("error_msg"), Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception e) {
                            Toast.makeText(Pdfmaildown.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(Pdfmaildown.this, t.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else {
                    ema.setError("Please Enter a Valid Email or Leave it Blank to download the file");
                }
            }
            });
    }
}
interface Pdf{
    @FormUrlEncoded
    @POST("make_pdf_file.php")
    Call<ResponseBody> getData(@Field("dept") String dept, @Field("prog") String prog, @Field("year") String year,@Field("email")String email);}