package aaa.pfe.auth.view.schemepattern;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import aaa.pfe.auth.R;
import aaa.pfe.auth.view.mother.AdminActivity;

/**
 * Created by Anasse on 21/11/2017.
 */

public class SchemeAdminActivity extends AdminActivity {

    private Spinner nbRowsSpinner;
    private Spinner nbColumnsSpinner;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheme_admin);
        getSupportActionBar().setTitle("Admin SchemePattern");

        sharedPreferences = getApplicationContext().getSharedPreferences("SchemePreferences", MODE_PRIVATE);
        nbRowsSpinner = (Spinner) findViewById(R.id.spinnerNbRows);
        nbColumnsSpinner = (Spinner) findViewById(R.id.spinnerNbColumns);
        
        setSpinnersAdapters();
        
        setDefaultsValues();

    }

    private void setDefaultsValues() {
        if (sharedPreferences.contains("nbRowsSpinner")){
            int nbRows = sharedPreferences.getInt("nbRowsSpinner",1);
            nbRowsSpinner.setSelection(nbRows-1);
        }

        if (sharedPreferences.contains("nbColumnSpinner")){
            int nbColumns = sharedPreferences.getInt("nbColumnSpinner",1);
            nbColumnsSpinner.setSelection(nbColumns-1);
        }

    }

    private void setSpinnersAdapters() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.nbColumnsRows, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nbRowsSpinner.setAdapter(adapter);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nbColumnsSpinner.setAdapter(adapter);
    }


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

        editor.putInt("nbRowsSpinner", Integer.valueOf(nbRowsSpinner.getSelectedItem().toString()));
        editor.putInt("nbColumn", Integer.valueOf(nbColumnsSpinner.getSelectedItem().toString()));
        editor.remove("schemePatternPass");
        
        editor.apply();
        finish();
    }
}
