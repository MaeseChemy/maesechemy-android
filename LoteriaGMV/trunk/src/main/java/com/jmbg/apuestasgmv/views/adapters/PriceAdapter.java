package com.jmbg.apuestasgmv.views.adapters;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

import com.jmbg.apuestasgmv.R;
import com.jmbg.apuestasgmv.model.dao.entities.Price;
import com.jmbg.apuestasgmv.views.PriceActivity;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PriceAdapter extends ArrayAdapter<Price> {

	private Context context;
	private int layout;
	private List<Price> items;
	
	private PriceActivity mActivity;

	public static class ViewHolder {
		public TextView txtPriceType;
		public TextView txtPriceDate;
		public LinearLayout layoutNumbers;
		public TextView txtPriceNextPrice;
		public TextView txtPriceNextPot;
		public ImageView searchButton;
	}

	public PriceAdapter(Context context, int textViewResourceId,
			List<Price> items, PriceActivity activity) {
		super(context, textViewResourceId, items);
		this.context = context;
		this.layout = textViewResourceId;
		this.items = items;
		this.mActivity = activity;
		Collections.sort(this.items, Collections.reverseOrder());
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
		DecimalFormat df = new DecimalFormat("###,###.##");
		
		LayoutInflater vi = (LayoutInflater) this.getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = vi.inflate(layout, null);

		holder = new ViewHolder();
		holder.txtPriceType = (TextView) v.findViewById(R.id.price_type);
		holder.txtPriceDate = (TextView) v.findViewById(R.id.price_date);
		holder.layoutNumbers = (LinearLayout) v
				.findViewById(R.id.price_layout_numbers);
		holder.txtPriceNextPrice = (TextView) v
				.findViewById(R.id.price_next_date);
		holder.txtPriceNextPot = (TextView) v.findViewById(R.id.price_next_pot);
		holder.searchButton = (ImageView) v.findViewById(R.id.price_search_results);
		
		v.setTag(holder);

		final Price item = this.items.get(position);
		holder.txtPriceType.setText(item.getPriceType());
		holder.txtPriceDate.setText("["+item.getPriceDate()+"]");
		holder.txtPriceNextPrice.setText("Siguiente sorteo: "
				+ item.getPriceNextDate());
		holder.txtPriceNextPot.setText("BOTE: "
				+ df.format(item.getPriceNextPot()) + " €");

		LinearLayout layoutNumeros = new LinearLayout(context);
		layoutNumeros.setGravity(Gravity.CENTER);
		layoutNumeros.setOrientation(LinearLayout.HORIZONTAL);
		String campos[] = item.getPriceNumbers().split("-");
		for (String campo : campos) {
			boolean isStar = false;
			String numero = null;
			if (campo.contains("N")) {
				numero = campo.replace("N", "") + " ";
			} else if (campo.contains("E")) {
				numero = campo.replace("E", "") + " ";
				isStar = true;
			}

			if (numero != null) {
				View numberLayout = vi.inflate(R.layout.number_format_layout,
						null);

				TextView numeroView = (TextView) numberLayout
						.findViewById(R.id.number_text);
				numeroView.setText(numero);

				ImageView numberImage = (ImageView) numberLayout
						.findViewById(R.id.number_image);
				if (isStar) {
					numberImage
							.setImageResource((R.drawable.ic_estrella_amarilla));
				} else {
					if (item.getPriceType().equals("Euromillones")) {
						numberImage.setImageResource(R.drawable.ic_bola_azul);

					} else if (item.getPriceType().equals("Primitiva")) {
						numberImage.setImageResource(R.drawable.ic_bola_gris);
					}
				}
				layoutNumeros.addView(numberLayout);
			}

		}

		holder.layoutNumbers.addView(layoutNumeros);
		
		holder.searchButton.setClickable(true);
		holder.searchButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				mActivity.searchPriceResults(item);
			}
		});
		
		return v;
	}

}
