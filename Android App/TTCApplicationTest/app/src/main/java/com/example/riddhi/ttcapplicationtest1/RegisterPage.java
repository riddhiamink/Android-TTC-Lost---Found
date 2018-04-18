package com.example.riddhi.ttcapplicationtest1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RegisterPage extends AppCompatActivity {

    Button btnRegister;
    EditText firstName, lastName, address, email, password, confirm_password, mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        firstName = findViewById(R.id.txtRegFirstName);
        lastName = findViewById(R.id.txtRegLastName);
        email = findViewById(R.id.txtRegEmail);
        address = findViewById(R.id.txtRegAddress);
        password = findViewById(R.id.txtPassword);
        confirm_password = findViewById(R.id.txtConfirmPassword);
        mobile = findViewById(R.id.txtRegPhoneNumber);

        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!password.getText().toString().isEmpty() &&
                        !confirm_password.getText().toString().isEmpty() &&
                        !email.getText().toString().isEmpty() &&
                        !firstName.getText().toString().isEmpty() &&
                        !lastName.getText().toString().isEmpty() &&
                        !mobile.getText().toString().isEmpty() &&
                        !address.getText().toString().isEmpty()) {
                    try {

                        if (!confirm_password.getText().toString().equals(password.getText().toString())) {
                            Toast.makeText(RegisterPage.this, "Password do not match", Toast.LENGTH_LONG).show();
                            return;
                        }

                        String data = new PostData().execute(
                                mobile.getText().toString(),
                                email.getText().toString(),
                                password.getText().toString(),
                                confirm_password.getText().toString(),
                                firstName.getText().toString(),
                                lastName.getText().toString(),
                                address.getText().toString()).get();
                        if (data != null) {
                            Toast.makeText(RegisterPage.this, "Account Created! Please Login", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(RegisterPage.this, MainActivityLogin.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(RegisterPage.this, "Please check inputs, and retry", Toast.LENGTH_SHORT).show();
                        }
                        return;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(RegisterPage.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(RegisterPage.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }

                return;
            }
        });
    }

    private class PostData extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... params) {
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Webhook.IPADDRESS + "/api/Account/Register");

            httppost.addHeader("Content-Type", "application/x-www-form-urlencoded");
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("Mobile", params[0]));
                nameValuePairs.add(new BasicNameValuePair("Email", params[1]));
                nameValuePairs.add(new BasicNameValuePair("Password", params[2]));
                nameValuePairs.add(new BasicNameValuePair("ConfirmPassword", params[3]));
                nameValuePairs.add(new BasicNameValuePair("FirstName", params[4]));
                nameValuePairs.add(new BasicNameValuePair("LastName", params[5]));
                nameValuePairs.add(new BasicNameValuePair("Address", params[6]));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));


//                HttpEntity entity = entityBuilder.build();
//                httppost.setEntity(entity);

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);

                String json = EntityUtils.toString(response.getEntity());

                if (response.getStatusLine().getStatusCode() == 200) {
                    return "OK";
                }

                JSONObject myObject = new JSONObject(json);
                Log.d("responseData1", myObject.getString("Message"));
                Log.d("responseData2", myObject.getString("ModelState"));

//                String token = String.valueOf(myObject.get("access_token"));
//
//                UserObject object = new UserObject();
//                object.fullName = String.valueOf(myObject.get("fullName"));
//                object.userName = String.valueOf(myObject.get("userName"));
//                object.token = String.valueOf(myObject.get("access_token"));
//                object.role = String.valueOf(myObject.get("role"));


//                JSONTokener tokener = new JSONTokener(json);
//                JSONArray finalResult = new JSONArray(tokener);

//                Log.d("responseData2", String.valueOf(finalResult.getString(0)));

                return null;
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

}
