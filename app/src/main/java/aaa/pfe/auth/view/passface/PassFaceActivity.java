package aaa.pfe.auth.view.passface;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import aaa.pfe.auth.R;
import aaa.pfe.auth.utils.Const;

public class PassFaceActivity extends AppCompatActivity {


    //View attributes
    private GridView gridView;
    private Button saveButton;
    private Button changeButton;
    private Button submitButton;
    private Button cancelButton;

    private SharedPreferences sharedPreferences;
    //Necessary for the PWD Treatment
    private String enteredPassword = "";
    private ImageView lastPicChosen;
    private List<ImageView> lastPicsChosen;
    private int lengthOfCurrentPWD = 0;
    private int currentStep = 0;

    //Different parameters
    private int nbPhotos;
    private String typePhotos;
    private int passwordLength;
    //private boolean orderOfPwd;
    private int nbStep;             //TODO
    private String typeMatching;    //TODO
    private boolean shuffle;
    private boolean twicePhoto;     //TODO

    private int idUser;
    private ArrayList<String> logs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_face);
        idUser = 1;
        logs.add("Date;Username;Event;Position;Value");
        PassFaceAdminActivity.logWriter.writePassFaceCols(logs);
        logs.clear();

        logs.add(getTimeAndUsername() + "Start of the experience");
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.passFace);

        //We reset the PWD
        lengthOfCurrentPWD = 0;
        sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.passFacePreferences), MODE_PRIVATE);

        loadParameters();

        gridView = (GridView) findViewById(R.id.gridView);


        updateGridView();


        lastPicsChosen = new ArrayList<>();

        saveButton = (Button) findViewById(R.id.saveButton);
        changeButton = (Button) findViewById(R.id.changeButton);
        submitButton = (Button) findViewById(R.id.submitButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        setupListeners();


        showButtons();

    }

    private void loadParameters() {
        nbPhotos = sharedPreferences.getInt(getString(R.string.nbPhotosPreference), Const.DEFAULT_NB_PHOTOS);
        typePhotos = sharedPreferences.getString(getString(R.string.typePhotosPreference), Const.DEFAULT_TYPE_PHOTOS);
        passwordLength = sharedPreferences.getInt(getString(R.string.passwordLengthPreference), Const.DEFAULT_PWD_LENGTH);
        nbStep = sharedPreferences.getInt(getString(R.string.numberStepsPreference), Const.DEFAULT_NB_STEPS);
        typeMatching = sharedPreferences.getString(getString(R.string.matchingTypePreference), Const.DEFAULT_MATCHING);
        shuffle = sharedPreferences.getBoolean(getString(R.string.doShufflePreference), Const.DEFAULT_SHUFFLE);
        twicePhoto = sharedPreferences.getBoolean(getString(R.string.twicePhotoPreference), Const.DEFAULT_TWICE_PHOTO);
        // orderOfPwd = sharedPreferences.getBoolean(getString(R.string.isInOrderPreference), Const.DEFAULT_ORDER);


    }

    //What kind of buttons we show, in function of an existing password or not.

    private void showButtons() {
        if (!sharedPreferences.contains("Password")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("No password are registered, you will now register one!")
                    .setTitle("New password");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    saveButton.setVisibility(View.VISIBLE);
                    cancelButton.setVisibility(View.VISIBLE);
                    changeButton.setVisibility(View.INVISIBLE);
                    submitButton.setVisibility(View.INVISIBLE);
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();

        } else {
            changeButton.setVisibility(View.VISIBLE);
            submitButton.setVisibility(View.VISIBLE);
            cancelButton.setVisibility(View.VISIBLE);
        }
    }


    //Method to set up all the click listeners of our view
    private void setupListeners() {

        //Handle the click on one image
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                ImageView i = (ImageView) ((LinearLayout) v).getChildAt(0);
                String newPhotoName = i.getTag().toString();
                lengthOfCurrentPWD++;

                logs.add(getTimeAndUsername() + "Photo clicked;" + position + ";" + newPhotoName);


                //When we try password, we don't have to show hints.
                //For example, when you save a PWD, if the size is 3 you don't show more than
                //3 images with black border. But in Try mode, you don't give this info.
                if (submitButton.getVisibility() == View.VISIBLE) {
                    lengthOfCurrentPWD++;
                    //If this is the first photo of the multiples one, don't add the separator
                    enteredPassword += (enteredPassword.equals("")) ? newPhotoName : "+" + newPhotoName;
                    //Add the imageView to the list, in case of removing the border
                    lastPicsChosen.add(i);
                    i.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                    i.setPadding(15, 15, 15, 15);
                } else {

                    //If the password is only with one image, we remove the border from the previous image
                    // we delete the password and replace it by the new image chosen.
                    if (passwordLength == 1) {
                        if (lastPicChosen != null)
                            lastPicChosen.setPadding(0, 0, 0, 0);
                        if (nbStep == 1)
                            enteredPassword = (String) i.getTag();
                        else
                            enteredPassword += (enteredPassword.equals("")) ? newPhotoName : "+" + newPhotoName;
                        lastPicChosen = i;
                        i.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                        i.setPadding(15, 15, 15, 15);
                    }
                    //Else, we add the new photo chosen
                    else {
                        if (lengthOfCurrentPWD < passwordLength) {
                            //If we already clicked on that image and we don't allow same image twice
                            if (i.getPaddingBottom() != 0 && !twicePhoto) {
                                //We remove the border
                                i.setPadding(0, 0, 0, 0);
                                lengthOfCurrentPWD--;
                                //We remove the name in the PWD
                                enteredPassword = enteredPassword.replace("+" + newPhotoName, "");
                                //In case of the name of image is the first one clicked
                                enteredPassword = enteredPassword.replace(newPhotoName, "");
                            } else {
                                lengthOfCurrentPWD++;
                                //If this is the first photo of the multiples one, don't add the separator
                                enteredPassword += (enteredPassword.equals("")) ? newPhotoName : "+" + newPhotoName;
                                //Add the imageView to the list, in case of removing the border
                                lastPicsChosen.add(i);
                                i.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                                i.setPadding(15, 15, 15, 15);

                            }

                        }
                    }
                }

            }
        });

        //Handle the Save button when we setup a new password

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logs.add(getTimeAndUsername() + "Saved button clicked");
                if (enteredPassword.equals("") || lengthOfCurrentPWD != passwordLength) {
                    Toast t = Toast.makeText(getApplicationContext(), R.string.Password_not_changed, Toast.LENGTH_SHORT);
                    t.show();
                    logs.add(getTimeAndUsername() + "Illegal new password");
                } else {
                    if (nbStep != 1 && currentStep != nbStep - 1) {
                        currentStep++;
                        lengthOfCurrentPWD = 0;
                        updateGridView();
                    } else {

                        sharedPreferences.edit().putString(getString(R.string.passwordPreference), enteredPassword).apply();

                        saveButton.setVisibility(View.INVISIBLE);
                        submitButton.setVisibility(View.VISIBLE);
                        changeButton.setVisibility(View.VISIBLE);

                        removeBorders();
                        enteredPassword = "";
                        currentStep = 0;
                        updateGridView();

                        Toast t = Toast.makeText(getApplicationContext(), R.string.Password_changed, Toast.LENGTH_SHORT);
                        t.show();
                        logs.add(getTimeAndUsername() + "New password saved");
                    }
                }

            }
        });


        //Handle the submit button when you want to test a Password
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String savedPassword = sharedPreferences.getString("Password", "");
                logs.add(getTimeAndUsername() + "Submit button clicked");
                //If we have more than one step
                if (nbStep != 1) {
                    int arrayStart = currentStep * passwordLength;
                    int arrayEnd = arrayStart + passwordLength;
                    String[] splitted = savedPassword.split("\\+");
                    String cmp = "";
                    for (int i = arrayStart; i < arrayEnd; i++)
                        cmp += splitted[i];

                    //If the password is the same for this step
                    if (samePwdDifferentOrders(cmp, enteredPassword)) {
                        //We remove the current step value because we will not need it anymore
                        enteredPassword = enteredPassword.replace(cmp, "");
                        //If we are not on the last step
                        if (currentStep != nbStep - 1) {
                            currentStep++;
                            updateGridView();
                        } else {
                            logs.add(getTimeAndUsername() + "Good password entered");
                            Toast.makeText(getApplicationContext(), R.string.good_pwd, Toast.LENGTH_SHORT).show();
                            currentStep = 0;
                            lengthOfCurrentPWD = 0;
                            updateGridView();
                            enteredPassword = "";
                            PassFaceAdminActivity.logWriter.writePassfaceEvent(logs);
                            logs.clear();
                        }
                    } else {
                        logs.add(getTimeAndUsername() + "Wrong password entered");
                        Toast t = Toast.makeText(getApplicationContext(), R.string.wrong_pwd, Toast.LENGTH_SHORT);
                        t.show();
                        removeBorders();
                        lengthOfCurrentPWD = 0;
                        enteredPassword = "";
                    }

                } else if (savedPassword.equals(enteredPassword) /*|| (!orderOfPwd && samePwdDifferentOrders(currentPwd, enteredPassword))*/) {
                    logs.add(getTimeAndUsername() + "Good password entered");
                    PassFaceAdminActivity.logWriter.writePassfaceEvent(logs);
                    logs.clear();
                    Toast.makeText(getApplicationContext(), R.string.good_pwd, Toast.LENGTH_SHORT).show();
                    currentStep = 0;
                } else {
                    logs.add(getTimeAndUsername() + "Wrong password entered");

                    Toast.makeText(getApplicationContext(), R.string.wrong_pwd, Toast.LENGTH_SHORT).show();
                    removeBorders();
                    lengthOfCurrentPWD = 0;
                    enteredPassword = "";
                }
            }
        });

        //If you forgot your password, you click on that to change the password
        changeButton.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                logs.add(getTimeAndUsername() + "Change password button clicked");
                sharedPreferences.edit().remove("Password").apply();
                submitButton.setVisibility(View.INVISIBLE);
                changeButton.setVisibility(View.INVISIBLE);
                saveButton.setVisibility(View.VISIBLE);
                cancelButton.setVisibility(View.VISIBLE);
                currentStep = 0;
                enteredPassword = "";
                lengthOfCurrentPWD = 0;
                idUser++;
                logs.add(getTimeAndUsername() + "Start of the experience");
                PassFaceAdminActivity.logWriter.writePassfaceEvent(logs);
                logs.clear();

                updateGridView();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                logs.add(getTimeAndUsername() + "Cancel button clicked");
                removeBorders();
                lengthOfCurrentPWD = 0;
                enteredPassword = "";
            }
        });


    }

    private void updateGridView() {

        String[] newArray = new String[0];
        int startArray = currentStep * (nbPhotos + 1);
        int endArray = startArray + nbPhotos;
        switch (typePhotos) {
            case "Misc":
                newArray = Arrays.copyOfRange(Const.MISC, startArray, endArray);
                break;
            case "Animals":
                newArray = Arrays.copyOfRange(Const.ANIMALS, startArray, endArray);
                break;
        }
        gridView.setAdapter(new ImageAdapter(this, newArray, shuffle));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


    public boolean samePwdDifferentOrders(String a, String b) {
        char[] first = a.toCharArray();
        char[] second = b.toCharArray();
        Arrays.sort(first);
        Arrays.sort(second);
        return Arrays.equals(first, second);
    }

    private void removeBorders() {
        for (ImageView i : lastPicsChosen) {
            i.setPadding(0, 0, 0, 0);
        }
        if (lastPicChosen != null) {
            lastPicChosen.setPadding(0, 0, 0, 0);
        }
    }

    private String getTimeAndUsername() {
        return ";" + System.currentTimeMillis() + ";" + "user" + idUser + ";";
    }
}
