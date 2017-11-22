package aaa.pfe.auth.view.passface;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import aaa.pfe.auth.R;

public class PassFaceActivity extends AppCompatActivity {
    static final String[] ANIMALS = new String[]{
            "Lion", "Tigre", "Ours", "Abeille", "Chien", "Chat", "Mouton", "Baleine", "Cheval"};
    static final String[] NUMBERS = new String[]{
            "0", "1", "2", "3", "4", "5", "6", "7", "8"};
    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_face);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Passface");


        gridView = (GridView) findViewById(R.id.gridView);

        gridView.setAdapter(new ImageAdapter(this, NUMBERS));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                //String picName =
                ImageView i = (ImageView) ((LinearLayout) v).getChildAt(0);
                String picName = (String) i.getTag();
                Toast.makeText(
                        getApplicationContext(), picName, Toast.LENGTH_SHORT).show();


            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
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
            Toast t = Toast.makeText(this, "Pull From Server", Toast.LENGTH_SHORT);
            t.show();
        }

        if (id == R.id.adminPanel) {
            Intent i = new Intent(PassFaceActivity.this, PassFaceAdminActivity.class);
            startActivity(i);

        }
        return super.onOptionsItemSelected(item);
    }


}
