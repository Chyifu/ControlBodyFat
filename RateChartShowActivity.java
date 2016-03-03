package com.afufu.controlbodyfat;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by user on 2015/8/18.
 */
public class RateChartShowActivity  extends ActionBarActivity {


    SQLiteDatabase db;
    MySQLiteDB MyDB;
    private SimpleDateFormat sdf, Smonth, SDate;
    private TextView DateTitle;
    private ImageButton IBnLeftMenu, IBnRightMenu, IBnLeftMon, IBnRightMon;

    protected int[] MonthDay = new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    private LineChart kgchart;
    ArrayList<String> xVals = new ArrayList<String>();
    ArrayList<Entry> yVals = new ArrayList<Entry>();
    List<BodyData> MonthData = new ArrayList<BodyData>();
    private long firstday;
    private int year, month;
    private Date showDay;
    private Calendar calender;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_chart_rate);

        Bundle bund = this.getIntent().getExtras();
        long Date = bund.getLong("Date");

        kgchart = (LineChart) findViewById(R.id.chart_line);
        DateTitle = (TextView) findViewById(R.id.tV_dateTitle);
        IBnLeftMenu = (ImageButton) findViewById(R.id.imb_left_menu);
        IBnRightMenu = (ImageButton) findViewById(R.id.imb_right_menu);
        IBnLeftMon = (ImageButton) findViewById(R.id.imb_left_month);
        IBnRightMon = (ImageButton) findViewById(R.id.imb_right_month);

        MyDB = new MySQLiteDB(this);
        db = MyDB.getWritableDatabase();
        firstday = MyDB.getFirstDate(db);
        if (firstday == -1) {
            firstday = Date;
        }
        sdf = new SimpleDateFormat("yyyy/MM");
        DateTitle.setText(sdf.format(new Date(firstday)));
        MonthData = getNeedBodydata(firstday);

        Date dat = new Date(firstday);
        calender = Calendar.getInstance();
        calender.setTime(dat);

        Smonth = new SimpleDateFormat("MM");
        month = Integer.valueOf(Smonth.format(new Date(firstday)));
        LineData data = getData(MonthData, month);
        kgchart.setDrawGridBackground(false);
        kgchart.setData(data);
        IBnLeftMenu.setOnClickListener(BtnLeftMenu);
        IBnRightMenu.setOnClickListener(BtnRightMenu);
        IBnLeftMon.setOnClickListener(BtnLeftMon);
        IBnRightMon.setOnClickListener(BtnRightMon);
    }

    private View.OnClickListener BtnLeftMenu=new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Bundle bund=new Bundle();
            long day;
            day=System.currentTimeMillis();
            bund.putLong("Date", day);

            Intent intent=new Intent();
            intent.setClass(RateChartShowActivity.this,KgChartShowActivity.class);
            intent.putExtras(bund);
            startActivity(intent);
            RateChartShowActivity.this.finish();
        }
    };

    private View.OnClickListener BtnRightMenu=new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Bundle bund=new Bundle();
            long day;
            day=System.currentTimeMillis();
            bund.putLong("Date", day);

            Intent intent=new Intent();
            intent.setClass(RateChartShowActivity.this,WaliHipsChartShowActivity.class);
            intent.putExtras(bund);
            startActivity(intent);
            RateChartShowActivity.this.finish();
        }
    };

    private View.OnClickListener BtnLeftMon=new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            MonthData.clear();
            kgchart.clear();
            xVals.clear();
            yVals.clear();
            calender.add(Calendar.MONTH,-1);
            Date dat=calender.getTime();

            sdf=new SimpleDateFormat("yyyy/MM");
            DateTitle.setText(sdf.format(dat));

            MonthData=getNeedBodydata(dat.getTime());   // date to long

            Smonth=new SimpleDateFormat("MM");
            month=Integer.valueOf(Smonth.format(dat));
            LineData data=getData(MonthData, month);

            kgchart.setData(data);
        }
    };

    private View.OnClickListener BtnRightMon=new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            MonthData.clear();
            kgchart.clear();
            xVals.clear();
            yVals.clear();
            calender.add(Calendar.MONTH,1);
            Date dat=calender.getTime();

            sdf=new SimpleDateFormat("yyyy/MM");
            DateTitle.setText(sdf.format(dat));
            MonthData=getNeedBodydata(dat.getTime());   // date to long

            Smonth=new SimpleDateFormat("MM");
            month=Integer.valueOf(Smonth.format(dat));
            LineData data=getData(MonthData, month);
            kgchart.setData(data);
        }
    };

    private List<BodyData> getNeedBodydata(long Date)
    {
        List<BodyData> BDList=new ArrayList<BodyData>();
        ArrayList<Long> DateList = new ArrayList<Long>();
        DateList=MyDB.getDateList(db);

        sdf=new SimpleDateFormat("yyyy/MM");
        String day=sdf.format(new Date(Date));


        for(int i=0;i<DateList.size();i++) {
            String temp=sdf.format(DateList.get(i));
            if(day.equals(temp)) {
                BDList.add(MyDB.getSearch(db,DateList.get(i)));
            }
        }
        return BDList;
    }

    private LineData getData(List<BodyData> monthdata, int Month)
    {
        SimpleDateFormat Sday;
        LineData data;
        for(int i=0;i<MonthDay[Month-1];i++) {
            xVals.add(i+1+"");
        }
        Sday=new SimpleDateFormat("dd");
        int monthDay;
        double rate;

        for(int i=1;i<MonthDay[Month-1];i++){
            for(int j=0;j<monthdata.size();j++){
                long D=monthdata.get(j).getDate();
                monthDay=Integer.valueOf(Sday.format(new Date(D)));
                if(i==monthDay){
                    rate=monthdata.get(j).getFatRate();
                    yVals.add(new Entry((float) rate,monthDay));
                    break;
                }
            }
        }


        LineDataSet set1=new LineDataSet(yVals,"Fat Rate (%)");
        set1.setLineWidth(3);
        set1.setCircleSize(3f);
        set1.setColor(Color.GRAY);
        set1.setHighLightColor(Color.BLUE);

        ArrayList<LineDataSet> dataSets=new ArrayList<LineDataSet>();
        dataSets.add(set1);
        data=new LineData(xVals,dataSets);
        return data;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            Intent intent=new Intent();
            intent.setClass(RateChartShowActivity.this,MainActivity.class );
            startActivity(intent);
            RateChartShowActivity.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }
}
