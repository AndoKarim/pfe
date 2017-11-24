package aaa.pfe.auth.view.passface;

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
    private RadioButton orderRadioButton;
    private RadioButton matchingRadioButton;


    private int nbPhotos;
    private String typePhotos;
    private int passwordLength;
    private int nbSteps;


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
        orderRadioButton = (RadioButton) findViewById(orderRadioGroup.getCheckedRadioButtonId());
        matchingRadioButton = (RadioButton) findViewById(matchingRadioGroup.getCheckedRadioButtonId());

        nbPhotos = Integer.valueOf(nbPhotosEditText.getText().toString());

        saveChanges();
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


