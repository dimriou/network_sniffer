package com.example.gui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asynctasks.InsertMaliciousPatterns;
import com.example.database.Database;
import com.example.threads.Connection;
public class Add extends Fragment {

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
		Database db = new Database(this.getActivity());
		String data = db.getStoredValue(Database.MALICIOUS_PATTERN_RESPONSE);
		final View view = inflater.inflate(R.layout.add_layout, container, false);
		TextView txt = (TextView) view.findViewById(R.id.addtext);
		txt.setText(data);
		txt.setText(data.toCharArray(), 0, txt.length());
		txt.setTextColor(Color.BLACK);
		txt.setMovementMethod(new ScrollingMovementMethod());
		
		
		Button button = (Button) view.findViewById(R.id.AddButtonIP);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EditText editText = (EditText) view.findViewById(R.id.added);
				String addme = editText.getText().toString();
				if(addme.isEmpty())
					Toast.makeText(Add.this.getActivity(), "Input something ", Toast.LENGTH_SHORT).show();
				else{
					addip(v, addme);
				}
				
			}
		});
		
		Button buttonb = (Button) view.findViewById(R.id.AddButtonPatterns);
		buttonb.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EditText editText = (EditText) view.findViewById(R.id.added);
				String addme = editText.getText().toString();
				if(addme.isEmpty())
					Toast.makeText(Add.this.getActivity(), "Input something ", Toast.LENGTH_SHORT).show();
				else{
					addpattern(v, addme);
				}
				
			}
		});
		return view;
	}
	private void addpattern(View view, String addme){
		if(!addme.isEmpty()){
			
			Connection check = new Connection();
		    Boolean conn = check.checkNow(this.getActivity().getApplicationContext());
			    
				
		    InsertMaliciousPatterns rg = new InsertMaliciousPatterns(conn,"admin","admin","",addme);
		    rg.execute(this);
			
		}
	}
	private void addip(View view, String addme){

		if(!addme.isEmpty()){
			
			Connection check = new Connection();
		    Boolean conn = check.checkNow(this.getActivity().getApplicationContext());
			    
				
		    InsertMaliciousPatterns rg = new InsertMaliciousPatterns(conn,"admin","admin",addme,"");
		    rg.execute(this);
		}
	}
}
