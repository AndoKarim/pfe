package aaa.pfe.auth.view.pincode;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import aaa.pfe.auth.R;
import aaa.pfe.auth.view.mother.AdminActivity;

public class PinCodeAdminActivity extends AdminActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_code_admin);
        getSupportActionBar().setTitle("Admin PinCode");
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void retrieveChanges(View v) {
        Intent i = new Intent(PinCodeAdminActivity.this,PinCodeActivity.class);
        startActivity(i);
        Toast t = Toast.makeText(this, "Save Parameters", Toast.LENGTH_SHORT);
        t.show();
    }


}
