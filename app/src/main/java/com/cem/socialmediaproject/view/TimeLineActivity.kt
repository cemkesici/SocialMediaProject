package com.cem.socialmediaproject.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cem.socialmediaproject.model.Post
import com.cem.socialmediaproject.R
import com.cem.socialmediaproject.adapter.TimeLineRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class TimeLineActivity : AppCompatActivity() {
    private lateinit var database:FirebaseFirestore
    private lateinit var auth:FirebaseAuth
    private lateinit var recyclerViewAdapter:TimeLineRecyclerAdapter
    private var postList= ArrayList <Post>()//çektiğimiz postları bir arada kullanmak için Post adında bir Class tanımladık ve bunu bir liste halinde değişkene tanımladık

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_line)
        auth= FirebaseAuth.getInstance()
        database= FirebaseFirestore.getInstance()
        veriAl()

        val recyclerView= findViewById<RecyclerView>(R.id.recyclerView)

        val layoutManager=LinearLayoutManager(this)
        recyclerView.layoutManager=layoutManager
        recyclerViewAdapter = TimeLineRecyclerAdapter(postList)
        recyclerView.adapter = recyclerViewAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun veriAl(){
        database.collection("Post").orderBy("date",Query.Direction.DESCENDING).addSnapshotListener{Snapshot , Exception ->
            //Tarihe göre listeleme yapılması için orderBy methodu ile çağırdık ve addSnapshotListener çağırdık
            if(Exception!=null){
                Toast.makeText(this,Exception.localizedMessage,Toast.LENGTH_LONG).show()
            }else{
                if (Snapshot!=null){
                    if (!Snapshot.isEmpty){
                        val documents = Snapshot.documents
                        postList.clear()// içerisinde veri kalmışsa diye listenin içeriğini en başta siliyoruz
                        for (element in documents){
                            val userEmail = element.get("userMail") as String
                            val userContext = element.get("userContext") as String
                            val imgUrl = element.get("imgUrl") as String

                            val indirilenPost= Post(userEmail,userContext, imgUrl)
                            postList.add(indirilenPost)
                        }
                        recyclerViewAdapter.notifyDataSetChanged()// yeni veri gelirse otomatik yenilemesi için
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
        if(item.itemId== R.id.fotograf_paylas){
            //fotoğraf paylaşma aktivetisine gidecek
            val Intent=Intent(this, FotografPaylasmaActivity::class.java)
            startActivity(Intent)
        }
        else if(item.itemId== R.id.cikis){
            auth.signOut()//firebase çıkış işlemi

            val Intent= Intent(this, LoginActivity::class.java)//login sayfasını çağırıypruz
            startActivity(Intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}