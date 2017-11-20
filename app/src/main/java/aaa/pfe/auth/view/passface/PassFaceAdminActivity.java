package aaa.pfe.auth.view.passface;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import aaa.pfe.auth.R;

public class PassFaceAdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_face_admin);

        getSupportActionBar().setTitle("Admin Passface");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
