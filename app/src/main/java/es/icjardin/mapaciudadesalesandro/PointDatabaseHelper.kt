package es.icjardin.mapaciudadesalesandro

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

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
     * Función que crea la tabla
     *
     * @param db base de datos
     */
    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery =
            "CREATE TABLE $TABLE_NAME (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY, " +
                    "$COLUMN_TITLE TEXT, " +
                    "$COLUMN_DESCRIPTION TEXT, " +
                    "$COLUMN_LAT DOUBLE, " +
                    "$COLUMN_LON DOUBLE, " +
                    "$COLUMN_IMAGE TEXT, " +
                    "$COLUMN_TEXT TEXT)"
        db?.execSQL(createTableQuery)
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
     * Recibe un integer que es su posición en la lista, con esto recuperara los datos del point
     *
     * @param idPoint id del punto
     * @return point
     */
    fun getIdPoint(idPoint: Int): Point {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID=$idPoint"
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