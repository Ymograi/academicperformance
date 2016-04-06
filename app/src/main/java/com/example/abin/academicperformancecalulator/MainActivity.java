package com.example.abin.academicperformancecalulator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Welcome User!");

        final TextView username = (TextView)findViewById(R.id.username);
        final TextView password = (TextView)findViewById(R.id.password);
        final RadioGroup usertype = (RadioGroup)findViewById(R.id.usertype);
        Button login = (Button) findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean valid=true;

                if(username.getText().toString().trim().equals("")){
                    username.setError("Please enter username.");
                    valid=false;
                }

                if(password.getText().toString().equals("")){
                    password.setError("Please enter password.");
                    valid=false;
                }

                int selectedUsertype = usertype.getCheckedRadioButtonId();
                RadioButton checkedusertype = (RadioButton)findViewById(selectedUsertype);

                if(valid){
//                    Toast.makeText(MainActivity.this, "Inside valid", Toast.LENGTH_SHORT).show();

                    final String usernameText = username.getText().toString();
                    String passwordText = password.getText().toString();
                    String usertypeText = checkedusertype.getText().toString();

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(getResources().getString(R.string.URL))
                            .build();
                    LoginUser lin = retrofit.create(LoginUser.class);
                    Call<ResponseBody> call = lin.getData(usernameText,passwordText,usertypeText);

                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try{
//                                Toast.makeText(MainActivity.this, "Inside call.enqueue", Toast.LENGTH_SHORT).show();
                                String result = response.body().string();
                                Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
                                int end = result.lastIndexOf("}")+1;
                                int start = result.indexOf("{");
                                result = result.substring(start,end);
                                JSONObject resultJSONStr = new JSONObject(result);//JSON object created
                                String errorCode = resultJSONStr.getString("error");
//                                Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
                                if(errorCode.equalsIgnoreCase("FALSE")){
                                    String userTypeLogin = resultJSONStr.getString("type");
//                                    Toast.makeText(MainActivity.this, userTypeLogin, Toast.LENGTH_SHORT).show();
                                    if(userTypeLogin.equals("Faculty Advisor")){
//                                        Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
                                        String prog = resultJSONStr.getString("prog");
                                        String year = resultJSONStr.getString("year");
                                        String dept = resultJSONStr.getString("dept");
                                        Intent i = new Intent(MainActivity.this,FaHomepage.class);
                                        i.putExtra("username",usernameText);
                                        i.putExtra("prog",prog);
                                        i.putExtra("year",year);
                                        i.putExtra("dept",dept);
                                        startActivity(i);
                                    }
                                    else{
                                        String prog = resultJSONStr.getString("prog");
                                        String year = resultJSONStr.getString("year");
                                        String dept = resultJSONStr.getString("dept");
                                        String name = resultJSONStr.getString("name");
                                        int valid = resultJSONStr.getInt("valid");
                                        String roll_no = resultJSONStr.getString("roll_no");

                                        Intent i = new Intent(MainActivity.this,StudentHomepage.class);
                                        i.putExtra("prog",prog);
                                        i.putExtra("year",year);
                                        i.putExtra("dept",dept);
                                        i.putExtra("name",name);
                                        i.putExtra("roll_no",roll_no);
                                        i.putExtra("valid",valid);

                                        startActivity(i);
                                    }
                                }
                                else{
                                    String errorMessage = resultJSONStr.getString("error_msg");
                                    Toast.makeText(MainActivity.this,errorMessage,Toast.LENGTH_LONG).show();
                                }
                            }
                            catch (Exception e){
                                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();

                            }

                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                           Toast.makeText(MainActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }



            }
        });

    }

    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
interface LoginUser{
    @FormUrlEncoded
    @POST("login.php")
    Call<ResponseBody> getData(@Field("username") String uname_text,@Field("password") String pass_text, @Field("type") String type);
}
