package aaa.pfe.auth.view.schemepattern;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import aaa.pfe.auth.R;
import aaa.pfe.auth.view.mother.AdminActivity;

/**
 * Created by Anasse on 21/11/2017.
 */

public class SchemeAdminActivity extends AdminActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheme_admin);
            getSupportActionBar().setTitle("Admin SchemePattern");


        Log.d("salut", "slaut");


        //getSupportActionBar().setTitle("Admin");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


        @Override
        public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

        @Override
        public void retrieveChanges(View v) {
        Toast t = Toast.makeText(this, "SaveScheme", Toast.LENGTH_SHORT);
        t.show();
    }
}
