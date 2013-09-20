package com.example.asteroidesapp.activities;

import com.example.asteroidesapp.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class ExitActivity extends Activity {
	
	private Button cancel;
	private Button ok;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exit);
		
		this.cancel = (Button)findViewById(R.id.exit_btn_cancel);
		this.cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				lauchFinish(v,false);
			}
		});
		
		this.ok = (Button)findViewById(R.id.exit_btn_ok);
		this.ok.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				lauchFinish(v,true);
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.exit, menu);
		return true;
	}
	
	public void lauchFinish(View v, boolean exit){
		Intent intent = new Intent();
		if(exit)
			setResult(RESULT_OK,intent);
		else
			setResult(RESULT_CANCELED,intent);
		finish();
	}
}
