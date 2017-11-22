package aaa.pfe.auth.view.passface;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import java.util.List;

import aaa.pfe.auth.R;

public class PassFaceActivity extends AppCompatActivity {
    static final String[] ANIMALS = new String[]{
            "Lion", "Tigre", "Ours", "Abeille", "Chien", "Chat", "Mouton", "Baleine", "Cheval"};
    static final String[] NUMBERS = new String[]{
            "0", "1", "2", "3", "4", "5", "6", "7", "8"};


    //View attributes
    GridView gridView;
    Button saveButton;
    Button changeButton;
    Button submitButton;

    SharedPreferences sharedPreferences;

    //Necessary for the PWD Treatment
    String picName = "";
    ImageView lastPicChosen;
    List<ImageView> lastPicsChosen;
    int lengthOfCurrentPWD = 0;

    //Differents parameters
    int passwordLength = 1;
    boolean orderOfPwd = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_face);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Passface");


        gridView = (GridView) findViewById(R.id.gridView);

        gridView.setAdapter(new ImageAdapter(this, NUMBERS));

        lastPicsChosen = new ArrayList<>();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                ImageView i = (ImageView) ((LinearLayout) v).getChildAt(0);

                //If the password is only with one image, we remove the border from the previous image
                // we delete the password and replace it by the new image chosen.
                if (passwordLength == 1) {
                    if (lastPicChosen != null)
                        lastPicChosen.setPadding(0, 0, 0, 0);
                    picName = (String) i.getTag();
                    lastPicChosen = i;
                }
                //Else, we add the new photo chosen
                else {
                    if (lengthOfCurrentPWD < passwordLength) {
                        String newPhoto = i.getTag().toString();
                        //If this is the first photo of the multiples one, don't add the separator
                        picName += (picName.equals("")) ? newPhoto : "+" + newPhoto;
                        //Add the imageView to the list, in case of removing the border
                        lastPicsChosen.add(i);
                    }
                }
                i.setBackgroundColor(getResources().getColor(R.color.black));
                i.setPadding(15, 15, 15, 15);

            }
        });

        sharedPreferences = getApplicationContext().getSharedPreferences("PassFacePreferences", MODE_PRIVATE);

        saveButton = (Button) findViewById(R.id.saveButton);
        changeButton = (Button) findViewById(R.id.changeButton);
        submitButton = (Button) findViewById(R.id.submitButton);

        if (!sharedPreferences.contains("Password"))
            saveButton.setVisibility(View.VISIBLE);
        else {
            changeButton.setVisibility(View.VISIBLE);
            submitButton.setVisibility(View.VISIBLE);
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!picName.equals("")) {
                    sharedPreferences.edit().putString("Password", picName).commit();
                    saveButton.setVisibility(View.INVISIBLE);
                    submitButton.setVisibility(View.VISIBLE);
                    changeButton.setVisibility(View.VISIBLE);
                    Toast t = Toast.makeText(getApplicationContext(), "Password changed", Toast.LENGTH_SHORT);
                    t.show();
                }

            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentPwd = sharedPreferences.getString("Password", "");
                if (currentPwd.equals(picName)) {
                    Toast t = Toast.makeText(getApplicationContext(), "Good Pwd", Toast.LENGTH_SHORT);
                    t.show();
                } else {
                    Toast t = Toast.makeText(getApplicationContext(), "Wrong Pwd", Toast.LENGTH_SHORT);
                    t.show();
                }
            }
        });

        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences.edit().remove("Password").commit();
                submitButton.setVisibility(View.INVISIBLE);
                changeButton.setVisibility(View.INVISIBLE);
                saveButton.setVisibility(View.VISIBLE);
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

        if (id == R.id.pullFromServer) {
            // do something here
            Toast t = Toast.makeText(this, "Pull From Server", Toast.LENGTH_SHORT);
            t.show();
        }

        if (id == R.id.adminPanel) {
            Intent i = new Intent(PassFaceActivity.this, PassFaceAdminActivity.class);
            startActivity(i);

        }
        return super.onOptionsItemSelected(item);
    }


}
