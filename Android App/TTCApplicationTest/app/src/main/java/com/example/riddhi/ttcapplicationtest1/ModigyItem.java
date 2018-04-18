package com.example.riddhi.ttcapplicationtest1;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ModigyItem extends AppCompatActivity {

    EditText category, color, description, location, date;
    String token;
    int itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modigy_item);

        category = findViewById(R.id.txtItemModifyCategory);
        color = findViewById(R.id.txtItemModifyColor);
        description = findViewById(R.id.txtItemModifyDescription);
        location = findViewById(R.id.txtItemModifyFoundLoc);
        date = findViewById(R.id.dateModifyPicker);

        token = getIntent().getExtras().getString("token");
        itemId = getIntent().getExtras().getInt("itemId");

        try {
            FoundItem item = new GetItemData().execute(token, "" + itemId).get();
            if(item!=null){
                date.setText(item.ItemDate);
                color.setText(item.Color);
                description.setText(item.Description);
                location.setText(item.Location);
                category.setText(item.ItemCategory);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void btnUpdate(View view){
        try {
            String data = new PutData().execute(
                    token,
                    "" + itemId,
                    category.getText().toString(),
                    color.getText().toString(),
                    description.getText().toString(),
                    location.getText().toString(),
                    date.getText().toString()).get();

            if(data != null){
                Toast.makeText(this, "Data Updated Successfully", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private class GetItemData extends AsyncTask<String, Void, FoundItem> {
        protected FoundItem doInBackground(String... params) {
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(Webhook.IPADDRESS + "/api/FoundItems/" + Integer.parseInt(params[1]));

            httpget.addHeader("Authorization", "Bearer " + params[0]);

            try {
                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httpget);

                if(response.getStatusLine().getStatusCode() != 200){
                    return null;
                }

                String json = EntityUtils.toString(response.getEntity());
                FoundItem item = new FoundItem();

                JSONObject obj = new JSONObject(json);
                item.Id = Integer.parseInt(params[1]);
                item.Image = obj.getString("Image");
                item.ItemCategory = obj.getString("Category");
                item.ItemDate = obj.getString("DateLost");
                item.Location = obj.getString("Location");
                item.Description = obj.getString("Description");
                item.Color = obj.getString("Color");


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

    private class PutData extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... params) {
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPut httpput = new HttpPut(Webhook.IPADDRESS + "/api/FoundItems/" + Integer.parseInt(params[1]));

            httpput.addHeader("Content-Type", "application/x-www-form-urlencoded");
            httpput.addHeader("Authorization", "Bearer " + params[0]);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("Category", params[2]));
                nameValuePairs.add(new BasicNameValuePair("Color", params[3]));
                nameValuePairs.add(new BasicNameValuePair("Description", params[4]));
                nameValuePairs.add(new BasicNameValuePair("Location", params[5]));
                nameValuePairs.add(new BasicNameValuePair("DateLost", params[6]));
                httpput.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = httpclient.execute(httpput);

                if (response.getStatusLine().getStatusCode() != 204) {
                    return null;
                }
                return "OK";
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

    class FoundItem {
        int Id;
        String Image;
        String ItemCategory;
        String ItemDate;
        String Color;
        String Location;
        String Description;

        public FoundItem() {
        }
    }

}
