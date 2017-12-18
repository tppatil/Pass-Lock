package com.example.trupti.pass_lock;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CategoriesActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView allDetailsListView;
    MyCursorAdapter adapter;
    DataBaseHandler categoriesHandle;
    Cursor categoriesCur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        allDetailsListView = (ListView) findViewById(R.id.listview1);

        categoriesHandle = new DataBaseHandler(this, "password-locker", null, 1);

        allDetailsListView.setOnItemClickListener(this);
        categoriesCur = categoriesHandle.secread();

        adapter = new MyCursorAdapter(this, categoriesCur);
        allDetailsListView.setAdapter(adapter);

        Boolean isFirstrunfordialog=getSharedPreferences("pref",MODE_PRIVATE).getBoolean("isFirstrunfordialog",true);

if(isFirstrunfordialog)
{
    AlertDialog.Builder builder=new AlertDialog.Builder(this);
    builder.setTitle("Note: ");
    builder.setMessage("To add new Entry press the (+) button.");
    builder.create().show();
   getSharedPreferences("pref",MODE_PRIVATE).edit().putBoolean("isFirstrunfordialog",false).apply();

}



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //menu.add(21,11,1,"Add Entry");

        MenuInflater mi=getMenuInflater();
        mi.inflate(R.menu.mymenu,menu);

    //    menu.add(21,12,2,"Reset Password");
     //   menu.add(21,13,3,"About");
       // menu.add(21,14,4,"Lock");


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Add:  Intent newEntryIntent = new Intent(this, NewEntry.class);
                      this.finish();
                      startActivity(newEntryIntent);
                break;

            case R.id.reset:



                final AlertDialog.Builder  resetbuilder=new AlertDialog.Builder(this);
                resetbuilder.setTitle("Reset Password");

                LayoutInflater resetinflater= LayoutInflater.from(this);
                View v= resetinflater.inflate(R.layout.reset_pass,null);
                resetbuilder.setView(v);

                final EditText newpass=(EditText)v.findViewById(R.id.editnewpass);
                final EditText connewpass=(EditText)v.findViewById(R.id.editconnewpass);
                final EditText oldpass=(EditText)v.findViewById(R.id.editoldpass);

                resetbuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                resetbuilder.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                        Cursor c=categoriesHandle.read();
                        c.moveToFirst();
                        if(oldpass.length()==0)
                        {Toast.makeText(CategoriesActivity.this, "Please enter old password", Toast.LENGTH_SHORT).show();}
                        else
                        if(newpass.length()==0)
                        {
                            Toast.makeText(CategoriesActivity.this,"Please enter new password ", Toast.LENGTH_SHORT).show();
                        }
                        else if(connewpass.length()==0)
                        {
                            Toast.makeText(CategoriesActivity.this, "Please confirm password", Toast.LENGTH_SHORT).show();
                        }
                        else

                            if(c.getString(c.getColumnIndex("password")).equals(oldpass.getText().toString())) {

                                if (newpass.getText().toString().equals(connewpass.getText().toString())) {

                                    int i = categoriesHandle.update(newpass.getText().toString());
                                    if (i == 1) {
                                        Toast.makeText(CategoriesActivity.this, "New password is set", Toast.LENGTH_SHORT).show();
                                        // password=newpass.getText().toString();
                                    } else {
                                        Toast.makeText(CategoriesActivity.this, "Try again", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    Toast.makeText(CategoriesActivity.this, "password not matches", Toast.LENGTH_SHORT).show();

                                }

                            }
                            else
                            {
                                Toast.makeText(CategoriesActivity.this, c.getString(c.getColumnIndex("password"))+"          "+(oldpass.getText())+"Please Enter Correct Password", Toast.LENGTH_SHORT).show();
                            }
                    }
                });


                final AlertDialog resetdg=resetbuilder.create();
                resetdg.show();
                Toast.makeText(CategoriesActivity.this, "Your answer is correct", Toast.LENGTH_SHORT).show();

                break;
            case R.id.about:
                Intent i=new Intent(this,AboutActivity.class);
                startActivity(i);
                break;
            case R.id.lock:
                Intent i1 =new Intent(this,Login.class);
                this.finish();
                startActivity(i1);




        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        getSharedPreferences("pref",MODE_PRIVATE).edit().putBoolean("isFirstRunSingleData",true).apply();
       /* Intent i=new Intent(this,SingleDataInfo.class);
        startActivity(i);*/

        Intent  categoriesIntent2=new Intent(this,SingleDataInfo.class);

        int id1=categoriesCur.getInt(0);
        categoriesIntent2.putExtra("id",id1+"");
        this.finish();
        startActivity(categoriesIntent2);
    }
}
    class MyCursorAdapter extends CursorAdapter {


        public MyCursorAdapter(Context context, Cursor c) {
            super(context, c, 0);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);
            View v = inflater.inflate(R.layout.categories_row, parent, false);
            return v;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

           TextView username_or_email = (TextView) view.findViewById(R.id.textView);

             username_or_email.setText(cursor.getString(cursor.getColumnIndex("secusername")));

            /*String password, web;
            password = cursor.getString(cursor.getColumnIndex("secpassword"));
            web = cursor.getString(cursor.getColumnIndex("website"));*/



        }
    }

