package com.jmbg.oldgloriescalendar.view;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Vector;

import com.jmbg.oldgloriescalendar.R;
import com.jmbg.oldgloriescalendar.dao.LigaDBSQLite;
import com.jmbg.oldgloriescalendar.dao.entities.Partido;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class EquipoDetailActivity extends Activity {

	private LigaDBSQLite liga;
	private Vector<Partido> partidos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_equipo_detail);

		Bundle extras = getIntent().getExtras();
		String equipo = (String) extras.getSerializable("equipo");

		this.setTitle(equipo);

		this.liga = new LigaDBSQLite(this, "DBCalendar", null);
		// Fecha actual
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(new Date());
		this.partidos = this.liga.listaPartidosEquipo(equipo);

		TabHost tabHost = (TabHost) findViewById(R.id.equipo_detail_tabhost);
		tabHost.setup();

		TabSpec spec1 = tabHost.newTabSpec("Ida");
		spec1.setContent(R.id.tab_ida);
		spec1.setIndicator("Ida");

		TabSpec spec2 = tabHost.newTabSpec("Vuelta");
		spec2.setIndicator("Vuelta");
		spec2.setContent(R.id.tab_vuelta);

		tabHost.addTab(spec1);
		tabHost.addTab(spec2);

		int i = 0;
		for (Partido partido : this.partidos) {
			// Seteamos los campos
			if (i == 0) {
				TextView textoRival = (TextView) findViewById(R.id.ida_jornada_textview);
				textoRival.setText(partido.getJornada());

				TextView textoJornada = (TextView) findViewById(R.id.ida_fecha_textView);
				SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US);
				String fechaPartido = df.format(partido.getFecha());
				textoJornada.setText("Fecha: " + fechaPartido);

				TextView textoCampo = (TextView) findViewById(R.id.ida_campo_textView);
				textoCampo.setText("Campo: " + partido.getLugar());

				TextView textoLocal = (TextView) findViewById(R.id.ida_local_textView);
				textoLocal.setText("Local: "
						+ (partido.isLocal() ? "Si" : "No"));
			} else {
				TextView textoRival = (TextView) findViewById(R.id.vuelta_jornada_textview);
				textoRival.setText(partido.getJornada());

				TextView textoJornada = (TextView) findViewById(R.id.vuelta_fecha_textView);
				SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US);
				String fechaPartido = df.format(partido.getFecha());
				textoJornada.setText("Fecha: " + fechaPartido);

				TextView textoCampo = (TextView) findViewById(R.id.vuelta_campo_textView);
				textoCampo.setText("Campo: " + partido.getLugar());

				TextView textoLocal = (TextView) findViewById(R.id.vuelta_local_textView);
				textoLocal.setText("Local: "
						+ (partido.isLocal() ? "Si" : "No"));
			}
			i++;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.equipo_detail, menu);
		return true;
	}

}
