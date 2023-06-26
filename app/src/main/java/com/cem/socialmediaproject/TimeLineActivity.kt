package com.cem.socialmediaproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth

class TimeLineActivity : AppCompatActivity() {
    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_line)
        auth= FirebaseAuth.getInstance()
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