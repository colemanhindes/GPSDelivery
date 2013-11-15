package edu.calpoly.gpsdelivery;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * This shows how to create a simple activity with a map and a marker on the
 * map.
 * <p>
 * Notice how we deal with the possibility that the Google Play services APK is
 * not installed/enabled/updated on a user's device.
 */
public class MainActivity extends FragmentActivity implements
		ConnectionCallbacks, OnConnectionFailedListener, LocationListener,
		OnMarkerDragListener {
	/**
	 * Note that this may be null if the Google Play services APK is not
	 * available.
	 */
	private GoogleMap mMap;
	private Button mButton;
	private LocationClient mLocationClient;
	private LatLng mCurrentCoordinates;
	private LatLng mMarkerPos;
	private boolean mSent = false;
	private Marker mMarker;

	private static final LocationRequest REQUEST = LocationRequest.create()
			.setInterval(5000) // 5 seconds
			.setFastestInterval(16) // 16ms = 60fps
			.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setUpMapIfNeeded();
		// Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
		// "mailto","colemanhindes@gmail.com", null));
		// emailIntent.putExtra(Intent.EXTRA_SUBJECT, "EXTRA_SUBJECT");
		// emailIntent.putExtra(Intent.EXTRA_TEXT, "I'm email body.");
		// startActivity(Intent.createChooser(emailIntent, "Send email..."));

		// mButton = (Button) findViewById(R.id.button);
		// if (mButton != null)
		// {
		// mButton.setOnClickListener(new View.OnClickListener() {
		// public void onClick(View v) {
		// // Perform action on click
		// }
		// });
		// }
		// else
		// Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
	}

	private void setUpLocationClientIfNeeded() {
		if (mLocationClient == null) {
			mLocationClient = new LocationClient(this, this, // ConnectionCallbacks
					this); // OnConnectionFailedListener
		}
	}

	/**
	 * Callback called when connected to GCore. Implementation of
	 * {@link ConnectionCallbacks}.
	 */
	@Override
	public void onConnected(Bundle connectionHint) {
		mLocationClient.requestLocationUpdates(REQUEST, this); // LocationListener
	}

	/**
	 * Implementation of {@link OnConnectionFailedListener}.
	 */
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// Do nothing
	}

	@Override
	protected void onResume() {
		super.onResume();
		setUpMapIfNeeded();
		setUpLocationClientIfNeeded();
		mLocationClient.connect();
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mLocationClient != null) {
			mLocationClient.disconnect();
		}
	}

	public void showMyLocation(View view) {
		if (mLocationClient != null && mLocationClient.isConnected()) {
			Location lastLocation = mLocationClient.getLastLocation();
			String msg = "Location = " + mLocationClient.getLastLocation();
			if (lastLocation != null) {
				LatLng currentCoordinates = new LatLng(
						lastLocation.getLatitude(), lastLocation.getLongitude());
				mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
						currentCoordinates, 10));

			}

			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT)
					.show();
		}
	}

	/**
	 * Sets up the map if it is possible to do so (i.e., the Google Play
	 * services APK is correctly installed) and the map has not already been
	 * instantiated.. This will ensure that we only ever call
	 * {@link #setUpMap()} once when {@link #mMap} is not null.
	 * <p>
	 * If it isn't installed {@link SupportMapFragment} (and
	 * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt
	 * for the user to install/update the Google Play services APK on their
	 * device.
	 * <p>
	 * A user can return to this FragmentActivity after following the prompt and
	 * correctly installing/updating/enabling the Google Play services. Since
	 * the FragmentActivity may not have been completely destroyed during this
	 * process (it is likely that it would only be stopped or paused),
	 * {@link #onCreate(Bundle)} may not be called again so we should call this
	 * method in {@link #onResume()} to guarantee that it will be called.
	 */
	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (mMap == null) {
			// Check if we were successful in obtaining the map.
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();

			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				mMap.setMyLocationEnabled(true);
				mMap.setOnMarkerDragListener(this);
				showMyLocation(this.findViewById(R.id.map));
			}
		}
	}

	private void setCoords() {
		Location lastLocation = mLocationClient.getLastLocation();
		mCurrentCoordinates = new LatLng(lastLocation.getLatitude(),
				lastLocation.getLongitude());

	}

	@Override
	public void onLocationChanged(Location location) {
		// Toast.makeText(getApplicationContext(), "Location = " + location,
		// Toast.LENGTH_SHORT).show();
		// Do nothing
		setCoords();
		if (mSent == false) {
			SmsManager smsManager = SmsManager.getDefault();
			String phoneNo = "3235335596";
			String msg = "Location = " + mCurrentCoordinates
					+ "\n1 pizza\n 19.99";
			//smsManager.sendTextMessage(phoneNo, null, msg, null, null);
			mSent = true;
			mMarker = mMap.addMarker(new MarkerOptions()
					.position(mCurrentCoordinates).title("Hello world")
					.draggable(true));
			mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
					mCurrentCoordinates, 18));
		}
		

	}

	@Override
	public void onMarkerDragEnd(Marker marker) {
		mMarkerPos = marker.getPosition();
		Toast.makeText(this, mMarkerPos.toString(), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onDisconnected() {
		// Do nothing

	}

	@Override
	public void onMarkerDrag(Marker arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMarkerDragStart(Marker arg0) {
		// TODO Auto-generated method stub

	}
}
