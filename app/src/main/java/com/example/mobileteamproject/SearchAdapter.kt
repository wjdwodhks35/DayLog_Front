package com.example.mobileteamproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SearchAdapter : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    private val items = mutableListOf<SearchItem>()

    fun submitList(list: List<SearchItem>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val tvIndex: TextView = v.findViewById(R.id.tvIndex)
        private val tvName: TextView = v.findViewById(R.id.tvName)
        private val tvTime: TextView = v.findViewById(R.id.tvTime)

        fun bind(item: SearchItem, index: Int) {
            tvIndex.text = "$index."
            tvName.text = item.name
            tvTime.text = item.time
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search_result, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], position + 1)
    }

    override fun getItemCount() = items.size
}
