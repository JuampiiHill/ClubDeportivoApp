package com.example.clubdeportivo

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi

class UserDBHelper(context: Context) : SQLiteOpenHelper(context, "UsersDB", null, 9) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT,
                surname TEXT,
                email TEXT UNIQUE,
                password TEXT
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS socios (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT,
                apellido TEXT,
                documento TEXT UNIQUE,
                nacimiento TEXT,
                genero TEXT,
                email TEXT,
                apto INTEGER,
                pago INTEGER,
                vencimiento TEXT,
                metodo_pago TEXT
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS actividades (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT,
                dia TEXT,
                horario TEXT,
                cupo_disponible INTEGER
            )
        """.trimIndent())

            db.execSQL("""
            INSERT INTO actividades (nombre, dia, horario, cupo_disponible) VALUES
            ('Fútbol', 'Lunes', '18:00', 10),
            ('Natación', 'Martes', '16:00', 8),
            ('Gimnasia', 'Miércoles', '19:00', 15),
            ('Básquet', 'Jueves', '17:00', 12),
            ('Yoga', 'Viernes', '10:00', 10),
            ('Musculacion', 'Lunes a Viernes', '7:00 a 22:00', 0)
    """.trimIndent())

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS inscripciones (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                id_socio INTEGER,
                id_actividad INTEGER,
                FOREIGN KEY(id_socio) REFERENCES socios(id),
                FOREIGN KEY(id_actividad) REFERENCES actividades(id)
            )
        """.trimIndent())


        Log.d("DB", "Tablas creadas con éxito")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS users")
        db.execSQL("DROP TABLE IF EXISTS socios")
        db.execSQL("DROP TABLE IF EXISTS actividades")
        db.execSQL("DROP TABLE IF EXISTS inscripciones")

        onCreate(db)
    }

    fun register(name: String, surname: String, email: String, password: String): Boolean {
        val db = writableDatabase
        return try {
            db.execSQL(
                "INSERT INTO users (name, surname, email, password) VALUES (?, ?, ?, ?)",
                arrayOf(name, surname, email, password)
            )
            true
        } catch (ex: Exception) {
            Log.e("DB_REGISTER_ERROR", "Error al registrar usuario", ex)
            false
        } finally {
            db.close()
        }
    }

    fun login(email: String, pass: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM users WHERE email=? AND password=?",
            arrayOf(email, pass)
        )
        val exist = cursor.count > 0
        cursor.close()
        db.close()
        return exist
    }

    fun getAllSocios(): List<Socio> {
        val lista = mutableListOf<Socio>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM socios", null)

        while (cursor.moveToNext()) {
            lista.add(
                Socio(
                    id = cursor.getInt(0),
                    nombre = cursor.getString(1),
                    apellido = cursor.getString(2),
                    documento = cursor.getString(3),
                    nacimiento = cursor.getString(4),
                    genero = cursor.getString(5),
                    email = cursor.getString(6),
                    apto = cursor.getInt(7) == 1,
                    pago = cursor.getInt(8) == 1,
                    vencimiento = cursor.getString(9),
                    metodoPago = cursor.getString(10)
                )
            )
        }

        cursor.close()
        return lista
    }

    fun getSocioPorDocumento(documento: String): Socio? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM socios WHERE documento = ?", arrayOf(documento))

        if (cursor.moveToFirst()) {
            val socio = Socio(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                apellido = cursor.getString(cursor.getColumnIndexOrThrow("apellido")),
                documento = cursor.getString(cursor.getColumnIndexOrThrow("documento")),
                nacimiento = cursor.getString(cursor.getColumnIndexOrThrow("nacimiento")),
                genero = cursor.getString(cursor.getColumnIndexOrThrow("genero")),
                email = cursor.getString(cursor.getColumnIndexOrThrow("email")),
                apto = cursor.getInt(cursor.getColumnIndexOrThrow("apto")) == 1,
                pago = cursor.getInt(cursor.getColumnIndexOrThrow("pago")) == 1,
                vencimiento = cursor.getString(cursor.getColumnIndexOrThrow("vencimiento")),
                metodoPago = cursor.getString(cursor.getColumnIndexOrThrow("metodo_pago"))
            )
            cursor.close()
            return socio
        }

        cursor.close()
        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun insertarSocio(
        nombre: String,
        apellido: String,
        documento: String,
        nacimiento: String,
        genero: String,
        email: String,
        apto: Boolean,
        pago: Boolean
    ): Boolean {
        val db = writableDatabase
        return try {
            val hoy = java.time.LocalDate.now()
            val vencimiento = hoy.plusDays(30).toString()

            db.execSQL(
                """
                INSERT INTO socios 
                (nombre, apellido, documento, nacimiento, genero, email, apto, pago, vencimiento, metodo_pago)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """.trimIndent(),
                arrayOf(
                    nombre,
                    apellido,
                    documento,
                    nacimiento,
                    genero,
                    email,
                    if (apto) 1 else 0,
                    if (pago) 1 else 0,
                    vencimiento,
                    ""
                )
            )
            true
        } catch (ex: Exception) {
            Log.e("DB_SOCIO_INSERT", "Error al insertar socio", ex)
            false
        } finally {
            db.close()
        }
    }

    fun actualizarPago(documento: String, nuevoVencimiento: String, metodoPago: String): Boolean {
        return try {
            val db = writableDatabase
            db.execSQL(
                "UPDATE socios SET pago = 1, vencimiento = ?, metodo_pago = ? WHERE documento = ?",
                arrayOf(nuevoVencimiento, metodoPago, documento)
            )
            db.close()
            true
        } catch (e: Exception) {
            Log.e("DB_UPDATE", "Error al actualizar pago", e)
            false
        }
    }

    fun getActividadPorNombre(nombre: String): Actividad? {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM actividades WHERE LOWER(nombre) = LOWER(?)",
            arrayOf(nombre)
        )
        var actividad: Actividad? = null

        if (cursor.moveToFirst()) {
            actividad = Actividad(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                dia = cursor.getString(cursor.getColumnIndexOrThrow("dia")),
                horario = cursor.getString(cursor.getColumnIndexOrThrow("horario")),
                cupoDisponible = cursor.getInt(cursor.getColumnIndexOrThrow("cupo_disponible"))
            )
        }

        cursor.close()
        db.close()
        return actividad
    }

    fun insertarActividad(nombre: String, dia: String, horario: String, cupo: Int): Boolean {
        val db = writableDatabase
        return try {
            db.execSQL(
                "INSERT INTO actividades (nombre, dia, horario, cupo_disponible) VALUES (?, ?, ?, ?)",
                arrayOf(nombre, dia, horario, cupo)
            )
            true
        } catch (e: Exception) {
            Log.e("DB", "Error al insertar actividad", e)
            false
        } finally {
            db.close()
        }
    }

    fun eliminarActividadPorNombre(nombre: String): Boolean {
        val db = writableDatabase
        return try {
            db.execSQL("DELETE FROM actividades WHERE LOWER(nombre) = LOWER(?)", arrayOf(nombre))
            true
        } catch (e: Exception) {
            Log.e("DB", "Error al eliminar actividad", e)
            false
        } finally {
            db.close()
        }
    }

    fun getAllActividades(): List<Actividad> {
        val lista = mutableListOf<Actividad>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM actividades", null)

        while (cursor.moveToNext()) {
            lista.add(
                Actividad(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                    dia = cursor.getString(cursor.getColumnIndexOrThrow("dia")),
                    horario = cursor.getString(cursor.getColumnIndexOrThrow("horario")),
                    cupoDisponible = cursor.getInt(cursor.getColumnIndexOrThrow("cupo_disponible"))
                )
            )
        }

        cursor.close()
        return lista
    }

    fun getActividadesDeSocio(dni: String): List<String> {
        val lista = mutableListOf<String>()
        val db = readableDatabase

        val cursor = db.rawQuery(
            """
        SELECT a.nombre FROM actividades a
        INNER JOIN inscripciones i ON a.id = i.id_actividad
        INNER JOIN socios s ON s.id = i.id_socio
        WHERE s.documento = ?
        """.trimIndent(),
            arrayOf(dni)
        )

        Log.d("DEBUG", "Cantidad de actividades: ${lista.size}")

        while (cursor.moveToNext()) {
            val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
            lista.add(nombre)
        }

        cursor.close()
        return lista
    }

    fun asignarActividad(idSocio: Int, idActividad: Int, documento: String, nombreActividad: String): Boolean {
        val db = writableDatabase
        return try {
            db.beginTransaction()

            db.execSQL(
                "INSERT INTO inscripciones (id_socio, id_actividad) VALUES (?, ?)",
                arrayOf(idSocio, idActividad)
            )

            db.execSQL(
                "UPDATE actividades SET cupo_disponible = cupo_disponible - 1 WHERE id = ?",
                arrayOf(idActividad)
            )

            db.setTransactionSuccessful()
            true
        } catch (e: Exception) {
            Log.e("DB", "Error al asignar actividad", e)
            false
        } finally {
            db.endTransaction()
            db.close()
        }
    }
}
