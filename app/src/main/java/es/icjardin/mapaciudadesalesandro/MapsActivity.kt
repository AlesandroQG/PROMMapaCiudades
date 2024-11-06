package es.icjardin.mapaciudadesalesandro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import es.icjardin.mapaciudadesalesandro.databinding.ActivityMapsBinding

/**
 * Clase que contiene el mapa de la actividad
 */
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private lateinit var db: PointDatabaseHelper

    /**
     * Función que se ejecuta al crear la actividad
     *
     * @param savedInstanceState estado de la actividad
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = PointDatabaseHelper(this)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Función que se ejecuta cuando el mapa está listo para ser utilizado
     *
     * @param googleMap mapa de Google
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.setMinZoomPreference(11.0f) // Set the minimum zoom level

        // Add a marker in Vitoria and move the camera
        val vitoria = LatLng(42.849998,-2.683333)
        mMap.addMarker(MarkerOptions()
            .position(vitoria)
            .title("Marker in Vitoria")
            .snippet("Too far away"))?.showInfoWindow()
        mMap.moveCamera(CameraUpdateFactory.newLatLng(vitoria))

        // Add a listener for marker clicks
        mMap.setOnMarkerClickListener { marker ->
            Toast.makeText(this@MapsActivity, "MARKER CLICKED", Toast.LENGTH_SHORT).show() // Display a toast message
            true // Return true to consume the event
        }
    }
}