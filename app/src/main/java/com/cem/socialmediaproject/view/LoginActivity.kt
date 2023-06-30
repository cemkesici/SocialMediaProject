package com.cem.socialmediaproject.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cem.socialmediaproject.R
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

        val kullanici=auth.currentUser// güncel kullanıcı bilgisi
        if(kullanici!= null){// güncel kullanıcı varsa giriş yapılmıştır ve tekrar login sayfasını açmadan anasayfaya atmamız lazım
            val intent=Intent(this, TimeLineActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun logIn(view: View){//giriş yapmak için
        val email=mail.text.toString()
        val password=pass.text.toString()

        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task->
            if (task.isSuccessful){
                val kullanici=auth.currentUser?.email.toString()// güncel kullanıcı bilgisi
                Toast.makeText(this,"Hoşgeldiniz $kullanici", Toast.LENGTH_LONG).show()

                val intent=Intent(this, TimeLineActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener{exception->
            Toast.makeText(this,exception.localizedMessage,Toast.LENGTH_LONG).show()
        }
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