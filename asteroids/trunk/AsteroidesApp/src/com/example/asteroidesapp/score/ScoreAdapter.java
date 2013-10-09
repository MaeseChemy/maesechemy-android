package com.example.asteroidesapp.score;

import java.util.Vector;

import com.example.asteroidesapp.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ScoreAdapter extends BaseAdapter {

	private final Activity activity;
	private final Vector<Score> list;
	
	public ScoreAdapter(Activity activity, Vector<Score> list) {
		super();
		this.activity = activity;
		this.list = list;
	}
	@Override
	public int getCount() {
		return this.list.size();
	}

	@Override
	public Score getItem(int position) {
		return this.list.elementAt(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = this.activity.getLayoutInflater();
		View view = inflater.inflate(R.layout.list_score_element, null, true);
		
		TextView tittle = (TextView)view.findViewById(R.id.tittle);
		tittle.setText(Integer.toString(this.list.elementAt(position).getScore()));
		TextView subtittle = (TextView)view.findViewById(R.id.subtittle);
		subtittle.setText(this.list.elementAt(position).getUsername());
		ImageView image = (ImageView)view.findViewById(R.id.icon);
		image.setImageResource(R.drawable.ic_launcher);
		
		return view;
	}

}
