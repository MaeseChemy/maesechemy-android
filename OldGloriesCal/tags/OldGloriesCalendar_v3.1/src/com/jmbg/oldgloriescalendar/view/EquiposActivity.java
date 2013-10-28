package com.jmbg.oldgloriescalendar.view;

import java.util.Vector;

import com.jmbg.oldgloriescalendar.R;
import com.jmbg.oldgloriescalendar.adapter.EquiposAdapter;
import com.jmbg.oldgloriescalendar.dao.LigaDBSQLite;
import com.jmbg.oldgloriescalendar.dao.entities.Equipo;
import com.jmbg.oldgloriescalendar.util.Constantes;

import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

public class EquiposActivity extends ListActivity {

	private Vector<Equipo> equipos;
	private LigaDBSQLite liga;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_equipos);
		
		Drawable background = getResources().getDrawable(R.drawable.ic_background);
		// setting the opacity (alpha)
		background.setAlpha(50);
		
		this.liga = new LigaDBSQLite(this, "DBCalendar", null);
		this.equipos = this.liga.listaEquipos();

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
		
		Equipo equipo = (Equipo)getListAdapter().getItem(position);
		String nombreEquipo = equipo.getNombre();
		
		if (!nombreEquipo.equals(Constantes.EQUIPO)){
			Intent iDetalles = new Intent(this, EquipoDetailActivity.class);
			iDetalles.putExtra("equipo", nombreEquipo);
			startActivity(iDetalles);
		}
	}
}
