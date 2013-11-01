package com.mycellspeed.main.fragment;

import com.mycellspeed.main.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ErrorFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_error, container, false);
		TextView dummyTextView = (TextView) rootView.findViewById(R.id.section_label);
		dummyTextView.setText(R.string.error_title);
		return rootView;
	}

}
