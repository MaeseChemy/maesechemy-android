package com.jmbg.apuestasgmv.views.adapters;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

import com.jmbg.apuestasgmv.R;
import com.jmbg.apuestasgmv.model.dao.entities.Pot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PotAdapter extends ArrayAdapter<Pot> {

	private Context context;
	private int layout;
	private List<Pot> items;

	public static class ViewHolder {
		public TextView txtPotDate;
		public TextView txtPotValue;
		public TextView txtPotBet;
		public TextView txtPotWon;
		public TextView txtPotFinalValue;
	}

	public PotAdapter(Context context, int textViewResourceId, List<Pot> items) {
		super(context, textViewResourceId, items);
		this.context = context;
		this.layout = textViewResourceId;
		this.items = items;
		Collections.sort(this.items, Collections.reverseOrder());
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
		DecimalFormat df = new DecimalFormat("#.##");

		LayoutInflater vi = (LayoutInflater) this.getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = vi.inflate(layout, null);

		holder = new ViewHolder();
		holder.txtPotDate = (TextView) v.findViewById(R.id.pot_date);
		holder.txtPotValue = (TextView) v.findViewById(R.id.pot_value);
		holder.txtPotBet = (TextView) v.findViewById(R.id.pot_bet);
		holder.txtPotWon = (TextView) v.findViewById(R.id.pot_won);
		holder.txtPotFinalValue = (TextView) v
				.findViewById(R.id.pot_final_value);

		v.setTag(holder);

		Pot item = this.items.get(position);

		holder.txtPotDate.setText(item.getPotDate());
		holder.txtPotValue.setText(df.format(item.getPotValue()) + " €");
		holder.txtPotBet.setText(df.format(item.getPotBet()) + " €");
		holder.txtPotWon.setText(df.format(item.getPotWon()) + " €");
		double potFinalValue = item.getPotValue() - item.getPotBet()
				+ item.getPotWon();
		holder.txtPotFinalValue.setText(df.format(potFinalValue) + " €");

		int color;
		if (potFinalValue < item.getPotValue())
			color = context.getResources().getColor(R.color.red);
		else if (potFinalValue > item.getPotValue())
			color = context.getResources().getColor(R.color.green);
		else
			color = context.getResources().getColor(R.color.black);
		holder.txtPotFinalValue.setTextColor(color);

		if (item.getPotWon() > 0) {
			holder.txtPotWon.setTextColor(context.getResources().getColor(
					R.color.green));
		}

		return v;
	}

}
