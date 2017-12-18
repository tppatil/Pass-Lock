package com.example.trupti.pass_lock;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by PRAVIN on 18/10/2017.
 */

public class DataBaseHandler extends SQLiteOpenHelper {
    public DataBaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {

        db.execSQL("create table Signuptable (_id Integer primary key autoincrement,username text,password text,secquestion text,answer text)");
        db.execSQL("create table Securedata(_id Integer primary key autoincrement,secusername text,secpassword text,website text)");

        System.out.println("==============================================table cretated========================================================");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }



    public void insertSignupUser(String username,String password,String question,String answer)
    {
        SQLiteDatabase db=getWritableDatabase();
        ContentValues row=new ContentValues();

        //creating row to insert

        row.put("username",username);
        row.put("password",password);
        row.put("answer",answer);
        row.put("secquestion",question);

        //inserting values into Signuptable

        db.insert("Signuptable",null,row);

    }

    public Cursor read ()
    {
        Cursor cursor;
        SQLiteDatabase db=getReadableDatabase();
        cursor=db.query("Signuptable",null,null,null,null,null,null);
        cursor.moveToFirst();
        return cursor;
    }


    public int update(String s)
    {
        SQLiteDatabase db=getWritableDatabase();
        ContentValues updaterow=new ContentValues();
        updaterow.put("password",s);
        return db.update("Signuptable",updaterow,null,null);
    }

    //-------------------------------------------------------------------------------------------------------------------------------
    public void insertSecuredata(String secusername, String secweb, String secpass)
    {
        SQLiteDatabase secdb=getWritableDatabase();

        ContentValues secrow=new ContentValues();

        secrow.put("secusername",secusername);
        secrow.put("secpassword",secpass);
        secrow.put("website",secweb);

        secdb.insert("Securedata",null,secrow);

    }
    public Cursor secread()
    {
        Cursor seccurser;
        SQLiteDatabase secdbR=getReadableDatabase();
        seccurser=secdbR.query("Securedata",null,null,null,null,null,null);
        seccurser.moveToFirst();
        return seccurser;
    }


    public Cursor find(int id)
    {

        SQLiteDatabase db=getReadableDatabase();
        String []id1={id+""};
        Cursor c=db.query("Securedata",null,"_id=?",id1,null,null,null);
        c.moveToFirst();
        System.out.println("In find====================Cursor count="+c.getCount());
        return c;
    }

    public int updateSecuredata(String id, String username, String pass, String web)
    {
        SQLiteDatabase db=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("secusername",username);
        values.put("secpassword",pass);
        values.put("website",web);

        String id1[]={id};
        int u=db.update("Securedata",values,"_id=?",id1);
return u;

    }

    public int deleteSecureData(String id1)
    {
        SQLiteDatabase db=getWritableDatabase();
        String[]id12={id1};
        int no=db.delete("Securedata","_id=?",id12);
        return no;
    }

    String getUserName()
    {
        Cursor c=read();
        String name=c.getString(c.getColumnIndex("username"));
        return  name;

    }
}
