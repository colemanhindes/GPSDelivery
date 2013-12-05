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

import edu.calpoly.gpsdatabase01.ItemsDataSource;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * The launcher activity for XYZ application. If the app is launched on a tablet
 * (x-large screen) in landscape mode the UI will display an item list and
 * details for the currently selected item. If the application is launched on
 * any device in any other orientation, only the item list is shown.
 *
 * Because of the extra logic around what to do with an item list click, the
 * activity implements {@link OnItemClickListener} instead of {@link ItemListFragment}.
 *
 */
public class ItemListActivity extends FragmentActivity implements OnItemClickListener {

    private Menu mMenu;
    private boolean mIsDualFrame = false;
    private ListView mItemList;
    private ItemDetailsFragment mDetailsFragment;
    public static String deliveryLoc = "No Location Data";
    
    // Cart to hold the items the user has selected
    public static ArrayList<Integer> itemCart = new ArrayList<Integer>();

    // @Param position is the position within ItemsDataSource.items 's list
    public static void addToCart(int position) {
    	itemCart.add(position);
    }
    
    // @Param position is the position within ItemsDataSource.items 's list
    public static void removeFromCart(int position) {
    	itemCart.remove(position);
    }
    
    public static long getTax() {
    	long tax = 0;
    	for(int i = 0; i < ItemListActivity.itemCart.size(); i++){
    		tax += ItemsDataSource.items.get(ItemListActivity.itemCart.get(i)).taxMicros;
    	}
    	return tax;
    }
    
    public static long getTotalPrice() {
    	long price = 0;
    	for(int i = 0; i < ItemListActivity.itemCart.size(); i++){
    		price += ItemsDataSource.items.get(ItemListActivity.itemCart.get(i)).priceMicros;
    	}
    	return price;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        mItemList = (ListView) findViewById(android.R.id.list);
        mDetailsFragment = (ItemDetailsFragment) getSupportFragmentManager()
                .findFragmentById(R.id.item_details);
        mIsDualFrame = mDetailsFragment != null;
        if (mIsDualFrame) {
            mItemList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            mDetailsFragment.setItemId(0);
        }        
        
        /*deliveryLoc = "No Location Data";
        if(!getIntent().getStringExtra("deliveryLoc").equals(""))
        	deliveryLoc = getIntent().getStringExtra("deliveryLoc");*/
    }

    @SuppressLint("NewApi")
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LoginActivity.REQUEST_USER_LOGIN) {
            if (resultCode == RESULT_OK) {
                invalidateOptionsMenu();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        mMenu = menu;
        MenuInflater inflater = getMenuInflater();
        if (((XyzApplication) getApplication()).isLoggedIn()) {
            inflater.inflate(R.menu.menu_logout, menu);
        } else {
            inflater.inflate(R.menu.menu_login, menu);
        }

        return true;
    }

    @SuppressLint("NewApi")
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
		if (itemId == R.id.login) {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivityForResult(intent, LoginActivity.REQUEST_USER_LOGIN);
			return true;
		} else if (itemId == R.id.logout) {
			((XyzApplication) getApplication()).logout();
			invalidateOptionsMenu();
			return true;
		} else {
			return false;
		}
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mIsDualFrame) {
            mDetailsFragment.setItemId(position);
        } else {
            Intent intent = new Intent(this, ItemDetailsActivity.class);
            intent.putExtra(Constants.EXTRA_ITEM_ID, position);
            startActivity(intent);
        }
    }
    
    public void gotoViewCart(View view) {
    	Intent intent = new Intent(view.getContext(), ViewCartActivity.class);
        startActivity(intent);
    }

    @TargetApi(11)
    @Override
    public void invalidateOptionsMenu() {
        if (Build.VERSION.SDK_INT >= 11) {
            super.invalidateOptionsMenu();
        } else if (mMenu != null) {
            MenuInflater inflater = getMenuInflater();
            if (((XyzApplication) getApplication()).isLoggedIn()) {
                inflater.inflate(R.menu.menu_logout, mMenu);
            } else {
                inflater.inflate(R.menu.menu_login, mMenu);
            }
        }
    }
}
