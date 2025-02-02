package org.ileewe.theseedjcli.adapter

import android.os.Build
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import org.ileewe.theseedjcli.R
import org.ileewe.theseedjcli.databinding.HomeListItemBinding
import org.ileewe.theseedjcli.model.Post

class PostAdapter: RecyclerView.Adapter<PostAdapter.SmallViewHolder>() {

    class SmallViewHolder(val binding: HomeListItemBinding): RecyclerView.ViewHolder(binding.root)


    private val diffUtil = object : DiffUtil.ItemCallback<Post>() {

        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }

    }

    private val differ = AsyncListDiffer(this, diffUtil)

    var post: List<Post>
        get() = differ.currentList
        set(value) {differ.submitList(value)}


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmallViewHolder {
        val binding: HomeListItemBinding = HomeListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SmallViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SmallViewHolder, position: Int) {

        holder.binding.apply {
            var post = post[position]

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tvSermonTitle.text = Html.fromHtml(post.title.rendered, Html.FROM_HTML_MODE_LEGACY)
            }else{
                tvSermonTitle.text = Html.fromHtml(post.title.rendered)
            }

            post.image?.let {
                Glide
                    .with(holder.itemView)
                    .load(post.image)
                    .placeholder(R.drawable.image_background)
                    .error(R.drawable.image_background)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(sermonImage)
            }

            viewWrapper.setOnClickListener {
                onItemClickListener?.let {
                    it(post)
                }
            }

        }



    }

    override fun getItemCount() = post.size


    private var onItemClickListener: ((Post) -> Unit)? = null

    fun setOnItemClickListener(listener: (Post) -> Unit) {
        onItemClickListener = listener
    }

}


