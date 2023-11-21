package com.example.final_exam

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(private val itemList: ArrayList<Item> ) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyAdapter.MyViewHolder, position: Int) {
        val item : Item = itemList[position]
        holder.name.text = item.name
        holder.description.text = item.description
        holder.price.text = item.price.toString()

    }

    override fun getItemCount(): Int {
        return itemList.size

    }

    public class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val name: TextView = itemView.findViewById(R.id.tvitemName)
        val description: TextView = itemView.findViewById(R.id.tvitemDesc)
        val price: TextView = itemView.findViewById(R.id.tvitemPrice)
    }
}