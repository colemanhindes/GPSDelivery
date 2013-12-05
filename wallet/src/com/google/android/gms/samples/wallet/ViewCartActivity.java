package com.google.android.gms.samples.wallet;

import java.util.List;

//import com.google.android.gms.samples.wallet.ItemListFragment.ItemAdapter;

import edu.calpoly.gpsdatabase01.ItemsDataSource;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewCartActivity extends FragmentActivity implements OnItemClickListener {
	
	private Menu mMenu;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cart);
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
            Intent intent = new Intent(this, ItemDetailsActivity.class);
            intent.putExtra(Constants.EXTRA_ITEM_ID, position);
            startActivity(intent);
    }
    
    public void gotoCheckout(View view) {
    	// Go to checkout
    	// Need to edit checkoutactivity.class to not expect anything in the extra
    	// instead pull cart from ItemListActivity.userCart
    	
    	Intent intent = new Intent(this, CheckoutActivity.class);
        startActivity(intent);
    }
    
    // Onclick for remove button
    public void removeItem(View view) {
    	Log.d("gpsdelivery", "remove pressed; id: " + view.getTag()); // Displays the correct position
    	//String str = (String) view.getTag();
    	//TextView tv = (TextView) view.getTag();
    	
    	//ItemListActivity.removeFromCart(Integer.valueOf(view.getTag()));
    	//view.invalidate();
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
