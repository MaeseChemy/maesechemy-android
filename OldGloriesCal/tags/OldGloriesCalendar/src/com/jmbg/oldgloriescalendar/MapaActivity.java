package com.jmbg.oldgloriescalendar;

import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jmbg.oldgloriescalendar.util.Constantes;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * This demo shows how GMS Location can be used to check for changes to the
 * users location. The "My Location" button uses GMS Location to set the blue
 * dot representing the users location. To track changes to the users location
 * on the map, we request updates from the {@link LocationClient}.
 */
public class MapaActivity extends FragmentActivity {

	private int tipoCapa;
	private GoogleMap mMap;
	private static final LatLng CANTERA = new LatLng(40.339661, -3.762539);

	// These settings are the same as the settings for the map. They will in
	// fact give you updates
	// at the maximal rates currently possible.

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mapa);

		setUpMapIfNeeded();
		iniciarPreferencias();
	}

	private void setUpMapIfNeeded() {
		if (mMap == null) {
			SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map);
			mMap = fm.getMap();
			if (mMap != null) {
				this.mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
				this.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CANTERA,
						15));

				// Habilitamos nuestra posicion
				this.mMap.setMyLocationEnabled(true);
				// Situamos el polideportivo
				this.mMap.addMarker(new MarkerOptions().position(CANTERA)
						.title("Polideportivo: La Cantera")
						.snippet("Calle Arquitectura, Leganes")
						.anchor(0.5f, 0.5f));
			}
		}
	}

	private void iniciarPreferencias() {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(this);
		this.tipoCapa = Integer.parseInt(pref.getString("layer", "1"));

		switch (this.tipoCapa) {
		case 0:
			this.mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			break;
		case 1:
			this.mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			break;
		case 2:
			this.mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
			break;
		case 3:
			this.mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
			break;
		case 4:
			this.mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mapa, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.mapa_config:
			abrirPreferencias(null);
			return true;
		default:
		}
		return false;
	}

	private void abrirPreferencias(View view) {
		Intent iPrefPart = new Intent(this, PreferenciasMapaActivity.class);
		startActivityForResult(iPrefPart, Constantes.BACK_PREF_MAP);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == Constantes.BACK_PREF_MAP) {
			iniciarPreferencias();
		}
	}

	/**
	 * Button to get current Location. This demonstrates how to get the current
	 * Location as required without needing to register a LocationListener.
	 */
	public void mostrarRuta(View view) {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(generarUrl()));
		intent.setComponent(new ComponentName("com.google.android.apps.maps",
				"com.google.android.maps.MapsActivity"));
		startActivity(intent);
	}

	private String generarUrl() {
		String res = "http://maps.google.com/maps?f=d&daddr=";
		res += CANTERA.latitude;
		res += ",";
		res += CANTERA.longitude;
		return res;
	}

}
