package com.cem.socialmediaproject

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TimeLineActivity : AppCompatActivity() {
    private lateinit var database:FirebaseFirestore
    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_line)
        auth= FirebaseAuth.getInstance()
        database= FirebaseFirestore.getInstance()
        veriAl()
    }
    private fun veriAl(){
        database.collection("Post").addSnapshotListener{Snapshot , Exception ->
            if(Exception!=null){
                Toast.makeText(this,Exception.localizedMessage,Toast.LENGTH_LONG).show()
            }else{
                if (Snapshot!=null){
                    if (!Snapshot.isEmpty){
                        val documents = Snapshot.documents
                        for (element in documents){
                            val userEmail = element.get("userMail") as String
                            val userContext = element.get("userContext") as String
                            val date = element.get("date") as Timestamp
                            println(userContext)
                            println(userEmail)
                            println(date)
                        }
                    }
                }
            }
        }
    }
    @Suppress("UsePropertyAccessSyntax")
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater=getMenuInflater()// menuyu çağırdık
        menuInflater.inflate(R.menu.secenekler_menu,menu)// menuyu ınflate ettik
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.fotograf_paylas){
            //fotoğraf paylaşma aktivetisine gidecek
            val Intent=Intent(this,FotografPaylasmaActivity::class.java)
            startActivity(Intent)
        }
        else if(item.itemId==R.id.cikis){
            auth.signOut()//firebase çıkış işlemi

            val Intent= Intent(this,LoginActivity::class.java)//login sayfasını çağırıypruz
            startActivity(Intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}