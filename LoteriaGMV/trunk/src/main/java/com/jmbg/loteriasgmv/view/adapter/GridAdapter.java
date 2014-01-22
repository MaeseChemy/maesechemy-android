package com.jmbg.loteriasgmv.view.adapter;

import java.util.List;

import com.jmbg.loteriasgmv.R;
import com.jmbg.loteriasgmv.util.ActivityElement;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridAdapter extends BaseAdapter {

	private List<ActivityElement> gridElements;
	private Activity activity;
	
	public GridAdapter(Activity activity,
			List<ActivityElement> gridElements) {
		super();
		this.activity = activity;
		this.gridElements = gridElements;
	}

	@Override
	public int getCount() {
		return this.gridElements.size();
	}

	@Override
	public Object getItem(int position) {
		return this.gridElements.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = this.activity.getLayoutInflater();
		View view = inflater.inflate(R.layout.element_main_grid, null, true);
		
		ImageView iconGrid = (ImageView)view.findViewById(R.id.image_grid_menu);
		//iconGrid.setLayoutParams(new GridView.LayoutParams(90, 90));

		iconGrid.setScaleType(ImageView.ScaleType.CENTER_CROP);

		TextView textGrid = (TextView)view.findViewById(R.id.name_grid_menu);

		ActivityElement ae = this.gridElements.get(position);
		
		iconGrid.setImageResource(ae.getActivityIcon());
		textGrid.setText(ae.getActivityName());
		
		if(!ae.isVisibility()){
			view.setVisibility(View.GONE);
		}
		return view;
	}


}
