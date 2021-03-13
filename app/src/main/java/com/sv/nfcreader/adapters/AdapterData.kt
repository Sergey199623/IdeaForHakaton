package com.sv.nfcreader.adapters

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sv.nfcreader.R
import com.sv.nfcreader.data.Account

class AdapterData : RecyclerView.Adapter<DataViewHolder>() {

    private var datas: List<Account> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_data, parent, false)
        return DataViewHolder(view)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.onBind(datas[position])
    }

    override fun getItemCount(): Int = datas.size

    fun bindData(newData: List<Account>) {
        datas = newData
    }
}

class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {
        private val imageOption = RequestOptions()
            .placeholder(R.drawable.ic_avatar_placeholder)
            .fallback(R.drawable.ic_avatar_placeholder)
            .circleCrop()
    }

    private val avatar: ImageView = itemView.findViewById(R.id.iv_social_avatar)
    private val id: TextView = itemView.findViewById(R.id.tv_id)
    private val path: TextView = itemView.findViewById(R.id.tv_path)

    fun onBind(data: Account) {
        Glide.with(context)
            .load(data.avatar)
            .apply(imageOption)
            .into(avatar)
        id.text = data.id.toString()
        path.text = data.path
        path.setOnClickListener {
            val adress = Uri.parse(data.path)
            val intent = Intent(Intent.ACTION_VIEW, adress)
            context.startActivity(intent)
        }
    }
}

private val RecyclerView.ViewHolder.context
    get() = this.itemView.context