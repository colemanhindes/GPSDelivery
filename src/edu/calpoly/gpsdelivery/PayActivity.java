package edu.calpoly.gpsdelivery;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.wallet.WalletClient;
import com.google.android.gms.wallet.WalletConstants;

import edu.calpoly.gpsdatabase01.Launch;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class PayActivity extends Activity 
{
	private WalletClient mWalletClient;
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
		Intent intent = new Intent(this, CheckoutActivity.class);
		startActivityForResult(intent, 0);
	}
	
}
