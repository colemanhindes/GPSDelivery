package edu.calpoly.gpsdelivery;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.samples.wallet.ItemListActivity;

import edu.calpoly.gpsdatabase01.Launch;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;

public class PayActivity extends android.support.v4.app.FragmentActivity 
{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay);
	}
	public void startMap(View view)
	{
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		
	}
	public void startDb(View view)
	{
		Intent intent = new Intent(this, Launch.class);
		startActivity(intent);	
	}
	public void startPay(View view)
	{
		LatLng location = MainActivity.getDeliveryLocation();
		/*String strLocation = "";
		if(location != null)
			strLocation += location.latitude+","+location.longitude;*/
		
		Intent intent = new Intent(this, ItemListActivity.class);
		//intent.putExtra("deliveryLoc", strLocation);
		startActivity(intent);
	}
//	protected void onActivityResult (int requestCode, int resultCode, Intent data)
//	{
//		if (requestCode == Activity.RESULT_OK)
//		{
//			Intent intent = new Intent(this, CheckoutActivity.class);
//			startActivityForResult(intent, LoginActivity.REQUEST_USER_LOGIN);
//		}
//	}
	
}
