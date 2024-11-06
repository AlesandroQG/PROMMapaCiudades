package es.icjardin.mapaciudadesalesandro

import android.content.Intent
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
    /**
     * Mapa de Google
     */
    private lateinit var mMap: GoogleMap

    /**
     * Binding de la actividad
     */
    private lateinit var binding: ActivityMapsBinding

    /**
     * Base de datos de la actividad
     */
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

        db.insertPoint(Point(0, "Vitoria", "Vitoria", 42.849998, -2.683333,"vitoria.jpg","Vitoria"))

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

        // Cargar los puntos
        val points: List<Point> = db.getAllPoints()
        for (point in points) {
            val latLng = LatLng(point.lat, point.lon)
            mMap.addMarker(MarkerOptions().position(latLng).title(point.title).snippet(point.description))
        }

        // Add a listener for marker clicks
        mMap.setOnMarkerClickListener { marker ->
            marker.showInfoWindow()
            //Toast.makeText(this, "MARKER CLICKED", Toast.LENGTH_SHORT).show() // Display a toast message
            true // Return true to consume the event
        }
        mMap.setOnInfoWindowClickListener { marker ->
            val intent = Intent(this, PointActivity::class.java)
            intent.putExtra("point", marker.id)
            startActivity(intent)
        }
    }
}