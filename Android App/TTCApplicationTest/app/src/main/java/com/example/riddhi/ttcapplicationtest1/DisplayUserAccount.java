package com.example.riddhi.ttcapplicationtest1;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
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

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class DisplayUserAccount extends Fragment {

    View fragmentView;
    String token;
    TextView firstName, lastName, email, address, mobile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.fragment_display_user_account, container, false);
        token = getArguments().getString(MainActivityLogin.Token);

        firstName = fragmentView.findViewById(R.id.labelFirstName);
        lastName = fragmentView.findViewById(R.id.labelLastName);
        email = fragmentView.findViewById(R.id.labelEmail);
        address = fragmentView.findViewById(R.id.labelAddress);
        mobile = fragmentView.findViewById(R.id.labelPhoneNumber);

        try {
            UserObj obj = new GetItemData().execute(token).get();

            if(obj != null){
                firstName.setText("First Name: " + obj.FirstName);
                lastName.setText("Last Name: " + obj.LastName);
                email.setText("Email: " + obj.Email);
                address.setText("Address: " + obj.Address);
                mobile.setText("Mobile: " +obj.Mobile);
            }
            else {
                Toast.makeText(getActivity(), "Something went wroing", Toast.LENGTH_SHORT).show();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return fragmentView;
    }

    private class GetItemData extends AsyncTask<String, Void, UserObj> {
        protected UserObj doInBackground(String... params) {
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(Webhook.IPADDRESS + "/api/Account/UserInfo");

            httpget.addHeader("Authorization", "Bearer " + params[0]);

            try {
                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httpget);

                if(response.getStatusLine().getStatusCode() != 200){
                    return null;
                }

                String json = EntityUtils.toString(response.getEntity());
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
