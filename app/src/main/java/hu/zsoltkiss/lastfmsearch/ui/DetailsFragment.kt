package hu.zsoltkiss.lastfmsearch.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hu.zsoltkiss.lastfmsearch.R
import hu.zsoltkiss.lastfmsearch.adapter.ClickHandler
import hu.zsoltkiss.lastfmsearch.adapter.SimilarArtistAdapter
import hu.zsoltkiss.lastfmsearch.ext.setDefaultDivider
import hu.zsoltkiss.lastfmsearch.model.ArtistAttribs
import hu.zsoltkiss.lastfmsearch.model.ArtistDetails
import hu.zsoltkiss.lastfmsearch.model.TAG_DETAILS
import hu.zsoltkiss.lastfmsearch.vm.SearchViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo

class DetailsFragment: Fragment(), ClickHandler {

    private lateinit var artistName: String
    private lateinit var nameTextView: TextView
    private lateinit var picture: ImageView
    private lateinit var recyclerView: RecyclerView

    private val similarArtistsAdapter: SimilarArtistAdapter = SimilarArtistAdapter(
        mutableListOf(), this)

    private val searchViewModel: SearchViewModel by activityViewModels()
    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        artistName = arguments?.getString(ARGS_NAME) ?: ""

        if (artistName.isNotEmpty()) {
            searchViewModel.fetchDetailsFor(artistName)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nameTextView = view.findViewById(R.id.tvArtistName)
        picture = view.findViewById(R.id.ivArtistPicture)

        recyclerView = view.findViewById(R.id.rvSimilarArtists)
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.adapter = similarArtistsAdapter
        recyclerView.setDefaultDivider(requireActivity())

        searchViewModel.selectedArtist.observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::updateUI, Throwable::printStackTrace).addTo(disposables)

    }

    override fun onResume() {
        super.onResume()
        Toast.makeText(requireActivity(), getString(R.string.back_button_info), Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }


    /***********************************************************************************************
     * INTERFACE ClickHandler
     ***********************************************************************************************/
    override fun artistSelected(a: ArtistAttribs) {
        searchViewModel.fetchDetailsFor(a.name)
    }

    /***********************************************************************************************
     * PRIVATE METHODS
     ***********************************************************************************************/
    private fun updateUI(details: ArtistDetails) {
        nameTextView.text = details.name

        details.imageUriForSize("large")?.let { imageUri ->
            Picasso.get().load(imageUri).into(picture)
        }

        details.similar?.let {
            val similarOnes = it.items
            Log.d(TAG_DETAILS, "Similar items: $similarOnes")

            if (similarOnes.isNotEmpty()) {
                similarArtistsAdapter.updateItems(similarOnes)
            }
        }
    }

    companion object {
        const val ARGS_NAME = "artistName"

        fun newInstance(name: String): DetailsFragment {
            return DetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARGS_NAME, name)
                }
            }
        }
    }
}