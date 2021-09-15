package hu.zsoltkiss.lastfmsearch.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hu.zsoltkiss.lastfmsearch.R
import hu.zsoltkiss.lastfmsearch.adapter.ClickHandler
import hu.zsoltkiss.lastfmsearch.adapter.SearchResultsAdapter
import hu.zsoltkiss.lastfmsearch.ext.setDefaultDivider
import hu.zsoltkiss.lastfmsearch.model.ArtistAttribs
import hu.zsoltkiss.lastfmsearch.model.TAG_SEARCH
import hu.zsoltkiss.lastfmsearch.vm.SearchViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo

class SearchFragment: Fragment() {

    private val searchViewModel: SearchViewModel by activityViewModels()

    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var noItemsTextView: TextView
    private lateinit var adapter: SearchResultsAdapter
    private val disposables = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchView = view.findViewById(R.id.searchView)
        noItemsTextView = view.findViewById(R.id.emptyView)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.setDefaultDivider(requireActivity())
        adapter = SearchResultsAdapter(mutableListOf(), requireActivity() as ClickHandler)
        recyclerView.adapter = adapter

        searchView.queryHint = getString(R.string.search_hint)
        searchView.isIconified = false
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null && query.isNotEmpty()) {
                    searchViewModel.search(query)
                }

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        val closeButtonId = searchView.context.resources
            .getIdentifier("android:id/search_close_btn", null, null)
        val closeButton = searchView.findViewById<ImageView>(closeButtonId)
        closeButton.setOnClickListener {
            searchViewModel.clearSearch()
            clearSearchString()
        }


        searchViewModel.lastResults.observeOn(AndroidSchedulers.mainThread()).subscribe(this::updateResults, Throwable::printStackTrace).addTo(disposables)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    /***********************************************************************************************
     * PRIVATE METHODS
     ***********************************************************************************************/
    private fun updateResults(newItems: List<ArtistAttribs>) {
        Log.d(TAG_SEARCH, "$newItems")
        adapter.updateItems(newItems)
        noItemsTextView.visibility = when (newItems.isEmpty()) {
            true -> View.VISIBLE
            else -> View.GONE
        }
    }

    private fun clearSearchString() {
        searchView.setQuery("", false)
    }


    companion object {
        fun newInstance() = SearchFragment()
    }
}