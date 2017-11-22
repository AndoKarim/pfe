package aaa.pfe.auth.view.schemepattern;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import java.util.List;

import aaa.pfe.auth.R;
import aaa.pfe.auth.view.patternlockview.PatternLockView;
import aaa.pfe.auth.view.patternlockview.listener.PatternLockViewListener;
import aaa.pfe.auth.view.patternlockview.utils.PatternLockUtils;

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
                setChangesButton();
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
            Intent i = new Intent(SchemePatternActivity.this, SchemeAdminActivity.class);
            startActivity(i);

        }
        return super.onOptionsItemSelected(item);
    }


    public void setChangesButton() {
        if(onChangesCode){
            changeButton.setText("Change");
            onChangesCode=false;
        }else{
            changeButton.setText("Annuler");
            onChangesCode=true;
        }
    }
}
