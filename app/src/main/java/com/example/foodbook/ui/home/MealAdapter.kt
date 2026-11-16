package com.example.foodbook.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodbook.R
import com.example.foodbook.data.model.MealItem
import com.example.foodbook.databinding.ItemMealBinding

class MealAdapter(private val onClick: (MealItem) -> Unit) :
    ListAdapter<MealItem, MealAdapter.VH>(Diff) {

    inner class VH(val b: ItemMealBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemMealBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        holder.b.tvName.text = item.name
        Glide.with(holder.b.ivThumb).load(item.thumb.ifBlank { null })
            .placeholder(R.drawable.bg_placeholder).fallback(R.drawable.bg_placeholder).error(R.drawable.bg_placeholder)
            .centerCrop().into(holder.b.ivThumb)  // tải ảnh bất đồng bộ
        holder.b.root.setOnClickListener { onClick(item) }
    }

    object Diff : DiffUtil.ItemCallback<MealItem>() {
        override fun areItemsTheSame(a: MealItem, b: MealItem) = a.id == b.id
        override fun areContentsTheSame(a: MealItem, b: MealItem) = a == b
    }
}
