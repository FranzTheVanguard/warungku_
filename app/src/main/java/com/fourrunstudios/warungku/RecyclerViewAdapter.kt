package com.fourrunstudios.warungku

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter(var context: Context, warungList: List<Warung>,
                          private var onWarungListener: OnWarungListener
) :
    RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {
    var warungList: List<Warung> = warungList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.warung_content, parent, false)
        return MyViewHolder(view, onWarungListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.warungName.text = warungList[position].name
        holder.warungAddress.text = warungList[position].address
        holder.warungCoords.text = warungList[position].latitude.toString()+","+warungList[position].longitude.toString()
    }

    override fun getItemCount(): Int {
        return warungList.size
    }

    class MyViewHolder(itemView: View, onWarungListener: OnWarungListener) : RecyclerView.ViewHolder(itemView), OnClickListener{
        var warungName : TextView
        var warungCoords: TextView
        var warungAddress: TextView
        var onWarungListener : OnWarungListener;
        init {
            warungName = itemView.findViewById(R.id.namawarung_textview)
            warungAddress = itemView.findViewById(R.id.alamatwarung_textview)
            warungCoords = itemView.findViewById(R.id.koordinatwarung_textview)
            this.onWarungListener = onWarungListener
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            onWarungListener.onWarungClick(adapterPosition)
        }
    }
    interface OnWarungListener{
        fun onWarungClick(position: Int);
    }

}