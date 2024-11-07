package es.icjardin.mapaciudadesalesandro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

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

    val cities: List<Point> = listOf(
        Point(0, "Vitoria", "Vitoria", 42.849998, -2.683333,"vitoria.jpg","Vitoria"),
        Point(0, "Bilbao", "Bilbao", 43.263056, -2.934611,"bilbao.jpg",""),
        Point(0, "Madrid", "Madrid", 40.416775, -3.703790,"madrid.jpg",""),
        Point(0, "Barcelona", "Barcelona", 41.385064, 2.173403,"barcelona.jpg",""),
        Point(0, "Sevilla", "Sevilla", 37.382830, -5.973174,"sevilla.jpg",""),
        Point(0, "Valencia", "Valencia", 39.466667, -0.375000,"valencia.jpg",""),
        Point(0, "Zaragoza", "Zaragoza", 41.650002, -0.883, "zaragoza.jpg",""),
        Point(0, "Valladolid", "Valladolid", 41.650002, -0.883, "valladolid.jpg",""),
        Point(0, "Salamanca", "Salamanca", 40.966668, -5.650000, "salamanca.jpg",""),
        Point(0, "Oviedo", "Oviedo", 43.366667, -5.800000, "oviedo.jpg","")
    )

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

        /*for (city in cities) {
            db.insertPoint(city)
        }*/
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
            true // Return true to consume the event
        }
        mMap.setOnInfoWindowClickListener { marker ->
            val intent = Intent(this, PointActivity::class.java)
            intent.putExtra("point", marker.title)
            startActivity(intent)
        }
    }
}