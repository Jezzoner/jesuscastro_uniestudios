package com.example.jesuscastro_uniestudios

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var etCedula: EditText
    private lateinit var etNombres: EditText
    private lateinit var etApellidos: EditText
    private lateinit var etCorreo: EditText
    private lateinit var cbPostularBeca: CheckBox
    private lateinit var rgGenero: RadioGroup
    private lateinit var btnRegistrar: Button
    private lateinit var btnCancelar: Button
    private lateinit var btnPostulantes: Button

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etCedula = findViewById(R.id.etCedula)
        etNombres = findViewById(R.id.etNombres)
        etApellidos = findViewById(R.id.etApellidos)
        etCorreo = findViewById(R.id.etCorreo)
        cbPostularBeca = findViewById(R.id.cbPostularBeca)
        rgGenero = findViewById(R.id.rgGenero)
        btnRegistrar = findViewById(R.id.btnRegistrar)
        btnCancelar = findViewById(R.id.btnCancelar)
        btnPostulantes = findViewById(R.id.btnPostulantes)

        db = Firebase.firestore

        btnRegistrar.setOnClickListener {
            registrarPostulante()
        }

        btnCancelar.setOnClickListener {
            limpiarCampos()
        }

        btnPostulantes.setOnClickListener {
            val intent = Intent(this, ListaPostulantesActivity::class.java)
            startActivity(intent)
        }
    }

    private fun registrarPostulante() {
        val cedula = etCedula.text.toString().trim()
        val nombres = etNombres.text.toString().trim()
        val apellidos = etApellidos.text.toString().trim()
        val correo = etCorreo.text.toString().trim()
        val postularBeca = cbPostularBeca.isChecked
        val generoId = rgGenero.checkedRadioButtonId
        val genero = if (generoId != -1) findViewById<RadioButton>(generoId).text.toString() else ""

        if (cedula.isEmpty()) {
            mostrarMensaje("Por favor, ingrese su cédula.")
            etCedula.requestFocus()
            return
        }

        if (nombres.isEmpty()) {
            mostrarMensaje("Por favor, ingrese sus nombres.")
            etNombres.requestFocus()
            return
        }

        if (apellidos.isEmpty()) {
            mostrarMensaje("Por favor, ingrese sus apellidos.")
            etApellidos.requestFocus()
            return
        }

        if (correo.isEmpty()) {
            mostrarMensaje("Por favor, ingrese su correo electrónico.")
            etCorreo.requestFocus()
            return
        }

        if (generoId == -1) {
            mostrarMensaje("Por favor, seleccione su género.")
            return
        }


        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            Toast.makeText(this, "Ingrese un correo electrónico válido", Toast.LENGTH_SHORT).show()
            return
        }

        val postulante = Postulante(cedula, nombres, apellidos, correo, postularBeca, genero)

        db.collection("postulantes")
            .add(postulante)
            .addOnSuccessListener {
                Toast.makeText(this, "¡Postulante registrado exitosamente!", Toast.LENGTH_SHORT).show()
                limpiarCampos()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error al registrar el postulante: ${exception.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun limpiarCampos() {
        etCedula.text.clear()
        etNombres.text.clear()
        etApellidos.text.clear()
        etCorreo.text.clear()
        cbPostularBeca.isChecked = false
        rgGenero.clearCheck()
    }

    data class Postulante(
        val cedula: String,
        val nombres: String,
        val apellidos: String,
        val correo: String,
        val postularBeca: Boolean,
        val genero: String
    )

    private fun mostrarMensaje(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
}