package aaa.pfe.auth.view.passface;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import aaa.pfe.auth.R;
import aaa.pfe.auth.view.mother.AdminActivity;

public class PassFaceAdminActivity extends AdminActivity {

    SharedPreferences sharedPreferences;
    ArrayAdapter<CharSequence> typePhotosAdapter;
    private EditText nbPhotosEditText;
    private EditText passwordLengthEditText;
    private EditText nbStepsEditText;
    private Spinner typePhotosSpinner;
    private RadioGroup orderRadioGroup;
    private RadioGroup matchingRadioGroup;
    private RadioButton yesRadioButton;
    private RadioButton noRadioButton;
    private RadioButton exactRadioButton;
    private RadioButton sameRadioButton;
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
        passwordLengthEditText = (EditText) findViewById(R.id.editTextPasswordLength);
        nbStepsEditText = (EditText) findViewById(R.id.editTextNbSteps);

        typePhotosSpinner = (Spinner) findViewById(R.id.spinnerTypePhotos);

        orderRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        matchingRadioGroup = (RadioGroup) findViewById(R.id.radioGroup2);

        yesRadioButton = (RadioButton) findViewById(R.id.yesRadioButton);
        noRadioButton = (RadioButton) findViewById(R.id.noRadioButton);
        sameRadioButton = (RadioButton) findViewById(R.id.sameRadioButton);
        exactRadioButton = (RadioButton) findViewById(R.id.exactRadioButton);

        sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.passFacePreferences), MODE_PRIVATE);

        setSpinnerAdapters();

        //We load the current saved settings

        loadSettings();


    }

    private void loadSettings() {
        int defaultNbPhotos = 9;
        String savedNbPhotos = String.valueOf(sharedPreferences.getInt(getString(R.string.nbPhotosPreference), defaultNbPhotos));
        nbPhotosEditText.setText(savedNbPhotos);

        int defaultPwdLength = 1;
        String savedPwdLength = String.valueOf(sharedPreferences.getInt(getString(R.string.passwordLengthPreference), defaultPwdLength));
        passwordLengthEditText.setText(savedPwdLength);

        int defaultSteps = 1;
        String savedNbSteps = String.valueOf(sharedPreferences.getInt(getString(R.string.numberStepsPreference), defaultSteps));
        nbStepsEditText.setText(savedNbSteps);

        boolean defaultOrder = true;
        boolean savedOrder = sharedPreferences.getBoolean(getString(R.string.isInOrderPreference), defaultOrder);
        if (savedOrder)
            yesRadioButton.setChecked(true);
        else
            noRadioButton.setChecked(false);

        String defaultMatching = "Exact";
        String savedMatching = sharedPreferences.getString(getString(R.string.matchingTypePreference), defaultMatching);
        if (savedMatching.equals("Exact"))
            exactRadioButton.setChecked(true);
        else
            sameRadioButton.setChecked(true);

        String defaultTypePhotos = "Faces";
        typePhotosSpinner.setSelection(typePhotosAdapter.getPosition(sharedPreferences.getString(getString(R.string.typePhotosPreference), defaultTypePhotos)));


    }

    private void setSpinnerAdapters() {


        typePhotosAdapter = ArrayAdapter.createFromResource(this,
                R.array.typePhotos, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        typePhotosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        typePhotosSpinner.setAdapter(typePhotosAdapter);




    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


    //Called when you press the save button.
    @Override
    public void retrieveChanges(View v) {
        typePhotos = typePhotosSpinner.getSelectedItem().toString();
        passwordLength = Integer.parseInt(passwordLengthEditText.getText().toString());
        nbSteps = Integer.parseInt(nbStepsEditText.getText().toString());


        RadioButton orderRadioButton = (RadioButton) findViewById(orderRadioGroup.getCheckedRadioButtonId());
        RadioButton matchingRadioButton = (RadioButton) findViewById(matchingRadioGroup.getCheckedRadioButtonId());

        isInOrder = (orderRadioButton.getText().equals(getString(R.string.yes)));
        typeOfMatching = matchingRadioButton.getText().toString();

        nbPhotos = Integer.valueOf(nbPhotosEditText.getText().toString());

        saveChanges();
    }

    //Save changes into the SharedPreferences
    @Override
    public void saveChanges() {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(getString(R.string.nbPhotosPreference), nbPhotos);
        editor.putString(getString(R.string.typePhotosPreference), typePhotos);
        editor.putInt(getString(R.string.passwordLengthPreference), passwordLength);
        editor.putBoolean(getString(R.string.isInOrderPreference), isInOrder);
        editor.putInt(getString(R.string.numberStepsPreference), nbSteps);
        editor.putString(getString(R.string.matchingTypePreference), typeOfMatching);

        editor.remove(getString(R.string.passwordPreference));

        editor.apply();

        finish();

    }

}


