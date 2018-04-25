package com.example.riddhi.ttcapplicationtest1;
/*
 * @author Riddhi Amin
 */
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class Claimant_Info extends AppCompatActivity
{
    TextView txtFName, txtLName, txtAddress, txtPhone, txtEmail;

    String FName, LName, Address, Phone, Email;

    ImageView imgSend;

    String UserID, Token, TrackingID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claimant__info);

        UserID = getIntent().getExtras().getString("UserID");
        TrackingID = getIntent().getExtras().getString("TrackingID");
        Token = getIntent().getExtras().getString("token");
        Log.d("Claimant_Info->User ID",UserID);

        txtFName = (TextView) findViewById(R.id.labelFirstName);
        txtLName = (TextView) findViewById(R.id.labelLastName);
        txtAddress = (TextView) findViewById(R.id.labelAddress);
        txtPhone = (TextView) findViewById(R.id.labelPhoneNumber);
        txtEmail = (TextView) findViewById(R.id.labelEmail);
        imgSend = (ImageView) findViewById(R.id.imgSend);
        imgSend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String to = Email;
                String subject = "Claim Approved";
                String message = "Hello "+FName+", Your Tracking ID is "+ TrackingID;

                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("plain/text");
                File data = null;
                i.putExtra(Intent.EXTRA_EMAIL, new String[] { to });
                i.putExtra(Intent.EXTRA_SUBJECT, subject);
                i.putExtra(Intent.EXTRA_TEXT, message);
                startActivity(Intent.createChooser(i, "E-mail"));
            }
        });

        try {
            UserObj obj = new GetItemData().execute(Token, UserID.toString()).get();

            if(obj != null){
                txtFName.setText("First Name: " + obj.FirstName);
                txtLName.setText("Last Name: " + obj.LastName);
                txtEmail.setText("Email: " + obj.Email);
                Email = obj.Email;
                txtAddress.setText("Address: " + obj.Address);
                txtPhone.setText("Mobile: " +obj.Mobile);
            }
            else {
                Toast.makeText(this, "Something went wroing", Toast.LENGTH_SHORT).show();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private class GetItemData extends AsyncTask<String, Void, UserObj> {
        protected UserObj doInBackground(String... params) {
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(Webhook.IPADDRESS + "/api/ClaimedItems?userId="+ params[1]);
            httpget.addHeader("Authorization", "Bearer " + params[0]);

            try {
                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httpget);

                if(response.getStatusLine().getStatusCode() != 200){
                    return null;
                }

                String json = EntityUtils.toString(response.getEntity());
                Log.d("Claimant_Info-> JSON", json);
                UserObj item = new UserObj();

                JSONObject obj = new JSONObject(json);
                item.Address = obj.getString("Address");
                item.Email = obj.getString("Email");
                item.FirstName = obj.getString("FirstName");
                item.LastName = obj.getString("LastName");
                item.Mobile = obj.getString("Mobile");
                return item;
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



    private class UserObj{
        public String FirstName;
        public String LastName;
        public String Address;
        public String Email;
        public String Mobile;

        public UserObj(){}
    }
}
