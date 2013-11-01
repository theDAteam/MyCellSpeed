package com.mycellspeed.main;

import java.util.Locale;

import com.mycellspeed.main.enumerator.AndroidOSEnumerator;
import com.mycellspeed.main.enumerator.NetworkTypeEnumerator;
import com.mycellspeed.main.enumerator.PhoneTypeEnumerator;
import com.mycellspeed.main.enumerator.SimStateEnumerator;
import com.mycellspeed.main.fragment.DataInfoFragment;
import com.mycellspeed.main.fragment.ErrorFragment;
import com.mycellspeed.main.fragment.GPSInfoFragment;
import com.mycellspeed.main.fragment.SignalInfoFragment;
import com.mycellspeed.main.fragment.SystemInfoFragment;
import com.mycellspeed.main.singleton.RuntimePropertyHandler;
import com.mycellspeed.main.singleton.ServiceDataHandler;
import com.mycellspeed.main.singleton.SystemDataHandler;
import com.mycellspeed.main.util.StringUtil;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.telephony.TelephonyManager;
import android.view.Menu;

public class Dashboard extends FragmentActivity implements ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_dashboard);
		if (!RuntimePropertyHandler.getInstance().isLoadSuccess()) {
			//TODO force exit
		}
		fillValues();

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dashboard, menu);
		return true;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	private void fillValues() {
		Locale l = Locale.getDefault();
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		
		// Loading System Values
		SystemDataHandler sys = SystemDataHandler.getInstance();
		sys.setManufacturer(StringUtil.capitalizeFirst(android.os.Build.MANUFACTURER, l));
		sys.setBrand(StringUtil.capitalizeFirst(android.os.Build.BRAND, l));
		sys.setDevice(android.os.Build.DEVICE);
		sys.setModel(android.os.Build.MODEL);
		sys.setPhoneType(PhoneTypeEnumerator.getValue(tm.getPhoneType()));
		sys.setIMEI(tm.getDeviceId());
		sys.setDeviceSoftwareVersion(tm.getDeviceSoftwareVersion());
		sys.setAndroidOSName(AndroidOSEnumerator.getValue(android.os.Build.VERSION.SDK_INT));
		sys.setAndroidOSVersion(android.os.Build.VERSION.RELEASE);
		
		// Loading Service Values
		ServiceDataHandler srv = ServiceDataHandler.getInstance();
		srv.setImsiID(tm.getSubscriberId());
		srv.setPhoneNumber(tm.getLine1Number());
		srv.setNetworkCountryISO(tm.getNetworkCountryIso().toUpperCase(l));
		srv.setNetworkOperatorName(tm.getNetworkOperatorName());
		// rebuild network operator, separate into MCC+MNC
		srv.setMobileCountryCode(tm.getNetworkOperator().substring(0, 3));
		srv.setMobileNetworkCode(tm.getNetworkOperator().substring(3));
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2)
			srv.setGroupIDLevelOne(tm.getGroupIdLevel1());
		//else srv.setGroupIDLevelOne("-1");
		srv.setSimStateValue(tm.getSimState());
		srv.setSimStateFull(tm.getSimState() + " - " + SimStateEnumerator.getValue(tm.getSimState()));
		//if (tm.getSimSerialNumber() != null)
			srv.setSimSerialNumber(tm.getSimSerialNumber());
		//else srv.setSimSerialNumber("-1");	
		srv.setSimCountryISO(tm.getSimCountryIso().toUpperCase(l));
		srv.setSimOperatorName(tm.getSimOperatorName());
		srv.setSimOperator(tm.getSimOperator());
	}
	
	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			switch(position) {
			case 0:
				return new GPSInfoFragment();
			case 1:
				return new SignalInfoFragment();
			case 2:
				return new DataInfoFragment();
			case 3:
				return new SystemInfoFragment();
			default:
				return new ErrorFragment();
			}
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 4;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.sec1_title).toUpperCase(l);
			case 1:
				return getString(R.string.sec2_title).toUpperCase(l);
			case 2:
				return getString(R.string.sec3_title).toUpperCase(l);
			case 3:
				return getString(R.string.sec4_title).toUpperCase(l);
			}
			return null;
		}
	}
}
