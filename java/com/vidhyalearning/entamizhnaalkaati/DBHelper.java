package com.vidhyalearning.entamizhnaalkaati;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by user on 09-Mar-18.
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String IMPDAYS_TABLE_NAME = "ImpDays";
    ArrayList<String> gMonthList = new ArrayList<>();
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 3);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table ImpDays " +
                        "( year text, month text,day text,dateOfDay date)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS Impdays");
        onCreate(db);

    }

    public boolean insertRecord (String year, String month, String day, String dateOfDay) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("year", year);
        contentValues.put("month", month);
        contentValues.put("day", day);
        contentValues.put("dateOfDay", dateOfDay);

        db.insert("Impdays", null, contentValues);
        return true;
    }
    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, IMPDAYS_TABLE_NAME);
        return numRows;
    }

    public int getData(String year) {
        //SQLiteDatabase db = this.getReadableDatabase();
        //Cursor res =  db.rawQuery( "select * from impdays where year="+year+"", null );
        String selectQuery = "SELECT  1 FROM  impdays  ";
        selectQuery = selectQuery + "where year=" + "'" + year + "'";

        Log.e("Here DB", selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        return(c.getCount());

    }

    public ArrayList<ImpDays> getAllDays(String year) {
        ArrayList<ImpDays> array_list = new ArrayList<ImpDays>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c =  db.rawQuery( "select * from impdays where year= "+ year + " order by  dateOfDay", null );
        c.moveToFirst();
        if (c.moveToFirst()) {
            do {
                ImpDays t = new ImpDays();
                t.setYear(c.getString((c.getColumnIndex("year"))));
                t.setMonth(c.getString(c.getColumnIndex("month")));
                String tempDate =  c.getString(c.getColumnIndex("dateOfDay"));
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy");
                try {
                    java.util.Date date = sdf.parse(tempDate);
                    t.setDateOfDay(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                t.setDay(c.getString(c.getColumnIndex("day")));

                array_list.add(t);
            } while (c.moveToNext());
        }

        if(c.isClosed()==false)
            c.close();
        db.close();

        return array_list;
    }

    public ArrayList<String> getAllDaysInMonth(String year,String month) {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c =  db.rawQuery( "select * from impdays where year= "+ year + " and month = "+ "'"  +month + "'" + "order by dateOfDay", null );
        c.moveToFirst();
        gMonthList.clear();
        if (c.moveToFirst()) {
            do {
               // ImpDays t = new ImpDays();

                String tempDate =  c.getString(c.getColumnIndex("dateOfDay"));
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM");
                String tempString = tempDate +" - " + c.getString(c.getColumnIndex("day"));
                gMonthList.add(tempDate.substring(0,2));
                array_list.add(tempString);
            } while (c.moveToNext());
        }
      //  gMonthList = array_list;
        if(c.isClosed()==false)
            c.close();
        db.close();

        return array_list;
    }

    public ArrayList<String> getMonthsList(){
        return gMonthList;
    }
    public Integer deleteRow (String date1,String notes  ) {
        SQLiteDatabase db = this.getWritableDatabase();

       return db.delete("ImpDays",
                "dateOfDay=? AND day = ?",
                new String[]{date1, notes});
    }

/*

    public boolean updateContact (Integer id, String name, String phone, String email, String street,String place) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("email", email);
        contentValues.put("street", street);
        contentValues.put("place", place);
        db.update("contacts", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteContact (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("contacts",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<String> getAllCotacts() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME)));
            res.moveToNext();
        }
        return array_list;
    }*/
}
