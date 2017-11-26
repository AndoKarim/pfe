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

import aaa.pfe.auth.R;
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

    private PinLockListener mPinLockListener = new PinLockListener() {
        @Override
        public void onComplete(String pin) {
            Log.i(TAG, "Pin complete: " + pin);

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


                    } else {
                        resultPin = "Incorrect Pin!";
                    }

                    Toast.makeText(context, resultPin, duration).show();
                }

            }else{
                editor.putString("pincode", pin);
                editor.commit();
                setChangesButton();

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
        //mIndicatorDots.setIndicatorType(IndicatorDots.IndicatorType.FILL_WITH_NUMBER_ANIMATION);
        mIndicatorDots.setIndicatorType(IndicatorDots.IndicatorType.FIXED);
        changeButton = (Button) findViewById(R.id.change_pin);
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        /*PARAMS*/
        if (sharedPreferences.contains("pinLength")){
            mPinLockView.setPinLength(sharedPreferences.getInt("pinLength",4));
        }

        if (sharedPreferences.contains("randNum")&&sharedPreferences.getBoolean("randNum",false)){
            mPinLockView.enableLayoutShuffling();
            shuffle = true;
            Log.i("PincodeActivity","randNum");
        }else {
            mPinLockView.setCustomKeySet(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 0}); //Remet les touches dans l'ordre quand je désactive le flush dans les paramètres admin
            shuffle = false;
        }

        if(sharedPreferences.contains("indicators")){
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

        if (id == R.id.pullFromServer) {
            // do something here
            Toast t =Toast.makeText(this,"Pull From Server",Toast.LENGTH_SHORT);
            t.show();
        }

        if (id == R.id.adminPanel) {
            Intent i = new Intent(PinCodeActivity.this, PinCodeAdminActivity.class);
            startActivity(i);

        }
        return super.onOptionsItemSelected(item);
    }

    public void setChangesButton() {
        if(onChangingCode){
            changeButton.setText("Change");
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
}
