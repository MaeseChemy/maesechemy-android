package com.jmbg.loteriasgmv.view.adapter;

import java.util.ArrayList;
import java.util.List;

import com.jmbg.loteriasgmv.R;
import com.jmbg.loteriasgmv.dao.entities.Bet;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BetAdapter extends ArrayAdapter<Bet> {

	@SuppressWarnings("unused")
	private Context context;
	private int textViewResourceId;
	private List<Bet> items;

	public static class ViewHolder {
		public ImageView imageBet;
		public TextView txtBetType;
		public TextView txtBetDate;
	}

	public BetAdapter(Context context, int textViewResourceId, List<Bet> items) {
		super(context, textViewResourceId, items);
		this.context = context;
		this.textViewResourceId = textViewResourceId;
		this.items = items;
	}

	public int getCount() {
		return this.items.size();
	}

	public Bet getItem(int position) {
		return this.items.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		ViewHolder holder;

		LayoutInflater vi = (LayoutInflater) this.getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = vi.inflate(textViewResourceId, null);

		holder = new ViewHolder();
		holder.imageBet = (ImageView) v.findViewById(R.id.bet_image);
		holder.txtBetType = (TextView) v.findViewById(R.id.bet_type);

		Bet item = this.items.get(position);

		holder.imageBet.setImageBitmap(BitmapFactory.decodeByteArray(
				item.getBetImage(), 0, item.getBetImage().length));
		holder.txtBetType.setText(item.getBetType() + "\n[" + item.getBetDate() + "]");

		return v;
	}
}
