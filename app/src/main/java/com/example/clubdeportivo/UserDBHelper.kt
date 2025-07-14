package com.example.clubdeportivo

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.icu.util.Calendar
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Locale

class UserDBHelper(context: Context) : SQLiteOpenHelper(context, "UsersDB", null, 25) {

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

        db.execSQL("""INSERT INTO users (name, surname, email, password)
            VALUES ("Super", "Usuario", "admin", "1234")""".trimMargin())

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS members (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT,
                surname TEXT,
                document TEXT UNIQUE,
                dateOfBirth TEXT,
                gender TEXT,
                email TEXT,
                apto INTEGER,
                pay INTEGER,
                expirationDate TEXT,
                paymentMethod TEXT
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
        db.execSQL("DROP TABLE IF EXISTS members")
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

    fun login(email: String, pass: String): String? {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM users WHERE email=? AND password=?",
            arrayOf(email, pass)
        )
        var userName: String? = null
        if (cursor.moveToFirst()) {
            userName = cursor.getString(cursor.getColumnIndexOrThrow("name"))
        }
        cursor.close()
        db.close()
        return userName
    }

    fun getAllMembers(): List<Member> {
        val list = mutableListOf<Member>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM members", null)

        while (cursor.moveToNext()) {
            list.add(
                Member(
                    id = cursor.getInt(0),
                    name = cursor.getString(1),
                    surname = cursor.getString(2),
                    document = cursor.getString(3),
                    dateOfBirth = cursor.getString(4),
                    gender = cursor.getString(5),
                    email = cursor.getString(6),
                    apto = cursor.getInt(7) == 1,
                    pay = cursor.getInt(8) == 1,
                    expirationDate = cursor.getString(9),
                    paymentMethod = cursor.getString(10)
                )
            )
        }
        cursor.close()
        return list
    }

    fun getMemberByDocument(document: String): Member? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM members WHERE document = ?", arrayOf(document))

        if (cursor.moveToFirst()) {
            val member = Member(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                name = cursor.getString(cursor.getColumnIndexOrThrow("name")),
                surname = cursor.getString(cursor.getColumnIndexOrThrow("surname")),
                document = cursor.getString(cursor.getColumnIndexOrThrow("document")),
                dateOfBirth = cursor.getString(cursor.getColumnIndexOrThrow("dateOfBirth")),
                gender = cursor.getString(cursor.getColumnIndexOrThrow("gender")),
                email = cursor.getString(cursor.getColumnIndexOrThrow("email")),
                apto = cursor.getInt(cursor.getColumnIndexOrThrow("apto")) == 1,
                pay = cursor.getInt(cursor.getColumnIndexOrThrow("pay")) == 1,
                expirationDate = cursor.getString(cursor.getColumnIndexOrThrow("expirationDate")),
                paymentMethod = cursor.getString(cursor.getColumnIndexOrThrow("paymentMethod"))
            )
            cursor.close()
            return member
        }
        cursor.close()
        return null
    }

    fun addMember(
        name: String,
        surname: String,
        document: String,
        dateOfBirth: String,
        gender: String,
        email: String,
        apto: Boolean,
        pay: Boolean,
    ): Boolean {
        val db = writableDatabase
        return try {
            val expirationDate = if (pay) {
                val calendar = Calendar.getInstance()
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                //val expirationDate = dateFormat.format(calendar.time)
                calendar.add(Calendar.MONTH, 1)
                dateFormat.format(calendar.time)
            } else {
                ""
            }
                db.execSQL(
                    """
                INSERT INTO members 
                (name, surname, document, dateOfBirth, gender, email, apto, pay, expirationDate, paymentMethod)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """.trimIndent(),
                    arrayOf(
                        name,
                        surname,
                        document,
                        dateOfBirth,
                        gender,
                        email,
                        if (apto) 1 else 0,
                        if (pay) 1 else 0,
                        expirationDate ?: "",
                        //expirationDate,
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

    fun updatePayment(document: String, newExpiration: String, paymentMethod: String): Boolean {
        return try {
            val db = writableDatabase
            db.execSQL(
                "UPDATE members SET pay = 1, expirationDate = ?, paymentMethod = ? WHERE document = ?",
                arrayOf(newExpiration, paymentMethod, document)
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
