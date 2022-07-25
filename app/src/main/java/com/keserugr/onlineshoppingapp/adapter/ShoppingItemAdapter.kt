package com.keserugr.onlineshoppingapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.keserugr.onlineshoppingapp.R
import com.keserugr.onlineshoppingapp.data.local.ShoppingItem
import javax.inject.Inject

class ShoppingItemAdapter @Inject constructor(
   private val glide: RequestManager
) : RecyclerView.Adapter<ShoppingItemAdapter.ShoppingViewHolder>() {

    class ShoppingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private var onItemClickListener: ((String) -> Unit)? = null

    fun setOnItemClickListener(listener: (String) -> Unit){
        onItemClickListener = listener
    }

    private val diffUtil = object : DiffUtil.ItemCallback<ShoppingItem>() {
        override fun areItemsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
            return oldItem == newItem
        }
    }

    private val recyclerListDiffer = AsyncListDiffer(this, diffUtil)

    var shoppingItems: List<ShoppingItem>
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingViewHolder {
        return ShoppingViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_shopping,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ShoppingViewHolder, position: Int) {
        val ivShoppingImage = holder.itemView.findViewById<ImageView>(R.id.ivShoppingImage)
        val tvShoppingItemAmount = holder.itemView.findViewById<TextView>(R.id.tvShoppingItemAmount)
        val tvName = holder.itemView.findViewById<TextView>(R.id.tvName)
        val tvShoppingItemPrice = holder.itemView.findViewById<TextView>(R.id.tvShoppingItemPrice)

        val shoppingItem = shoppingItems[position]

        holder.itemView.apply {
            glide.load(shoppingItem.imageUrl).into(ivShoppingImage)
        }
        tvName.text = shoppingItem.name
        tvShoppingItemAmount.text = "${shoppingItem.amount} x"

        tvShoppingItemPrice.text = "£${shoppingItem.price}"
    }

    override fun getItemCount(): Int {
        return shoppingItems.size
    }
}