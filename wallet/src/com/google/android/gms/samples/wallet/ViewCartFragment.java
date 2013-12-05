/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.gms.samples.wallet;

import java.util.ArrayList;
import java.util.List;

import edu.calpoly.gpsdatabase01.Item;
import edu.calpoly.gpsdatabase01.ItemsDataSource;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ViewCartFragment extends ListFragment implements OnClickListener {

  

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.view_cart, container);

        return root;
    }
	
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        List<ItemInfo> items = new ArrayList<ItemInfo>();
        
        for(int i = 0; i < ItemListActivity.itemCart.size(); i++){
        	items.add(ItemsDataSource.items.get(ItemListActivity.itemCart.get(i)));
        }
        
        
        // use items in place of Cosntants.ITEMS_FOR_SALE below        
        ArrayAdapter<ItemInfo> adapter = new ItemAdapter(getActivity(),
        		  items);
        if(adapter != null)
        	setListAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
    	int id = view.getId();
    	Log.d("GLeN", "SOMEBUTTON PRESSED");
    	if(id == R.id.button_view_cart){
    		Log.d("GLEN", "VIEWCART PRESSED");
    		Intent intent = new Intent(null, ViewCartActivity.class);
            startActivity(intent);
    	}else{
	        
    	}
    }


    @Override
    public void onListItemClick(ListView list, View v, int position, long id) {
        ((OnItemClickListener) getActivity()).onItemClick(list, v, position, id);
    }

    private static class ItemAdapter extends ArrayAdapter<ItemInfo> {
        private LayoutInflater mInflater;
        private Context mContext;

        public ItemAdapter(Context context, List<ItemInfo> objects) {
            super(context, R.layout.cart_item, R.id.name, objects);
            mInflater = LayoutInflater.from(context);
            mContext = context;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (view == null) {
                view = mInflater.inflate(R.layout.cart_item, parent, false);
            }

            ItemInfo info = getItem(position);
            TextView title = (TextView) view.findViewById(R.id.name);
            TextView price = (TextView) view.findViewById(R.id.price);
            ImageView image = (ImageView) view.findViewById(R.id.image);
            
            // Set button id to determine which one to remove when clicked
           // Button btn = (Button) view.findViewById(R.id.remove_item);
            //btn.setTag(position);

            title.setText(info.name);
            price.setText(Util.formatPrice(mContext, info.priceMicros));
            image.setImageResource(info.imageResourceId);

            return view;
        }
    }
}
