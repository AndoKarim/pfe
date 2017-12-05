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


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import aaa.pfe.auth.R;
import aaa.pfe.auth.utils.LogWriter;
import aaa.pfe.auth.view.patternlockview.PatternLockView;
import aaa.pfe.auth.view.patternlockview.listener.PatternLockViewListener;
import aaa.pfe.auth.view.patternlockview.utils.PatternLockUtils;
import aaa.pfe.auth.view.patternlockview.utils.ResourceUtils;

import static aaa.pfe.auth.view.passface.PassFaceAdminActivity.logWriter;

public class SchemePatternActivity extends AppCompatActivity {

    private PatternLockView mPatternLockView;
    private Button changeButton;
    private boolean onChangesCode=false;
    SharedPreferences sharedPreferences;

    private int nbRows = 3;
    private int nbColumns = 3;
    private int dotSize;
    private int nbUser=1;
    private int maxDot;
    private boolean vibration;
    private boolean stealth;
    private boolean captureMode;
    private boolean newUser;
    private ArrayList<String> logsArray = new ArrayList<>();

    private int nbTry;
    private int maxTry=3;

    private PatternLockViewListener mPatternLockViewListener = new PatternLockViewListener() {

        private long begin;
        @Override
        public void onStarted() {
            Log.d(getClass().getName(), "Pattern drawing started");

            begin = System.currentTimeMillis();
        }

        @Override
        public void onProgress(List<PatternLockView.Dot> progressPattern) {
            if(captureMode && !onChangesCode) {
                String dot = PatternLockUtils.getLastClickedDot(mPatternLockView, progressPattern);
                Log.d(getClass().getName(), "Pattern progress:" +
                        dot);
                Log.d(getClass().getName(), "Pattern Size:" +
                        progressPattern.size());
                    logsArray.add(dot);
                    long currentTime = System.currentTimeMillis();
                    long lastTouchTime = currentTime - begin;
                    logsArray.add(lastTouchTime + "");

                Log.d("test","test");

            }

        }

        @Override
        public void onComplete(List<PatternLockView.Dot> pattern, boolean sizeReached) {
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

                //mPatternLockView.clearPattern();
            }else{
                nbTry ++;
                if(sizeReached){
                    Log.d(getClass().getName(), "Max Size Reached: " +
                            PatternLockUtils.patternToString(mPatternLockView, pattern));
                    Toast.makeText(SchemePatternActivity.this, "Max Size Reached", Toast.LENGTH_SHORT).show();
                }



                if (sharedPreferences.contains("schemePatternPass")){
                    String savedPass = sharedPreferences.getString("schemePatternPass", null);
                    if(savedPass.equals(result)){
                        Toast.makeText(SchemePatternActivity.this, "Correct password", Toast.LENGTH_SHORT).show();
                        logsArray.add(0,"Success");
                    }else{
                        Toast.makeText(SchemePatternActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                        logsArray.add(0,"Fail");
                    }
                }

                if(nbTry==maxTry){
                    //TODO Créer un dialogBox

                    AlertDialog.Builder builder = new AlertDialog.Builder(SchemePatternActivity.this);
                    builder.setMessage("Max try reached")
                            .setTitle("Max try");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            changeButton.performClick();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();

                }

                //mPatternLockView.clearPattern();

                if(newUser) {
                    logWriter.logFirstTentative("User" + nbUser, logsArray);
                    newUser=false;
                }else{
                    logWriter.logNextTentative(logsArray);
                }
                logsArray.clear();
            }
        }
        @Override
        public void onCleared() {
            Log.d(getClass().getName(), "Pattern has been cleared");
        }

        @Override
        public void onSizeReached(List<PatternLockView.Dot> pattern) {

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

        /*Log*/
        if (captureMode) {
            logWriter = new LogWriter("lockPattern");
            ArrayList<String> params = new ArrayList<>();
            params.add("nbRows="+nbRows);
            params.add("nbColumns="+nbColumns);
            params.add("maxSize="+maxDot);
            params.add("dotSize="+dotSize);
            params.add("vibration="+vibration);
            params.add("stealth="+stealth);
            logWriter.logParams(params);
            nbUser = 0;
            ArrayList<String> columns = new ArrayList<>();
            columns.add("Result");
            for(int i=0; i < maxDot ; i++) {
                columns.add("Value "+i);
                columns.add("Time");
            }
            logWriter.logColumnsNames(columns);
        }
    }

    private void setAllPreferences() {

            nbRows = sharedPreferences.getInt("nbRows",3);

            nbColumns = sharedPreferences.getInt("nbColumns",3);

            maxDot = sharedPreferences.getInt("maxSize",9);
            mPatternLockView.setPatternMaxSize(maxDot);

            maxTry = sharedPreferences.getInt("maxTry",3);

            vibration = sharedPreferences.getBoolean("vibration",true);
            mPatternLockView.setTactileFeedbackEnabled(vibration);

            stealth = sharedPreferences.getBoolean("stealth",false);
            mPatternLockView.setInStealthMode(stealth);

            dotSize = sharedPreferences.getInt("dotSize",30);
            mPatternLockView.setDotNormalSize(dotSize);

            captureMode = sharedPreferences.getBoolean("captureMode",true);


        mPatternLockView.setAspectRatio(PatternLockView.AspectRatio.ASPECT_RATIO_FREE);


        mPatternLockView.setDotCount(nbColumns,nbRows);
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
            if (captureMode) {
                nbUser++;
                newUser = true;
                nbTry = 0;
            }
        }
    }
}
