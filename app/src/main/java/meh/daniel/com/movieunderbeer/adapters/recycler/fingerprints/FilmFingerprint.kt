package meh.daniel.com.movieunderbeer.adapters.recycler.fingerprints

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import meh.daniel.com.movieunderbeer.R
import meh.daniel.com.movieunderbeer.adapters.recycler.base.BaseViewHolder
import meh.daniel.com.movieunderbeer.adapters.recycler.common.Item
import meh.daniel.com.movieunderbeer.adapters.recycler.common.ItemFingerprint
import meh.daniel.com.movieunderbeer.app.Constants
import meh.daniel.com.movieunderbeer.databinding.ItemFilmBinding
import meh.daniel.com.movieunderbeer.model.entities.films.Film
import java.net.URL

class FilmFingerprint : ItemFingerprint<ItemFilmBinding, Film> {

    override fun isRelativeItem(item: Item) = item is Film

    override fun getLayoutId(): Int = R.layout.item_film

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<ItemFilmBinding, Film> {
        val binding = ItemFilmBinding.inflate(layoutInflater, parent, false)
        return FilmViewHolder(binding)
    }

    override fun getDiffUtil() = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<Film>() {

        override fun areItemsTheSame(oldItem: Film, newItem: Film) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Film, newItem: Film) = oldItem == newItem

    }
}

class FilmViewHolder(
    binding: ItemFilmBinding
) : BaseViewHolder<ItemFilmBinding, Film>(binding) {

    override fun onBind(item: Film) = with(binding) {
        when {
            item.imageUrl.isNullOrEmpty() -> {
                Glide.with(filmBackgroundImage)
                    .load(R.drawable.img_1)
                    .into(filmBackgroundImage)
            }
            item.imageUrl != null -> {
                Glide.with(filmBackgroundImage)
                    .load(item.imageUrl)
                    .into(filmBackgroundImage)

            }
            item.imageUrl == "null"-> {
                Glide.with(filmBackgroundImage)
                    .load(R.drawable.img_1)
                    .into(filmBackgroundImage)

            }
        }

        Log.d("xxx:", "${item.imageUrl.toString()} recycler")

        filmTitleName.text = item.localizedName
    }

}