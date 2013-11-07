package edu.calpoly.gpsdelivery;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.wallet.WalletClient;
import com.google.android.gms.wallet.WalletConstants;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class PayActivity extends Activity implements 
com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks,
com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener
{
	private static final int REQUEST_CODE_PRE_AUTH = 1010;
	private boolean mIsPreAuthed = false;

	private WalletClient mWalletClient;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay);
		mWalletClient = new WalletClient(this, WalletConstants.ENVIRONMENT_SANDBOX, null,
		        WalletConstants.THEME_HOLO_LIGHT, this,
		        this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.pay, menu);
		return true;
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnected(Bundle connectionHint) {
	    mWalletClient.checkForPreAuthorization(
	        REQUEST_CODE_PRE_AUTH);
	}

	public void onActivityResult(int requestCode, int resultCode,
	        Intent data) {
	    switch (requestCode) {
	        case REQUEST_CODE_PRE_AUTH:
	            switch (resultCode) {
	                case Activity.RESULT_OK:
	                    mIsPreAuthed = data.getBooleanExtra(

	                            WalletConstants.EXTRA_IS_USER_PREAUTHORIZED, false);

	                    break;
	                case Activity.RESULT_CANCELED:
	                    break;
	                default:
	                    break;
	            }
	    }
	}

}
