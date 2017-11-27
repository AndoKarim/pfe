package aaa.pfe.auth.view.pincode;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import aaa.pfe.auth.R;
import aaa.pfe.auth.view.pincodeview.IndicatorDots;
import aaa.pfe.auth.view.pincodeview.PinLockListener;
import aaa.pfe.auth.view.pincodeview.PinLockView;
import aaa.pfe.auth.view.schemepattern.SchemePatternActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PinCodeActivity extends AppCompatActivity {
    public static final String TAG = "PinLockView";
    public static final String PREFERENCES = "PinPreferences";

    private boolean onChangingCode = false;
    private Button changeButton;

    private PinLockView mPinLockView;
    private IndicatorDots mIndicatorDots;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private boolean shuffle;

    //capture mode
    private boolean captureMode;
    private int nbTry,MAX_TRY;
    private ArrayList<Integer> succeedArray,failArray; //met dans un tableau le nombre d'essai quand on réussi ou quand on rate (pour avoir le nombre de reussite .lenght() )
    private OutputStream logFile;

    private PinLockListener mPinLockListener = new PinLockListener() {
        @Override
        public void onComplete(String pin) {
            Log.i(TAG, "Pin complete: " + pin);
            nbTry ++;

            //TOAST
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;
            CharSequence resultPin;

            if (!onChangingCode) {
                if (!sharedPreferences.contains("pincode")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(PinCodeActivity.this);
                    showAlertNoCode(builder);
                }else {
                    if (sharedPreferences.getString("pincode", null).equals(pin)) {
                        //Correct PIN
                        resultPin = "Correct Pin!";
                        nbTry = 0;
                        if (captureMode)
                            succeedArray.add(nbTry);
                    } else {//Incorrect PIN
                        resultPin = "Incorrect Pin!";
                        if (captureMode && (nbTry >=MAX_TRY)){
                            failArray.add(nbTry);
                            nbTry = 0;
                        }

                    }

                    Toast.makeText(context, resultPin, duration).show();
                }

            }else{
                editor.putString("pincode", pin);
                editor.commit();
                setChangesButton();
                nbTry = 0;
                Toast.makeText(context,"pin updated", duration).show();

            }

            if (shuffle)
                mPinLockView.enableLayoutShuffling();
            mPinLockView.resetPinLockView();
        }

        @Override
        public void onEmpty() {

            Log.i(TAG, "Pin empty");
            mPinLockView.resetPinLockView();
        }

        @Override
        public void onPinChange(int pinLength, String intermediatePin) {
            Log.i(TAG, "Pin changed, new length " + pinLength + " with intermediate pin " + intermediatePin);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_code);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Pincode");

        sharedPreferences = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if (!sharedPreferences.contains("pincode")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            showAlertNoCode(builder);
        }


        //PINLOCKVIEW
        mPinLockView = (PinLockView) findViewById(R.id.pin_lock_view);
        mIndicatorDots = (IndicatorDots) findViewById(R.id.indicator_dots);

        mPinLockView.attachIndicatorDots(mIndicatorDots);
        mPinLockView.setPinLockListener(mPinLockListener);

        mPinLockView.setTextColor(ContextCompat.getColor(this, R.color.greyish));
        changeButton = (Button) findViewById(R.id.change_pin);
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!onChangingCode) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PinCodeActivity.this);
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
                mPinLockView.resetPinLockView();

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        //re-initialize Pin
        mPinLockView.resetPinLockView();
        nbTry = 0;
        succeedArray = new ArrayList<>();
        failArray = new ArrayList<>();

        /*PARAMS*/
        int pinLength =sharedPreferences.getInt("pinLength",4);
        mPinLockView.setPinLength(pinLength);


        MAX_TRY = sharedPreferences.getInt("nbTry",3);

        if (sharedPreferences.contains("randNum")&&sharedPreferences.getBoolean("randNum",false)){
            mPinLockView.enableLayoutShuffling();
            shuffle = true;
            Log.i("PincodeActivity","randNum");
        }else {
            mPinLockView.setCustomKeySet(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 0}); //Remet les touches dans l'ordre quand je désactive le flush dans les paramètres admin
            shuffle = false;
        }


       int  indicatorType=  sharedPreferences.getInt("indicators",-1);
       if (indicatorType>=0){
           if (!mPinLockView.isIndicatorDotsAttached()) {
               mIndicatorDots.setVisibility(View.VISIBLE);
               mPinLockView.attachIndicatorDots(mIndicatorDots);
           }
           mIndicatorDots.setIndicatorType(indicatorType);
       }else{
           mPinLockView.takeOffIndicatorDots();
           mIndicatorDots.setVisibility(View.INVISIBLE);
       }



        captureMode = sharedPreferences.getBoolean("captureMode",false);

        /*Log*/
        if (captureMode) {
            String params = "" + shuffle + MAX_TRY + pinLength + indicatorType;
            String filename = String.valueOf(params.hashCode()) + ".log";
            if (CheckExisting(getApplicationContext(), filename)) {
                Log.i("test","here1");
                try {
                    logFile = openFileOutput(filename, MODE_APPEND);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    Log.i("test","here2");
                    logFile = openFileOutput(filename, MODE_PRIVATE);
                    JSONObject tmp = new JSONObject();
                    tmp.put("test",3);
                    logFile.write(tmp.toString().getBytes());
                } catch (IOException e) {
                            e.printStackTrace();
                } catch (JSONException e) {
                        e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(captureMode && (logFile != null))
            try {
                logFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
            Intent i = new Intent(PinCodeActivity.this, PinCodeAdminActivity.class);
            finish();
            startActivity(i);

        }
        return super.onOptionsItemSelected(item);
    }

    public void setChangesButton() {
        if(onChangingCode){
            changeButton.setText("New user");
            onChangingCode=false;
        }else{
            changeButton.setText("Cancel");
            onChangingCode=true;
        }
    }

    private void showAlertNoCode(AlertDialog.Builder builder){
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

    private  boolean CheckExisting(Context context, String file) {
        String[] filenames = context.fileList();
        for (String name : filenames) {
            if (name.equals(file)) {
                return true;
            }
        }
        return false;
    }
}
