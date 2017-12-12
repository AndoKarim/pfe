package aaa.pfe.auth.view.passface;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import aaa.pfe.auth.R;
import aaa.pfe.auth.utils.Const;
import aaa.pfe.auth.utils.LogWriter;
import aaa.pfe.auth.view.mother.AdminActivity;

public class PassFaceAdminActivity extends AdminActivity {

    public static LogWriter logWriter;
    SharedPreferences sharedPreferences;
    ArrayAdapter<CharSequence> typePhotosAdapter;
    private EditText nbPhotosEditText;
    private EditText passwordLengthEditText;
    private EditText nbStepsEditText;
    private EditText nbAttemptsEditText;
    private Spinner typePhotosSpinner;
    // private RadioGroup orderRadioGroup;
    private RadioGroup matchingRadioGroup;
    private RadioGroup shuffleRadioGroup;
    private RadioGroup captureModeRadioGroup;
    private RadioButton exactRadioButton;
    private RadioButton sameRadioButton;
    private RadioButton yesShuffleRadioButton;
    private RadioButton noShuffleRadioButton;
    private RadioButton yesTwiceRadioButton;
    private RadioButton noTwiceRadioButton;
    private RadioButton yesCaptureRadioButton;
    private RadioButton noCaptureRadioButton;
    //private RadioButton yesOrderRadioButton;
    // private RadioButton noOrderRadioButton;
    private RadioGroup twiceRadioGroup;
    private int nbPhotos;
    private String typePhotos;
    private int passwordLength;
    private int nbSteps;
    private int nbAttempts;
    private String typeOfMatching;
    private boolean doShuffle;
    //private boolean isInOrder;
    private boolean twicePhoto;
    private boolean doCapture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_face_admin);

        nbPhotosEditText = (EditText) findViewById(R.id.editTextNbPhotos);
        passwordLengthEditText = (EditText) findViewById(R.id.editTextPasswordLength);
        nbStepsEditText = (EditText) findViewById(R.id.editTextNbSteps);
        nbAttemptsEditText = (EditText) findViewById(R.id.editTextNbAttempts);

        typePhotosSpinner = (Spinner) findViewById(R.id.spinnerTypePhotos);

        //orderRadioGroup = (RadioGroup) findViewById(R.id.orderRadioGroup);
        matchingRadioGroup = (RadioGroup) findViewById(R.id.matchingRadioGroup);
        shuffleRadioGroup = (RadioGroup) findViewById(R.id.shuffleRadioGroup);
        twiceRadioGroup = (RadioGroup) findViewById(R.id.twiceRadioGroup);
        sameRadioButton = (RadioButton) findViewById(R.id.semanticRadioButton);
        exactRadioButton = (RadioButton) findViewById(R.id.exactRadioButton);
        yesShuffleRadioButton = (RadioButton) findViewById(R.id.yesShuffleRadioButton);
        noShuffleRadioButton = (RadioButton) findViewById(R.id.noShuffleRadioButton);
        yesTwiceRadioButton = (RadioButton) findViewById(R.id.yesTwiceRadioButton);
        noTwiceRadioButton = (RadioButton) findViewById(R.id.noTwiceRadioButton);

        captureModeRadioGroup = (RadioGroup) findViewById(R.id.captureModeRadioGroup);
        yesCaptureRadioButton = (RadioButton) findViewById(R.id.yesCaptureRadioButton);
        noCaptureRadioButton = (RadioButton) findViewById(R.id.noCaptureRadioButton);

        // yesOrderRadioButton = (RadioButton) findViewById(R.id.yesRadioButton);
        //noOrderRadioButton = (RadioButton) findViewById(R.id.noRadioButton);

        sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.passFacePreferences), MODE_PRIVATE);

        setSpinnerAdapters();

        //We load the current saved settings
        loadSettings();

        logWriter = new LogWriter("PassFace");


    }

    private void loadSettings() {
        String savedNbPhotos = String.valueOf(sharedPreferences.getInt(getString(R.string.nbPhotosPreference), Const.DEFAULT_NB_PHOTOS));
        nbPhotosEditText.setText(savedNbPhotos);

        String savedPwdLength = String.valueOf(sharedPreferences.getInt(getString(R.string.passwordLengthPreference), Const.DEFAULT_PWD_LENGTH));
        passwordLengthEditText.setText(savedPwdLength);

        String savedNbSteps = String.valueOf(sharedPreferences.getInt(getString(R.string.numberStepsPreference), Const.DEFAULT_NB_STEPS));
        nbStepsEditText.setText(savedNbSteps);

        String nbAttempts = String.valueOf(sharedPreferences.getInt(getString(R.string.numberAttemptsPreference), Const.DEFAULT_NB_ATTEMPTS));
        nbAttemptsEditText.setText(nbAttempts);


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


        boolean captureMode = sharedPreferences.getBoolean(getString(R.string.captureModePreference), Const.DEFAULT_CAPTURE_MODE);
        if (captureMode)
            yesCaptureRadioButton.setChecked(true);
        else
            noCaptureRadioButton.setChecked(true);

       /* boolean savedOrder = sharedPreferences.getBoolean(getString(R.string.isInOrderPreference), Const.DEFAULT_ORDER);
        if (savedOrder)
            yesOrderRadioButton.setChecked(true);
        else
            noOrderRadioButton.setChecked(true);*/

        typePhotosSpinner.setSelection(typePhotosAdapter.getPosition(sharedPreferences.getString(getString(R.string.typePhotosPreference), Const.DEFAULT_TYPE_PHOTOS)));


    }

    private void setSpinnerAdapters() {


        typePhotosAdapter = ArrayAdapter.createFromResource(this,
                R.array.typePhotos, android.R.layout.simple_spinner_item);

        typePhotosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
        nbAttempts = Integer.parseInt(nbAttemptsEditText.getText().toString());


        RadioButton matchingRadioButton = (RadioButton) findViewById(matchingRadioGroup.getCheckedRadioButtonId());
        RadioButton shuffleRadioButton = (RadioButton) findViewById(shuffleRadioGroup.getCheckedRadioButtonId());
        RadioButton twiceRadioButton = (RadioButton) findViewById(twiceRadioGroup.getCheckedRadioButtonId());
        RadioButton captureModeRadioButton = (RadioButton) findViewById(captureModeRadioGroup.getCheckedRadioButtonId());

        //RadioButton orderRadioButton = (RadioButton) findViewById(orderRadioGroup.getCheckedRadioButtonId());
        typeOfMatching = matchingRadioButton.getText().toString();
        doShuffle = (shuffleRadioButton.getText()).equals("Yes");
        twicePhoto = (twiceRadioButton.getText().equals("Yes"));
        nbPhotos = Integer.valueOf(nbPhotosEditText.getText().toString());
        //isInOrder = (orderRadioButton.getText().equals(getString(R.string.yes)));
        doCapture = (captureModeRadioButton.getText().equals("Yes"));


        //Parameters checking
        checkParameters();
    }

    //Save changes into the SharedPreferences
    @Override
    public void saveChanges() {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(getString(R.string.nbPhotosPreference), nbPhotos);
        editor.putString(getString(R.string.typePhotosPreference), typePhotos);
        editor.putInt(getString(R.string.passwordLengthPreference), passwordLength);
        editor.putInt(getString(R.string.numberStepsPreference), nbSteps);
        editor.putInt(getString(R.string.numberAttemptsPreference), nbAttempts);
        editor.putString(getString(R.string.matchingTypePreference), typeOfMatching);
        editor.putBoolean(getString(R.string.doShufflePreference), doShuffle);
        editor.putBoolean(getString(R.string.twicePhotoPreference), twicePhoto);
        editor.putBoolean(getString(R.string.captureModePreference), doCapture);
        //editor.putBoolean(getString(R.string.isInOrderPreference), isInOrder);

        editor.remove(getString(R.string.passwordPreference));

        editor.apply();

        if (doCapture)
            createLog(buildSettingsLog());


        Intent i = new Intent(PassFaceAdminActivity.this, PassFaceActivity.class);
        finish();
        startActivity(i);

    }

    private void setRedColor(TextView v) {
        v.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
    }

    private void checkParameters() {
        if (nbPhotos < 1 || nbPhotos > 12) {
            TextView t1 = (TextView) findViewById(R.id.textViewNbPictures);
            setRedColor(t1);
            Toast.makeText(getApplicationContext(), "Number of photos parameters incorrect", Toast.LENGTH_SHORT).show();
        } else if (passwordLength < 1) {
            TextView t1 = (TextView) findViewById(R.id.textViewPasswordLength);
            setRedColor(t1);
            Toast.makeText(getApplicationContext(), "The length of password is under 1", Toast.LENGTH_SHORT).show();


        } else if (!twicePhoto && passwordLength > nbPhotos) {
            TextView t1 = (TextView) findViewById(R.id.textViewNbPictures);
            TextView t2 = (TextView) findViewById(R.id.textViewImageTwice);
            TextView t3 = (TextView) findViewById(R.id.textViewPasswordLength);
            setRedColor(t1);
            setRedColor(t2);
            setRedColor(t3);

            Toast.makeText(getApplicationContext(), "You cannot set this password length with those parameters", Toast.LENGTH_SHORT).show();

        } else if (nbSteps < 1 || nbSteps > 5) {
            TextView t1 = (TextView) findViewById(R.id.textViewNbSteps);
            setRedColor(t1);
            Toast.makeText(getApplicationContext(), "The number of steps is not correct", Toast.LENGTH_SHORT).show();
        } else if (nbAttempts < 1) {
            TextView t1 = (TextView) findViewById(R.id.textViewNbAttempts);
            setRedColor(t1);
            Toast.makeText(getApplicationContext(), "The number of attempts is under 1", Toast.LENGTH_SHORT).show();

        } else
            saveChanges();
    }


    private ArrayList<String> buildSettingsLog() {
        ArrayList<String> settings = new ArrayList<>();
        String columns = "Number Of Photos;Type of Photos;Length per steps;Number of Steps;Number of Attempts;Type of Matching;Shuffle;Use twice an image;Capture mode";
        settings.add(columns);

        String paramVals = ";" + nbPhotos + ";" + typePhotos + ";" + passwordLength + ";" + nbSteps + ";" + nbAttempts + ";" + typeOfMatching + ";" + doShuffle + ";" + twicePhoto + ";" + doCapture;

        settings.add(paramVals);
        return settings;

    }

    private void createLog(ArrayList<String> settings) {
        logWriter.writePassFaceParams(settings);
    }

}


