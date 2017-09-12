package com.example.gui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.asynctasks.Delete;
import com.example.threads.Connection;

public class Deletor extends Fragment {
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if(container==null){
			return null;
		}

		final View view = (RelativeLayout)inflater.inflate(R.layout.delete_layout, container, false);
		Button button = (Button) view.findViewById(R.id.DelButton);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EditText editText = (EditText) view.findViewById(R.id.deleted);
				String delme = editText.getText().toString();
				if(delme.isEmpty())
					Toast.makeText(Deletor.this.getActivity(), "Input something ", Toast.LENGTH_SHORT).show();
				else{
				Connection check = new Connection();
				Boolean conn = check.checkNow(getActivity().getApplicationContext());


				Delete rg = new Delete(conn,"administrator","admin",delme);
				rg.execute(this);
					
				}
				
			}
		});
		return view;
	}
//	public void deletoras(View view){
//		EditText editText = (EditText) view.findViewById(R.id.deleted);
//		String delme = editText.getText().toString();
//		System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
//		/*
//		 * edw tha kaneis to del
//		 * to delme einai auto pou ebale o admin
//		 * 
//		 */
//		Connection check = new Connection();
//		Boolean conn = check.checkNow(this.getActivity().getApplicationContext());
//
//
//		Delete rg = new Delete(conn,"administrator","admin",delme);
//		rg.execute(this);
//
//
//
//	}

}
