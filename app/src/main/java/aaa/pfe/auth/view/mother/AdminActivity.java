package aaa.pfe.auth.view.mother;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import aaa.pfe.auth.R;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Admin");
    }


    //Function to override. This function will parse the parameters, create the JSON of these changes
    //And will have to send them to server
    public void retrieveChanges(View v) {
        Toast t = Toast.makeText(this, "Saved", Toast.LENGTH_SHORT);
        t.show();
    }

    public void sendServer(String changes) {
        //Send to the distant server
    }
}
