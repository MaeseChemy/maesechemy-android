package com.jmbg.apuestasgmv.views.adapters;

import java.util.List;

import com.jmbg.apuestasgmv.R;
import com.jmbg.apuestasgmv.utils.ActivityElement;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridAdapter extends BaseAdapter {

	private List<ActivityElement> gridElements;
	private Activity activity;

	public class GridHolder {
		public ImageView iconGrid;
		public TextView textGrid;
	}

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
		View v = convertView;

		GridHolder holder;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) this.activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.grid_element, null);

			holder = new GridHolder();
			holder.iconGrid = (ImageView) v.findViewById(R.id.image_grid_menu);
			holder.textGrid = (TextView) v.findViewById(R.id.name_grid_menu);

			v.setTag(holder);
		} else {
			holder = (GridHolder) v.getTag();
		}

		ActivityElement ae = this.gridElements.get(position);
		holder.iconGrid.setScaleType(ImageView.ScaleType.CENTER_CROP);
		holder.iconGrid.setImageResource(ae.getActivityIcon());
		holder.textGrid.setText(ae.getActivityName());

		if (!ae.isVisibility()) {
			v.setVisibility(View.GONE);
		}
		return v;
	}

}
