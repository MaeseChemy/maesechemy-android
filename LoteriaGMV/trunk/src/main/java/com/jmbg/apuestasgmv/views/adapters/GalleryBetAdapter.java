package com.jmbg.apuestasgmv.views.adapters;

import java.util.List;

import com.jmbg.apuestasgmv.R;
import com.jmbg.apuestasgmv.model.dao.entities.Bet;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GalleryBetAdapter extends BaseAdapter {
	private Context context;
	private int layout;
	private List<Bet> items;

	public class GaleryHolder {
		public ImageView betImage;
		public TextView betTitle;
		public TextView betSubtitle;
	}

	public GalleryBetAdapter(Context context, int layout, List<Bet> items) {
		this.context = context;
		this.layout = layout;
		this.items = items;
	}

	public int getCount() {
		return items.size();
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		GaleryHolder holder;
		if (v == null){
			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(layout, null);
	
			holder = new GaleryHolder();
			holder.betImage = (ImageView) v.findViewById(R.id.bet_image);
			holder.betTitle = (TextView) v.findViewById(R.id.bet_title);
			holder.betSubtitle = (TextView) v.findViewById(R.id.bet_subTitle);
			
			v.setTag(holder);
		}else {
			holder = (GaleryHolder) v.getTag();
		}
		Bet item = getItem(position);
		holder.betImage.setImageBitmap(BitmapFactory.decodeByteArray(
				item.getBetImage(), 0, item.getBetImage().length));
		holder.betTitle.setText(item.getBetType());
		holder.betSubtitle.setText("[" + item.getBetDate() + "]");

		return v;
	}

	public Bet getItem(int position) {
		return items.get(position);
	}

}