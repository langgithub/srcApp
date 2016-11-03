package com.lang.acrapp.view;


import com.lang.scrapp.R;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class OnBusFragment extends Fragment {

	public static TextView tv_bus;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v=inflater.inflate(R.layout.onbusway, container, false);
		tv_bus=(TextView) v.findViewById(R.id.et_myendPosition_bus);
		//setOnClick();
		return v;
		
	}

	@Override
	public View getView() {
		// TODO Auto-generated method stub
		return super.getView();
	}
	
	
}
