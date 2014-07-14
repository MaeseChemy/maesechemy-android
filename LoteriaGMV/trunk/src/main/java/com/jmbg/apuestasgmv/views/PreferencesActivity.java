package com.jmbg.apuestasgmv.views;

import com.jmbg.apuestasgmv.R;
import com.jmbg.apuestasgmv.utils.LogManager;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PreferencesActivity extends PreferenceActivity {
	private LogManager logger = LogManager.getLogger(this.getClass());
	private RelativeLayout customActionBar;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		setContentView(R.layout.preferences_activity);
		initActionBar(getResources().getString(R.string.action_settings));
		enableGoHomeButton();
	}

	protected void initActionBar(String barText) {
		customActionBar = (RelativeLayout) findViewById(R.id.app_header);
		setActionBarText(barText);
	}

	protected void changeActionBarBrandImage(Drawable brandImage) {
		ImageView imageBarBrand = (ImageView) findViewById(R.id.action_bar_brand);
		imageBarBrand.setImageDrawable(brandImage);
	}

	private void setActionBarText(String barText) {
		TextView actionBarText = (TextView) customActionBar
				.findViewById(R.id.action_bar_name_window);
		actionBarText.setText(barText);
	}

	protected void enableGoHomeButton() {
		LinearLayout layoutName = (LinearLayout) customActionBar
				.findViewById(R.id.action_bar_layout_name);
		((ImageView) layoutName.findViewById(R.id.ic_back_action))
				.setVisibility(View.VISIBLE);
		layoutName.setClickable(true);
		layoutName.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finishActivity();
			}
		});

	}

	@Override
	public void onBackPressed() {
		logger.debug("onBackPressed");
		finishActivity();
	}

	private void finishActivity() {
		finish();
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}

}
