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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import aaa.pfe.auth.R;
import aaa.pfe.auth.utils.LogWriter;
import aaa.pfe.auth.utils.Pair;
import aaa.pfe.auth.view.pincodeview.IndicatorDots;
import aaa.pfe.auth.view.pincodeview.PinLockListener;
import aaa.pfe.auth.view.pincodeview.PinLockView;


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
    private boolean captureMode,newUser;
    private int nbTry,MAX_TRY,nbUser;
    LogWriter logWriter;
    Timestamp timestamp;
    long lastTouchTime;
    long begin;
    Map<Integer,Pair<String,Long>> toucheMap;

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
                        if (captureMode){
                            saveTouch(pin.length(),pin);//last touch
                            writeAttempt("success");
                        }
                        nbTry = 0;
                    } else {//Incorrect PIN
                        resultPin = "Incorrect Pin!";
                        if (captureMode){

                            if (nbTry >= MAX_TRY){
                                writeAttempt("fatal fail");
                                nbTry = 0;
                            }else{
                                writeAttempt("fail");
                            }

                        }

                    }

                    Toast.makeText(context, resultPin, duration).show();
                }

            }else{
                editor.putString("pincode", pin);
                editor.commit();
                setChangesButton();
                nbTry = 0;
                newUser = true;
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
        public void onPinChange(int intermediatePinLength, String intermediatePin) {
            Log.i(TAG, "Pin changed, new length " + intermediatePinLength + " with intermediate pin " + intermediatePin);
            if (captureMode && !onChangingCode) {
                if (intermediatePinLength==1)
                    begin = System.currentTimeMillis();
                saveTouch(intermediatePinLength,intermediatePin);
                //derniere lettre intermediatePin.substring(intermediatePin.length()-1, intermediatePin.length());
            }

        }
    };

    private void saveTouch(int length, String intermediatePin) {
        long currentTime = System.currentTimeMillis();
        lastTouchTime = currentTime - begin;
        toucheMap.put(length,new Pair<String, Long>(intermediatePin.substring(intermediatePin.length()-1, intermediatePin.length()),lastTouchTime));
    }

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

        newUser = true;

    }

    @Override
    protected void onResume() {
        super.onResume();
        //re-initialize Pin
        mPinLockView.resetPinLockView();
        nbTry = 0;

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
            logWriter = new LogWriter("pincode");
            ArrayList<String> params = new ArrayList<>();
            params.add("shuffle="+shuffle);
            params.add("pinLength="+pinLength);
            params.add("try="+MAX_TRY);
            params.add("indicatorType="+indicatorType);
            logWriter.logParams(params);
            nbUser = 0;
            timestamp = new Timestamp(System.currentTimeMillis());
            toucheMap = new HashMap();
            //Coloumns name
            ArrayList<String> columns = new ArrayList<>();
            columns.add("result");
            for (int i=1;i<pinLength+1;i++){
                columns.add("touche"+i);
                columns.add("value");
                columns.add("time");
            }
            logWriter.logNextTentative(columns);//ecris la valeur des colonnes
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (captureMode){
            fillUserLog();
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

            if (captureMode) {
                fillUserLog();
                nbUser++;
            }
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

    private void fillUserLog(){
        while ((nbTry < MAX_TRY) && (nbTry!=0)){ //!=0 si l'user quitte juste après après avoir réussi (ou fail MAX_TRY fois)
            //TODO: remplir ligne avec valeur vide ("")
            nbTry ++;
        }
    }

    private void writeAttempt(String result){
            String[] valTab = new String[3*toucheMap.size()+1];
            //valArr.add(result);
            valTab[0]=result;

            int j;
            for(Integer key: toucheMap.keySet()){
                j=3*(key-1)+1;
                //valArr.add(j++,key.toString()); //position touche
                //valArr.add(j++,toucheMap.get(key).getFirst()); // valeur touche
                //valArr.add(j,toucheMap.get(key).getSecond().toString()); // temps touche
                valTab[j++]=key.toString();
                valTab[j++]=toucheMap.get(key).getFirst();
                valTab[j++]=toucheMap.get(key).getSecond().toString();
            }

            ArrayList<String> valArr =  new ArrayList( Arrays.asList(valTab));

            if (nbTry == 1 && newUser) {
                logWriter.logFirstTentative("user" + nbUser, valArr);
                newUser = false;
            }else {
                logWriter.logNextTentative(valArr);
            }
    }
}


//TODO: timing, eviter extinction de l'écran pdt un certain temps,si le mec appuie sur cancel??