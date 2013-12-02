package com.jmbg.oldgloriescalendar.view;
import java.util.Vector;

import com.jmbg.oldgloriescalendar.R;
import com.jmbg.oldgloriescalendar.adapter.PlantillaAdapter;
import com.jmbg.oldgloriescalendar.dao.LigaDBSQLite;
import com.jmbg.oldgloriescalendar.dao.entities.Jugador;
import com.jmbg.oldgloriescalendar.util.Constantes;
import com.jmbg.oldgloriescalendar.view.PullToRefreshListView.OnRefreshListener;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.app.ListActivity;

public class PlantillaActivity extends ListActivity {

	private Vector<Jugador> plantilla;
	private LigaDBSQLite liga;

	private PullToRefreshListView lv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_plantilla);
		// Show the Up button in the action bar.
		
		this.liga = new LigaDBSQLite(this, "DBCalendar", null, true);
		lv = (PullToRefreshListView) getListView();
		lv.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				Log.d(Constantes.TAG, "["+PlantillaActivity.class.getName()+".setOnRefreshListener] Main Refresh");
				liga.actualizarPlantilla();
				lv.postDelayed(new Runnable() {
					@Override
					public void run() {
						lv.onRefreshComplete();
					}
				}, 2000);
			}
		});
		refrescarJugadores();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.plantilla, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d(Constantes.TAG, "["+PlantillaActivity.class.getName()+".onOptionsItemSelected] Opcion seleccionada en el menu");
		switch (item.getItemId()) {
		case R.id.main_refresh:
			Log.d(Constantes.TAG, "["+PlantillaActivity.class.getName()+".onOptionsItemSelected] Main Refresh");
			this.liga.actualizarPlantilla();
			return true;
		default:
		}
		return false;
	}
	
	public void refrescarJugadores(){
		this.plantilla = this.liga.listaJugadores();
		PlantillaAdapter adapterPlantilla = new PlantillaAdapter(PlantillaActivity.this, plantilla);
		setListAdapter(adapterPlantilla);
	}
}
