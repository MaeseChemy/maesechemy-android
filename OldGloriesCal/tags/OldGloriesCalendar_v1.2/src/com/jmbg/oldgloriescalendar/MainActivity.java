package com.jmbg.oldgloriescalendar;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Drawable background = getResources().getDrawable(R.drawable.ic_background);

		// setting the opacity (alpha)
		background.setAlpha(50);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void abrirPartidos(View view){
		Intent iPartidos = new Intent(this, PartidosActivity.class);
		startActivity(iPartidos);
	}
	
	public void abrirEquipos(View view){
		Intent iEquipos = new Intent(this, EquiposActivity.class);
		startActivity(iEquipos);
	}

	public void mostrarComoLlegar(View view){
		Intent iMap = new Intent(this, MapaActivity.class);
		startActivity(iMap);
	}
	
}
