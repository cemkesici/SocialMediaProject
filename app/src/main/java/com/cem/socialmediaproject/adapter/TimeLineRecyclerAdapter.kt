package com.cem.socialmediaproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cem.socialmediaproject.R
import com.cem.socialmediaproject.model.Post
import com.squareup.picasso.Picasso

class TimeLineRecyclerAdapter(private val postList:ArrayList<Post>) : RecyclerView.Adapter<TimeLineRecyclerAdapter.PostViewHolder>() {
    class PostViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val inflater=LayoutInflater.from(parent.context)
        val view= inflater.inflate(R.layout.recycler_row,parent,false)
        return PostViewHolder(view)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.recyclerEmailText).text=postList[position].userMail
        holder.itemView.findViewById<TextView>(R.id.recyclerContextText).text=postList[position].userContext

        Picasso.get().load(postList[position].imgUrl).into(holder.itemView.findViewById<ImageView>(R.id.recyclerİmg))//Picasso kütüphanesi ile resimleri ekledik
    }

}