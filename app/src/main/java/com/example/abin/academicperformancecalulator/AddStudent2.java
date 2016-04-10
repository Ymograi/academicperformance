package com.example.abin.academicperformancecalulator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

public class AddStudent2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student2);

        final int nos = getIntent().getIntExtra("nos",0);
//        Toast.makeText(this,"nos is "+nos,Toast.LENGTH_LONG).show();
        LinearLayout root = (LinearLayout)findViewById(R.id.root);

        final String username = getIntent().getStringExtra("username");
        final String prog = getIntent().getStringExtra("prog");
        final String year = getIntent().getStringExtra("year");
        final String dept = getIntent().getStringExtra("dept");
        //Toast.makeText(AddStudent2.this,"Dept in AddStud2 is "+ dept,Toast.LENGTH_LONG).show();

        if(nos!=0){
//            Toast.makeText(this,"Inside if",Toast.LENGTH_LONG).show();
            int i;//Loop counter
            int next = 0;//for next button work
            final EditText[] rolls = new EditText[nos];
            final EditText[] names = new EditText[nos];
            final EditText[] emails = new EditText[nos];

            for(i=0;i<nos;i++){
               LinearLayout stud = new LinearLayout(AddStudent2.this);
//                Toast.makeText(this,"Inside for",Toast.LENGTH_LONG).show();
                //stud.setId(i);
                TextView no = new TextView(this);
                EditText roll = new EditText(this);
                EditText name = new EditText(this);
                EditText email = new EditText(this);


                roll.setHint("Roll Number " + (i + 1));
                name.setHint("Name "+(i + 1));
                email.setHint("Email " + (i + 1));
                no.setText(Integer.toString(i+1));

                name.setId(next++);
                name.setNextFocusDownId(next);
                roll.setId(next++);
                roll.setNextFocusDownId(next);
                email.setId(next++);
                email.setNextFocusDownId(next);

                roll.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                name.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                email.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS);

                stud.addView(no);
                stud.addView(name);
                stud.addView(roll);
                stud.addView(email);

                root.addView(stud);

                names[i] = name;
                rolls[i] = roll;
                emails[i] = email;
            }

            Button submit = new Button(this);
            submit.setText("Submit");
            root.addView(submit);
            final boolean[] valid2 = new boolean[1];

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean valid = false;

//                    Toast.makeText(AddStudent2.this,"On click",Toast.LENGTH_LONG).show();
                    int i;//loop counter
                    String[] namesText = new String[nos];//text to be sent to server
                    String[] rollsText = new String[nos];
                    String[] emailsText = new String[nos];
                    int count = 0;//to count number of valid entries

                    for(i=0;i<nos;i++){

                        if(!names[i].getText().toString().trim().equals("")
                                &&!rolls[i].getText().toString().trim().equals("")
                                &&!emails[i].getText().toString().trim().equals("")) {
                            //if all are not null for a particular student
//                            Toast.makeText(AddStudent2.this,"Inside for if",Toast.LENGTH_LONG).show();

                            valid = true;//Check email
                            valid2[0]=false;

                            Pattern pattern = Patterns.EMAIL_ADDRESS;
                            if (!pattern.matcher(emails[i].getText()).matches()) {
                                valid = false;
                                emails[i].setError("Enter a valid email address.");
                            }
                            if (valid) {
                                //Atleast 1 entry is valid
                                valid2[0] = true;
                                namesText[count] = names[i].getText().toString().trim();//get the text and store in array to send
                                rollsText[count] = rolls[i].getText().toString().trim();
                                emailsText[count] = emails[i].getText().toString().trim();
                                count++;//increment valid entry count
                            }
                            else
                            {
                                Toast.makeText(AddStudent2.this,"There are no valid entries",Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    if(valid2[0]){
                        //Send to server

//                        Toast.makeText(AddStudent2.this,"Inside valid if",Toast.LENGTH_LONG).show();

                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(getResources().getString(R.string.URL))
                                .build();
                        AddStudents addStuds = retrofit.create(AddStudents.class);
                        Call<ResponseBody> call = addStuds.getData(namesText,rollsText,emailsText,dept,prog,year);


                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                try{
                                    String result = response.body().string();
                                    Toast.makeText(AddStudent2.this, result, Toast.LENGTH_LONG).show();
                                    int end = result.lastIndexOf("}")+1;
                                    int start = result.indexOf("{");
                                    result = result.substring(start,end);
                                    JSONObject resultJSONStr = new JSONObject(result);
                                    String errorCode = resultJSONStr.getString("error");
//                                    Toast.makeText(AddStudent2.this, result, Toast.LENGTH_SHORT).show();
                                    if(errorCode.equalsIgnoreCase("FALSE")){
                                        int nos = resultJSONStr.getInt("nos");
                                        int duplicate = resultJSONStr.getInt("duplicate");

                                        Intent i = new Intent(AddStudent2.this,AddStudent3.class);

                                        if(duplicate!=0) {
                                            String[] duplicateRolls = new String[duplicate];

                                            int j;

                                            for (j = 0; j < duplicate; j++) {
                                                duplicateRolls[j] = resultJSONStr.getString("Duplicate" + j);
                                            }
                                            i.putExtra("duplicateRolls",duplicateRolls);
                                        }


                                        i.putExtra("nos",nos);
                                        i.putExtra("duplicate",duplicate);
                                        i.putExtra("username",username);
                                        i.putExtra("prog",prog);
                                        i.putExtra("year",year);
                                        i.putExtra("dept",dept);


                                        startActivity(i);


                                    }
                                    else{
                                        Toast.makeText(AddStudent2.this,resultJSONStr.getString("error_msg"),Toast.LENGTH_SHORT).show();
                                    }
                                }
                                catch (Exception e){
                                    Toast.makeText(AddStudent2.this, e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(AddStudent2.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else{
                        Toast.makeText(AddStudent2.this,"There are no valid entries",Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        this.finish();
        Intent i = new Intent(AddStudent2.this,FaHomepage.class);
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



interface AddStudents{
    @FormUrlEncoded
    @POST("students_3_newcode.php")
    Call<ResponseBody> getData(@Field("name[]") String name_text[],@Field("roll[]") String roll_text[], @Field("email[]") String email[],
                               @Field("dept") String dept, @Field("prog") String prog, @Field("year") String year);
}
