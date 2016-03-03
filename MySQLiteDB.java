package com.afufu.controlbodyfat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by user on 2015/8/13.
 */

public class MySQLiteDB extends SQLiteOpenHelper{

    private static final String DbName="Record_CBFapp";
    private static final int DbVersion=1;
    private static final String TableName="BodyFatRecord";
    private SimpleDateFormat sdf;

    private static final String DB_TableCreate=
            "CREATE TABLE  "+ TableName +"  ( " +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "Date INTEGER NOT NULL," +
            "Weight REAL  NOT NULL," +
            "FatRate REAL  NOT NULL," +
            "FatWeight REAL  NOT NULL," +
            "Waistline REAL  NOT NULL," +
            "Hips REAL  NOT NULL);" ;

    public MySQLiteDB(Context context) {
        super(context,DbName, null, DbVersion);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_TableCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
/*
    public long insert(long date,double weight,double fatrate, double fatweight , double waistline,double hips){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("Date",date);
        cv.put("Weight",weight);
        cv.put("FatRate",fatrate);
        cv.put("FatWeight",fatweight);
        cv.put("Waistline",waistline);
        cv.put("Hips",hips);
        long row=db.insert(TableName,null,cv);
        return row;         //成功回傳row  失敗回傳-1
    }

    public long update(int id,long date,double weight,double fatrate, double fatweight , double waistline,double hips){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("Date",date);
        cv.put("Weight",weight);
        cv.put("FatRate",fatrate);
        cv.put("FatWeight",fatweight);
        cv.put("Waistline",waistline);
        cv.put("Hips",hips);
        long row=db.update(TableName, cv, "_id=" + id, null);
        return row;
    }

    public void delete(int id){
        SQLiteDatabase db=this.getWritableDatabase();
        String where="_id = "+Integer.toString(id);
        db.delete(TableName, where, null);
    }
*/
    //如果日期不存在就新增資料  日期存在就更新資料
    public long add(SQLiteDatabase db,long date,double weight,double fatrate, double fatweight , double waistline, double hips){
        ArrayList<Long> DateList=getDateList(db);
        boolean isNew=true;
        long row;

        sdf=new SimpleDateFormat("yyyy/MM/dd");
        String Date=sdf.format(new Date(date));
        //檢查輸入之日期是否存在資料庫中
        for(int i=0;i<DateList.size();i++)
        {
            String temp=sdf.format(DateList.get(i));
            if(Date.equals(temp)){
                isNew=false;
                break;
            }
        }
        ContentValues cv=new ContentValues();
        cv.put("Date",date);
        cv.put("Weight",weight);
        cv.put("FatRate",fatrate);
        cv.put("FatWeight",fatweight);
        cv.put("Waistline",waistline);
        cv.put("Hips",hips);

        if(isNew==false)
        {
            row=db.update(TableName,cv,"Date= "+date,null);
        }
        else
        {
            row=db.insert(TableName,null,cv);
        }
        return row;
    }

    //取得資料庫內所有日期之資料列
    public ArrayList<Long> getDateList(SQLiteDatabase db) {
        ArrayList<Long> DateList = new ArrayList<Long>();
        Cursor c = db.rawQuery("Select Date from " + TableName, null);
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++) {
            int dateIndex = c.getColumnIndex("Date");
            long Date = c.getLong(dateIndex);
            DateList.add(Date);
            c.moveToNext();
        }
        return DateList;
    }

    //取得日期最早的資料 如果沒有資料就回傳-1
    public long getFirstDate(SQLiteDatabase db){
        long firstday ;
        ArrayList<Long> DateList = new ArrayList<Long>();
        DateList=getDateList(db);
        if(DateList.isEmpty()){
            firstday=-1;
        }
        else {
            firstday=DateList.get(0);
            for(int i=0;i<DateList.size();i++) {        //資料並不一定按照目前時間書入資料庫 所以要確認所有資料的最早日期為何
                if(DateList.get(i)<firstday) {
                    firstday = DateList.get(i);
                }
            }
        }

        return firstday;
    }

    //取得資料庫內所有資料
    public List<BodyData> getCbfData(SQLiteDatabase db){
        List<BodyData> BDList=new ArrayList<BodyData>();
        String[] columns = {"_id","Date","Weight", "FatRate","FatWeight","Waistline","Hips" };

        Cursor c=db.query(TableName,columns,null,null,null,null,null);
        while (c.moveToNext()){
            int id=c.getInt(0);
            long date=c.getLong(1);
            double weight=c.getDouble(2);
            double fatrate=c.getDouble(3);
            double fatweight=c.getDouble(4);
            double waistline=c.getDouble(5);
            double hips=c.getDouble(6);
            BodyData body=new BodyData(id,date,weight,fatrate,fatweight,waistline,hips);
            BDList.add(body);
        }
        return BDList;
    }

    public BodyData getSearch(SQLiteDatabase db,long day){
        BodyData data;
        ArrayList<Long> DateList=getDateList(db);
        boolean isExist=true;
        long row;

        sdf=new SimpleDateFormat("yyyy/MM/dd");
        String Date=sdf.format(new Date(day));

        for(int i=0;i<DateList.size();i++)
        {
            String temp=sdf.format(DateList.get(i));
            if(Date.equals(temp)){
                day=DateList.get(i);
                break;
            }
        }
        Cursor c=db.rawQuery("Select * from "+TableName+" where Date="+day+";",null);
        c.moveToFirst();
        int id=c.getInt(0);
        long date=c.getLong(1);
        double weight=c.getDouble(2);
        double fatrate=c.getDouble(3);
        double fatweight=c.getDouble(4);
        double waistline=c.getDouble(5);
        double hips=c.getDouble(6);
        data=new BodyData(id,date,weight,fatrate,fatweight,waistline,hips);

        return data;
    }


    public Boolean checkDate(SQLiteDatabase db,long date){
        ArrayList<Long> DateList=getDateList(db);
        sdf=new SimpleDateFormat("yyyy/MM/dd");
        String Date=sdf.format(new Date(date));

        boolean isExist=false;
        //檢查輸入之日期是否存在資料庫中
        for(int i=0;i<DateList.size();i++)
        {
            String temp=sdf.format(DateList.get(i));
            if(Date.equals(temp)){
                isExist=true;
                break;
            }
        }
       return isExist;
    }

}
