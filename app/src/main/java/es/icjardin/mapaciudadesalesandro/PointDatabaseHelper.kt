package es.icjardin.mapaciudadesalesandro

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

/**
 * Clase con funciones para interactuar con la base de datos de SQLite
 */
class PointDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "points.db";
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "points"
        // Columnas de la tabla
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_LAT = "lat"
        private const val COLUMN_LON = "lon"
        private const val COLUMN_IMAGE = "image"
        private const val COLUMN_TEXT = "text"
    }

    /**
     * Lista de ciudades
     */
    private val ciudades: List<Point> = listOf(
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
     * Función que crea la tabla
     *
     * @param db base de datos
     */
    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery =
            "CREATE TABLE $TABLE_NAME (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY, " +
                    "$COLUMN_TITLE TEXT UNIQUE, " +
                    "$COLUMN_DESCRIPTION TEXT, " +
                    "$COLUMN_LAT DOUBLE, " +
                    "$COLUMN_LON DOUBLE, " +
                    "$COLUMN_IMAGE TEXT, " +
                    "$COLUMN_TEXT TEXT)"
        db?.execSQL(createTableQuery)
        cargarCiudades() // Cargar ciudades
    }

    /**
     * Función que modífica la base de datos
     *
     * @param db base de datos
     * @param oldVersion versión anterior
     * @param newVersion versión nueva
     */
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery =
            "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }

    /**
     * Carga las ciudades en la base de datos
     */
    private fun cargarCiudades() {
        Log.d("cargarCiudades", "CARGANDO")
        for (ciudad in ciudades) {
            insertPoint(ciudad)
        }
    }

    /**
     * Pasado un objeto point, la añade a la base de datos
     *
     * @param point point a añadir
     */
    fun insertPoint(point: Point) {
        //la abro en modo escritura
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, point.title)
            put(COLUMN_DESCRIPTION, point.description)
            put(COLUMN_LAT, point.lat)
            put(COLUMN_LON, point.lon)
            put(COLUMN_IMAGE, point.image)
            put(COLUMN_TEXT, point.text)
            //en este caso ID es autonumérico
        }
        //insertamos datos
        db.insert(TABLE_NAME, null, values)
        //y ahora a cerrar
        db.close()
    }

    /**
     * Lee la base de datos y rellena una List de tipo Point
     *
     * @return Una lista de tipo Point
     */
    fun getAllPoints(): List<Point> {
        //creo una lista mutable para poder cambiar cosas
        val pointList = mutableListOf<Point>()
        //la abro en modo lectura
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        //lanza un cursor
        val cursor = db.rawQuery(query, null)
        //itera mientras que exista otro
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
            val description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
            val lat = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LAT))
            val lon = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LON))
            val image = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE))
            val text = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEXT))
            //creamos un objeto temporal de tipo Task
            val point = Point(id, title, description, lat, lon, image, text)
            //añadimos la task
            pointList.add(point)
        }
        //cerrar las conexiones
        cursor.close()
        db.close()
        return pointList
    }

    /**
     * Recibe un string con el título del punto, y devuelve el punto correspondiente de la base de datos
     *
     * @param titlePoint título del punto
     * @return point
     */
    fun getTitlePoint(titlePoint: String): Point {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_TITLE='$titlePoint'"
        //lanza un cursor
        val cursor = db.rawQuery(query, null)
        //ve al primer registro que cumpla esa condicion (esperemos que el único)
        cursor.moveToFirst()
        //leo los datos de la consulta
        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
        val description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
        val lat = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LAT))
        val lon = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LON))
        val image = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE))
        val text = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEXT))
        //cierro conexiones y devuelvo la task
        cursor.close()
        db.close()
        return Point(id, title, description, lat, lon, image, text)
    }

}