package com.jmbg.apuestasgmv.views;

import com.jmbg.apuestasgmv.R;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AlertImageDialog extends AlertDialog {

	private String title;
	private Bitmap imageResourceId;

	private int layoutId;

	public AlertImageDialog(Context context) {
		super(context);
		layoutId = R.layout.custom_alert_image_dialog;
	}

	@Override
	public void setTitle(CharSequence title) {
		this.title = title.toString();
	}

	public void setImageBitmap(Bitmap bitmap) {
		this.imageResourceId = bitmap;
	}

	@Override
	public void show() {
		super.show();

		setContentView(layoutId);
		((ImageView) findViewById(R.id.image)).setImageBitmap(imageResourceId);
		((TextView) findViewById(R.id.title)).setText(title);
		((Button) findViewById(R.id.btn_accept)).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dismiss();
			}
		});
	}
}
