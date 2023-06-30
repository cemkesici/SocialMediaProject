package com.cem.socialmediaproject.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.cem.socialmediaproject.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class FotografPaylasmaActivity : AppCompatActivity() {
    private var secilenGorsel:Uri?=null
    private var gorselBitmap:Bitmap?=null
    private lateinit var storage:FirebaseStorage
    private lateinit var auth:FirebaseAuth
    private lateinit var database:FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fotograf_paylasma)

        //Firebase tanımlamaları
        storage= FirebaseStorage.getInstance()
        auth= FirebaseAuth.getInstance()
        database= FirebaseFirestore.getInstance()
    }

    fun paylas(view: View){
        //depo işlemleri
        //UUID , universal unique id
        val uuid= UUID.randomUUID()//eklenecek resimleri farklı isim ile eklememiz lazım bu yüzde uuid ile random id alacağız
        val gorselIsim="$uuid.jpg"
        val reference=storage.reference
        val gorselReference=reference.child("images").child(gorselIsim)

        if (secilenGorsel!=null) {
            gorselReference.putFile(secilenGorsel!!).addOnSuccessListener { _ ->
                //görsel yüklendi. Görselin yüklendiği adrese ulaşmalk için yükleme işleminden sonra referance alarak uri erişmemiz gerekli
                val secilenGorselRef= storage.reference.child("images").child(gorselIsim)
                secilenGorselRef.downloadUrl.addOnSuccessListener { Uri->
                    //uri başarılıysa
                    val downloadUrl = Uri.toString()
                    val guncelKullaniciEmail = auth.currentUser!!.email.toString()
                    val yorumText = findViewById<TextView>(R.id.yorumText)
                    val kullaniciYorum = yorumText.text.toString()
                    val tarih = com.google.firebase.Timestamp.now()

                    //veritabanı işlemleri
                    //Firebase veritabanı saklama işlemi hashmap ile yapılır.Key value mantığı ile çalışır
                    val postHashMap= hashMapOf<String,Any>() // Giren değeri Any yapıyorum, birçık farklı değer girdisi olacak
                    postHashMap["imgUrl"] = downloadUrl
                    postHashMap["userMail"] = guncelKullaniciEmail
                    postHashMap["userContext"] = kullaniciYorum
                    postHashMap["date"] = tarih

                    //hangi koleksiyondan okuma yazma yapacağımızı seçmemiz gereklidir
                    database.collection("Post").add(postHashMap).addOnCompleteListener { it->
                        if (it.isSuccessful){
                            finish()
                        }
                    }.addOnFailureListener{exc->
                        Toast.makeText(this,exc.localizedMessage,Toast.LENGTH_LONG).show()
                    }
                }.addOnFailureListener{exc->
                    Toast.makeText(this,exc.localizedMessage,Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    @Suppress("DEPRECATION")
    fun gorselSec(view:View){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            //İZİN ALINMAMIŞSA
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),0)
        }else {
            val galeriIntent=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galeriIntent,1)
        }
    }

    @Suppress("DEPRECATION")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode==0){
            if (grantResults.isNotEmpty() &&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                val galeriIntent=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeriIntent,1)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    @Deprecated("Deprecated in Java")
    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val ImageView=findViewById<ImageView>(R.id.imageView2)

        if(requestCode==1 && resultCode==Activity.RESULT_OK && data!=null){
           secilenGorsel= data.data
            if(secilenGorsel!=null){
                if (Build.VERSION.SDK_INT>=28){
                    val source=ImageDecoder.createSource(this.contentResolver,secilenGorsel!!)
                    gorselBitmap=ImageDecoder.decodeBitmap(source)
                    ImageView.setImageBitmap(gorselBitmap)
                }else {
                    gorselBitmap =MediaStore.Images.Media.getBitmap(this.contentResolver, secilenGorsel)
                    ImageView.setImageBitmap(gorselBitmap)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
