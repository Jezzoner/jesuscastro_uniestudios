package com.example.jesuscastro_uniestudios

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ListaPostulantesActivity : AppCompatActivity() {

    private lateinit var lvPostulantes: ListView
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_postulantes)

        lvPostulantes = findViewById(R.id.lvPostulantes)

        cargarPostulantes()
    }

    private fun cargarPostulantes() {
        db.collection("postulantes")
            .get()
            .addOnSuccessListener { result ->
                val postulantes = mutableListOf<String>()
                for (document in result) {
                    val nombre = document.getString("nombres") ?: ""
                    val apellido = document.getString("apellidos") ?: ""
                    postulantes.add("$nombre $apellido")
                }
                val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, postulantes)
                lvPostulantes.adapter = adapter
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error al cargar los postulantes: ${exception.message}", Toast.LENGTH_LONG).show()
            }
    }
}