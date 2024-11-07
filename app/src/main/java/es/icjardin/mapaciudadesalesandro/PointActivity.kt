package es.icjardin.mapaciudadesalesandro

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import es.icjardin.mapaciudadesalesandro.databinding.ActivityPointBinding

/**
 * Clase que representa la actividad de los puntos
 */
class PointActivity : AppCompatActivity() {
    /**
     * Instancia de la clase de enlace de datos
     */
    private lateinit var binding: ActivityPointBinding

    /**
     * Instancia de la clase de base de datos
     */
    private lateinit var db: PointDatabaseHelper

    /**
     * Título del punto
     */
    private var titlePoint: String = ""

    /**
     * Función que se ejecuta al crear la actividad
     *
     * @param savedInstanceState estado de la actividad
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPointBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = PointDatabaseHelper(this)

        titlePoint = intent.getStringExtra("point").toString() // Obtener el título del punto

        // Si el título del punto es nulo, volver atras
        if (titlePoint.isBlank()) {
            finish()
        }

        val point = db.getTitlePoint(titlePoint) // Obtener el punto de la base datos

        // Relleno la ventana con la información del punto
        binding.title.text = point.title
        val imageResId = resources.getIdentifier(point.image, "raw", packageName) // Obtener el id de la imagen
        binding.image.setImageResource(imageResId) // Asigna la imagen al ImageView
        binding.text.text = point.text

        // Event listener para el botón de atras
        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}