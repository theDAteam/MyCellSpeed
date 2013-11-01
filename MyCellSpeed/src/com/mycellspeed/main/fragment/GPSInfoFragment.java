package com.mycellspeed.main.fragment;

import com.mycellspeed.main.R;
import com.mycellspeed.main.singleton.DataUnitHandler;

import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class GPSInfoFragment extends Fragment implements OnClickListener {
	private static LocationManager lm;
	
	private View rView;
	
	public GPSInfoFragment() {}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Load view for display
		rView = inflater.inflate(R.layout.fragment_gps_info, container, false);

    	// Set interaction listener
    	rView.findViewById(R.id.button1).setOnClickListener(this);
		
		return rView;
	}

	public void onClick(View view) {
		switch (view.getId()) {
        case R.id.button1:
        	updatePositionDisplay("Fragment Refresh");
        	break;
        }
	}
	
	public void updatePositionDisplay(String result) {
		TextView tv = (TextView) rView.findViewById(R.id.sec1_01_row01_value_textView);
		tv.setText(DataUnitHandler.getInstance().pullDataUnit("DvcLat"));
    	tv = (TextView) rView.findViewById(R.id.sec1_01_row02_value_textView);
    	tv.setText(DataUnitHandler.getInstance().pullDataUnit("DvcLon"));
    	
    	tv = (TextView) rView.findViewById(R.id.sec1_01_row03_value_textView);
    	tv.setText(result);
	}
	
	
}
