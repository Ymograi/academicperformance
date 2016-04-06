package com.example.abin.academicperformancecalulator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class VerifyCgpa1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_cgpa1);
        setTitle("Verify CGPA");
        final LinearLayout root = (LinearLayout) findViewById(R.id.root_2);
        final String username = getIntent().getStringExtra("username");
        final String prog = getIntent().getStringExtra("prog");
        final String year = getIntent().getStringExtra("year");
        final String dept = getIntent().getStringExtra("dept");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.URL))
                .build();
        final VerifyCgpa ver=retrofit.create(VerifyCgpa.class);
        Call<ResponseBody> call = ver.getData(dept, prog, year);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try{
                    String result = response.body().string();
                    int end = result.lastIndexOf("}")+1;
                    int start = result.indexOf("{");
                    result = result.substring(start,end);
                    //Toast.makeText(VerifyCgpa1.this, result, Toast.LENGTH_SHORT).show();
                    JSONObject resultJSONStr = new JSONObject(result);
                    String errorCode = resultJSONStr.getString("error");
                    final int nos = resultJSONStr.getInt("nos");
                    String res = "";
                    if(nos!=0) {
                        res = resultJSONStr.getString("students");

                        //res = res.substring(1,res.length()-1);
                        //Toast.makeText(VerifyCgpa1.this, res, Toast.LENGTH_SHORT).show();
                        // Toast.makeText(VerifyCgpa1.this, "hello sir", Toast.LENGTH_SHORT).show()
                        JSONArray students = new JSONArray(res);
//                    end=res.lastIndexOf("}"+1);
//                    start=res.indexOf("{");
//                    Toast.makeText(VerifyCgpa1.this, "After JSON array", Toast.LENGTH_SHORT).show();


                        final TextView[] rolls = new TextView[nos];
                        final TextView[] cgpas = new TextView[nos];
                        final TextView[] names = new TextView[nos];
                        final RadioGroup[] rad = new RadioGroup[nos];

                        for (int i = 0; i < nos; i++) {

//                        Toast.makeText(VerifyCgpa1.this,"Inside for loop",Toast.LENGTH_LONG).show();

                            JSONObject student_1 = students.getJSONObject(i);

                            String rollText = student_1.getString("roll_no");
                            String nameText = student_1.getString("name");
                            String cgpaText = student_1.getString("cgpa");

                            LinearLayout stud = new LinearLayout(VerifyCgpa1.this);
                            stud.setOrientation(LinearLayout.HORIZONTAL);
                            TextView roll = new TextView(VerifyCgpa1.this);
                            roll.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
                            TextView name = new TextView(VerifyCgpa1.this);
                            TextView cgpa = new TextView(VerifyCgpa1.this);
                            RadioGroup zo = new RadioGroup(VerifyCgpa1.this);
//                        Toast.makeText(VerifyCgpa1.this,"After views initialised",Toast.LENGTH_LONG).show();
                            zo.setOrientation(LinearLayout.HORIZONTAL);
                            LinearLayout.LayoutParams Params1 = new LinearLayout.LayoutParams(170, 45);
                            LinearLayout.LayoutParams Params2 = new LinearLayout.LayoutParams(100, 45);
                            roll.setLayoutParams(Params1);
                            cgpa.setLayoutParams(Params2);
                            name.setLayoutParams(Params2);
                            RadioButton zero = new RadioButton(VerifyCgpa1.this);
                            zero.setText("Invalid");

                            //name.setPadding(10, 10, 0, 0);
                            roll.setPadding(10, 10, 0, 0);
                            //name.setPadding(15, 15, 15, 15);
                            cgpa.setPadding(20, 0, 0, 0);
                            zo.setPadding(10, 0, 0, 0);
                            roll.setText(rollText);
                            name.setText(nameText);
                            cgpa.setText(cgpaText);

                            RadioButton one = new RadioButton(VerifyCgpa1.this);
                            //one.setId();
                            one.setText("Valid");
                            zo.addView(zero);
//                        Toast.makeText(VerifyCgpa1.this,"After zero add",Toast.LENGTH_LONG).show();
                            zo.addView(one);
                            zo.check(zero.getId());
//                        Toast.makeText(VerifyCgpa1.this,"After one add",Toast.LENGTH_LONG).show();
//                        Toast.makeText(VerifyCgpa1.this,"After zo add",Toast.LENGTH_LONG).show();
                            stud.addView(roll);
//                        Toast.makeText(VerifyCgpa1.this,"After roll add",Toast.LENGTH_LONG).show();
                            //stud.addView(name);
                            stud.addView(cgpa);
//                        Toast.makeText(VerifyCgpa1.this,"After num add", Toast.LENGTH_LONG).show();
                            stud.addView(zo);
                            root.addView(stud);
                            rolls[i] = roll;
                            //names[i]=name;
                            cgpas[i] = cgpa;
                            rad[i] = zo;
                        }
                        Button submit = new Button(VerifyCgpa1.this);
                        submit.setText("Submit");
                        root.addView(submit);

                        submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String[] rollInfo = new String[nos];
                                int[] validStatuses = new int[nos];

                               //Toast.makeText(VerifyCgpa1.this, Integer.toString(nos), Toast.LENGTH_SHORT).show();

                                int i;
                                for (i = 0; i < nos; i++) {
                                    rollInfo[i] = rolls[i].getText().toString();
                                    //Toast.makeText(VerifyCgpa1.this, rolls[i].getText().toString(), Toast.LENGTH_SHORT).show();
                                    int selectedValidtype = rad[i].getCheckedRadioButtonId();
                                    RadioButton checkedvalidtype = (RadioButton) findViewById(selectedValidtype);
                                    String validity = checkedvalidtype.getText().toString();

                                    if (validity.equalsIgnoreCase("Valid")) {
                                        validStatuses[i] = 1;
                                    }
                                    else {
                                        validStatuses[i] = 0;
                                    }
                                }

                                Intent j = new Intent(VerifyCgpa1.this, VerifyCgpa2.class);

                                j.putExtra("rolls", rollInfo);
                                j.putExtra("valid", validStatuses);
                                j.putExtra("username",username);
                                j.putExtra("prog",prog);
                                j.putExtra("year",year);
                                j.putExtra("dept",dept);

                                startActivity(j);

                            }
                        });

                    }
                    else{
                        TextView tv = new TextView(VerifyCgpa1.this);
                        tv.setText("There are no unverified students with a valid CGPA.");
                        root.addView(tv);
                    }
                }

                catch (Exception e){
                    Toast.makeText(VerifyCgpa1.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(VerifyCgpa1.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onBackPressed() {
        this.finish();
        Intent i = new Intent(VerifyCgpa1.this,FaHomepage.class);
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
interface VerifyCgpa {
    @FormUrlEncoded
    @POST("verify_cgpa_1.php")
    Call<ResponseBody> getData(@Field("dept") String dept, @Field("prog") String prog, @Field("year") String year);
}
