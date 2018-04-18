package com.example.riddhi.ttcapplicationtest1;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ListDisplay extends Fragment {
    int[] Images = {R.drawable.bag, R.drawable.phone};
    String[] ItemCategory = {"Micheal Kor Handbag", "Iphone 7 PLus"};
    String[] ItemDate = {"02/04/2018", "03/04/2018"};
    View fragamentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragamentView = inflater.inflate(R.layout.activity_list_display, container, false);

        ListView listview = fragamentView.findViewById(R.id.listviewNewsFeeds);
        CustomAdapter customAdapter = new CustomAdapter();
        listview.setAdapter(customAdapter);

        return fragamentView;
    }

    class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return Images.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


        @Override
        public View getView(int i, View view, ViewGroup viewgroup) {
            view = getLayoutInflater().inflate(R.layout.list_custom_layout, null);
            ImageView imageview = (ImageView) view.findViewById(R.id.displayItemImage);
            TextView textview_itemName = (TextView) view.findViewById(R.id.labelItemId);
            TextView textview_itemDate = (TextView) view.findViewById(R.id.labelItmDateLost);

            imageview.setImageResource(Images[i]);
            textview_itemName.setText(ItemCategory[i]);
            textview_itemDate.setText(ItemDate[i]);
            return view;
        }
    }
}
