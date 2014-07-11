package com.jmbg.apuestasgmv.views.adapters;

import java.util.List;

import com.jmbg.apuestasgmv.R;
import com.jmbg.apuestasgmv.utils.ActivityElement;

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

	public GridAdapter(Activity activity, List<ActivityElement> gridElements) {
		super();
		this.activity = activity;
		this.gridElements = gridElements;
	}

	public int getCount() {
		return this.gridElements.size();
	}

	public Object getItem(int position) {
		return this.gridElements.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = this.activity.getLayoutInflater();
		View view = inflater.inflate(R.layout.grid_element, null, true);

		ImageView iconGrid = (ImageView) view
				.findViewById(R.id.image_grid_menu);

		iconGrid.setScaleType(ImageView.ScaleType.CENTER_CROP);

		TextView textGrid = (TextView) view.findViewById(R.id.name_grid_menu);

		ActivityElement ae = this.gridElements.get(position);

		iconGrid.setImageResource(ae.getActivityIcon());
		textGrid.setText(ae.getActivityName());

		if (!ae.isVisibility()) {
			view.setVisibility(View.GONE);
		}
		return view;
	}

}
