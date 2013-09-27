package com.jmbg.oldgloriescalendar.partido;

import java.text.SimpleDateFormat;
import java.util.Locale;

import com.jmbg.oldgloriescalendar.R;
import com.jmbg.oldgloriescalendar.data.Partido;


import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class PartidoDetailActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_partido_detail);
		
		Bundle extras = getIntent().getExtras();
		Partido partido = (Partido)extras.getSerializable("partido");
		
		this.setTitle(partido.getJornada());
		
		TextView textoJornada = (TextView)findViewById(R.id.fecha_textView);
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US);
		String fechaPartido = df.format(partido.getFecha());
		textoJornada.setText("Fecha: "+ fechaPartido);
		
		TextView textoCampo = (TextView)findViewById(R.id.campo_textView);
		textoCampo.setText("Campo: "+ partido.getLugar());
		
		TextView textoRival = (TextView)findViewById(R.id.rival_textView);
		textoRival.setText("Rival: "+ partido.getOponente());
		
		TextView textoLocal = (TextView)findViewById(R.id.local_textView);
		textoLocal.setText("Local: "+ (partido.isLocal()?"Si":"No"));
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.partido_detail, menu);
		return true;
	}

}
