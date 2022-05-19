package meh.daniel.com.movieunderbeer.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import meh.daniel.com.movieunderbeer.R
import meh.daniel.com.movieunderbeer.adapters.recycler.FingerprintAdapter
import meh.daniel.com.movieunderbeer.adapters.recycler.animations.AddableItemAnimator
import meh.daniel.com.movieunderbeer.adapters.recycler.animations.custom.SimpleCommonAnimator
import meh.daniel.com.movieunderbeer.adapters.recycler.animations.custom.SlideInLeftCommonAnimator
import meh.daniel.com.movieunderbeer.adapters.recycler.animations.custom.SlideInTopCommonAnimator
import meh.daniel.com.movieunderbeer.adapters.recycler.common.Item
import meh.daniel.com.movieunderbeer.adapters.recycler.common.helpers.TitleFingerprint
import meh.daniel.com.movieunderbeer.adapters.recycler.decorations.FeedHorizontalDividerItemDecoration
import meh.daniel.com.movieunderbeer.adapters.recycler.decorations.GroupVerticalItemDecoration
import meh.daniel.com.movieunderbeer.adapters.recycler.fingerprints.FilmFingerprint
import meh.daniel.com.movieunderbeer.databinding.FragmentMovieListBinding
import meh.daniel.com.movieunderbeer.model.entities.helpers.FeedTitle
import meh.daniel.com.movieunderbeer.mvp.presenters.MovieListPresenter
import meh.daniel.com.movieunderbeer.mvp.view.MovieListView
import meh.daniel.com.movieunderbeer.ui.base.BaseFragment
import moxy.presenter.InjectPresenter

class MovieListFragment : BaseFragment(), MovieListView {

    private lateinit var binding: FragmentMovieListBinding

    private val titlesList: MutableList<Item> by lazy {
        MutableList(1) { FeedTitle("Та за шо ты меня так") }
    }
//    private val filmsList: MutableList<Item> by lazy {
//        MutableList(1) {Film(id = 1, localizedName = "localName", name = null, year = null, rating = 3.3, imageUrl = "https://st.kp.yandex.net/images/film_iphone/iphone360_42664.jpg", description = null, genres = null)}
//    }

    private val titleAdapter = FingerprintAdapter(listOf(TitleFingerprint()))
    private val filmAdapter = FingerprintAdapter(listOf(FilmFingerprint()))

    private val concatAdapter = ConcatAdapter(
        ConcatAdapter.Config.Builder()
            .setIsolateViewTypes(false)
            .build(),
        titleAdapter,
        filmAdapter
    )


    @InjectPresenter
    lateinit var movieListPresenter: MovieListPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMovieListBinding.inflate(inflater, container, false)
        injectDependency()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        injectDependency()
        movieListPresenter.start(titlesList)
    }
    override fun injectDependency(){
        movieListPresenter.injectDependency()
    }

    override fun setupAdapter() {

        try {
            with(binding.contentFilms) {
                var gridLayoutManager = GridLayoutManager(context, 2)
                gridLayoutManager.spanSizeLookup = object : SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if (position % 3 === 0) 2 else 1
                    }
                }
                layoutManager = gridLayoutManager
                adapter = concatAdapter

                addItemDecoration(FeedHorizontalDividerItemDecoration(70))
                addItemDecoration(GroupVerticalItemDecoration(R.layout.item_title, 50, 100))
                addItemDecoration(GroupVerticalItemDecoration(R.layout.item_film, 0, 0))

                itemAnimator = AddableItemAnimator(SimpleCommonAnimator()).also { animator ->
                    animator.addViewTypeAnimation(R.layout.item_title, SlideInTopCommonAnimator())
                    animator.addViewTypeAnimation(R.layout.item_film, SlideInLeftCommonAnimator())
                    animator.addDuration = 300L
                }
                filmAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT
            }

        }catch (e : Exception){
            Log.d("expection:", "${e.toString()} fuck scope1")
        }
    }

    override fun setData(titlesList: MutableList<Item>, filmsList: MutableList<Item>) {
        binding.contentFilms.postDelayed({
            titleAdapter.submitList(titlesList.toList())
            filmAdapter.submitList(filmsList.toList())
            filmAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.ALLOW
        }, 300L)
    }
}




