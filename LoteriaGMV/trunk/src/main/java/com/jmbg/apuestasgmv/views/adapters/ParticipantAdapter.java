package com.jmbg.apuestasgmv.views.adapters;

import java.util.List;

import com.jmbg.apuestasgmv.R;
import com.jmbg.apuestasgmv.model.dao.entities.Participant;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ParticipantAdapter extends ArrayAdapter<Participant> {

	private Context context;
	private int layout;
	private List<Participant> items;

	public static class ViewHolder {
		public ImageView imageParticipant;
		public TextView txtParticipantName;
		public TextView txtParticipantFund;
		public ImageView imageFundParticipant;
	}

	public ParticipantAdapter(Context context, int textViewResourceId,
			List<Participant> items) {
		super(context, textViewResourceId, items);
		this.context = context;
		this.layout = textViewResourceId;
		this.items = items;
	}

	public int getCount() {
		return this.items.size();
	}

	public Participant getItem(int position) {
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
			v = vi.inflate(layout, null);

			holder = new ViewHolder();
			holder.imageParticipant = (ImageView) v
					.findViewById(R.id.participant_image);
			holder.txtParticipantName = (TextView) v
					.findViewById(R.id.participant_name);
			holder.txtParticipantFund = (TextView) v
					.findViewById(R.id.participant_fund);
			holder.imageFundParticipant = (ImageView) v
					.findViewById(R.id.participant_image_fund);
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}
		Participant item = this.items.get(position);

		holder.imageParticipant.setImageBitmap(BitmapFactory.decodeByteArray(
				item.getParticipantImageURL(), 0,
				item.getParticipantImageURL().length));
		holder.txtParticipantName.setText(item.getParticipantName());
		holder.txtParticipantFund.setText(Float.toString(item
				.getParticipantFund()) + " €");

		if (item.getParticipantFund() < 0) {
			holder.txtParticipantFund.setTextColor(context.getResources()
					.getColor(R.color.red));
			holder.imageFundParticipant.setImageResource(R.drawable.ic_down);
		} else if (item.getParticipantFund() == 0) {
			holder.txtParticipantFund.setTextColor(context.getResources()
					.getColor(R.color.black));
			holder.imageFundParticipant.setVisibility(View.INVISIBLE);
		} else {
			holder.txtParticipantFund.setTextColor(context.getResources()
					.getColor(R.color.green));
			holder.imageFundParticipant.setImageResource(R.drawable.ic_up);
		}

		return v;
	}
}
