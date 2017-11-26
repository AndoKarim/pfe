package aaa.pfe.auth.view.pincode;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

import aaa.pfe.auth.R;
import aaa.pfe.auth.view.mother.AdminActivity;

import static aaa.pfe.auth.view.pincode.PinCodeActivity.PREFERENCES;

public class PinCodeAdminActivity extends AdminActivity{

    private ToggleButton tRandNum,tCaptureMode;

    private EditText ptPinLength;

    private EditText ptTry;

    private RadioGroup radiogroupIndicators;
    private RadioButton radioButtonNoDots, radioButtonDotsFill, radioButtonDotsAnimation;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_code_admin);
        getSupportActionBar().setTitle("Admin PinCode");

        //Retrieve Button
        tRandNum =  (ToggleButton) findViewById(R.id.toggleRandomNum);

        tCaptureMode = (ToggleButton) findViewById(R.id.toggleButtonCaptureMode);

        ptPinLength = (EditText) findViewById(R.id.editTextPinLength);

        ptTry = (EditText) findViewById(R.id.editTextTry);

        radiogroupIndicators = (RadioGroup) findViewById(R.id.radioGroupIndicator);
        radioButtonNoDots = (RadioButton) findViewById(R.id.radioButton1);
        radioButtonDotsFill = (RadioButton) findViewById(R.id.radioButton2);
        radioButtonDotsAnimation = (RadioButton) findViewById(R.id.radioButton3);

        //Shared Pref
        sharedPreferences = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setDefaultsValues();
        tRandNum.forceLayout();
    }

    private void setDefaultsValues() {
        if (sharedPreferences.contains("randNum")){
            tRandNum.setChecked((boolean) sharedPreferences.getBoolean("randNum",true));
        }


        if (sharedPreferences.contains("pinLength")){
            ptPinLength.setText(String.valueOf(sharedPreferences.getInt("pinLength",4)));
        }

        if (sharedPreferences.contains("nbTry")){
            ptTry.setText(String.valueOf(sharedPreferences.getInt("nbTry",3)));
        }

        if (sharedPreferences.contains("indicators")){
            int indicatorType=  sharedPreferences.getInt("indicators",-1);
            switch (indicatorType){
                case -1:
                    radioButtonNoDots.setChecked(true);
                    break;
                case 0:
                    radioButtonDotsFill.setChecked(true);
                    break;
                case 2:
                    radioButtonDotsAnimation.setChecked(true);
                    break;
            }
        }

        if (sharedPreferences.contains("captureMode")){
            tCaptureMode.setChecked((boolean) sharedPreferences.getBoolean("captureMode",true));
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void retrieveChanges(View v) {
        Toast t = Toast.makeText(this, "Save Parameters", Toast.LENGTH_SHORT);
        saveChanges();
        t.show();
    }

    @Override
    public void saveChanges() {
        Log.i("PinCodeAdmin","saveChanges");

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("randNum",tRandNum.isChecked());
        editor.putInt("pinLength",Integer.valueOf(ptPinLength.getText().toString()));
        editor.putInt("nbTry",Integer.valueOf(ptTry.getText().toString()));
        editor.putBoolean("captureMode",tCaptureMode.isChecked());

        int selectedId = radiogroupIndicators.getCheckedRadioButtonId();
        if(selectedId == radioButtonNoDots.getId()) {
            editor.putInt("indicators",-1);
        } else if(selectedId == radioButtonDotsFill.getId()) {
            editor.putInt("indicators",0);

        } else if (selectedId == radioButtonDotsAnimation.getId()){
            editor.putInt("indicators",2); //ou 1
        }

        editor.apply();
        finish();
    }

}
