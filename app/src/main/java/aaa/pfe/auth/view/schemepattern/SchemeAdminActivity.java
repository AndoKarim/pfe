package aaa.pfe.auth.view.schemepattern;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import aaa.pfe.auth.R;
import aaa.pfe.auth.view.mother.AdminActivity;
import aaa.pfe.auth.view.pincode.PinCodeActivity;
import aaa.pfe.auth.view.pincode.PinCodeAdminActivity;

/**
 * Created by Anasse on 21/11/2017.
 */

public class SchemeAdminActivity extends AdminActivity {

    private EditText nbRowsEditText;
    private EditText nbColumnsEditText;
    private SharedPreferences sharedPreferences;
    private EditText lengthEditText;
    private CheckBox vibrationBox;
    private CheckBox stealthBox;
    private CheckBox captureBox;
    private EditText dotSizeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheme_admin);
        getSupportActionBar().setTitle("Admin SchemePattern");

        sharedPreferences = getApplicationContext().getSharedPreferences("SchemePreferences", MODE_PRIVATE);
        nbRowsEditText = (EditText) findViewById(R.id.nbRows);
        nbColumnsEditText = (EditText) findViewById(R.id.nbColumns);
        lengthEditText = (EditText) findViewById(R.id.lengthPattern);
        vibrationBox = (CheckBox) findViewById(R.id.vibration);
        stealthBox = (CheckBox) findViewById(R.id.stealth);
        captureBox = (CheckBox) findViewById(R.id.capture);
        dotSizeEditText = (EditText) findViewById(R.id.dotSize);


        
        setDefaultsValues();

    }

    private void setDefaultsValues() {
        if (sharedPreferences.contains("nbRows")){
            int nbRows = sharedPreferences.getInt("nbRows",1);
            nbRowsEditText.setText(String.valueOf(nbRows));
        }

        if (sharedPreferences.contains("nbColumns")){
            int nbColumns = sharedPreferences.getInt("nbColumns",1);
            nbColumnsEditText.setText(String.valueOf(nbColumns));
        }

        if (sharedPreferences.contains("lengthPattern")){
            int length = sharedPreferences.getInt("lengthPattern",1);
            lengthEditText.setText(String.valueOf(length));
        }

        if (sharedPreferences.contains("vibration")){
            boolean vibration = sharedPreferences.getBoolean("vibration",true);
            vibrationBox.setChecked(vibration);
        }

        if (sharedPreferences.contains("stealth")){
            boolean stealth = sharedPreferences.getBoolean("stealth",false);
            stealthBox.setChecked(stealth);
        }

        if (sharedPreferences.contains("dotSize")){
            int dotSize = sharedPreferences.getInt("dotSize",30);
            dotSizeEditText.setText(String.valueOf(dotSize));
        }

        if (sharedPreferences.contains("captureMode")){
            boolean captureMode = sharedPreferences.getBoolean("captureMode",true);
            captureBox.setChecked(captureMode);
        }

    }

    /*private void setSpinnersAdapters() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.nbColumnsRows, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nbRowsSpinner.setAdapter(adapter);

        nbColumnsSpinner.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(this,
                R.array.lengthSchemePattern, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lengthSpinner.setAdapter(adapter);
    }*/


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void retrieveChanges(View v) {
        Toast t = Toast.makeText(this, "SaveScheme", Toast.LENGTH_SHORT);
        saveChanges();
        t.show();
    }

    @Override
    public void saveChanges() {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("nbRows", Integer.valueOf(nbRowsEditText.getText().toString()));
        editor.putInt("nbColumns", Integer.valueOf(nbColumnsEditText.getText().toString()));
        editor.putInt("lengthPattern", Integer.valueOf(lengthEditText.getText().toString()));
        editor.putBoolean("vibration", vibrationBox.isChecked());
        editor.putBoolean("stealth", stealthBox.isChecked());
        editor.putInt("dotSize", Integer.valueOf(dotSizeEditText.getText().toString()));
        editor.putBoolean("captureMode",captureBox.isChecked());


        editor.remove("schemePatternPass");
        
        editor.apply();
        Intent i = new Intent(SchemeAdminActivity.this,SchemePatternActivity.class);
        finish();
        startActivity(i);
    }
}
