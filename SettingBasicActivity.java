package com.afufu.controlbodyfat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioButton;

/**
 * Created by user on 2015/8/12.
 */
public class SettingBasicActivity extends ActionBarActivity {

    private NumberPicker NPheight, NPage;
    private Button BtnSave;
    private SharedPreferences Sp_setting;
    private Editor ED;
    private RadioButton Rb_M, Rb_F;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_setting);

        Sp_setting = getSharedPreferences("BasicSettingFile",MODE_PRIVATE);

        NPheight = (NumberPicker) findViewById(R.id.nP_height);
        NPage = (NumberPicker) findViewById(R.id.nP_age);
        BtnSave=(Button) findViewById(R.id.btnSave);
        Rb_M = (RadioButton)findViewById(R.id.rB_gender_M);
        Rb_F = (RadioButton)findViewById(R.id.rB_gender_F);

        NPheight.setMaxValue(200);
        NPheight.setMinValue(50);
        NPheight.setValue(155);

        NPage.setMaxValue(100);
        NPage.setMinValue(10);
        NPage.setValue(25);

        BtnSave.setOnClickListener(BtnSav);


    }

    private OnClickListener BtnSav=new OnClickListener() {
        @Override
        public void onClick(View v) {
            int Hei=NPheight.getValue();
            int Age=NPage.getValue();
            Boolean gender;     // male:0 false   Female:1 true
            Boolean checkM,checkFM;
            checkM=Rb_M.isChecked();
            checkFM=Rb_F.isChecked();

            if(checkM==true)
                gender=false;
            else
                gender=true;


            ED=Sp_setting.edit();
           ED.putInt("Height", Hei);
            ED.putInt("Age", Age);
            ED.putBoolean("Gender",gender);
      /*     Toast.makeText(getApplicationContext(),
                    "Height: " + Hei + "\n" +
                            "age: " + Age + "\n"+
                            "gender: " + gender + "\n",
                    Toast.LENGTH_LONG).show();   */
            Intent intent=new Intent();
            intent.setClass(SettingBasicActivity.this, MainActivity.class);
            startActivity(intent);
             SettingBasicActivity.this.finish();
        }
    };

    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            Intent intent=new Intent();
            intent.setClass(SettingBasicActivity.this,MainActivity.class );
            startActivity(intent);
            SettingBasicActivity.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }
}
