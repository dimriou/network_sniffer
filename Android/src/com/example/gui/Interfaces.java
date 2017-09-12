package com.example.gui;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

public class Interfaces extends Fragment implements AdapterView.OnItemClickListener{
	ListView list; 
	VAdapter adapter;
	String data;
	Database db;
	boolean valid=true;
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#oonCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if(container==null){
			return null;
		}
		View v = inflater.inflate(R.layout.interfaces_layout, container, false);
		Toast.makeText(Interfaces.this.getActivity(), "Select from list to display ", Toast.LENGTH_LONG).show();
		list = (ListView) v.findViewById(R.id.ilist);


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
		TextView txt = (TextView) view;
		Database db = new Database(this.getActivity());
		
		Toast.makeText(Interfaces.this.getActivity(), txt.getText() + " " + position + " long " + id, Toast.LENGTH_SHORT).show();
		new AlertDialog.Builder(Interfaces.this.getActivity()).setTitle(txt.getText() + "'s Info").setMessage(db.getIPStatistics(txt.getText().toString(), this.data) + db.getPatternStatistics(txt.getText().toString(), this.data))
		.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) { 

			}}).setIcon(android.R.drawable.ic_dialog_alert).show();
	}
	public void getData(String ata){	
		data=ata;
		db = new Database(this.getActivity());
		ArrayList<String> al = (ArrayList<String>) db.getInterfaces(data);
		db.print();
			if(!valid)
				this.list.setAdapter(null);
			adapter = new VAdapter(Interfaces.this.getActivity(), al.toArray(new String[al.size()]));
			list.setAdapter(adapter);
			list.setOnItemClickListener(this);
			valid=false;
		
		if(data.equals("remake")){
			//make the list like it was in the start
			
			//if(!valid)
				this.list.setAdapter(null);
			//list.setAdapter(adapter);
			//list.setOnItemClickListener(this);
			valid = false;
		}
		
		
	}
}
