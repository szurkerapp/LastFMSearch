package hu.zsoltkiss.lastfmsearch.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import hu.zsoltkiss.lastfmsearch.R
import hu.zsoltkiss.lastfmsearch.adapter.ClickHandler
import hu.zsoltkiss.lastfmsearch.model.ArtistAttribs
import hu.zsoltkiss.lastfmsearch.model.TAG_MAIN
import hu.zsoltkiss.lastfmsearch.vm.SearchViewModel

class MainActivity: AppCompatActivity(), ClickHandler {

    private val searchViewModel: SearchViewModel by viewModels()

    private val detailsFragment: DetailsFragment?
        get() {
            return supportFragmentManager.findFragmentByTag(TAG_DETAILS_FRAGMENT) as? DetailsFragment
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadFragment(null)
    }

    /***********************************************************************************************
     * INTERFACE ClickHandler
     ***********************************************************************************************/
    override fun artistSelected(a: ArtistAttribs) {
        loadFragment(a.name)
    }

    override fun onBackPressed() {
        detailsFragment?.let {
            Log.d(TAG_MAIN, "Active fragment: $it")
            loadFragment(null)
        }
    }

    /***********************************************************************************************
     * PRIVATE METHODS
     ***********************************************************************************************/
    private fun loadFragment(artistName: String?) {

        Log.d(TAG_MAIN, "loadFragment() called...")

        val data = when (artistName == null) {
            true -> Pair(SearchFragment.newInstance(), TAG_SEARCH_FRAGMENT)
            else -> Pair(DetailsFragment.newInstance(artistName), TAG_DETAILS_FRAGMENT)
        }

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentHolder, data.first, data.second)
            commit()
        }

    }

    companion object {
        private const val TAG_SEARCH_FRAGMENT = "SearchFragment"
        private const val TAG_DETAILS_FRAGMENT = "DetailsFragment"
    }

}