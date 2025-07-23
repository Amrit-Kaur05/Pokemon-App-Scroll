package com.example.scroll

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class PokeAdapter (private val pokeImgList: List<String>,private val pokeTitleList:List<String>,private val pokeDescList:List<String>) : RecyclerView.Adapter<PokeAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val pokeImg: ImageView
        val pokeName: TextView
        val pokeDesc: TextView

        init {
            // Find our RecyclerView item's ImageView for future use
            pokeImg = view.findViewById(R.id.pokeImg)
            pokeName = view.findViewById(R.id.name)
            pokeDesc = view.findViewById(R.id.abilities)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.poke_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // updating pokemon image
        Glide.with(holder.itemView)
            .load(pokeImgList[position])
            .centerCrop()
            .into(holder.pokeImg)


        // updating pokemon name
        holder.pokeName.text = pokeTitleList[position]

       // updating pokemon abilities
        holder.pokeDesc.text = pokeDescList[position]
    }

    override fun getItemCount() = pokeImgList.size
}
