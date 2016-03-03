package com.afufu.controlbodyfat;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by user on 2015/8/13.
 */
public class RecordEditActivity extends ActionBarActivity {

    SQLiteDatabase db;
    MySQLiteDB MyDB;

    private TextView RecoDate;
    private EditText RecoWeight, RecoFatRate, RecoWaistline, RecoHips;
    private Button  BtnRecoSave;
    private long Date;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_record);
        Bundle bund=this.getIntent().getExtras();

        String Sdate=bund.getString("StringDate");
        Date=bund.getLong("Date");

        RecoDate=(TextView)findViewById(R.id.tV_RecoDate);
        RecoWeight=(EditText)findViewById(R.id.eT_Weight);
        RecoFatRate=(EditText)findViewById(R.id.eT_FatRate);
        RecoWaistline=(EditText)findViewById(R.id.eT_Waistline);
        RecoHips=(EditText)findViewById(R.id.eT_Hips);
        BtnRecoSave=(Button)findViewById(R.id.btn_recSave);

        RecoDate.setText(Sdate);

        /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~DataBase*/
        MyDB=new MySQLiteDB(this);
        db=MyDB.getWritableDatabase();
        ArrayList<Long> DateList=MyDB.getDateList(db);
        BodyData record;
        if(DateList.isEmpty())
        {
            RecoWeight.setText("50.00");
            RecoFatRate.setText("28.00");
            RecoWaistline.setText("30.00");
            RecoHips.setText("40.00");
        }
        else
        {
            boolean check=MyDB.checkDate(db,Date);
            if(check==false)
            {
                int count=DateList.size();
                long day=DateList.get(count-1);
                record=MyDB.getSearch(db,day);
            }
            else
            {
                record=MyDB.getSearch(db,Date);
            }
            RecoWeight.setText(""+record.getWeight()+"");
            RecoFatRate.setText(""+record.getFatRate()+"");
            RecoWaistline.setText(""+record.getWaistline()+"");
            RecoHips.setText(""+record.getHips()+"");
        }

        BtnRecoSave.setOnClickListener(BtnRecoSav);

    }

    private View.OnClickListener BtnRecoSav=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            double weight=Double.valueOf((RecoWeight.getText()).toString());
            double fatrate=Double.valueOf((RecoFatRate.getText()).toString());
            double waistline=Double.valueOf((RecoWaistline.getText()).toString());
            double hips=Double.valueOf((RecoHips.getText()).toString());
            double fatweight=weight*(fatrate*0.01);

            long success=MyDB.add(db,Date,weight,fatrate,fatweight,waistline,hips);
           /* Toast.makeText(getApplicationContext(),
                    "save success:" +success,
                    Toast.LENGTH_LONG).show();*/
            db.close();


            Intent intent=new Intent();
            intent.setClass(RecordEditActivity.this,MainActivity.class );
            startActivity(intent);
            RecordEditActivity.this.finish();
        }
    };

    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            Intent intent=new Intent();
            intent.setClass(RecordEditActivity.this,MainActivity.class );
            startActivity(intent);
            RecordEditActivity.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }
}
