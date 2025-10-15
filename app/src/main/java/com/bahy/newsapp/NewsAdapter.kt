package com.bahy.newsapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bahy.newsapp.databinding.ArticleListItemBinding
import com.bahy.nota.Article
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class NewsAdapter(val a: Activity, val articles: ArrayList<Article>, private val removeOnUnfavorite: Boolean = false) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    class NewsViewHolder(val binding: ArticleListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NewsViewHolder {
        val b = ArticleListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NewsViewHolder(b)
    }

    override fun onBindViewHolder(
        holder: NewsViewHolder,
        position: Int
    ) {
        val db = FirebaseFirestore.getInstance()
        val userId = Firebase.auth.currentUser?.uid
        val article = articles.getOrNull(position)

        fun setFavoriteStatus(isFavorite: Boolean) {
            if (isFavorite) {
                holder.binding.favoriteIcon.setImageResource(R.drawable.ic_favorites)
            } else {
                holder.binding.favoriteIcon.setImageResource(R.drawable.ic_favorites_border)
            }
        }

        if (userId != null && article?.url != null) {
            val articleId = article.url.hashCode().toString()
            val favoriteRef = db.collection("users").document(userId).collection("favorites").document(articleId)

            favoriteRef.get()
                .addOnSuccessListener { document ->
                    setFavoriteStatus(document.exists())
                }
                .addOnFailureListener { e ->
                    Log.e("NewsAdapter", "Failed to prefetch favorite status", e)
                }
        }

        holder.binding.favoriteIcon.setOnClickListener {
            if (userId == null) {
                Toast.makeText(a, "You need to be logged in", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val currentPos = holder.adapterPosition
            if (currentPos == RecyclerView.NO_POSITION) return@setOnClickListener
            val currentArticle = articles.getOrNull(currentPos) ?: return@setOnClickListener
            val urlStr = currentArticle.url
            if (urlStr.isNullOrBlank()) {
                Toast.makeText(a, "Cannot favorite: missing URL", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val articleId = urlStr.hashCode().toString()
            val favoriteRef = db.collection("users").document(userId).collection("favorites").document(articleId)

            favoriteRef.get().addOnSuccessListener { document ->
                if (document.exists()) {
                    favoriteRef.delete().addOnSuccessListener {
                        setFavoriteStatus(false)
                        Toast.makeText(a, "Removed from favorites", Toast.LENGTH_SHORT).show()
                        if (removeOnUnfavorite) {
                            val pos = holder.adapterPosition
                            if (pos != RecyclerView.NO_POSITION) {
                                articles.removeAt(pos)
                                notifyItemRemoved(pos)
                            }
                        }
                    }.addOnFailureListener {
                        Log.e("NewsAdapter", "Failed to remove favorite", it)
                        Toast.makeText(a, it.message ?: "Failed to update favorites", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val data = hashMapOf(
                        "title" to (currentArticle.title ?: ""),
                        "url" to urlStr,
                        "urlToImage" to (currentArticle.urlToImage ?: "")
                    )
                    favoriteRef.set(data).addOnSuccessListener {
                        setFavoriteStatus(true)
                        Toast.makeText(a, "Added to favorites", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Log.e("NewsAdapter", "Failed to add favorite", it)
                        Toast.makeText(a, it.message ?: "Failed to add to favorites", Toast.LENGTH_SHORT).show()
                    }
                }
            }.addOnFailureListener {
                Log.e("NewsAdapter", "Failed to check favorite status", it)
                Toast.makeText(a, it.message ?: "Failed to check favorite status", Toast.LENGTH_SHORT).show()
            }
        }

        holder.binding.articleText.text = article?.title ?: ""

        Glide
            .with(holder.binding.articleImage.context)
            .load(article?.urlToImage)
            .error(R.drawable.broken_image)
            .transition(DrawableTransitionOptions.withCrossFade(1000))
            .into(holder.binding.articleImage)

        val url = article?.url
        holder.binding.articleContainer.setOnClickListener {
            val u = url ?: return@setOnClickListener
            val i = Intent(Intent.ACTION_VIEW, u.toUri())
            a.startActivity(i)
        }

        holder.binding.share.setOnClickListener {
            val u = url ?: return@setOnClickListener
            ShareCompat
                .IntentBuilder(a)
                .setType("text/plain")
                .setChooserTitle("Share article with:")
                .setText(u)
                .startChooser()
        }
    }

    override fun getItemCount() = articles.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newArticles: ArrayList<Article>) {
        articles.clear()
        articles.addAll(newArticles)
        notifyDataSetChanged()
    }

}