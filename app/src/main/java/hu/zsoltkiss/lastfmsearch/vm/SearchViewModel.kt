package hu.zsoltkiss.lastfmsearch.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import hu.zsoltkiss.lastfmsearch.datasource.RemoteDatasource
import hu.zsoltkiss.lastfmsearch.model.ArtistAttribs
import hu.zsoltkiss.lastfmsearch.model.ArtistDetails
import hu.zsoltkiss.lastfmsearch.model.TAG_DETAILS
import hu.zsoltkiss.lastfmsearch.network.RetrofitClient
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject


class SearchViewModel: ViewModel() {

    val lastResults: BehaviorSubject<List<ArtistAttribs>> = BehaviorSubject.create()
    val selectedArtist: BehaviorSubject<ArtistDetails> = BehaviorSubject.create()

    private val datasource = RemoteDatasource()

    private val disposables = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }


    fun search(expression: String) {
        datasource.searchResultsObservable("artist.search", expression, RetrofitClient.API_KEY)
            .subscribeOn(Schedulers.io())
            .map {
                it.matches.wrapper.items
            }
            .subscribe(lastResults::onNext, Throwable::printStackTrace).addTo(disposables)
    }

    fun clearSearch() {
        lastResults.onNext(emptyList())
    }


    fun fetchDetailsFor(name: String) {
        datasource.artistDetailsObservable("artist.getinfo", name, RetrofitClient.API_KEY)
            .subscribeOn(Schedulers.io()).subscribe(
                {
                    Log.d(TAG_DETAILS, "wrapper: $it")
                    selectedArtist.onNext(it.details)
                },
                Throwable::printStackTrace
            ).addTo(disposables)
    }



}