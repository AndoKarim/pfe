package aaa.pfe.auth.view.pincode;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
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

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    private PinLockListener mPinLockListener = new PinLockListener() {
        @Override
        public void onComplete(String pin) {
            Log.i(TAG, "Pin complete: " + pin);

            //TOAST
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;
            CharSequence resultPin;

            if (!onChangingCode) {

                if (sharedpreferences.contains("pincode") && sharedpreferences.getString("pincode",null).equals(pin)) {
                    //Correct PIN
                    resultPin = "Correct Pin!";


                } else {
                    resultPin = "Incorrect Pin!";
                }

                Toast.makeText(context, resultPin, duration).show();

            }else{
                editor.putString("pincode", pin);
                editor.commit();
                changeButton.setText("Change");
                onChangingCode = false;

                Toast.makeText(context,"pin updated", duration).show();

            }

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

        sharedpreferences = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();


        //PINLOCKVIEW
        mPinLockView = (PinLockView) findViewById(R.id.pin_lock_view);
        mIndicatorDots = (IndicatorDots) findViewById(R.id.indicator_dots);

        mPinLockView.attachIndicatorDots(mIndicatorDots);
        mPinLockView.setPinLockListener(mPinLockListener);

        /*PARAMS*/
        //mPinLockView.setCustomKeySet(new int[]{2, 3, 1, 5, 9, 6, 7, 0, 8, 4}); //change ordre clavier
        //mPinLockView.enableLayoutShuffling(); //disposition aléatoire
        mPinLockView.setPinLength(5);
        mPinLockView.setTextColor(ContextCompat.getColor(this, R.color.greyish));
        //mIndicatorDots.setIndicatorType(IndicatorDots.IndicatorType.FILL_WITH_NUMBER_ANIMATION);
        mIndicatorDots.setIndicatorType(IndicatorDots.IndicatorType.FILL_WITH_ANIMATION);

        changeButton = (Button) findViewById(R.id.change_pin);
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!onChangingCode) {
                    changeButton.setText("Annuler");
                    onChangingCode = true;
                    mPinLockView.resetPinLockView();
                }else{
                    changeButton.setText("Change");
                    onChangingCode = false;
                    mPinLockView.resetPinLockView();
                }
            }
        });
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
}
