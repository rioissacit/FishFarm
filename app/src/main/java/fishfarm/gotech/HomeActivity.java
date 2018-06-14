package fishfarm.gotech;


import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import helperClass.CommonFunctions;
import helperClass.EncodeDecode64;
import helperClass.SessionManager;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    ListView mListView;
    private TextView mToolbarTitle;
    private ProgressDialog pDialog;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private DrawerLayout mDrawerLayout;
    NavigationView navigationView;
    String scanContent;
    String scanFormat;
    SessionManager sessionManager;
    private static final int PICK_IMAGE = 234;
    private static final int ACCESS_LOCATION = 567;
    int menuPosition=1;
    private CharSequence mTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLocale();
        setContentView(R.layout.activity_home);
        pDialog = new ProgressDialog(this);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        sessionManager = new SessionManager(HomeActivity.this);
        createFolders();

        mToolbarTitle = (TextView) findViewById(R.id.toolbar_title);



        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

         navigationView = (NavigationView) findViewById(R.id.nav_view);

        ((TextView)  (navigationView.getHeaderView(0)).findViewById(R.id.txtUserName)).setText(sessionManager.getUserName());
//        ((TextView)  (navigationView.getHeaderView(0)).findViewById(R.id.txtUserEmail)).setText(sessionManager.getUserEmail());
        navigationView.setNavigationItemSelectedListener(this);
        if(!sessionManager.getDesignation().equals("MANG"))
            ((Menu)navigationView.getMenu()).findItem(R.id.rcpt_eggs).setVisible(false);
        else
            ((Menu)navigationView.getMenu()).findItem(R.id.rcpt_eggs).setVisible(true);





        if (sessionManager.isLoggedIn()) {
            CommonFunctions.checkAndRequestPermissions(this, android.Manifest.permission.CAMERA, PICK_IMAGE);
            if (savedInstanceState == null) {
                if(sessionManager.getDesignation().equals("MANG")) {
                    displayView(1);
                }else  {
                    displayView(2);
                }

            }

        } else {
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }





    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (sessionManager.getDesignation().equals("MANG") && String.valueOf(mToolbarTitle.getText()).equals("Reception of Eggs")) {
            finish();
        }
        else if (String.valueOf(mToolbarTitle.getText()).equals("Barcode Scanner")) {
            finish();
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private void createFolders() {

        EncodeDecode64 encodeDecode64 = new EncodeDecode64(HomeActivity.this);

        File dirFishFarm = encodeDecode64.getFile(HomeActivity.this, "/FishFarm");
        if (!dirFishFarm.exists())
            dirFishFarm.mkdirs();

        File dirFishfarmReports = encodeDecode64.getFile(HomeActivity.this, "/FishFarm/Reports");
        if (!(dirFishfarmReports.exists()))
            dirFishfarmReports.mkdirs();
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_logout)
            displayView(3);
        else if(id==R.id.rcpt_eggs)
            displayView(1);
        else if(id==R.id.tank_insp)
            displayView(2);


        return true;
    }

    private void createLogoutAlert() {

        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_alert_dialog, null);

        // Set the custom layout as alert dialog view
        builder.setView(dialogView);

        // Get the custom alert dialog view widgets reference
        TextView txtTitle = (TextView) dialogView.findViewById(R.id.txtTitle);
        TextView txtMessage = (TextView) dialogView.findViewById(R.id.txtMessage);
        Button btn_positive = (Button) dialogView.findViewById(R.id.btnPositive);
        Button btn_negative = (Button) dialogView.findViewById(R.id.btnNegative);

        // Create the alert dialog
        final AlertDialog dialog = builder.create();

        txtTitle.setText("Logout");
        txtMessage.setText(R.string.proceedMsg);
        txtTitle.setTextSize(16);
        txtTitle.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        txtMessage.setTextColor(getResources().getColor(R.color.colorPrimary));

        btn_negative.setText("NO");
        btn_positive.setText("YES");

        // Set positive/yes button click listener
        btn_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the alert dialog
                logout();
                dialog.dismiss();
            }
        });

        // Set negative/no button click listener
        btn_negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the alert dialog
                dialog.dismiss();
            }
        });

        // Display the custom alert dialog on interface
        dialog.show();
    }

    private void logout() {
//        JSONObject params = new JSONObject();
//        try {
//            params.put("EmpId", String.valueOf(session.getEmpID()));
//            params.put("DeviceId", String.valueOf(session.getDeviceID()));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        HttpsTrustManager.allowAllSSL();
//        JsonUTF8Request strReq = new JsonUTF8Request(Request.Method.POST,
//                appConfig.URL_LOGOUT, params, //Not null.
//                new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject jObj) {
        sessionManager.clearSessionVariables();
//                    }
//                }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d("PANDA", "Error: " + error.getMessage());
//                Toast.makeText(getApplicationContext(),
//                        appConfig.NO_INTERNET, Toast.LENGTH_LONG)
//                        .show();
//            }
//        });
//
//        // Adding request to request queue
//
//        requestQueue.add(strReq);
        Intent myIntent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(myIntent);
        sessionManager.setLogin(false);
        finish();
    }

    @Override
    protected void onResume() {
        CommonFunctions.hideSoftwareKeyboard(this);
        super.onResume();
    }


    private void setLocale(){
        Resources res = getApplicationContext().getResources();
        Locale locale = new Locale("en");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        res.updateConfiguration(config, res.getDisplayMetrics());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);


        return true;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PICK_IMAGE: {
                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(android.Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA)) {
                        }
                    }
                }

                CommonFunctions.checkAndRequestPermissions(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION, ACCESS_LOCATION);
            }

            case ACCESS_LOCATION: {
                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        }
                    }
                }

            }
        }

    }

    public void displayView(int position) {
        // update the main content by replacing fragments
        String tag = "";
        Fragment fragment = null;
        menuPosition=position;
        switch (position) {

            case 1:
                fragment = new ReceptionEggsFragment();
                tag = "ReceptionEggsFragment";
                navigationView.setCheckedItem(R.id.rcpt_eggs);
                mTitle="Reception of Eggs";
                mToolbarTitle.setText(mTitle);
                break;
            case 2:
                fragment = new BarcodeScannerFragment();
                tag = "ManagerTankInspFragment";
                navigationView.setCheckedItem(R.id.tank_insp);
                mTitle="Barcode Scanner";
                mToolbarTitle.setText(mTitle);
                break;
            case 3:
                createLogoutAlert();
                break;
            case 6:
                fragment = new ManagerTankInspFragment();
                tag = "ManagerTankInspFragment";
                navigationView.setCheckedItem(R.id.tank_insp);
                mTitle="Hatchery Manager";
                mToolbarTitle.setText(mTitle);
                break;
            default:
                break;
        }

        if (fragment != null) {
            CommonFunctions.hideSoftwareKeyboard(this);
            final Fragment finalFragment = fragment;
            final String finalTag = tag;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.item_detail_container, finalFragment, finalTag).addToBackStack(null).commitAllowingStateLoss();
                    mToolbarTitle.bringToFront();
                }
            }, 200);


            // update selected item and title, then close the drawer
            //setTitle(navMenuTitles[position - 1]);//First row is set to user info

            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            // error in creating fragment
            Log.e("HomeActivity", "Error in creating fragment");
        }
    }

    public void setView(int position) {
        // update the main content by replacing fragments

        menuPosition=position;
        switch (position) {

            case 1:
                navigationView.setCheckedItem(R.id.rcpt_eggs);
                mTitle="Reception of Eggs";
                mToolbarTitle.setText(mTitle);
                break;
            case 2:
                navigationView.setCheckedItem(R.id.tank_insp);
                mTitle="Barcode Scanner";
                mToolbarTitle.setText(mTitle);
                break;
            case 3:
                createLogoutAlert();
                break;
            case 6:
                navigationView.setCheckedItem(R.id.tank_insp);
                mTitle="Hatchery Manager";
                mToolbarTitle.setText(mTitle);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (scanningResult != null) {
            //we have a result
             scanContent = scanningResult.getContents();
             scanFormat = scanningResult.getFormatName();
//            Toast toast = Toast.makeText(getApplicationContext(),scanFormat +" Content"+scanContent, Toast.LENGTH_SHORT);
//            toast.show();
            if(sessionManager.getDesignation().equals("MANG")){
                Intent intentTank = new Intent(HomeActivity.this, ManagerTankInspActivity.class);
                startActivity(intentTank);
            } else   if(sessionManager.getDesignation().equals("FEED")){
                Intent intentTank = new Intent(HomeActivity.this, FeederTankInspActivity.class);
                startActivity(intentTank);
            } else{
                Intent intentTank = new Intent(HomeActivity.this, TransporterTankInspActivity.class);
                startActivity(intentTank);
            }


        }else{
            Toast toast = Toast.makeText(getApplicationContext(),"No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

}
