package aaa.pfe.auth.view.passface;

import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import aaa.pfe.auth.R;
import aaa.pfe.auth.view.mother.AdminActivity;

public class PassFaceAdminActivity extends AdminActivity {

    Spinner nbPhotos;
    Spinner typePhotos;
    Spinner passwordLength;
    Spinner nbSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_face_admin);

        nbPhotos = (Spinner) findViewById(R.id.spinnerNbPhotos);
        typePhotos = (Spinner) findViewById(R.id.spinnerTypePhotos);
        passwordLength = (Spinner) findViewById(R.id.spinnerPasswordLength);
        nbSteps = (Spinner) findViewById(R.id.spinnerNbSteps);


    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void retrieveChanges(View v) {
        Toast t = Toast.makeText(this, "SavePassface", Toast.LENGTH_SHORT);
        t.show();
        String changes = "";

        sendServer(changes);
    }



}


