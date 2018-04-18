package com.example.riddhi.ttcapplicationtest1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivityLogin extends AppCompatActivity {

    TextView userName;
    TextView password;
    SharedPreferences sharedPref;

    public static final String Token = "token";
    public static final String FullName = "full_name";
    public static final String Role = "role";
    public static final String UserName = "user_name";
    public static final String MyPreference = "ttc_sp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        userName = (findViewById(R.id.txtUserName));
        password = (findViewById(R.id.textPassword));

        sharedPref = getSharedPreferences(MyPreference, Context.MODE_PRIVATE);

    }

    public void registerBtnClick(View view){
        Intent intent = new Intent(this, RegisterPage.class);
        startActivity(intent);
    }


    public void loginClick(View view) throws ExecutionException, InterruptedException {
        UserObject object = new PostData().execute(userName.getText().toString(), password.getText().toString()).get();
        if (object == null) {
            Toast.makeText(this.getApplicationContext(), "Incorrect username or Password", Toast.LENGTH_LONG).show();
        } else {
            // Shared Preferences Save Token
//            Toast.makeText(this.getApplicationContext(), "Password Correct : " + object.token, Toast.LENGTH_LONG).show();
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(Token, object.token);
            editor.putString(FullName, object.fullName);
            editor.putString(Role, object.role);
            editor.putString(UserName, object.userName);
            editor.commit();
            editor.apply();

            Intent  intent = new Intent(this, CustomerAccountLayout.class);
            startActivity(intent);
        }
    }

    private class PostData extends AsyncTask<String, Void, UserObject> {
        protected UserObject doInBackground(String... params) {
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Webhook.IPADDRESS + "/Token");

            httppost.addHeader("Content-Type", "application/x-www-form-urlencoded");

            try {
                // Add your data
                String username = params[0];
                String password = params[1];
//            String Email = params[2];
//            String Password = params[3];
//            String House = params[4];
//            String Street = params[5];
//            String Landmark = params[6];

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("username", username));
                nameValuePairs.add(new BasicNameValuePair("password", password));
                nameValuePairs.add(new BasicNameValuePair("grant_type", "password"));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);

                String json = EntityUtils.toString(response.getEntity());

                JSONObject myObject = new JSONObject(json);
//                Log.d("responseData1", String.valueOf(myObject.get("access_token")));

                String token = String.valueOf(myObject.get("access_token"));

                UserObject object = new UserObject();
                object.fullName = String.valueOf(myObject.get("fullName"));
                object.userName = String.valueOf(myObject.get("userName"));
                object.token = String.valueOf(myObject.get("access_token"));
                object.role = String.valueOf(myObject.get("role"));


//                JSONTokener tokener = new JSONTokener(json);
//                JSONArray finalResult = new JSONArray(tokener);

//                Log.d("responseData2", String.valueOf(finalResult.getString(0)));

                return object;
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                // TODO Auto-generated catch block
            } catch (IOException e) {
                e.printStackTrace();
                // TODO Auto-generated catch block
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    class UserObject {
        public String token;
        public String userName;
        public String role;
        public String fullName;
    }
}


