package com.jmbg.loteriasgmv.view.adapter;

import java.util.List;

import com.jmbg.loteriasgmv.R;
import com.jmbg.loteriasgmv.dao.entities.Participant;
import com.jmbg.loteriasgmv.dao.entities.Pot;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ParticipantAdapter extends ArrayAdapter<Participant> {

	@SuppressWarnings("unused")
	private Context context;
	private int textViewResourceId;
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
		this.textViewResourceId = textViewResourceId;
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

		LayoutInflater vi = (LayoutInflater) this.getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = vi.inflate(textViewResourceId, null);

		holder = new ViewHolder();
		holder.imageParticipant = (ImageView) v
				.findViewById(R.id.participant_image);
		holder.txtParticipantName = (TextView) v
				.findViewById(R.id.participant_name);
		holder.txtParticipantFund = (TextView) v
				.findViewById(R.id.participant_fund);
		holder.imageFundParticipant = (ImageView) v
				.findViewById(R.id.participant_image_fund);

		Participant item = this.items.get(position);

		holder.imageParticipant.setImageResource(getParticipantImage(item
				.getParticipantName()));
		holder.txtParticipantName.setText(item.getParticipantName());
		holder.txtParticipantFund.setText("Fondo: " + Float.toString(item
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

	private enum ParticipantList {
		CRISTINA, SERGIO, VANESSA, ISIS, JOSEMIGUEL, MIGUELANGEL, DAVID, IVAN, PABLO, GASPAR, RODRIGO, DEFAULT
	}

	private int getParticipantImage(String name) {
		ParticipantList participant = getEnumParticipant(name);
		int idRes;
		switch (participant) {
		case CRISTINA:
			idRes = R.drawable.foto_ccrb_gmv;
			break;
		case DAVID:
			idRes = R.drawable.foto_exddcb_gmv;
			break;
		case GASPAR:
			idRes = R.drawable.foto_exgahe_gmv;
			break;
		case ISIS:
			idRes = R.drawable.foto_exipgg_gmv;
			break;
		case IVAN:
			idRes = R.drawable.foto_ircc_gmv;
			break;
		case JOSEMIGUEL:
			idRes = R.drawable.foto_jmbg_gmv;
			break;
		case MIGUELANGEL:
			idRes = R.drawable.foto_magd_gmv;
			break;
		case PABLO:
			idRes = R.drawable.foto_paam_gmv;
			break;
		case SERGIO:
			idRes = R.drawable.foto_exsspm_gmv;
			break;
		case VANESSA:
			idRes = R.drawable.foto_vabp_gmv;
			break;
		case RODRIGO:
			idRes = R.drawable.foto_exrosm_gmv;
			break;
		default:
			idRes = R.drawable.ic_contact_picture;
			break;
		}
		return idRes;
	}

	private ParticipantList getEnumParticipant(String name) {
		if (name.equals("Cristina")) {
			return ParticipantList.CRISTINA;
		} else if (name.equals("David")) {
			return ParticipantList.DAVID;
		} else if (name.equals("Isis")) {
			return ParticipantList.ISIS;
		} else if (name.equals("Ivan")) {
			return ParticipantList.IVAN;
		} else if (name.equals("Jose Miguel")) {
			return ParticipantList.JOSEMIGUEL;
		} else if (name.equals("Miguel Angel")) {
			return ParticipantList.MIGUELANGEL;
		} else if (name.equals("Pablo")) {
			return ParticipantList.PABLO;
		} else if (name.equals("Sergio")) {
			return ParticipantList.SERGIO;
		} else if (name.equals("Vanessa")) {
			return ParticipantList.VANESSA;
		} else if (name.equals("Gaspar")) {
			return ParticipantList.GASPAR;
		} else if (name.equals("Rodrigo")) {
			return ParticipantList.RODRIGO;
		} else {
			return ParticipantList.DEFAULT;
		}
	}
}
