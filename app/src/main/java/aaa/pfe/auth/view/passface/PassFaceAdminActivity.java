package aaa.pfe.auth.view.passface;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import aaa.pfe.auth.R;
import aaa.pfe.auth.view.mother.AdminActivity;

public class PassFaceAdminActivity extends AdminActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_face_admin);

        Toolbar t = (Toolbar) findViewById(R.id.toolbar);
        setActionBar(t);


        //getSupportActionBar().setTitle("Admin");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void saveChanges(View v) {
        Toast t = Toast.makeText(this, "SavePassface", Toast.LENGTH_SHORT);
        t.show();
    }


}
