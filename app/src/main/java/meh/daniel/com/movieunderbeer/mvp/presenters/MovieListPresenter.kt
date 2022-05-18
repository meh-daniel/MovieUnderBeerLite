package meh.daniel.com.movieunderbeer.mvp.presenters

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import meh.daniel.com.movieunderbeer.adapters.recycler.common.Item
import meh.daniel.com.movieunderbeer.app.App
import meh.daniel.com.movieunderbeer.app.Constants
import meh.daniel.com.movieunderbeer.di.modules.ApiModule
import meh.daniel.com.movieunderbeer.di.modules.RepositoryModule
import meh.daniel.com.movieunderbeer.model.entities.films.Film
import meh.daniel.com.movieunderbeer.model.entities.films.FilmData
import meh.daniel.com.movieunderbeer.mvp.base.BasePresenter
import meh.daniel.com.movieunderbeer.mvp.view.MovieListView
import moxy.InjectViewState
import retrofit2.Response

@InjectViewState
class MovieListPresenter : BasePresenter<MovieListView>() {
    override fun injectDependency() {
        App.instance.appComponent.inject(this)
    }



//    private var filmsList: MutableList<Item> = MutableList(1){Film(id = 1, localizedName = "localName", name = null, year = null, rating = 3.3, imageUrl = null, description = null, genres = null)}

    fun start(titlesList: MutableList<Item>){
        GlobalScope.launch {
            try {
                viewState.setupAdapter()
            }catch (e:Exception){
                Log.d("expection:", "${e.toString()} fuck scope1")
            }
            try {

                GlobalScope.launch(Dispatchers.Main) {
                    val api = ApiModule().api(Constants.API_URL, ApiModule().gson())
                    val repo = RepositoryModule().filmRepository(api)
                    var response: Response<FilmData> = repo.loadFilms()
                    Log.d("expection:", "${response.body().toString()} fuck scope1")



                    var listFilm = mutableListOf<Item>()
                    GlobalScope.launch {
                        try {
                            if (response.isSuccessful) {
                                val items = response.body()?.films

                                if (items != null) {
                                    for (i in 0 until items.count()) {
                                        val id = items[i].id?.toInt()
                                        val localName = items[i].localizedName.toString()
                                        val rating = items[i].rating?.toDouble()
                                        val imageUrl = items[i].imageUrl?.toString()
                                        listFilm.add(i, Film(id = id, localizedName = localName, name = null, year = null, rating = rating, imageUrl = imageUrl, description = null, genres = null))
                                        Log.d("xxx:", "${listFilm[i].toString()} fuck")
                                    }
                                }
                                viewState.setData(titlesList, listFilm)
                            }
                        } catch (e: Exception) {
                            Log.d("expection:", "${e.toString()} fuck")
                        }
                    }
                }
            }catch (e:Exception){
                Log.d("expection:", "${e.toString()} fuck scope2")
            }
        }
    }

    private fun getBodyResponse(response: Response<FilmData>): MutableList<Item>{
        var listFilm = mutableListOf<Item>()
        GlobalScope.launch {
            try {
                if (response.isSuccessful) {
                    val items = response.body()?.films

                    if (items != null) {
                        for (i in 0 until items.count()) {
                            val id = items[i].id?.toInt()
                            val localName = items[i].localizedName.toString()
                            val rating = items[i].rating?.toDouble()
                            val imageUrl = items[i].imageUrl?.toString()
                            listFilm.add(i, Film(id = id, localizedName = localName, name = null, year = null, rating = rating, imageUrl = imageUrl, description = null, genres = null))
                            Log.d("xxx:", "${listFilm[i].toString()} fuck")
                        }
                    }

                }
            } catch (e: Exception) {
                Log.d("expection:", "${e.toString()} fuck")
            }
        }
        Log.d("expection:", "${listFilm[5].toString()} fuck")
        return listFilm
    }

}