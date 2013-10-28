package com.jmbg.oldgloriescalendar.view;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

import com.jmbg.oldgloriescalendar.R;
import com.jmbg.oldgloriescalendar.adapter.PartidosAdapter;
import com.jmbg.oldgloriescalendar.config.PreferenciasPartidoActivity;
import com.jmbg.oldgloriescalendar.dao.LigaDBSQLite;
import com.jmbg.oldgloriescalendar.dao.entities.Partido;
import com.jmbg.oldgloriescalendar.dao.entities.SaveSharedPreference;
import com.jmbg.oldgloriescalendar.util.Constantes;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

public class PartidosActivity extends ListActivity {

	private LigaDBSQLite liga;
	private Vector<Partido> partidos;
	private Vector<Partido> partidosFiltrados;

	private int tipoLocal;
	private int tipoHora;
	private int tipoCampo;
	private boolean mostrarTodosPartidos;

	private String username;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_partidos);

		username = SaveSharedPreference.getUserName(PartidosActivity.this);
		if(username.length() == 0){
			Intent iLogin = new Intent(this, LoginActivity.class);
			startActivity(iLogin);
		}
		
		Drawable background = getResources().getDrawable(
				R.drawable.ic_background);
		// setting the opacity (alpha)
		background.setAlpha(50);

		iniciarPreferencias();

		this.liga = new LigaDBSQLite(this, "DBCalendar", null);
		this.partidos = this.liga.listaPartidos();

		iniciarAdapterListView();

	}

	private void iniciarAdapterListView() {
		this.filtrarPartidos();
		PartidosAdapter adapterScore = new PartidosAdapter(this,
				this.partidosFiltrados);
		setListAdapter(adapterScore);
	}

	private void iniciarPreferencias() {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(this);
		this.tipoLocal = Integer.parseInt(pref.getString("local", "0"));
		this.tipoHora = Integer.parseInt(pref.getString("horas", "0"));
		this.tipoCampo = Integer.parseInt(pref.getString("campo", "0"));
		this.mostrarTodosPartidos = pref.getBoolean("todosPartidos", false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.partidos, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.partidos_config:
			abrirPreferencias(null);
			return true;
		default:
		}
		return false;
	}

	@Override
	protected void onListItemClick(ListView listView, View view, int position,
			long id) {
		super.onListItemClick(listView, view, position, id);

		Partido partido = (Partido) getListAdapter().getItem(position);

		Intent iDetalles = new Intent(this, PartidoDetailActivity.class);
		iDetalles.putExtra("partido", partido);
		iDetalles.putExtra("usuario", username);

		startActivity(iDetalles);
	}

	private void abrirPreferencias(View view) {
		Intent iPrefPart = new Intent(this, PreferenciasPartidoActivity.class);
		startActivityForResult(iPrefPart, Constantes.BACK_PREF);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == Constantes.BACK_PREF) {
			iniciarPreferencias();
			iniciarAdapterListView();
		}
	}

	private void filtrarPartidos() {
		this.partidosFiltrados = new Vector<Partido>();
		for (Partido partido : this.partidos) {
			boolean valido = false;

			// Comprobamos el local
			if (this.tipoLocal == Constantes.LOCAL_TODO) {
				valido = true;
			} else if (partido.isLocal()
					&& this.tipoLocal == Constantes.LOCAL_LOCAL) {
				valido = true;
			} else if (!partido.isLocal()
					&& this.tipoLocal == Constantes.LOCAL_VISITANTE) {
				valido = true;
			} else {
				valido = false;
			}

			if (valido) {
				// Comprobamos el campo
				if (this.tipoCampo == Constantes.CAMPO_TODOS) {
					valido = true;
				} else if (partido.getLugar().equals(
						Constantes.TEXTO_CAMPO_F7_1)
						&& this.tipoCampo == Constantes.CAMPO_F7_1) {
					valido = true;
				} else if (partido.getLugar().equals(
						Constantes.TEXTO_CAMPO_F7_2)
						&& this.tipoCampo == Constantes.CAMPO_F7_2) {
					valido = true;
				} else if (partido.getLugar().equals(
						Constantes.TEXTO_CAMPO_FT2_M1)
						&& this.tipoCampo == Constantes.CAMPO_FT2_M1) {
					valido = true;
				} else if (partido.getLugar().equals(
						Constantes.TEXTO_CAMPO_FT2_M2)
						&& this.tipoCampo == Constantes.CAMPO_FT2_M2) {
					valido = true;
				} else {
					valido = false;
				}
			}

			if (valido) {
				Date date = partido.getFecha();

				DateFormat df = new SimpleDateFormat("HH:mm", Locale.US);
				String hour = df.format(date);
				if (this.tipoHora == Constantes.HORA_TODAS) {
					valido = true;
				} else if (hour.equals(Constantes.TEXTO_HORA_CUATRO)
						&& this.tipoHora == Constantes.HORA_CUATRO) {
					valido = true;
				} else if (hour.equals(Constantes.TEXTO_HORA_CINCO)
						&& this.tipoHora == Constantes.HORA_CINCO) {
					valido = true;
				} else if (hour.equals(Constantes.TEXTO_HORA_SEIS)
						&& this.tipoHora == Constantes.HORA_SEIS) {
					valido = true;
				} else if (hour.equals(Constantes.TEXTO_HORA_OCHO)
						&& this.tipoHora == Constantes.HORA_OCHO) {
					valido = true;
				} else {
					valido = false;
				}
			}
			
			if (valido) {
				if(!mostrarTodosPartidos){
					Date date = partido.getFecha();
					Date fechaAct = new Date();
					
					if(fechaAct.getTime() > date.getTime()){
						valido = false;
					}else{
						valido = true;
					}
				}
			}
			if (valido)
				this.partidosFiltrados.add(partido);
		}
	}

}
