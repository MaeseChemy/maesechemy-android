package com.jmbg.loteriasgmv.view.adapter;

import java.util.List;

import com.jmbg.loteriasgmv.R;
import com.jmbg.loteriasgmv.dao.entities.Pot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PotAdapter extends ArrayAdapter<Pot> {
	
	@SuppressWarnings("unused")
	private Context context;
	private int textViewResourceId;
	private List<Pot> items;

	public static class ViewHolder {
		public TextView txtPotDate;
		public TextView txtPotValue;
		public TextView txtPotBet;
		public TextView txtPotWon;
	}
	
	public PotAdapter(Context context, int textViewResourceId, List<Pot> items) {
		super(context, textViewResourceId, items);
		this.context = context;
		this.textViewResourceId = textViewResourceId;
		this.items = items;
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

		if (v == null) {
			LayoutInflater vi = (LayoutInflater) this.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(textViewResourceId, null);
			
			holder = new ViewHolder();
			holder.txtPotDate = (TextView) v.findViewById(R.id.pot_date);
			holder.txtPotValue = (TextView) v.findViewById(R.id.pot_value);
			holder.txtPotBet = (TextView) v.findViewById(R.id.pot_bet);
			holder.txtPotWon = (TextView) v.findViewById(R.id.pot_won);
			

			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}
		
		Pot item = this.items.get(position);
		
		holder.txtPotDate.setText(item.getPotDate());
		holder.txtPotValue.setText(Float.toString(item.getPotValue()) + " €");
		holder.txtPotBet.setText("Apostado\t: " +Float.toString(item.getPotBet()) + " €");
		holder.txtPotWon.setText("Ganado\t: " +Float.toString(item.getPotWon()) + " €");
		return v;
	}


}
