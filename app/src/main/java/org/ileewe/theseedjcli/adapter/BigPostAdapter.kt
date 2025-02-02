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
import org.ileewe.theseedjcli.model.Post
import org.ileewe.theseedjcli.utils.DateTimeUtils

class BigPostAdapter: RecyclerView.Adapter<BigPostAdapter.BigPostHolder>() {

    class BigPostHolder(val binding: BigListItemBinding): RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, diffUtil)

    var post: List<Post>
        get() {
            return differ.currentList
        }
        set(value) {
            differ.submitList(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BigPostHolder {
        val binding: BigListItemBinding = BigListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BigPostHolder(binding)
    }

    override fun onBindViewHolder(holder: BigPostHolder, position: Int) {
        holder.binding.apply {

            var post = post[position]

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
               tvPostTitle.text = Html.fromHtml(post.title.rendered, Html.FROM_HTML_MODE_LEGACY)
            }else{
                tvPostTitle.text = Html.fromHtml(post.title.rendered)
            }

            tvPostDate.text = DateTimeUtils.getDate(post.date)

            post.image?.let {
                Glide
                    .with(holder.itemView)
                    .load(post.image)
                    .placeholder(R.drawable.image_background)
                    .error(R.drawable.image_background)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(postImage)
            }

            articleWrapper.setOnClickListener {
                itemClickListener?.let {
                    it(post)
                }
            }
        }


    }

    override fun getItemCount(): Int {
       return post.size
    }


    private var itemClickListener: ((Post) -> Unit)? = null
    fun setOnItemClickListener(listener: (Post) -> Unit) {
        itemClickListener = listener
    }

}