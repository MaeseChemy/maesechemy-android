package com.jmbg.oldgloriescalendar;

import java.util.Vector;

import com.jmbg.oldgloriescalendar.data.PartidosSQLite;
import com.jmbg.oldgloriescalendar.util.Constantes;
import com.jmbg.oldgloriescalendar.util.EquiposAdapter;

import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

public class EquiposActivity extends ListActivity {

	private Vector<String> equipos;
	private PartidosSQLite partidosSQLite;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_equipos);
		
		Drawable background = getResources().getDrawable(R.drawable.ic_background);
		// setting the opacity (alpha)
		background.setAlpha(50);
		
		this.partidosSQLite = new PartidosSQLite(this, "DBCalendar", null, 1);
		this.equipos = this.partidosSQLite.listaEquipos();

		EquiposAdapter adapterScore = new EquiposAdapter(this, this.equipos);
		setListAdapter(adapterScore);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.equipos, menu);
		return true;
	}

	@Override
	protected void onListItemClick(ListView listView, View view, int position, long id){
		super.onListItemClick(listView, view, position, id);
		
		String equipo = (String)getListAdapter().getItem(position);
		
		if (!equipo.equals(Constantes.equipo)){
			Intent iDetalles = new Intent(this, EquipoDetailActivity.class);
			iDetalles.putExtra("equipo", equipo);
			startActivity(iDetalles);
		}
	}
}
