package com.jmbg.oldgloriescalendar.view;

import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jmbg.oldgloriescalendar.R;
import com.jmbg.oldgloriescalendar.config.PreferenciasMapaActivity;
import com.jmbg.oldgloriescalendar.util.Constantes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * This demo shows how GMS Location can be used to check for changes to the
 * users location. The "My Location" button uses GMS Location to set the blue
 * dot representing the users location. To track changes to the users location
 * on the map, we request updates from the {@link LocationClient}.
 */
public class MapaActivity extends FragmentActivity {

	private int tipoCapa;
	private GoogleMap mMap;
	public Marker marker;

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
				this.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
						Constantes.CANTERA, 15));
				this.mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());
				// Habilitamos nuestra posicion
				this.mMap.setMyLocationEnabled(true);
				// Situamos el polideportivo
				this.marker = this.mMap.addMarker(new MarkerOptions()
						.position(Constantes.CANTERA)
						.title(Constantes.NOMBRE_POLIDEPORTIVO)
						.snippet(Constantes.DIRECCION_POLIDEPORTIVO)
						.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.ic_map_pin))
						.anchor(0.5f, 0.5f));
			}
		}
	}

	private void iniciarPreferencias() {
		if (mMap != null) {
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

	public void mostrarRuta(View view) {
		// Intent intent = new Intent(Intent.ACTION_VIEW,
		// Uri.parse(generarUrl()));
		// intent.setComponent(new
		// ComponentName("com.google.android.apps.maps","com.google.android.maps.MapsActivity"));

		Intent intent = new Intent(Intent.ACTION_VIEW,
				Uri.parse(generarUrl(Constantes.MAP_URI_GENERIC)));
		startActivity(intent);
	}
	
	public void centrarCampo(View view) {
		this.mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
				Constantes.CANTERA, 15));
	}

	private String generarUrl(int tipoUri) {
		String res = "";
		switch (tipoUri) {
		case Constantes.MAP_URI_GENERIC:
			res = "geo:0,0?q=" + Constantes.CANTERA.latitude + ","
					+ Constantes.CANTERA.longitude;
			break;
		case Constantes.MAP_URI_GOOGLE_MAPS:
			res = "http://maps.google.com/maps?f=d&daddr="
					+ Constantes.CANTERA.latitude + ","
					+ Constantes.CANTERA.longitude;
			break;
		default:
			break;
		}
		return res;
	}

	private class CustomInfoWindowAdapter implements InfoWindowAdapter {

		private View view;

		public CustomInfoWindowAdapter() {
			view = getLayoutInflater().inflate(R.layout.custom_info_window,
					null);
		}

		@Override
		public View getInfoContents(Marker marker) {

			if (MapaActivity.this.marker != null
					&& MapaActivity.this.marker.isInfoWindowShown()) {
				MapaActivity.this.marker.hideInfoWindow();
				MapaActivity.this.marker.showInfoWindow();
			}
			return null;
		}

		@Override
		public View getInfoWindow(final Marker marker) {
			MapaActivity.this.marker = marker;

			final ImageView image = ((ImageView) view
					.findViewById(R.id.custom_window_badge));
			image.setImageResource(R.drawable.ic_campo);

			final String title = marker.getTitle();
			final TextView titleUi = ((TextView) view
					.findViewById(R.id.custom_window_title));
			if (title != null) {
				titleUi.setText(title);
			} else {
				titleUi.setText("");
			}

			final String snippet = marker.getSnippet();
			final TextView snippetUi = ((TextView) view
					.findViewById(R.id.custom_window_subtitle));
			if (snippet != null) {
				snippetUi.setText(snippet);
			} else {
				snippetUi.setText("");
			}

			return view;
		}
	}

}
