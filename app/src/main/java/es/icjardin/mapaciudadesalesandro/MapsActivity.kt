package es.icjardin.mapaciudadesalesandro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE // Tipo del mapa

        // Cargar los puntos
        val points: List<Point> = db.getAllPoints() // Obtiene todos los puntos de la base de datos
        for (point in points) {
            val latLng = LatLng(point.lat, point.lon) // Coordenadas de la ciudad
            mMap.addMarker(MarkerOptions().position(latLng).title(point.title).snippet(point.description)) // Añade un marcador
        }

        // Centrar el mapa en centro de España
        val centro = LatLng(40.416775, -3.703790)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centro, 5.5f))

        // Event listener para el click de los marcadores
        mMap.setOnMarkerClickListener { marker ->
            marker.showInfoWindow() // Mostrar la ventana de información
            true // Return true to consume the event
        }

        // Event listener para cuando se pulsa la ventana de información de los marcadores
        mMap.setOnInfoWindowClickListener { marker ->
            val intent = Intent(this, PointActivity::class.java) // Iniciar la actividad
            intent.putExtra("point", marker.title) // Pasar el título del marcador
            startActivity(intent) // Lanzar la actividad
        }
    }
}