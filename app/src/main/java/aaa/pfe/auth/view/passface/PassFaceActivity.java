package aaa.pfe.auth.view.passface;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import aaa.pfe.auth.R;

public class PassFaceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_face);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Passface");
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
            Intent i = new Intent(PassFaceActivity.this, PassFaceAdminActivity.class);
            startActivity(i);

        }
        return super.onOptionsItemSelected(item);
    }


}
