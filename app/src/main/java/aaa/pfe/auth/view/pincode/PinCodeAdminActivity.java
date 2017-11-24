package aaa.pfe.auth.view.pincode;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

import aaa.pfe.auth.R;
import aaa.pfe.auth.view.mother.AdminActivity;

import static aaa.pfe.auth.view.pincode.PinCodeActivity.PREFERENCES;

public class PinCodeAdminActivity extends AdminActivity{

    private ToggleButton tRandNum;

    private EditText ptPinLength;

    private EditText ptTry;

    private ArrayList<CheckBox> checkBoxList;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_code_admin);
        getSupportActionBar().setTitle("Admin PinCode");

        //Retrieve Button
        tRandNum =  (ToggleButton) findViewById(R.id.toggleRandomNum);

        ptPinLength = (EditText) findViewById(R.id.editTextPinLength);

        ptTry = (EditText) findViewById(R.id.editTextTry);

        /*checkBoxList = new ArrayList<>();
        checkBoxList.add(1,(CheckBox)findViewById(R.id.checkBox1));
        checkBoxList.add(2,(CheckBox)findViewById(R.id.checkBox2));
        checkBoxList.add(3,(CheckBox)findViewById(R.id.checkBox3));
        checkBoxList.add(4,(CheckBox)findViewById(R.id.checkBox4));
        checkBoxList.add(5,(CheckBox)findViewById(R.id.checkBox5));
        checkBoxList.add(6,(CheckBox)findViewById(R.id.checkBox6));
        checkBoxList.add(7,(CheckBox)findViewById(R.id.checkBox7));
        checkBoxList.add(8,(CheckBox)findViewById(R.id.checkBox8));
        checkBoxList.add(9,(CheckBox)findViewById(R.id.checkBox9));*/

        //Shared Pref
        sharedPreferences = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);

        setDefaultsValues();
    }

    private void setDefaultsValues() {
        if (sharedPreferences.contains("randNum")){
            Log.i("PinCodeAdmin","setDefautValues numRand");
            tRandNum.setChecked(sharedPreferences.getBoolean("numRand",false));
        }

        if (sharedPreferences.contains("pinLength")){
            ptPinLength.setText(sharedPreferences.getInt("pinLength",4));
        }

        if (sharedPreferences.contains("nbTry")){
            ptTry.setText(sharedPreferences.getInt("nbTry",3));
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
        Log.i("PinCodeAdmin","saveChanges2");
        editor.putInt("pinLength",Integer.valueOf(ptPinLength.getText().toString()));
        editor.putInt("nbTry",Integer.valueOf(ptTry.getText().toString()));
        editor.apply();
        finish();
    }

}
