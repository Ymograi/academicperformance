package com.example.abin.academicperformancecalulator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class VerifiedStudents extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verified_students);
        final LinearLayout root = (LinearLayout) findViewById(R.id.root_3);
        final String prog = getIntent().getStringExtra("prog");
        final String year = getIntent().getStringExtra("year");
        final String dept = getIntent().getStringExtra("dept");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.URL))
                .build();
        verified v = retrofit.create(verified.class);
        Call<ResponseBody> call = v.getData(dept, prog, year);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try{
                    String result = response.body().string();
                    int end = result.lastIndexOf("}")+1;
                    int start = result.indexOf("{");
                    result = result.substring(start,end);
                    JSONObject resultJSONStr = new JSONObject(result);
                    String errorCode = resultJSONStr.getString("error");
                    String res = resultJSONStr.getString("students");

                    //res = res.substring(1,res.length()-1);
                    //Toast.makeText(VerifyCgpa1.this, res, Toast.LENGTH_SHORT).show();
                    // Toast.makeText(VerifyCgpa1.this, "hello sir", Toast.LENGTH_SHORT).show()
                    JSONArray students = new JSONArray(res);
//                    end=res.lastIndexOf("}"+1);
//                    start=res.indexOf("{");
//                    Toast.makeText(VerifyCgpa1.this, "After JSON array", Toast.LENGTH_SHORT).show();
                    final int nos = resultJSONStr.getInt("nos");


                    final TextView[] rolls = new TextView[nos];
                    final TextView[] cgpas = new TextView[nos];
                    final TextView[] names = new TextView[nos];
                    //final RadioGroup[] rad = new RadioGroup[nos];

                    for (int i = 0; i < nos; i++) {

//                        Toast.makeText(VerifyCgpa1.this,"Inside for loop",Toast.LENGTH_LONG).show();

                        JSONObject student_1 = students.getJSONObject(i);

                        String rollText = student_1.getString("roll_no");
                        String nameText = student_1.getString("name");
                        String cgpaText= student_1.getString("cgpa");

                        LinearLayout stud = new LinearLayout(VerifiedStudents.this);
                        stud.setOrientation(LinearLayout.HORIZONTAL);
                        TextView roll=new TextView(VerifiedStudents.this);
                        roll.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
                        TextView name=new TextView(VerifiedStudents.this);
                        TextView cgpa=new TextView(VerifiedStudents.this);
                        //RadioGroup zo=new RadioGroup(verified_students.this);
//                        Toast.makeText(VerifyCgpa1.this,"After views initialised",Toast.LENGTH_LONG).show();
                        //zo.setOrientation(LinearLayout.HORIZONTAL);
                        LinearLayout.LayoutParams Params1 = new LinearLayout.LayoutParams(190,45);
                        LinearLayout.LayoutParams Params2 = new LinearLayout.LayoutParams(190,45);
                        roll.setLayoutParams(Params1);
                        cgpa.setLayoutParams(Params2);
                        name.setLayoutParams(Params2);
                        //RadioButton zero = new RadioButton(verified_students.this);
                        //zero.setText("Invalid");

                        //name.setPadding(10, 10, 0, 0);
                        roll.setPadding(10, 10, 0, 0);
                        name.setPadding(0, 0, 0, 0);
                        cgpa.setPadding(50, 0, 0, 0);
                        //zo.setPadding(30, 0,0, 0);
                        roll.setText(rollText);
                        name.setText(nameText);
                        cgpa.setText(cgpaText);

                        //RadioButton one = new RadioButton(verified_students.this);
                        //one.setId();
                        //one.setText("Valid");
                       // zo.addView(zero);
//                        Toast.makeText(VerifyCgpa1.this,"After zero add",Toast.LENGTH_LONG).show();
                        //zo.addView(one);
                        //zo.check(zero.getId());
//                        Toast.makeText(VerifyCgpa1.this,"After one add",Toast.LENGTH_LONG).show();
//                        Toast.makeText(VerifyCgpa1.this,"After zo add",Toast.LENGTH_LONG).show();
                        stud.addView(name);
                        stud.addView(roll);
//                        Toast.makeText(VerifyCgpa1.this,"After roll add",Toast.LENGTH_LONG).show();
                        //stud.addView(name);
                        stud.addView(cgpa);
//                        Toast.makeText(VerifyCgpa1.this,"After num add", Toast.LENGTH_LONG).show();
                        //stud.addView(zo);
                        root.addView(stud);
                        rolls[i]=roll;
                        names[i]=name;
                        cgpas[i]=cgpa;
                        //rad[i]=zo;
                    }

                }
                catch (Exception e){
                    Toast.makeText(VerifiedStudents.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(VerifiedStudents.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        this.finish();
        Intent i = new Intent(VerifiedStudents.this,FaHomepage.class);
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




interface verified
{
    @FormUrlEncoded
    @POST("verified_list.php")
    Call<ResponseBody> getData(@Field("dept") String dept, @Field("prog") String prog, @Field("year") String year);
}
