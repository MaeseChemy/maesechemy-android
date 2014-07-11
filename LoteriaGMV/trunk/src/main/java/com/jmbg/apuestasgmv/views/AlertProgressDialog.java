package com.jmbg.apuestasgmv.views;

import com.jmbg.apuestasgmv.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.TextView;

public class AlertProgressDialog extends ProgressDialog {

	private String title;
	private String message;
	private int layoutId;

	public AlertProgressDialog(Context context) {
		super(context);
		layoutId = R.layout.custom_progress_dialog;
	}

	@Override
	public void setTitle(CharSequence title) {
		this.title = title.toString();
	}

	@Override
	public void setMessage(CharSequence message) {
		this.message = message.toString();
	}
	
	@Override
	public void setProgressStyle(int style) {
		if (ProgressDialog.STYLE_SPINNER == style){
			layoutId = R.layout.custom_progress_dialog;
		}else if (ProgressDialog.STYLE_HORIZONTAL == style){
			layoutId = R.layout.custom_progress_dialog;
		}
	}

	@Override
	public void show() {
		super.show();
		setContentView(layoutId);
		((TextView) findViewById(R.id.title)).setText(title);
		((TextView) findViewById(R.id.message)).setText(message);
	}
	
	@Override
	public void dismiss() {
		super.dismiss();
	}
}
