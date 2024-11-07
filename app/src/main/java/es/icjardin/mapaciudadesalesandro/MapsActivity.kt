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
     * Lista de ciudades
     */
    val ciudades: List<Point> = listOf(
        Point(0, "Vitoria-Gasteiz", "Capital del País Vasco", 42.849998, -2.683333,"vitoria","Capital del País Vasco, conocida por su enfoque en la sostenibilidad y la calidad de vida. Destaca por su casco medieval bien conservado y el anillo verde que rodea la ciudad, ideal para el senderismo y el ciclismo."),
        Point(0, "Bilbao", "Centro económico y cultural del País Vasco", 43.263056, -2.934611,"bilbao","Centro económico y cultural del País Vasco, famosa por el Museo Guggenheim, que impulsó su transformación urbana. Es una ciudad moderna y dinámica, con una rica gastronomía y tradiciones vascas."),
        Point(0, "Madrid", "Capital de España", 40.416775, -3.703790,"madrid","Capital de España y una de las ciudades más vibrantes de Europa. Alberga una mezcla de historia y modernidad, con monumentos emblemáticos como el Palacio Real y el Museo del Prado, junto con una vida nocturna animada y diversa."),
        Point(0, "Barcelona", "Capital de Cataluña", 41.385064, 2.173403,"barcelona","Capital de Cataluña, reconocida por su arquitectura modernista, especialmente las obras de Antoni Gaudí como la Sagrada Familia. Tiene un clima mediterráneo, playas y una vida cultural activa."),
        Point(0, "Sevilla", "Capital de Andalucia", 37.382830, -5.973174,"sevilla","Capital de Andalucía, famosa por su flamenco, su rica historia y monumentos icónicos como la Giralda y la Plaza de España. Sus calles estrechas y su gastronomía la convierten en un destino encantador."),
        Point(0, "Valencia", "Ciudad costera conocida por su Ciudad de las Artes y las Ciencias", 39.466667, -0.375000,"valencia","Ciudad costera conocida por su Ciudad de las Artes y las Ciencias, un impresionante complejo arquitectónico. También es famosa por la paella y por albergar las tradicionales Fallas, una colorida festividad anual."),
        Point(0, "Zaragoza", "Capital de Aragón", 41.650002, -0.883, "zaragoza","Capital de Aragón, famosa por la Basílica del Pilar a orillas del río Ebro. Ofrece una mezcla de historia romana, musulmana y cristiana, y destaca por su gastronomía y su ambiente acogedor."),
        Point(0, "Valladolid", "Ciudad con un gran patrimonio histórico", 41.6544, -4.723611, "valladolid","Ciudad con un gran patrimonio histórico, famosa por ser un importante centro en la época de los Reyes Católicos. Sus monumentos, como la Catedral y la Plaza Mayor, reflejan su importancia histórica y cultural."),
        Point(0, "Salamanca", "Hogar de una de las universidades más antiguas de Europa", 40.966668, -5.650000, "salamanca","Hogar de una de las universidades más antiguas de Europa, la Universidad de Salamanca. Su casco histórico es Patrimonio de la Humanidad y destaca por sus edificios renacentistas, como la Plaza Mayor y la Casa de las Conchas."),
        Point(0, "Oviedo", "Capital de Asturias", 43.366667, -5.800000, "oviedo","Capital de Asturias, conocida por su casco antiguo medieval y la catedral de San Salvador. Es una ciudad limpia y ordenada, famosa por su sidra y por ser un punto importante en el Camino de Santiago.")
    )

    /**
     * Función que se ejecuta al crear la actividad
     *
     * @param savedInstanceState estado de la actividad
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = PointDatabaseHelper(this)

        db.deleteAllPoints() // Elimino todos los puntos de la tabla

        // Cargar las ciudades a la base de datos
        for (ciudad in ciudades) {
            db.insertPoint(ciudad)
        }

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