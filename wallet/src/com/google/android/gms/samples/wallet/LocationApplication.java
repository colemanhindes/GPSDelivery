package com.google.android.gms.samples.wallet;

import android.os.Bundle;
import android.app.Activity;
import android.app.Application;
import android.view.Menu;

public class LocationApplication extends Application {

    private String locationString;

    public void setLocation(String locationString) {
        this.locationString = locationString;
    }
    public String getLocation() {
        return locationString;
    }
}
