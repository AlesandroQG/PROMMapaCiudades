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
     * Id del punto
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

        titlePoint = intent.getStringExtra("point").toString()

        if (titlePoint.isBlank()) {
            finish()
        }

        val point = db.getTitlePoint(titlePoint)
        binding.title.text = point.title
        val imageResId = resources.getIdentifier(point.image, "raw", packageName)
        binding.image.setImageResource(imageResId)
        binding.text.text = point.text

        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}