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
import aaa.pfe.auth.utils.Const;
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
    private RadioGroup shuffleRadioGroup;
    private RadioGroup twiceRadioGroup;
    private RadioButton yesOrderRadioButton;
    private RadioButton noOrderRadioButton;
    private RadioButton exactRadioButton;
    private RadioButton sameRadioButton;
    private RadioButton yesShuffleRadioButton;
    private RadioButton noShuffleRadioButton;
    private RadioButton yesTwiceRadioButton;
    private RadioButton noTwiceRadioButton;
    private int nbPhotos;
    private String typePhotos;
    private int passwordLength;
    private int nbSteps;
    private boolean isInOrder;
    private String typeOfMatching;
    private boolean doShuffle;
    private boolean twicePhoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_face_admin);

        nbPhotosEditText = (EditText) findViewById(R.id.editTextNbPhotos);
        passwordLengthEditText = (EditText) findViewById(R.id.editTextPasswordLength);
        nbStepsEditText = (EditText) findViewById(R.id.editTextNbSteps);

        typePhotosSpinner = (Spinner) findViewById(R.id.spinnerTypePhotos);

        orderRadioGroup = (RadioGroup) findViewById(R.id.orderRadioGroup);
        matchingRadioGroup = (RadioGroup) findViewById(R.id.matchingRadioGroup);
        shuffleRadioGroup = (RadioGroup) findViewById(R.id.shuffleRadioGroup);
        twiceRadioGroup = (RadioGroup) findViewById(R.id.twiceRadioGroup);

        yesOrderRadioButton = (RadioButton) findViewById(R.id.yesRadioButton);
        noOrderRadioButton = (RadioButton) findViewById(R.id.noRadioButton);
        sameRadioButton = (RadioButton) findViewById(R.id.sameRadioButton);
        exactRadioButton = (RadioButton) findViewById(R.id.exactRadioButton);
        yesShuffleRadioButton = (RadioButton) findViewById(R.id.yesShuffleRadioButton);
        noShuffleRadioButton = (RadioButton) findViewById(R.id.noShuffleRadioButton);
        yesTwiceRadioButton = (RadioButton) findViewById(R.id.yesTwiceRadioButton);
        noTwiceRadioButton = (RadioButton) findViewById(R.id.noTwiceRadioButton);

        sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.passFacePreferences), MODE_PRIVATE);

        setSpinnerAdapters();

        //We load the current saved settings

        loadSettings();


    }

    private void loadSettings() {
        String savedNbPhotos = String.valueOf(sharedPreferences.getInt(getString(R.string.nbPhotosPreference), Const.DEFAULT_NB_PHOTOS));
        nbPhotosEditText.setText(savedNbPhotos);

        String savedPwdLength = String.valueOf(sharedPreferences.getInt(getString(R.string.passwordLengthPreference), Const.DEFAULT_PWD_LENGTH));
        passwordLengthEditText.setText(savedPwdLength);

        String savedNbSteps = String.valueOf(sharedPreferences.getInt(getString(R.string.numberStepsPreference), Const.DEFAULT_NB_STEPS));
        nbStepsEditText.setText(savedNbSteps);

        boolean savedOrder = sharedPreferences.getBoolean(getString(R.string.isInOrderPreference), Const.DEFAULT_ORDER);
        if (savedOrder)
            yesOrderRadioButton.setChecked(true);
        else
            noOrderRadioButton.setChecked(false);

        boolean savedShuffle = sharedPreferences.getBoolean(getString(R.string.doShufflePreference), Const.DEFAULT_SHUFFLE);
        if (savedShuffle)
            yesShuffleRadioButton.setChecked(true);
        else
            noShuffleRadioButton.setChecked(true);


        String savedMatching = sharedPreferences.getString(getString(R.string.matchingTypePreference), Const.DEFAULT_MATCHING);
        if (savedMatching.equals("Exact"))
            exactRadioButton.setChecked(true);
        else
            sameRadioButton.setChecked(true);

        boolean twicePhoto = sharedPreferences.getBoolean(getString(R.string.twicePhotoPreference), Const.DEFAULT_TWICE_PHOTO);
        if (twicePhoto)
            yesTwiceRadioButton.setChecked(true);
        else
            noTwiceRadioButton.setChecked(true);


        typePhotosSpinner.setSelection(typePhotosAdapter.getPosition(sharedPreferences.getString(getString(R.string.typePhotosPreference), Const.DEFAULT_TYPE_PHOTOS)));


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
        RadioButton shuffleRadioButton = (RadioButton) findViewById(shuffleRadioGroup.getCheckedRadioButtonId());
        RadioButton twiceRadioButton = (RadioButton) findViewById(twiceRadioGroup.getCheckedRadioButtonId());

        isInOrder = (orderRadioButton.getText().equals(getString(R.string.yes)));
        typeOfMatching = matchingRadioButton.getText().toString();
        doShuffle = (shuffleRadioButton.getText()).equals("Yes");
        twicePhoto = (twiceRadioButton.getText().equals("Yes"));
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
        editor.putBoolean(getString(R.string.doShufflePreference), doShuffle);
        editor.putBoolean(getString(R.string.twicePhotoPreference), twicePhoto);

        editor.remove(getString(R.string.passwordPreference));

        editor.apply();

        finish();

    }

}


