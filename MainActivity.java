package com.afufu.controlbodyfat;


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.Date;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

public class MainActivity extends ActionBarActivity {

    private CalendarView myCalendarView;
    private ImageButton IBnSetting, IBnRecord,IBnChart;
    private TextView recordShow;
    private SimpleDateFormat sdf;
    private NumberFormat nf;
    private String Date;

   public SQLiteDatabase db;
    public MySQLiteDB MyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myCalendarView = (CalendarView)findViewById(R.id.calendar);
        IBnSetting = (ImageButton)findViewById(R.id.iBtn_setting);
        IBnRecord = (ImageButton)findViewById(R.id.iBtn_add_record);
        IBnChart = (ImageButton)findViewById(R.id.iBtn_charts);
        recordShow=(TextView)findViewById(R.id.tV_ShowRecord);
        sdf=new SimpleDateFormat("yyyy/MM/dd");
        nf=NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);


        myCalendarView.setDate(myCalendarView.getDate());
        long day=myCalendarView.getDate();
        Date=sdf.format(new Date(day));

        MyDB=new MySQLiteDB(this);
        db = MyDB.getWritableDatabase();
        Boolean check=MyDB.checkDate(db,day);
    //    recordShow.setText(day+"\n"+Date);
     if(check==true) {
            BodyData data=MyDB.getSearch(db,myCalendarView.getDate());
            recordShow.setText(Date + "\n" +
                    "體重: " +nf.format(data.getWeight())  + " kg\n體脂肪率: " + nf.format(data.getFatRate() )+ " %\n體脂肪重: " + nf.format(data.getFatWeight()) + " kg\n腰圍: " +nf.format( data.getWaistline()) + " 吋\n臀圍: " + nf.format(data.getHips()) + "吋");
        }
        else{
            recordShow.setText(Date + "\n" +
                    "沒有記錄!! ");
        }
        db.close();

        IBnSetting.setOnClickListener(BtnSet);
        IBnRecord.setOnClickListener(BtnRec);
        IBnChart.setOnClickListener(BtnCha);
        myCalendarView.setOnDateChangeListener(DCRec);


    }

    private OnClickListener BtnSet=new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent();
            intent.setClass(MainActivity.this,SettingBasicActivity.class);
            startActivity(intent);
            MainActivity.this.finish();
        }
    };

    private OnClickListener BtnCha=new OnClickListener() {
        @Override
        public void onClick(View v) {
            Bundle bund=new Bundle();
            long day;
            day=System.currentTimeMillis();
            bund.putLong("Date", day);

            Intent intent=new Intent();
            intent.setClass(MainActivity.this,KgChartShowActivity.class);
            intent.putExtras(bund);
            startActivity(intent);
            MainActivity.this.finish();
        }
    };

    private OnClickListener BtnRec=new OnClickListener() {
        @Override
        public void onClick(View v) {
            Bundle bund=new Bundle();
            long day;

            day=myCalendarView.getDate();
            Date=sdf.format(new Date(day));

            bund.putString("StringDate", Date);
            bund.putLong("Date", day);

            Intent intent=new Intent();
            intent.setClass(MainActivity.this,RecordEditActivity.class);
            intent.putExtras(bund);
            startActivity(intent);
            MainActivity.this.finish();

        }
    };


    private OnDateChangeListener DCRec=new OnDateChangeListener() {
        @Override
        public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
            long day=myCalendarView.getDate();
            Date=sdf.format(new Date(day));
            nf.setMaximumFractionDigits(2);
            MyDB=new MySQLiteDB(MainActivity.this);
            db = MyDB.getWritableDatabase();

            Boolean check=MyDB.checkDate(db,day);

           if(check==true) {
               BodyData data=MyDB.getSearch(db,myCalendarView.getDate());
                recordShow.setText(year + " 年  " + (month + 1) + " 月  " + dayOfMonth + " 日  " + "\n" +
                        "體重: " + nf.format(data.getWeight()) + " kg\n體脂肪率: " + nf.format(data.getFatRate()) + " %\n體脂肪重: " + nf.format(data.getFatWeight()) + " kg\n腰圍: " + nf.format(data.getWaistline()) + " 吋\n臀圍: " +nf.format( data.getHips()) + "吋");
           }
            else{
                recordShow.setText(year + " 年  " + (month + 1) + " 月  " + dayOfMonth + " 日  " + "\n" +
                        "沒有記錄噢! ");
            }
            db.close();
        }
    };
}

