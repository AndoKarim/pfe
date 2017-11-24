package aaa.pfe.auth.view.pincode;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

import aaa.pfe.auth.R;
import aaa.pfe.auth.view.mother.AdminActivity;

import static aaa.pfe.auth.view.pincode.PinCodeActivity.PREFERENCES;

public class PinCodeAdminActivity extends AdminActivity{

    private ToggleButton tRandNum;

    private Spinner sPinLength;

    private EditText ptTry;

    private ArrayList<CheckBox> checkBoxList;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_code_admin);
        getSupportActionBar().setTitle("Admin PinCode");

        //Retrieve Button
        tRandNum =  (ToggleButton) findViewById(R.id.toggleRandomNum);

        sPinLength = (Spinner) findViewById(R.id.spinnerPinLength);


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
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void retrieveChanges(View v) {
        Toast t = Toast.makeText(this, "Save Parameters", Toast.LENGTH_SHORT);
        t.show();

        //Shared Pref
        sharedPreferences = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        editor.putBoolean("randNum",tRandNum.isChecked());
        editor.putInt("pinLength",Integer.valueOf(sPinLength.getSelectedItem().toString()));
        editor.putInt("nbTry",Integer.valueOf(ptTry.getText().toString()));
        editor.apply();

        Intent i = new Intent(PinCodeAdminActivity.this,PinCodeActivity.class);
        startActivity(i);

    }

}
