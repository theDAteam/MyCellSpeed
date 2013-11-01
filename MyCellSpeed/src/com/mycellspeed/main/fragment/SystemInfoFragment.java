package com.mycellspeed.main.fragment;

import com.mycellspeed.main.R;
import com.mycellspeed.main.singleton.ServiceDataHandler;
import com.mycellspeed.main.singleton.SystemDataHandler;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SystemInfoFragment extends Fragment {
	
	public SystemInfoFragment() {}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Load view for display + initialize
		View rootView = inflater.inflate(R.layout.fragment_system_info, container, false);
		ServiceDataHandler srv = ServiceDataHandler.getInstance();
		SystemDataHandler sys = SystemDataHandler.getInstance();
		
		/*
		 * tv = (TextView) rootView.findViewById(R.id.sec4_01_row07_value_textView);
		 * tv.setText("" + tm.getSimCountryIso() + "," + tm.getSimOperator() + "," + tm.getSimOperatorName() + "," + tm.getSimSerialNumber() + "," + tm.getSimState());
		 */
		// Load data for Make/Model Section
		((TextView) rootView.findViewById(R.id.sec4_01_row01_value_textView)).setText(sys.getManufacturer());
		((TextView) rootView.findViewById(R.id.sec4_01_row02_value_textView)).setText(sys.getBrand());
		((TextView) rootView.findViewById(R.id.sec4_01_row03_value_textView)).setText(sys.getDevice() + "/" + sys.getModel());
		((TextView) rootView.findViewById(R.id.sec4_01_row04_value_textView)).setText(sys.getPhoneType());
		((TextView) rootView.findViewById(R.id.sec4_01_row05_value_textView)).setText(sys.getIMEI());
		((TextView) rootView.findViewById(R.id.sec4_01_row06_value_textView)).setText(sys.getDeviceSoftwareVersion());
		
		// Load Data for Service Information
		((TextView) rootView.findViewById(R.id.sec4_02_row01_value_textView)).setText(srv.getImsiID());
		((TextView) rootView.findViewById(R.id.sec4_02_row02_value_textView)).setText(srv.getPhoneNumber());
		((TextView) rootView.findViewById(R.id.sec4_02_row03_value_textView)).setText(srv.getNetworkType());
		((TextView) rootView.findViewById(R.id.sec4_02_row04_value_textView)).setText(srv.getNetworkCountryISO() + " - " + srv.getNetworkOperatorName());
		((TextView) rootView.findViewById(R.id.sec4_02_row05_value_textView)).setText(srv.getMobileCountryCode()+srv.getMobileNetworkCode());

		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2)
			((TextView) rootView.findViewById(R.id.sec4_02_row06_value_textView)).setText(srv.getGroupIDLevelOne());
		else
			rootView.findViewById(R.id.sec4_02_row06).setVisibility(TextView.GONE);
		
		((TextView) rootView.findViewById(R.id.sec4_02_row08_value_textView)).setText(srv.getSimStateFull());
		if (srv.getSimStateValue() != null) {
			if (srv.getSimStateValue() == TelephonyManager.SIM_STATE_UNKNOWN
				|| srv.getSimStateValue() == TelephonyManager.SIM_STATE_ABSENT) {
				rootView.findViewById(R.id.sec4_02_row07).setVisibility(TextView.GONE);
				rootView.findViewById(R.id.sec4_02_row09).setVisibility(TextView.GONE);
				rootView.findViewById(R.id.sec4_02_row10).setVisibility(TextView.GONE);
			} else {
				((TextView) rootView.findViewById(R.id.sec4_02_row07_value_textView)).setText(srv.getSimSerialNumber());
				((TextView) rootView.findViewById(R.id.sec4_02_row09_value_textView)).setText(srv.getSimCountryISO() + " - " + srv.getSimOperatorName());
				((TextView) rootView.findViewById(R.id.sec4_02_row10_value_textView)).setText(srv.getSimOperator());
			} 
		}
		
		((TextView) rootView.findViewById(R.id.sec4_03_row01_value_textView)).setText(sys.getAndroidOSName() + " (v " + sys.getAndroidOSVersion() + ")");
		
		//TODO print datapoints to file
		
		//TODO send file
		
		// Return generated view
		return rootView;
	}
}
