package com.jmbg.oldgloriescalendar;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;

import com.jmbg.oldgloriescalendar.data.Partido;
import com.jmbg.oldgloriescalendar.data.PartidosSQLite;


import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

public class MainActivity extends ListActivity {
	
	private PartidosSQLite partidosSQLite;
	private Vector<Partido> partidos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		this.partidosSQLite = new PartidosSQLite(this, "DBCalendar", null, 1);
		
		//Fecha actual
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(new Date());		
		
		this.partidos = this.partidosSQLite.listaPartidos(calendar);
        //this.partidos = this.partidosSQLite.listaPartidos();
        
		
		PartidosAdapter adapterScore = new PartidosAdapter(this, this.partidos);
		setListAdapter(adapterScore);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	protected void onListItemClick(ListView listView, View view, int position, long id){
		super.onListItemClick(listView, view, position, id);
		
		Partido partido = (Partido)getListAdapter().getItem(position);
		
		Intent iDetalles = new Intent(this, PartidoDetailActivity.class);
		iDetalles.putExtra("partido", partido);
		startActivity(iDetalles);
	}

}
