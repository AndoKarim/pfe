package aaa.pfe.auth.view.passface;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import aaa.pfe.auth.R;
import aaa.pfe.auth.view.mother.AdminActivity;

public class PassFaceAdminActivity extends AdminActivity implements AdapterView.OnItemSelectedListener {

    private EditText nbPhotosEditText;
    private Spinner typePhotosSpinner;
    private Spinner passwordLengthSpinner;
    private Spinner nbStepsSpinner;
    private RadioGroup orderRadioGroup;
    private RadioGroup matchingRadioGroup;


    private int nbPhotos;
    private String typePhotos;
    private int passwordLength;
    private int nbSteps;
    private boolean isInOrder;
    private String typeOfMatching;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_face_admin);

        nbPhotosEditText = (EditText) findViewById(R.id.editTextNbPhotos);
        typePhotosSpinner = (Spinner) findViewById(R.id.spinnerTypePhotos);
        passwordLengthSpinner = (Spinner) findViewById(R.id.spinnerPasswordLength);
        nbStepsSpinner = (Spinner) findViewById(R.id.spinnerNbSteps);
        orderRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        matchingRadioGroup = (RadioGroup) findViewById(R.id.radioGroup2);


        setSpinnerAdapters();


    }

    private void setSpinnerAdapters() {


        ArrayAdapter<CharSequence> typePhotosAdapter = ArrayAdapter.createFromResource(this,
                R.array.typePhotos, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        typePhotosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        typePhotosSpinner.setAdapter(typePhotosAdapter);
        typePhotosSpinner.setOnItemSelectedListener(this);


        ArrayAdapter<CharSequence> iteratorAdapter = ArrayAdapter.createFromResource(this,
                R.array.iterator, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        iteratorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        passwordLengthSpinner.setAdapter(iteratorAdapter);
        passwordLengthSpinner.setOnItemSelectedListener(this);


        nbStepsSpinner.setAdapter(iteratorAdapter);
        nbStepsSpinner.setOnItemSelectedListener(this);


    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


    //Called when you press the save button.
    @Override
    public void retrieveChanges(View v) {
        RadioButton orderRadioButton = (RadioButton) findViewById(orderRadioGroup.getCheckedRadioButtonId());
        RadioButton matchingRadioButton = (RadioButton) findViewById(matchingRadioGroup.getCheckedRadioButtonId());

        isInOrder = (orderRadioButton.getText().equals(getString(R.string.yes)));
        typeOfMatching = matchingRadioButton.getText().toString();

        nbPhotos = Integer.valueOf(nbPhotosEditText.getText().toString());

        saveChanges();
    }

    @Override
    public void saveChanges() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.passFacePreferences), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(getString(R.string.nbPhotos), String.valueOf(nbPhotos));
        editor.putString(getString(R.string.typePhotos), typePhotos);
        editor.putString(getString(R.string.passwordLength), String.valueOf(passwordLength));
        editor.putString(getString(R.string.isInOrder), String.valueOf(isInOrder));
        editor.putString(getString(R.string.numberSteps), String.valueOf(nbSteps));
        editor.putString(getString(R.string.matchingType), typeOfMatching);

        editor.remove(getString(R.string.password));

        editor.apply();

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String value = (String) parent.getItemAtPosition(position);

        switch (parent.getId()) {
            case R.id.spinnerTypePhotos:
                typePhotos = value;
                break;
            case R.id.spinnerPasswordLength:
                passwordLength = Integer.valueOf(value);
                break;
            case R.id.spinnerNbSteps:
                nbSteps = Integer.valueOf(value);
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}


