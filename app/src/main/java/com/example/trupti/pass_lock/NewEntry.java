package com.example.trupti.pass_lock;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewEntry extends AppCompatActivity implements View.OnClickListener {

    Button addBtn;
    EditText usernameBox,passwordBox,webBox;
    DataBaseHandler newEntryHandle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_entry);

        addBtn= (Button)findViewById(R.id.addbtn);
        usernameBox= (EditText) findViewById(R.id.editusername);
        passwordBox= (EditText) findViewById(R.id.editpass);
        webBox= (EditText) findViewById(R.id.editweb);

        addBtn.setOnClickListener(this);
      newEntryHandle= new DataBaseHandler(this,"password-locker", null, 1);

    }

    @Override
    public void onClick(View v)
    {

        if(usernameBox.length()==0 && passwordBox.length()==0 && webBox.length()==0 )
        {
            Toast.makeText(this, "Please enter all details", Toast.LENGTH_SHORT).show();
        }
        else {

            String secusername, secpass, secweb;
            secpass = passwordBox.getText().toString();
            secusername = usernameBox.getText().toString();
            secweb = webBox.getText().toString();

            newEntryHandle.insertSecuredata(secusername, secweb, secpass);

            Toast.makeText(this, "Entry Created Successfully", Toast.LENGTH_LONG).show();

            Cursor seccur = newEntryHandle.secread();

            System.out.println("" + seccur.getString(seccur.getColumnIndex("secusername")));
            System.out.println("" + seccur.getString(seccur.getColumnIndex("secpassword")));
            System.out.println("" + seccur.getString(seccur.getColumnIndex("website")));

            Intent newEntryIntent=new Intent(this,CategoriesActivity.class);
            this.finish();
            startActivity(newEntryIntent);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i=new Intent(this,CategoriesActivity.class);
        startActivity(i);
    }
}
