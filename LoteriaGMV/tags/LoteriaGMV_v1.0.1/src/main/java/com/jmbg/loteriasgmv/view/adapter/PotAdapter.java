package com.jmbg.loteriasgmv.view.adapter;

import java.util.Collections;
import java.util.List;

import com.jmbg.loteriasgmv.R;
import com.jmbg.loteriasgmv.dao.entities.Pot;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PotAdapter extends ArrayAdapter<Pot> {

	private Context context;
	private int textViewResourceId;
	private List<Pot> items;

	public static class ViewHolder {
		public TextView txtPotDate;
		public TextView txtPotValue;
		public TextView txtPotBet;
		public TextView txtPotWon;
		public LinearLayout layoutPrizes;
	}

	public PotAdapter(Context context, int textViewResourceId, List<Pot> items) {
		super(context, textViewResourceId, items);
		this.context = context;
		this.textViewResourceId = textViewResourceId;
		this.items = items;
		Collections.sort(this.items);
	}

	public int getCount() {
		return this.items.size();
	}

	public Pot getItem(int position) {
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
		holder.txtPotDate = (TextView) v.findViewById(R.id.pot_date);
		holder.txtPotValue = (TextView) v.findViewById(R.id.pot_value);
		holder.txtPotBet = (TextView) v.findViewById(R.id.pot_bet);
		holder.txtPotWon = (TextView) v.findViewById(R.id.pot_won);
		holder.layoutPrizes = (LinearLayout) v.findViewById(R.id.layout_prizes);

		v.setTag(holder);

		Pot item = this.items.get(position);

		holder.txtPotDate.setText(item.getPotDate());
		holder.txtPotValue.setText("Bote Acumulado: " + Float.toString(item.getPotValue()) + " €");
		holder.txtPotBet.setText("Apo.: " + Float.toString(item.getPotBet())
				+ " €");
		if (item.getPotValid() == Pot.POT_INVALID)
			holder.txtPotWon.setText("Gan.: "
					+ Float.toString(item.getPotWon()) + " €");
		else
			holder.txtPotWon.setText("Gan.: -");
		
		
		Drawable back;
		if(item.getPotWon()> 0)
			back = context.getResources().getDrawable(R.drawable.win_box_shape);		
		else
			back = context.getResources().getDrawable(R.drawable.box_shape);
		back.setAlpha(60);
		holder.layoutPrizes.setBackgroundDrawable(back);
		
		return v;
	}

}
