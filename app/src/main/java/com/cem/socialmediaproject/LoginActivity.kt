package com.cem.socialmediaproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var auth:FirebaseAuth

    private lateinit var mail: EditText
    private lateinit var pass: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth=FirebaseAuth.getInstance()

        mail=findViewById(R.id.MailText)
        pass=findViewById(R.id.PasswordText)
    }

    fun logIn(view: View){

    }
    fun signIn(view:View){
        val email=mail.text.toString()
        val password=pass.text.toString()

        //firebase ile email/password kayıt oluşturmak için kullanıyoruz
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{task->// kayıt olma işlemi başarılı ise çalışacak
            //asenkron
            if(task.isSuccessful){
                //diğer aktivite
                val intent = Intent(this, TimeLineActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener{exception->// kayıt olunurken hata alınırsa çalışacak
            Toast.makeText(applicationContext,exception.localizedMessage,Toast.LENGTH_LONG).show()
        }

    }
}