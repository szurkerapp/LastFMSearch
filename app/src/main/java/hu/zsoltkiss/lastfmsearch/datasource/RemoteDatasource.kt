package hu.zsoltkiss.lastfmsearch.datasource

import hu.zsoltkiss.lastfmsearch.model.ArtistDetails
import hu.zsoltkiss.lastfmsearch.model.DetailsWrapper
import hu.zsoltkiss.lastfmsearch.model.SearchResults
import hu.zsoltkiss.lastfmsearch.network.RetrofitClient
import io.reactivex.rxjava3.core.Observable


class RemoteDatasource {

    fun searchResultsObservable(method: String, searchExpression: String,  apiKey: String): Observable<SearchResults> {
        return RetrofitClient.lastFMSearchApi.search(method, searchExpression, apiKey, "json")
    }

    fun artistDetailsObservable(method: String, name: String, apiKey: String): Observable<DetailsWrapper> {
        return RetrofitClient.lastFMSearchApi.details(method, name, apiKey, "json")
    }


}