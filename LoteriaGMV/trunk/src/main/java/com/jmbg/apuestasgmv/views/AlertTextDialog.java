package com.jmbg.apuestasgmv.views;

import com.jmbg.apuestasgmv.R;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AlertTextDialog extends AlertDialog {

	private String title;
	private LinearLayout layoutMessage;
	private String message;

	private int layoutId;

	public AlertTextDialog(Context context) {
		super(context);
		layoutId = R.layout.custom_alert_dialog;
	}

	@Override
	public void setTitle(CharSequence title) {
		this.title = title.toString();
	}

	@Override
	public void setMessage(CharSequence message) {
		this.message = message.toString();
	}

	public void setLayoutMessage(LinearLayout layoutMessage) {
		this.layoutMessage = layoutMessage;

	}

	@Override
	public void show() {
		super.show();

		setContentView(layoutId);
		((TextView) findViewById(R.id.title)).setText(title);
		((TextView) findViewById(R.id.message)).setText(message);

		if (layoutMessage != null) {
			((LinearLayout) findViewById(R.id.content_message))
					.addView(layoutMessage);
			((TextView) findViewById(R.id.message)).setVisibility(View.GONE);
		} 
		
		((Button) findViewById(R.id.btn_accept))
				.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						dismiss();
					}
				});

	}

}
