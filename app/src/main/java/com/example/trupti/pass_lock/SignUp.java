package com.example.trupti.pass_lock;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class SignUp extends AppCompatActivity implements View.OnClickListener {



    EditText usernameEditText,passwordEditText,conPasswordEditText,secreteAnsEditText;
    Spinner spinnersecreteQuestion;
    Button btnsignup;
     String secreteQuestion;


  DataBaseHandler handle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);



        //Checking if activity is run First time

        Boolean isFirstRun=getSharedPreferences("pref",MODE_PRIVATE).getBoolean("isFirstRun",true);


        if(isFirstRun)
        {
            getSharedPreferences("pref",MODE_PRIVATE).edit().putBoolean("isFirstRun",false).apply();
        }
        else
        {
            Intent i = new Intent(this, Login.class);
            startActivity(i);
            this.finish();
        }

        handle=new DataBaseHandler(this,"password-locker",null,1);

        //find view by id
        //for EditText
        usernameEditText= (EditText) findViewById(R.id.editusername);
        passwordEditText= (EditText) findViewById(R.id.editpassword);
        conPasswordEditText= (EditText) findViewById(R.id.editconpass);
        secreteAnsEditText= (EditText) findViewById(R.id.editscreteans);

        //for Button
        btnsignup= (Button) findViewById(R.id.btnok);

        //for spinner
        spinnersecreteQuestion= (Spinner) findViewById(R.id.spinnerscrq);

        //Setting listener

        btnsignup.setOnClickListener(this);



    }

        //onClick  Defination

    @Override
    public void onClick(View v)
    {
        String userName,password,conPassword,answer;
        userName=usernameEditText.getText().toString();
        password=passwordEditText.getText().toString();
        conPassword=conPasswordEditText.getText().toString();
        answer=secreteAnsEditText.getText().toString();
        secreteQuestion=spinnersecreteQuestion.getSelectedItem().toString();

        //For validation

        if(usernameEditText.length()==0)
            {Toast.makeText(this,"Please Enter Username",Toast.LENGTH_LONG).show();}
        else
            if(passwordEditText.length()==0)
            {Toast.makeText(this,"Please Enter Password",Toast.LENGTH_LONG).show();}
        else if(conPasswordEditText.length()==0)
            {Toast.makeText(this,"Please Confirm Password",Toast.LENGTH_LONG).show();}
        else
                //If Security Question is not selected
            if(secreteQuestion.equals(getString(R.string.selectquestion)))
        {
            Toast.makeText(this,"Please Select Your Secret Question ",Toast.LENGTH_LONG).show();
        }
        else
        if(secreteAnsEditText.length()==0)
            {Toast.makeText(this,"Please Enter Secret Answer",Toast.LENGTH_LONG).show();}

        //For match password and confirm password
 else
    if(password.equals(conPassword)==false)
        {
            Toast.makeText(this,"Both Password fields should Be same",Toast.LENGTH_LONG).show();
        }

        //Calling insert method to insert data into table
        else
            {
            handle.insertSignupUser(userName, password,secreteQuestion,answer);
            Toast.makeText(this, "Welcome to Pass-Lock.....", Toast.LENGTH_LONG).show();

            //Going to Login activity and no  more displaying this activity
            this.finish();
            Intent i = new Intent(this, Login.class);
            startActivity(i);


        }

    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        getSharedPreferences("pref",MODE_PRIVATE).edit().putBoolean("isFirstRun",true).apply();

    }
}
