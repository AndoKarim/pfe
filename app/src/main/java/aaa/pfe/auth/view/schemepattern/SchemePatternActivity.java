package aaa.pfe.auth.view.schemepattern;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import java.util.List;

import aaa.pfe.auth.R;
import aaa.pfe.auth.view.patternlockview.PatternLockView;
import aaa.pfe.auth.view.patternlockview.listener.PatternLockViewListener;
import aaa.pfe.auth.view.patternlockview.utils.PatternLockUtils;
import aaa.pfe.auth.view.patternlockview.utils.ResourceUtils;

public class SchemePatternActivity extends AppCompatActivity {

    
    /*
    TODO : modifier le nombre min. le nombre max.
     */
    private PatternLockView mPatternLockView;
    private Button changeButton;
    private boolean onChangesCode=false;
    SharedPreferences sharedPreferences;

    private PatternLockViewListener mPatternLockViewListener = new PatternLockViewListener() {
        @Override
        public void onStarted() {
            Log.d(getClass().getName(), "Pattern drawing started");
        }

        @Override
        public void onProgress(List<PatternLockView.Dot> progressPattern) {
            Log.d(getClass().getName(), "Pattern progress: " +
                    PatternLockUtils.patternToString(mPatternLockView, progressPattern));
        }

        @Override
        public void onComplete(List<PatternLockView.Dot> pattern) {
            Log.d(getClass().getName(), "Pattern complete: " +
                    PatternLockUtils.patternToString(mPatternLockView, pattern));
            String result = PatternLockUtils.patternToSha1(mPatternLockView, pattern);

            if(onChangesCode){
                Log.d("Save pattern sha1",result);
                sharedPreferences
                        .edit()
                        .putString("schemePatternPass", result)
                        .apply();
                setChangesButton();
                Toast.makeText(SchemePatternActivity.this, "Password saved", Toast.LENGTH_SHORT).show();
                mPatternLockView.clearPattern();

            }else{
                if (sharedPreferences.contains("schemePatternPass")){
                    String savedPass = sharedPreferences.getString("schemePatternPass", null);
                    if(savedPass.equals(result)){
                        Toast.makeText(SchemePatternActivity.this, "Correct password", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(SchemePatternActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        @Override
        public void onCleared() {
            Log.d(getClass().getName(), "Pattern has been cleared");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheme_pattern);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("SchemePattern");


        sharedPreferences = getBaseContext().getSharedPreferences("SchemePreferences", MODE_PRIVATE);

        if (sharedPreferences.contains("schemePatternPass")){
            Toast.makeText(this, "Already saved :" + sharedPreferences.getString("schemePatternPass", null), Toast.LENGTH_SHORT).show();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("No password are registered, you will now register one!")
                    .setTitle("New password");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    setChangesButton();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();

        }

        mPatternLockView = (PatternLockView) findViewById(R.id.pattern_lock_view);
        mPatternLockView.addPatternLockListener(mPatternLockViewListener);

        changeButton = (Button) findViewById(R.id.changeSchemeButton);
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!onChangesCode) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SchemePatternActivity.this);
                    builder.setMessage("You're a new user, please save a password")
                            .setTitle("New user");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                setChangesButton();
            }
        });
        
        //setAllPreferences();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setAllPreferences();
    }

    private void setAllPreferences() {
        int nbRows = 3;
        int nbColumns = 3;
        if (sharedPreferences.contains("nbRows")){
            nbRows = sharedPreferences.getInt("nbRows",1);

        }
        if (sharedPreferences.contains("nbColumns")){
            nbColumns = sharedPreferences.getInt("nbColumns",1);
        }

        if(sharedPreferences.contains("vibration")){
            boolean vibration = sharedPreferences.getBoolean("vibration",true);
            mPatternLockView.setTactileFeedbackEnabled(vibration);
        }

        if(sharedPreferences.contains("stealth")){
            boolean stealth = sharedPreferences.getBoolean("stealth",false);
            mPatternLockView.setInStealthMode(stealth);
        }

        if (sharedPreferences.contains("dotSize")){
            int dotSize = sharedPreferences.getInt("dotSize",30);
            mPatternLockView.setDotNormalSize(dotSize);
        }

        mPatternLockView.setAspectRatio(PatternLockView.AspectRatio.ASPECT_RATIO_FREE);


        mPatternLockView.setDotCount(nbColumns,nbRows);


        /*if(sharedPreferences.contains("lengthPattern")){
            int length = sharedPreferences.getInt("lengthPattern",1);
            //lengthEditText.setText(length);
        }*/




    }


    @Override
    public boolean onSupportNavigateUp(){
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

            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);

            View mView = layoutInflaterAndroid.inflate(R.layout.admin_input_dialog, null);
            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
            alertDialogBuilderUserInput.setView(mView);

            final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
            alertDialogBuilderUserInput
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogBox, int id) {
                            Intent i = new Intent(SchemePatternActivity.this, SchemeAdminActivity.class);
                            finish();
                            startActivity(i);
                        }
                    })

                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogBox, int id) {
                                    dialogBox.cancel();
                                }
                            }) ;

            AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
            alertDialogAndroid.show();



        }
        return super.onOptionsItemSelected(item);
    }


    public void setChangesButton() {
        if(onChangesCode){
            changeButton.setText("New user");
            onChangesCode=false;
        }else{
            changeButton.setText("Cancel");
            onChangesCode=true;
        }
    }
}
