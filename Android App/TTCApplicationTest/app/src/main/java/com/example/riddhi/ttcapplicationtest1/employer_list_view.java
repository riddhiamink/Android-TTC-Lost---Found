package com.example.riddhi.ttcapplicationtest1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class employer_list_view extends Fragment {
    ArrayList<DisplayObject> displayObjects;

    View fragmentView;
    String token, role;
    ListView listView;
    EditText searchText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.activity_employer_list_view, container, false);
        token = getArguments().getString(MainActivityLogin.Token);
        role = getArguments().getString(MainActivityLogin.Role);

        searchText = fragmentView.findViewById(R.id.foundItemSerach);

        listView = fragmentView.findViewById(R.id.listviewFoundItem);
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
            HttpGet httpget = new HttpGet(Webhook.IPADDRESS + "/api/FoundItems");

            ArrayList<DisplayObject> listObject = new ArrayList<>();
            httpget.addHeader("Authorization", "Bearer " + params[0]);

//            httpget.addHeader("Content-Type", "application/x-www-form-urlencoded");

            try {
                // Add your data
//                String username = params[0];
//                String password = params[1];
//            String Email = params[2];
//            String Password = params[3];
//            String House = params[4];
//            String Street = params[5];
//            String Landmark = params[6];

//                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//                nameValuePairs.add(new BasicNameValuePair("username", username));
//                nameValuePairs.add(new BasicNameValuePair("password", password));
//                nameValuePairs.add(new BasicNameValuePair("grant_type", "password"));
//                httpget.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httpget);

                String json = EntityUtils.toString(response.getEntity());

//                JSONObject myObject = new JSONObject(json);
//                Log.d("responseData1", String.valueOf(myObject));

//                String token = String.valueOf(myObject.get("access_token"));

//                UserObject object = new UserObject();
//                object.fullName = String.valueOf(myObject.get("fullName"));
//                object.userName = String.valueOf(myObject.get("userName"));
//                object.token = String.valueOf(myObject.get("access_token"));
//                object.role = String.valueOf(myObject.get("role"));

                JSONTokener tokener = new JSONTokener(json);
                JSONArray jsonArray = new JSONArray(tokener);

//                Log.d("responseData2", String.valueOf(finalResult.getString(0)));

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = new JSONObject(jsonArray.getString(i));
                    DisplayObject d = new DisplayObject();

                    d.Id = obj.getInt("Id");
                    d.Image = obj.getString("Image");
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
            HttpGet httpget = new HttpGet(Webhook.IPADDRESS + "/api/FoundItems?query=" + params[1]);

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
                    d.Image = obj.getString("Image");
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
            HttpDelete httpdelete = new HttpDelete(Webhook.IPADDRESS + "/api/FoundItems/" + Integer.parseInt(params[1]));

            ArrayList<DisplayObject> listObject = new ArrayList<>();
            httpdelete.addHeader("Authorization", "Bearer " + params[0]);

//            httpget.addHeader("Content-Type", "application/x-www-form-urlencoded");

            try {
                // Add your data
//                String username = params[0];
//                String password = params[1];
//            String Email = params[2];
//            String Password = params[3];
//            String House = params[4];
//            String Street = params[5];
//            String Landmark = params[6];

//                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//                nameValuePairs.add(new BasicNameValuePair("username", username));
//                nameValuePairs.add(new BasicNameValuePair("password", password));
//                nameValuePairs.add(new BasicNameValuePair("grant_type", "password"));
//                httpget.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httpdelete);

                if (response.getStatusLine().getStatusCode() != 200)
                    return null;

                String json = EntityUtils.toString(response.getEntity());

//                JSONObject myObject = new JSONObject(json);
//                Log.d("responseData1", String.valueOf(myObject));

//                String token = String.valueOf(myObject.get("access_token"));

//                UserObject object = new UserObject();
//                object.fullName = String.valueOf(myObject.get("fullName"));
//                object.userName = String.valueOf(myObject.get("userName"));
//                object.token = String.valueOf(myObject.get("access_token"));
//                object.role = String.valueOf(myObject.get("role"));

//                JSONTokener tokener = new JSONTokener(json);
//                JSONArray jsonArray = new JSONArray(tokener);
//
////                Log.d("responseData2", String.valueOf(finalResult.getString(0)));
//
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    JSONObject obj = new JSONObject(jsonArray.getString(i));
//                    DisplayObject d = new DisplayObject();
//                    d.Id = obj.getInt("Id");
//                    d.Image = obj.getString("Image");
//                    d.ItemCategory = obj.getString("Category");
//                    d.ItemDate = obj.getString("DateLost");
//
//                    listObject.add(d);
//                }
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
            view = getLayoutInflater().inflate(R.layout.employer_found_list_layout, null);
            ImageView imageview = (ImageView) view.findViewById(R.id.displayItemImage);
            TextView textview_itemId = (TextView) view.findViewById(R.id.labelItemId);
            TextView textview_itemDate = (TextView) view.findViewById(R.id.labelItmDateLost);
            TextView textview_itemName = (TextView) view.findViewById(R.id.labelItemCategory);
            TextView textview_trackingId = (TextView) view.findViewById(R.id.labelItmTrackingId);

            Picasso.get().load(Webhook.IPADDRESS + "/" + displayObjects.get(i).Image).into(imageview);

            textview_itemName.setText(displayObjects.get(i).ItemCategory);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
            try
            {
                Date date = simpleDateFormat.parse(displayObjects.get(i).ItemDate);
                textview_itemDate.setText(date.toString());
            }
            catch (ParseException ex)
            {
                textview_itemDate.setText(displayObjects.get(i).ItemDate);
            }

            final String StringId = "" + displayObjects.get(i).Id;
            final int id = displayObjects.get(i).Id;
            textview_itemId.setText("ID: " + id);

            Button deleteImageView = view.findViewById(R.id.btnDelete);
            Button modifyImageView = view.findViewById(R.id.btnModify);
            String trakingId = displayObjects.get(i).TrakingId;

            if (!role.equals("ItemValidator") || !trakingId.equals("null")) {
                deleteImageView.setVisibility(View.INVISIBLE);
                modifyImageView.setVisibility(View.INVISIBLE);
            }

            if (!role.equals("Customer")) {
                if (trakingId.equals("null")) {
                    textview_trackingId.setVisibility(View.INVISIBLE);
                } else {
                    textview_trackingId.setText("Traking Id: " + trakingId);
                }
            } else {
                textview_trackingId.setVisibility(View.INVISIBLE);
            }


            deleteImageView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try {
                        String data = new DeleteData().execute(token, StringId).get();

                        if (data != null) {
                            SetListView(GetListData());
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            });

            modifyImageView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ModigyItem.class);
                    intent.putExtra("itemId", id);
                    intent.putExtra("token", token);
                    startActivity(intent);
                }
            });


            return view;
        }
    }

    class DisplayObject {
        int Id;
        String Image;
        String ItemCategory;
        String ItemDate;
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
