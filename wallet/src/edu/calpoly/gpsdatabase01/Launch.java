package edu.calpoly.gpsdatabase01;

//import edu.calpoly.gpsdelivery.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import java.util.List;
import java.util.Random;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

public class Launch extends ListActivity {
	private ItemsDataSource datasource;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.glenactivity_launch);

		datasource = new ItemsDataSource(this);
		datasource.open();

		List<Item> values = datasource.getAllItems();

		// use the SimpleCursorAdapter to show the
		// elements in a ListView
		ArrayAdapter<Item> adapter = new ArrayAdapter<Item>(this,
				android.R.layout.simple_list_item_1, values);
		setListAdapter(adapter);
	}

	// Will be called via the onClick attribute
	// of the buttons in main.xml
	public void onClick(View view) {
		@SuppressWarnings("unchecked")
		ArrayAdapter<Item> adapter = (ArrayAdapter<Item>) getListAdapter();
		Item item = null;
		switch (view.getId()) {
		/*case R.id.add:
			String[] items = new String[] { "Pizza", "Ice Cream", "Cookies" };
			int nextInt = new Random().nextInt(3);
			// save the new item to the database
			item = datasource.createItem(items[nextInt]);
			adapter.add(item);
			break;
		case R.id.delete:
			if (getListAdapter().getCount() > 0) {
				item = (Item) getListAdapter().getItem(0);
				datasource.deleteItem(item);
				adapter.remove(item);
			}
			break;
			
		case R.id.showReceiptButton:
			setContentView(R.layout.glenactivity_receipt);
			break;
			
		case R.id.backToDB:
			setContentView(R.layout.glenactivity_launch);
			break;
		*/}
		adapter.notifyDataSetChanged();
	}

	@Override
	protected void onResume() {
		datasource.open();
		super.onResume();
	}

	@Override
	protected void onPause() {
		datasource.close();
		super.onPause();
	}

} 