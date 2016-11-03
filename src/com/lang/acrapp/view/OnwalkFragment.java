package com.lang.acrapp.view;

import com.lang.scrapp.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class OnwalkFragment extends Fragment {

	public static TextView tv_walk;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v=inflater.inflate(R.layout.onwalkway, container, false);
		tv_walk=(TextView) v.findViewById(R.id.et_myendPosition_walk);
		return v;
		
	}
}
