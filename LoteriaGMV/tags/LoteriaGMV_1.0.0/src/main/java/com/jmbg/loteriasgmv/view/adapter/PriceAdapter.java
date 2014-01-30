package com.jmbg.loteriasgmv.view.adapter;

import java.util.List;

import com.jmbg.loteriasgmv.R;
import com.jmbg.loteriasgmv.dao.entities.Price;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PriceAdapter extends ArrayAdapter<Price> {

	private Context context;
	private int textViewResourceId;
	private List<Price> items;

	public static class ViewHolder {
		public ImageView imagePrice;
		public TextView txtPriceType;
		public TextView txtPriceDate;
		public TextView txtPriceNumbers;
		public TextView txtPriceNextPrice;
		public TextView txtPriceNextPot;
	}

	public PriceAdapter(Context context, int textViewResourceId,
			List<Price> items) {
		super(context, textViewResourceId, items);
		this.context = context;
		this.textViewResourceId = textViewResourceId;
		this.items = items;
	}

	public int getCount() {
		return this.items.size();
	}

	public Price getItem(int position) {
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
		holder.txtPriceType = (TextView) v.findViewById(R.id.price_type);
		holder.txtPriceDate = (TextView) v.findViewById(R.id.price_date);
		holder.txtPriceNumbers = (TextView) v.findViewById(R.id.price_numbers);
		holder.txtPriceNextPrice = (TextView) v.findViewById(R.id.price_next_date);
		holder.txtPriceNextPot = (TextView) v.findViewById(R.id.price_next_pot);

		Price item = this.items.get(position);
		holder.txtPriceType.setText(item.getPriceType());
		holder.txtPriceDate.setText(item.getPriceDate());
		holder.txtPriceNumbers.setText(item.getPriceNumbersFormat());
		holder.txtPriceNextPrice.setText("Siguiente sorteo: "+ item.getPriceNextDate());
		holder.txtPriceNextPot.setText("BOTE: " + Integer.toString(item.getPriceNextPot()) + " €");

		return v;
	}
}
