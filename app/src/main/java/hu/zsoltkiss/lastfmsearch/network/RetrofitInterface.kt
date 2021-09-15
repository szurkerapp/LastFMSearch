package hu.zsoltkiss.lastfmsearch.network

import hu.zsoltkiss.lastfmsearch.model.ArtistDetails
import hu.zsoltkiss.lastfmsearch.model.DetailsWrapper
import hu.zsoltkiss.lastfmsearch.model.SearchResults
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitInterface {

    // JSON: /2.0/?method=artist.search&artist=cher&api_key=YOUR_API_KEY&format=json
    @GET("2.0")
    fun search(
        @Query("method") method: String,
        @Query("artist") artist: String,
        @Query("api_key") apiKey: String,
        @Query("format") format: String): Observable<SearchResults>



    // JSON: /2.0/?method=artist.getinfo&artist=Cher&api_key=YOUR_API_KEY&format=json
    @GET("2.0")
    fun details(
        @Query("method") method: String,
        @Query("artist") artist: String,
        @Query("api_key") apiKey: String,
        @Query("format") format: String
    ): Observable<DetailsWrapper>
}