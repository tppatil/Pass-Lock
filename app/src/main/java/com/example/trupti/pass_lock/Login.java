package com.example.trupti.pass_lock;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity implements View.OnClickListener {

    DataBaseHandler loginHandle;
    TextView tvWelcome,tvfp;
    Button btnUnlock;
    EditText passtext;
    String username,password,answer,question;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tvWelcome =(TextView)findViewById(R.id.tvwel);
        btnUnlock = (Button) findViewById(R.id.btnunlcok);
        passtext = (EditText) findViewById(R.id.editText);
        tvfp=(TextView)findViewById(R.id.tvforgotpassword);

        btnUnlock.setOnClickListener(this);
        tvfp.setOnClickListener(this);

        loginHandle = new DataBaseHandler(this, "password-locker", null, 1);
        display();
    }


    void display() {
        Cursor c;
        c = loginHandle.read();


        username = c.getString(c.getColumnIndex("username"));
        password=c.getString(c.getColumnIndex("password"));
       question=c.getString(c.getColumnIndex("secquestion"));
        answer=c.getString(c.getColumnIndex("answer"));


       /* System.out.println("Name=" + username);
        System.out.println("password=" + password);
        System.out.println("Question=" + question );
        System.out.println("Answer=" + answer );
*/

        tvWelcome.setText(tvWelcome.getText().toString() + "  " + username + " !");

    }

    @Override
    public void onClick(View v) {
if(v.getId()==R.id.btnunlcok) {

    if (passtext.length() == 0)
        Toast.makeText(this, "Please Enter password", Toast.LENGTH_SHORT).show();
    else if (password.equals(passtext.getText().toString()))
    {
        passtext.setText("");

        Intent savePassIntent=new Intent(Login.this,CategoriesActivity.class);

        Toast.makeText(this, "Login successful", Toast.LENGTH_LONG).show();
        getSharedPreferences("pref",MODE_PRIVATE).edit().putBoolean("isFirstrunfordialog",true).apply();
        startActivity(savePassIntent);
        this.finish();

    }
    else
        Toast.makeText(this, "Please Enter correct password", Toast.LENGTH_SHORT).show();

}
else


if(v.getId()==R.id.tvforgotpassword)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);

        builder.setTitle("Answer Your Security Question");


        LayoutInflater inflate= LayoutInflater.from(this);
        View diaologview=inflate.inflate(R.layout.security_question_dialog,null);
        builder.setView(diaologview);

        TextView tvSecurityQuestion= (TextView) diaologview.findViewById(R.id.tvdsq);
        final  EditText edAnswer=(EditText)diaologview.findViewById(R.id.editdsa);

        tvSecurityQuestion.setText(question);

        builder.setNegativeButton("Cancel", new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                 builder.setPositiveButton("OK", new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                                 if(edAnswer.length()==0)
                            Toast.makeText(Login.this, "Please Enter Answer",
                                    Toast.LENGTH_SHORT).show();
                        else
                        if(edAnswer.getText().toString().equals(answer))
                        {
                            final AlertDialog.Builder  resetbuilder=new AlertDialog.Builder(Login.this);
                            resetbuilder.setTitle("Reset Password");
                            LayoutInflater resetinflater= LayoutInflater.from(Login.this);

                            View v= resetinflater.inflate(R.layout.reset_dialog_box,null);

                            resetbuilder.setView(v);

                            final EditText newpass=(EditText)v.findViewById(R.id.editnewpass);
                            final EditText connewpass=(EditText)v.findViewById(R.id.editconnewpass);


                            resetbuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                            resetbuilder.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                                @Override
                                    public void onClick(DialogInterface dialog, int which)
                                {
                                    if(newpass.length()==0)
                                    {
                                        Toast.makeText(Login.this, "Please enter new password ", Toast.LENGTH_SHORT).show();
                                    }
                                    else if(connewpass.length()==0)
                                    {
                                        Toast.makeText(Login.this, "Please confirm password", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                        if(newpass.getText().toString().equals(connewpass.getText().toString()))
                                        {

                                           int i= loginHandle.update(newpass.getText().toString());
                                            if(i==1)
                                            {
                                                Toast.makeText(Login.this, "New password is set", Toast.LENGTH_SHORT).show();
                                                password=newpass.getText().toString();
                                            }
                                            else
                                            {
                                                Toast.makeText(Login.this,"ERROR", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                        else
                                            {
                                                    Toast.makeText(Login.this, "password not matches", Toast.LENGTH_SHORT).show();

                                            }

                                }
                            });


                            final AlertDialog resetdg=resetbuilder.create();
                            resetdg.show();

                        }
                        else
                        {

                            Toast.makeText(Login.this, "Please enter correct answer", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            builder.setCancelable(false);
        AlertDialog dg=builder.create();
        dg.show();
}
       }

}