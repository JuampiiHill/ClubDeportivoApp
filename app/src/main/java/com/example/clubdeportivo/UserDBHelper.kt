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

class UserDBHelper(context: Context) : SQLiteOpenHelper(context, "UsersDB", null, 26) {

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
            CREATE TABLE IF NOT EXISTS activities (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT,
                day TEXT,
                schedule TEXT,
                availableSpace INTEGER
            )
        """.trimIndent())

            db.execSQL("""
            INSERT INTO activities (name, day, schedule, availableSpace) VALUES
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
        db.execSQL("DROP TABLE IF EXISTS activities")
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

    fun getActivityByName(name: String): Activity? {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM activities WHERE LOWER(name) = LOWER(?)",
            arrayOf(name)
        )
        var activity: Activity? = null

        if (cursor.moveToFirst()) {
            activity = Activity(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                name = cursor.getString(cursor.getColumnIndexOrThrow("name")),
                day = cursor.getString(cursor.getColumnIndexOrThrow("day")),
                schedule = cursor.getString(cursor.getColumnIndexOrThrow("schedule")),
                availableSpace = cursor.getInt(cursor.getColumnIndexOrThrow("availableSpace"))
            )
        }

        cursor.close()
        db.close()
        return activity
    }

    fun addActivity(name: String, day: String, schedule: String, space: Int): Boolean {
        val db = writableDatabase
        return try {
            db.execSQL(
                "INSERT INTO activities (name, day, schedule, availableSpace) VALUES (?, ?, ?, ?)",
                arrayOf(name, day, schedule, space)
            )
            true
        } catch (e: Exception) {
            Log.e("DB", "Error al insertar actividad", e)
            false
        } finally {
            db.close()
        }
    }

    fun deleteActivity(name: String): Boolean {
        val db = writableDatabase
        return try {
            db.execSQL("DELETE FROM activities WHERE LOWER(name) = LOWER(?)", arrayOf(name))
            true
        } catch (e: Exception) {
            Log.e("DB", "Error al eliminar actividad", e)
            false
        } finally {
            db.close()
        }
    }

    fun getAllActividades(): List<Activity> {
        val list = mutableListOf<Activity>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM activities", null)

        while (cursor.moveToNext()) {
            list.add(
                Activity(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    name = cursor.getString(cursor.getColumnIndexOrThrow("name")),
                    day = cursor.getString(cursor.getColumnIndexOrThrow("day")),
                    schedule = cursor.getString(cursor.getColumnIndexOrThrow("schedule")),
                    availableSpace = cursor.getInt(cursor.getColumnIndexOrThrow("availableSpace"))
                )
            )
        }

        cursor.close()
        return list
    }

    fun getMemberActitivies(dni: String): List<String> {
        val list = mutableListOf<String>()
        val db = readableDatabase

        val cursor = db.rawQuery(
            """
        SELECT a.name FROM activities a
        INNER JOIN inscripciones i ON a.id = i.id_actividad
        INNER JOIN member m ON m.id = i.id_socio
        WHERE m.document = ?
        """.trimIndent(),
            arrayOf(dni)
        )

        Log.d("DEBUG", "Cantidad de actividades: ${list.size}")

        while (cursor.moveToNext()) {
            val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
            list.add(nombre)
        }

        cursor.close()
        return list
    }

    fun assignActivity(memberId: Int, activityId: Int, document: String, nameActivity: String): Boolean {
        val db = writableDatabase
        return try {
            db.beginTransaction()

            db.execSQL(
                "INSERT INTO inscripciones (id_socio, id_actividad) VALUES (?, ?)",
                arrayOf(memberId, activityId)
            )

            db.execSQL(
                "UPDATE activities SET availableSpace = availableSpace - 1 WHERE id = ?",
                arrayOf(activityId)
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
