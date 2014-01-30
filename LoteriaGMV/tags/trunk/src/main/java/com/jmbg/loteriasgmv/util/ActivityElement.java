package com.jmbg.loteriasgmv.util;

import android.content.Intent;

public abstract class ActivityElement {

	private int activityIconId;
	private String activityName;

	private boolean visibility;

	public ActivityElement(int activityIconId, String activityName) {
		super();
		this.activityIconId = activityIconId;
		this.activityName = activityName;
		this.visibility = true;
	}

	public int getActivityIcon() {
		return activityIconId;
	}

	public void setActivityIcon(int activityIconId) {
		this.activityIconId = activityIconId;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public boolean isVisibility() {
		return visibility;
	}

	public void setVisibility(boolean visibility) {
		this.visibility = visibility;
	}
	
	public abstract Intent getIntent();
}
