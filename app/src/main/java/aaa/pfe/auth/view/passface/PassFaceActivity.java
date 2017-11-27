package aaa.pfe.auth.view.passface;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
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
    final String[] ANIMALS = new String[]{
            "lion", "tigre", "ours", "abeille", "chien", "chat", "mouton", "baleine", "cheval", "koala", "chevre",
            "fourmi", "renard", "loup", "pigeon", "papillon", "lapin", "lama", "ecureuil", "autruche"};
    final String[] MISC = new String[]{
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19"};


    //View attributes
    private GridView gridView;
    private Button saveButton;
    private Button changeButton;
    private Button submitButton;
    private Button cancelButton;

    private SharedPreferences sharedPreferences;
    //Necessary for the PWD Treatment
    private String userPassword = "";
    private ImageView lastPicChosen;
    private List<ImageView> lastPicsChosen;
    private int lengthOfCurrentPWD = 0;

    //Different parameters
    private int nbPhotos;
    private String typePhotos;
    private int passwordLength;
    private boolean orderOfPwd;
    private int nbStep;             //TODO
    private String typeMatching;    //TODO
    private boolean shuffle;
    private boolean twicePhoto;     //TODO

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_face);

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

        switch (typePhotos) {
            case "Misc":
                String[] newArrayMisc = Arrays.copyOfRange(MISC, 0, nbPhotos);
                gridView.setAdapter(new ImageAdapter(this, newArrayMisc, shuffle));
                break;
            case "Animals":
                String[] newArrayAnimals = Arrays.copyOfRange(ANIMALS, 0, nbPhotos);
                gridView.setAdapter(new ImageAdapter(this, newArrayAnimals, shuffle));

                break;

        }

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
        orderOfPwd = sharedPreferences.getBoolean(getString(R.string.isInOrderPreference), Const.DEFAULT_ORDER);
        nbStep = sharedPreferences.getInt(getString(R.string.numberStepsPreference), Const.DEFAULT_NB_STEPS);
        typeMatching = sharedPreferences.getString(getString(R.string.matchingTypePreference), Const.DEFAULT_MATCHING);
        shuffle = sharedPreferences.getBoolean(getString(R.string.doShufflePreference), Const.DEFAULT_SHUFFLE);
        twicePhoto = sharedPreferences.getBoolean(getString(R.string.twicePhotoPreference), Const.DEFAULT_TWICE_PHOTO);


    }

    //What kind of buttons we show, in function of an existing password or not.

    private void showButtons() {
        if (!sharedPreferences.contains("Password")) {
            saveButton.setVisibility(View.VISIBLE);
            cancelButton.setVisibility(View.VISIBLE);
            changeButton.setVisibility(View.INVISIBLE);
            submitButton.setVisibility(View.INVISIBLE);
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

                //When we try password, we don't have to show hints.
                //For example, when you save a PWD, if the size is 3 you don't show more than
                //3 images with black border. But in Try mode, you don't give this info.
                if (submitButton.getVisibility() == View.VISIBLE) {
                    lengthOfCurrentPWD++;
                    //If this is the first photo of the multiples one, don't add the separator
                    userPassword += (userPassword.equals("")) ? newPhotoName : "+" + newPhotoName;
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
                        userPassword = (String) i.getTag();
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
                                userPassword = userPassword.replace("+" + newPhotoName, "");
                                //In case of the name of image is the first one clicked
                                userPassword = userPassword.replace(newPhotoName, "");
                            } else {
                                lengthOfCurrentPWD++;
                                //If this is the first photo of the multiples one, don't add the separator
                                userPassword += (userPassword.equals("")) ? newPhotoName : "+" + newPhotoName;
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
                if (userPassword.equals("") || lengthOfCurrentPWD != passwordLength) {
                    Toast t = Toast.makeText(getApplicationContext(), R.string.Password_not_changed, Toast.LENGTH_SHORT);
                    t.show();
                } else {
                    sharedPreferences.edit().putString(getString(R.string.passwordPreference), userPassword).apply();

                    saveButton.setVisibility(View.INVISIBLE);
                    submitButton.setVisibility(View.VISIBLE);
                    changeButton.setVisibility(View.VISIBLE);

                    for (ImageView i : lastPicsChosen) {
                        i.setPadding(0, 0, 0, 0);
                        userPassword = "";

                    }
                    Toast t = Toast.makeText(getApplicationContext(), R.string.Password_changed, Toast.LENGTH_SHORT);
                    t.show();
                }

            }
        });


        //Handle the submit button when you want to test a Password
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentPwd = sharedPreferences.getString("Password", "");


                if (currentPwd.equals(userPassword) || (!orderOfPwd && samePwdDifferentOrders(currentPwd, userPassword))) {
                    Toast t = Toast.makeText(getApplicationContext(), R.string.good_pwd, Toast.LENGTH_SHORT);
                    t.show();
                } else {
                    Toast t = Toast.makeText(getApplicationContext(), R.string.wrong_pwd, Toast.LENGTH_SHORT);
                    t.show();
                }
            }
        });

        //If you forgot your password, you click on that to change the password
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences.edit().remove("Password").apply();
                submitButton.setVisibility(View.INVISIBLE);
                changeButton.setVisibility(View.INVISIBLE);
                saveButton.setVisibility(View.VISIBLE);
                cancelButton.setVisibility(View.VISIBLE);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (ImageView i : lastPicsChosen) {
                    i.setPadding(0, 0, 0, 0);
                }
                lengthOfCurrentPWD = 0;
                userPassword = "";
            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.authmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.adminPanel) {
            Intent i = new Intent(PassFaceActivity.this, PassFaceAdminActivity.class);
            finish();
            startActivity(i);

        }
        return super.onOptionsItemSelected(item);
    }

    public boolean samePwdDifferentOrders(String a, String b) {
        char[] first = a.toCharArray();
        char[] second = b.toCharArray();
        Arrays.sort(first);
        Arrays.sort(second);
        return Arrays.equals(first, second);
    }
}
