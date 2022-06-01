package meh.daniel.com.movieunderbeer.mvp.view

import meh.daniel.com.movieunderbeer.entities.films.Film
import meh.daniel.com.movieunderbeer.mvp.base.BaseView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndStrategy::class)
interface MovieDetailsView : BaseView {

    fun init()

    fun loadFilm(film: Film)

}