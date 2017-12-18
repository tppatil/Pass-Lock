package com.example.trupti.pass_lock;

import android.app.Application;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class SingleDataInfo extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    DataBaseHandler singleDatabaseinfoHandler;
    Cursor c;
    String id1,isUpdate="0";
     int id;
    static int count=0;
   static Intent i;
    ClipboardManager clipboard;
    ClipData clip;
    int vs=1;


    TextView usernamtText, passwordText, webText,nameText;
   String username,pass,web;
    Button useAppbtn,btnpassvisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_data_info);

        Boolean isFirstRunSingleData=getSharedPreferences("pref",MODE_PRIVATE).getBoolean("isFirstRunSingleData",true);


        singleDatabaseinfoHandler = new DataBaseHandler(this, "password-locker", null, 1);


       usernamtText = (TextView) findViewById(R.id.username);
        passwordText = (TextView) findViewById(R.id.pass);
        webText = (TextView) findViewById(R.id.web);
        nameText = (TextView) findViewById(R.id.name);
        useAppbtn=(Button)findViewById(R.id.button2) ;
        btnpassvisible=(Button)findViewById(R.id.button);

        usernamtText.setOnLongClickListener(this);
        passwordText.setOnLongClickListener(this);
        btnpassvisible.setOnClickListener(this);


        webText.setOnClickListener(this);
        useAppbtn.setOnClickListener(this);
        nameText.setText(singleDatabaseinfoHandler.getUserName());





       if(isFirstRunSingleData)
        {
            i = getIntent();
            id1 = i.getStringExtra("id");

                if(id1!=null) {


                          id = Integer.parseInt(id1);
                          c = singleDatabaseinfoHandler.find(id);
                           username = c.getString(c.getColumnIndex("secusername"));
                           pass = c.getString(c.getColumnIndex("secpassword"));
                            web = c.getString(c.getColumnIndex("website"));
                            usernamtText.setText(username);
                            passwordText.setText(pass);
                            webText.setText(web);
                    String star="*";
                    for(int i=1;i<passwordText.length();i++)
                    {
                        star=star+"*";

                    }
                    passwordText.setText(star);
                            }
                            else
                {
                    //if user click edit and press back button without editing data
                    Intent i =getIntent();
                    id1=i.getStringExtra("id_from_update");


                    if(id1!=null)
                    {
                        id = Integer.parseInt(id1);
                        c = singleDatabaseinfoHandler.find(id);
                        username = c.getString(c.getColumnIndex("secusername"));
                       pass = c.getString(c.getColumnIndex("secpassword"));
                        web = c.getString(c.getColumnIndex("website"));
                        usernamtText.setText(username);
                       passwordText.setText(pass);
                        webText.setText(web);
                        String star="*";
                        for(int i1=1;i1<Integer.parseInt(i.getStringExtra("passlength"));i1++)
                        {
                            star=star+"*";

                        }
                        passwordText.setText(star);
                    }

                    else
                    {
                        Toast.makeText(this, "Try again", Toast.LENGTH_SHORT).show();

                                         }

                }

        }
        else
        {
                Intent i = getIntent();

                username = i.getStringExtra("username");
                pass = i.getStringExtra("pass");
                web = i.getStringExtra("web");

                usernamtText.setText(username);
                passwordText.setText(pass);
                webText.setText(web);

              getSharedPreferences("pref", MODE_PRIVATE).edit().putBoolean("isFirstRunSingleData", true).apply();


        }



    }





    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(this,CategoriesActivity.class);

        startActivity(i);
        this.finish();    }



    @Override
    public void onClick(View v)
    {


            if(R.id.web==v.getId())
            {
                    //Uri uri= Uri.parse("com.android.chrome/com.google.android.apps.chrome.Main");
                    String url = "http://"+web;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
            }
            else
        if(R.id.button2==v.getId())
            {
                    Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
                   mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                    startActivity(mainIntent);
            }
            else
            if(vs==0) {
                btnpassvisible.setBackgroundResource(R.drawable.invisiblepass);
                String star="*";
                for(int i=1;i<passwordText.length();i++)
                {
                    star=star+"*";

                }
                passwordText.setText(star);
                vs=1;
            }
            else {

                btnpassvisible.setBackgroundResource(R.drawable.visiblepass);
                passwordText.setText(pass);
                vs=0;
            }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //menu.add(1,11,1,"Edit").setIcon(R.drawable.deleticon);
      //  menu.add(1,12,2,"Delete").setIcon(R.drawable.editicon);
        MenuInflater mi= getMenuInflater();
        mi.inflate(R.menu.singledatatmenu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {



        switch (item.getItemId())
        {
            case R.id.edit:

                Intent i=new Intent(this,UpdateEntry.class);
                i.putExtra("username",username);
                i.putExtra("password",pass);
                i.putExtra("web",web);
                i.putExtra("id",id1);
                this.finish();
                startActivity(i);

                break;
            case R.id.delete:
                    AlertDialog.Builder builder=new AlertDialog.Builder(this);
                    builder.setTitle("Delete Entry?");

                    builder.setMessage("Really delete Entry"+" "+singleDatabaseinfoHandler.getUserName()+"?");

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.dismiss();

                                }
                            });


                    builder.setPositiveButton("Delete Entry", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            int no= singleDatabaseinfoHandler.deleteSecureData(id1);
                            if(no==1) {
                                Toast.makeText(SingleDataInfo.this, "Entry Deleted", Toast.LENGTH_LONG).show();
                                usernamtText.setText("");
                                passwordText.setText("");
                                webText.setText("");

                                Intent i1 = new Intent(SingleDataInfo.this, CategoriesActivity.class);
                                SingleDataInfo.this.finish();
                                startActivity(i1);

                            }
                            else
                            {

                                Toast.makeText(SingleDataInfo.this, "Try again", Toast.LENGTH_SHORT).show();}
                        }
                         });


                    Dialog dg=builder.create();
                    dg.show();

                }







        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onLongClick(View v) {


        if (v.getId() == R.id.pass) {

           // Toast.makeText(this, "Longpress", Toast.LENGTH_SHORT).show();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Copy hiddden Field to Clipboard?");
            builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.setPositiveButton("Copy", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    clip = ClipData.newPlainText("password", passwordText.getText());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(SingleDataInfo.this, "Text are copy to text board", Toast.LENGTH_SHORT).show();

                }
            });


            Dialog dg = builder.create();
            dg.show();


        }
        else
        {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Copy hiddden Field to Clipboard?");
            builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.setPositiveButton("Copy", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    clip = ClipData.newPlainText("password", usernamtText.getText());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(SingleDataInfo.this, "Text are copy to clipboard", Toast.LENGTH_SHORT).show();

                }
            });
            Dialog dg = builder.create();
            dg.show();

        }

        return false;

    }
}
