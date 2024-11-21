package com.shevarev.kursmob

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var sp = getSharedPreferences("PC", Context.MODE_PRIVATE)
        if (sp.getString("TY", "-9")!= "-9") {
        startActivity(Intent(this, MainActivity2::class.java))
        }
        else {
            var signuptext: TextView = findViewById(R.id.signuptext)
            signuptext.setOnClickListener {
                var intent = Intent(this, signupActivity::class.java)
                startActivity(intent)
            }
            var email: TextView = findViewById(R.id.email)
            var password: TextView = findViewById(R.id.password)
            var button:ConstraintLayout = findViewById(R.id.button)
            var db = Firebase.firestore
            var df = false
            button.setOnClickListener{
                db.collection("users")
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            if(document.getString("email")==email.text.toString()) {
                                if(document.getString("password")==password.text.toString()){
                                    df = true;
                                    sp.edit().putString("Email",email.text.toString()).commit()
                                    startActivity(Intent(this, MainActivity2::class.java))
                                }
                                else if(document.getString("password")!=password.text){
                                   password.text = ""
                                }
                            }

                        }
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(this, "Не получилось, попробуйте еще раз", Toast.LENGTH_LONG).show()
                    }
                var h = Handler()
                h.postDelayed({
                    if(df==false){
                        Toast.makeText(this, "Непраильно набраны поля", Toast.LENGTH_LONG).show()
                    }
                },1600)
            }
        }
    }
}