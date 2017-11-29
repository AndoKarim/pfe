package aaa.pfe.auth.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import aaa.pfe.auth.R;
import aaa.pfe.auth.utils.LogWriter;
import aaa.pfe.auth.view.passface.PassFaceActivity;
import aaa.pfe.auth.view.passface.PassFaceAdminActivity;
import aaa.pfe.auth.view.pincode.PinCodeActivity;
import aaa.pfe.auth.view.pincode.PinCodeAdminActivity;
import aaa.pfe.auth.view.schemepattern.SchemeAdminActivity;
import aaa.pfe.auth.view.schemepattern.SchemePatternActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        verifyStoragePermissions(this);
    }




    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_pin) {
            Intent i = new Intent(MainActivity.this,PinCodeActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_pattern) {

            Intent i = new Intent(MainActivity.this,SchemePatternActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_passface) {
            Intent i = new Intent(MainActivity.this,PassFaceActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void launchPinCodeAdmin(View v){
        Intent i = new Intent(MainActivity.this, PinCodeAdminActivity.class);
        startActivity(i);
    }

    public void launchSchemeAdmin(View v){
        Intent i = new Intent(MainActivity.this, SchemeAdminActivity.class);
        startActivity(i);
    }

    public void launchPassFaceAdmin(View v){
        Intent i = new Intent(MainActivity.this, PassFaceAdminActivity.class);
        startActivity(i);
    }
}
