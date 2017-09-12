package com.example.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.database.Database;
import com.example.threads.Implementations;
import com.example.threads.Statistics_thread;

public class Statistics extends Fragment implements AdapterView.OnItemClickListener{
	SendStat ss;
	ListView list;
	VAdapter adapter;
	Database db;
	String username, password;
	Thread megazork;
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(container==null){
			return null;
		}
		//String[] data = { "AAA ", "BBB", "ccc", "ddd", "eee", "fff","GGG", "HHH", "III", "JJJ", "KKK", "LLL"};
		db = new Database(this.getActivity());
		
		Implementations.isRunning = true;
		Statistics_thread is =  new Statistics_thread(this.username, this.password, this.getActivity());
		// We create the thread that scans the Network interfaces...
		megazork= new Thread( is );
		// ...and we start it.
		megazork.start();
	
		
		ArrayList<String> al = (ArrayList<String>) db.getAllIds();
		View v = inflater.inflate(R.layout.statistics_layout, container, false);
		list = (ListView) v.findViewById(R.id.list);
		adapter = new VAdapter(Statistics.this.getActivity(), al.toArray(new String[al.size()]));
		list.setAdapter(adapter);
		list.setOnItemClickListener(this);
		return v;
	}

	class VAdapter extends ArrayAdapter<String>{
		Context context;
		String[] data;
		public VAdapter(Context context, String[] data) {
			super(context, android.R.layout.simple_list_item_1, data);
			this.context = context;
			this.data = data;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent){
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View row = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
			TextView txt = (TextView)row.findViewById(android.R.id.text1);
			txt.setText(data[position]);			
			return row;
		}
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		String fragData;
		TextView txt = (TextView) view;
		fragData = txt.getText().toString();
		//Toast.makeText(Statistics.this.getActivity(), fragData + " " + position + " long " + id, Toast.LENGTH_SHORT).show();
		ss.sendData(fragData);
	}
	
	interface SendStat{
		public void sendData(String stat);
	}
	interface MyStats{
		public void showme();
		public void refresh();
	}
	public void showMine(){/*****************************************************************************************/
		
		ArrayList<String> tmp = new ArrayList<String>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader( this.getActivity().getAssets().open("Configuration.txt")));

			String mLine = reader.readLine();
			int change = 0;
			while (mLine != null) {
				if(mLine.equals("[Available nodes]")){
					change = 1;
				}
				else if (change == 1){
					tmp.add(mLine);
				}
				mLine = reader.readLine();
			}
		}
		catch (IOException e) {}
		finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {}

			}
		}
		ArrayList<String> all = (ArrayList<String>) db.getAllIds();
		ArrayList<String> finals = new ArrayList<String>();
		for (String s:tmp){
			if (all.indexOf(s)!= -1){
				finals.add(s);
			}
		}
		
		
		this.list.setAdapter(null);
		adapter = new VAdapter(Statistics.this.getActivity(), finals.toArray(new String[finals.size()]));
		list.setAdapter(adapter);
		list.setOnItemClickListener(this);
	}
	public void getString(String user, String pass){
		this.username = user;
		this.password = pass;
	}
	public void refresh(){
		//String[] newData = { "AAA ", "BBB", "ccc", "ddd", "eee", "fff","GGG", "HHH", "III", "JJJ", "KKK", "LLL"};
		ArrayList<String> al = (ArrayList<String>) db.getAllIds();
		this.list.setAdapter(null);
		adapter = new VAdapter(Statistics.this.getActivity(), al.toArray(new String[al.size()]));
		list.setAdapter(adapter);
		list.setOnItemClickListener(this);
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try{
			ss = (SendStat)activity;	
			}
		catch(ClassCastException e){
			throw new ClassCastException("Error in ClassCast");
		}
		
	
	}

}
