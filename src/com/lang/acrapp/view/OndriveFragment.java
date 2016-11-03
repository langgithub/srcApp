package com.lang.acrapp.view;

import com.lang.scrapp.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class OndriveFragment extends Fragment {

	public static TextView tv_drive;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub\
		View v=inflater.inflate(R.layout.ondriveway, container, false);
		tv_drive=(TextView) v.findViewById(R.id.et_myendPosition_walk);
		return v;
		
	}

	@Override
	public View getView() {
		// TODO Auto-generated method stub
		return super.getView();
	}
	

}
