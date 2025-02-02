package org.ileewe.theseedjcli.adapter

import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import org.ileewe.theseedjcli.R
import org.ileewe.theseedjcli.databinding.BigListItemBinding
import org.ileewe.theseedjcli.model.Category

class BigMinistryAdapter: RecyclerView.Adapter<BigMinistryAdapter.BigCategoryViewHolder>() {


    class BigCategoryViewHolder(val binding: BigListItemBinding): RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<Category>() {

        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }

    }

    private val differ = AsyncListDiffer(this, diffUtil)

    var categories: List<Category>
        get() = differ.currentList
        set(value) {differ.submitList(value)}



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BigCategoryViewHolder {
        val binding: BigListItemBinding = BigListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BigCategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BigCategoryViewHolder, position: Int) {
        holder.binding.apply {
            val category = categories[position]

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tvPostTitle.text = Html.fromHtml(category.name, Html.FROM_HTML_MODE_LEGACY)
            }else{
                @Suppress("DEPRECATION")
                tvPostTitle.text = Html.fromHtml(category.name)
            }

            category.description?.let {
                Glide
                    .with(holder.itemView)
                    .load(category.description)
                    .placeholder(R.drawable.image_background)
                    .error(R.drawable.image_background)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(postImage)
            }

            articleWrapper.setOnClickListener {
                onItemClickListener?.let {
                    it(category)
                }
            }

        }
    }



    override fun getItemCount() = categories.size

    private var onItemClickListener: ((Category) -> Unit)? = null

    fun setOnItemClickListener(listener: (Category) -> Unit) {
        onItemClickListener = listener
    }
}