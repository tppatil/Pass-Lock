package com.example.trupti.pass_lock;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateEntry extends AppCompatActivity implements View.OnClickListener {


    EditText boxusername,boxpass,boxweb;
    Button btnupadate;
    DataBaseHandler updateHandler;

    String  user,pass,web;
    int passlength;
    String id;
    int isUpdate=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_entry);

        boxusername=(EditText) findViewById(R.id.editTextusername);
        boxpass=(EditText) findViewById(R.id.editTextpass);
        boxweb=(EditText) findViewById(R.id.editTextweb);
        btnupadate=(Button)findViewById(R.id.editbtn);

        updateHandler=new DataBaseHandler(this, "password-locker", null, 1);
        btnupadate.setOnClickListener(this);


        Intent i=getIntent();
        user=i.getStringExtra("username");
        pass=i.getStringExtra("password");
        web=i.getStringExtra("web");

        boxusername.setText(user);
        boxpass.setText(pass);
        boxweb.setText(web);
        passlength= boxpass.length();
        id=i.getStringExtra("id");


    }

    @Override
    public void onBackPressed() {
        getSharedPreferences("pref",MODE_PRIVATE).edit().putBoolean("isFirstRunSingleData",true).apply();
        Intent i= new Intent(this,SingleDataInfo.class);
        i.putExtra("id_from_update",id);
        i.putExtra("passlength",passlength+"");
        this.finish();
        startActivity(i);


        super.onBackPressed();
    }

    @Override
    public void onClick(View v)
    {

if(boxweb.length()==0 || boxpass.length()==0||boxusername.length()==0)
{
    Toast.makeText(this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
}
else
{

  isUpdate=updateHandler.updateSecuredata(id,boxusername.getText().toString(),boxpass.getText().toString(),boxweb.getText().toString());

    if(isUpdate==1)
    {   getSharedPreferences("pref",MODE_PRIVATE).edit().putBoolean("isFirstRunSingleData",false).apply();
        Toast.makeText(this, "Entry updated Successfully... ", Toast.LENGTH_SHORT).show();

        Intent i=new Intent(this,CategoriesActivity.class);

        Cursor c=updateHandler.find(Integer.parseInt(id));

        user=c.getString(c.getColumnIndex("secusername"));
        pass=c.getString(c.getColumnIndex("secpassword"));
        web=c.getString(c.getColumnIndex("website"));

        this.finish();
        startActivity(i);


    }
else
{
    Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show();
}
}

    }


}
