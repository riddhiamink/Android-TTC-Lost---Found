package com.example.riddhi.ttcapplicationtest1;
/*
 * @author Riddhi Amin
 */
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class ViewClaimList extends Fragment {

    View fragmentView;
    ListView listView;
    ArrayList<DisplayObject> displayObjects;
    String token, role;
    EditText searchText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.activity_view_claim_list, container, false);
        token = getArguments().getString(MainActivityLogin.Token);
        role = getArguments().getString(MainActivityLogin.Role);

        Toast.makeText(getActivity(), "view claim list", Toast.LENGTH_SHORT).show();

        searchText = fragmentView.findViewById(R.id.claimItemSearch);

        listView = fragmentView.findViewById(R.id.listviewClaimList);
        SetListView(GetListData());

        searchText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (!searchText.getText().toString().isEmpty())
                        SetListView(SerchListData(searchText.getText().toString()));
                    else
                        SetListView(GetListData());
                }

                return false;
            }
        });

        return fragmentView;
    }

    private void SetListView(ArrayList<DisplayObject> obj) {
        displayObjects = obj;
        if (displayObjects != null) {
            CustomAdapter customAdapter = new CustomAdapter();
            listView.setAdapter(customAdapter);
        }
    }

    private class GetData extends AsyncTask<String, Void, ArrayList<DisplayObject>> {
        protected ArrayList<DisplayObject> doInBackground(String... params) {
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(Webhook.IPADDRESS + "/api/ClaimedItems");

            ArrayList<DisplayObject> listObject = new ArrayList<>();
            httpget.addHeader("Authorization", "Bearer " + params[0]);

            try {

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httpget);
                String json = EntityUtils.toString(response.getEntity());

                JSONTokener tokener = new JSONTokener(json);
                JSONArray jsonArray = new JSONArray(tokener);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = new JSONObject(jsonArray.getString(i));
                    DisplayObject d = new DisplayObject();
                    d.Id = obj.getInt("Id");
                    d.Color = obj.getString("Color");
                    d.UserID = obj.getString("UserId");
                    d.Description = obj.getString("Description");
                    d.Location = obj.getString("Location");
                    d.ItemCategory = obj.getString("Category");
                    d.ItemDate = obj.getString("DateLost");
                    d.TrakingId = obj.getString("TrackingId");
                    listObject.add(d);
                }
                return listObject;
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

    private class SearchData extends AsyncTask<String, Void, ArrayList<DisplayObject>> {
        protected ArrayList<DisplayObject> doInBackground(String... params) {
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(Webhook.IPADDRESS + "/api/ClaimedItems?query=" + params[1]);

            ArrayList<DisplayObject> listObject = new ArrayList<>();
            httpget.addHeader("Authorization", "Bearer " + params[0]);

            try {
                HttpResponse response = httpclient.execute(httpget);

                String json = EntityUtils.toString(response.getEntity());

                JSONTokener tokener = new JSONTokener(json);
                JSONArray jsonArray = new JSONArray(tokener);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = new JSONObject(jsonArray.getString(i));
                    DisplayObject d = new DisplayObject();
                    d.Id = obj.getInt("Id");
                    d.Color = obj.getString("Color");
                    d.UserID = obj.getString("UserId");
                    d.Description = obj.getString("Description");
                    d.Location = obj.getString("Location");
                    d.ItemCategory = obj.getString("Category");
                    d.ItemDate = obj.getString("DateLost");
                    d.TrakingId = obj.getString("TrackingId");
                    listObject.add(d);
                }
                return listObject;
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

    private class DeleteData extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... params) {
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpDelete httpdelete = new HttpDelete(Webhook.IPADDRESS + "/api/ClaimedItems/" + Integer.parseInt(params[1]));

            httpdelete.addHeader("Authorization", "Bearer " + params[0]);
            try {

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httpdelete);

                if (response.getStatusLine().getStatusCode() != 200)
                    return null;

                String json = EntityUtils.toString(response.getEntity());

                return json;
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

    class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return displayObjects.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint("ViewHolder")
        @Override
        public View getView(int i, View view, ViewGroup viewgroup) {
            view = getLayoutInflater().inflate(R.layout.view_claim_list_items, null);
            TextView textview_itemName = view.findViewById(R.id.displayItemCat);
            TextView textview_itemDate = view.findViewById(R.id.displayItemDate);
            TextView textview_itemLoc = view.findViewById(R.id.displayItemLocation);
            TextView textview_itemColor = view.findViewById(R.id.displayItemColor);
            TextView textview_itemDesc = view.findViewById(R.id.displayItemDes);

            final int id = displayObjects.get(i).Id;
            final String category = "Item: " + displayObjects.get(i).ItemCategory;
            final String date = "Date: " + displayObjects.get(i).ItemDate;
            final String location = "Location: "+displayObjects.get(i).Location;
            final String description = "Description: " + displayObjects.get(i).Description;
            final String color = "Color: "+displayObjects.get(i).Color;
            final String userid = "ID: "+displayObjects.get(i).UserID;

            final String uId = displayObjects.get(i).UserID;

            final String trakingId =displayObjects.get(i).TrakingId;

            textview_itemName.setText(category);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
            try
            {
                Date mdate = simpleDateFormat.parse(date);
                textview_itemDate.setText(mdate.toString());
            }
            catch (ParseException ex)
            {
                textview_itemDate.setText(date);
            }
            textview_itemLoc.setText(location);
            textview_itemDesc.setText(description);
            textview_itemColor.setText(color);


            final  Button btnValidate = view.findViewById(R.id.btnValidate);

            if (!role.equals("ItemValidator")) {
                btnValidate.setVisibility(View.INVISIBLE);
            }

            if(!trakingId.equals("null")) {
                btnValidate.setText("Validated");
                btnValidate.setEnabled(false);
            }

            Log.d("TrakingId", trakingId);

            btnValidate.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ValidateFoundItem.class);
                    intent.putExtra("itemId", id);
                    intent.putExtra("token", token);
                    intent.putExtra("UserId", uId);
                    intent.putExtra("category", category);
                    intent.putExtra("description", description);
                    intent.putExtra("date", date);
                    intent.putExtra("color", color);
                    intent.putExtra("location", location);

                    startActivity(intent);
                }
            });

            return view;
        }
    }

    class DisplayObject {
        int Id;
        String UserID;
        String ItemCategory;
        String ItemDate;
        String Description;
        String Color;
        String Location;
        String TrakingId;
    }

    @Override
    public void onResume() {
        super.onResume();
        SetListView(GetListData());
    }

    private ArrayList<DisplayObject> SerchListData(String query) {

        try {
            return new SearchData().execute(token, query).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

    private ArrayList<DisplayObject> GetListData() {

        try {
            return new GetData().execute(token).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

}
