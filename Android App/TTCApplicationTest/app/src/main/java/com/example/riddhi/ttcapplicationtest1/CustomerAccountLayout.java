package com.example.riddhi.ttcapplicationtest1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class CustomerAccountLayout extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences sharedPref;
    String token, role, fullName, userName;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_account_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        sharedPref = getSharedPreferences(MainActivityLogin.MyPreference, Context.MODE_PRIVATE);
        token = sharedPref.getString(MainActivityLogin.Token, null);
        role = sharedPref.getString(MainActivityLogin.Role, null);
        fullName = sharedPref.getString(MainActivityLogin.FullName, null);
        userName = sharedPref.getString(MainActivityLogin.UserName, null);

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        TextView txt_customerName = headerView.findViewById(R.id.txtCustomerName);
        TextView txt_userName = headerView.findViewById(R.id.txtUserName);

        txt_customerName.setText(fullName);
        txt_userName.setText(userName);

        Menu menu = navigationView.getMenu();

        // find MenuItem you want to change
        MenuItem addFoundItem = menu.findItem(R.id.nav_add_found_item);
        MenuItem addClaimItem = menu.findItem(R.id.nav_add_claim_item);
        MenuItem listClaimItem = menu.findItem(R.id.nav_list_claim_item);
//        MenuItem listFoundItem = menu.findItem(R.id.nav_list_found_item);

        // Log.d("Role", role);

        if (role.equalsIgnoreCase("BusGanitor")) {
            addClaimItem.setVisible(false);
            listClaimItem.setVisible(false);
        } else if (role.equalsIgnoreCase("ItemValidator")) {
            addClaimItem.setVisible(false);
        } else if (role.equalsIgnoreCase("BoothReceptionist")) {
            addClaimItem.setVisible(false);
            addFoundItem.setVisible(false);
        } else {
            addFoundItem.setVisible(false);
            listClaimItem.setVisible(false);
        }

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.customer_account_layout, menu);
        return true;
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        bundle.putString(MainActivityLogin.Token, token);
        bundle.putString(MainActivityLogin.Role, role);

        if(token == null){
            LoadLoginActivity();
            return true;
        }

        if (id == R.id.nav_add_found_item) {
            fragment = new AddFountItems();
        } else if (id == R.id.nav_add_claim_item) {
            fragment = new ItemClaim();
        } else if (id == R.id.nav_list_found_item) {
            fragment = new employer_list_view();
        } else if (id == R.id.nav_list_claim_item) {
            fragment = new ViewClaimList();
        } else if (id == R.id.nav_account_info) {
            fragment = new DisplayUserAccount();
        } else if (id == R.id.nav_setting) {

        } else if (id == R.id.nav_logout) {
            try {
                String data=  new PostData().execute(token).get();
                if(data != null){
                    SharedPreferences.Editor editor = sharedPref.edit();
                    token = null;
                    editor.clear();
                    editor.commit();
                    editor.apply();
                    LoadLoginActivity();
                    return true;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        if (fragment != null) {
            fragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.layoutMain, fragment).commit();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private class PostData extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... params) {
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(Webhook.IPADDRESS + "/api/Account/Logout");

            httpPost.addHeader("Authorization", "Bearer " + params[0]);

            try {
                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httpPost);
                if(response.getStatusLine().getStatusCode() != 200)
                    return null;

                return "";
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                // TODO Auto-generated catch block
            } catch (IOException e) {
                e.printStackTrace();
                // TODO Auto-generated catch block
            }
            return null;
        }
    }

    private void LoadLoginActivity(){
        Intent intent = new Intent(this, MainActivityLogin.class);
        startActivity(intent);

    }

}
