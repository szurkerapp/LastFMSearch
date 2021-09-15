package hu.zsoltkiss.lastfmsearch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hu.zsoltkiss.lastfmsearch.R
import hu.zsoltkiss.lastfmsearch.model.ArtistAttribs

interface ClickHandler {
    fun artistSelected(a: ArtistAttribs)
}

class SearchResultsAdapter(private var artists: MutableList<ArtistAttribs>, private val clickHandler: ClickHandler): RecyclerView.Adapter<ArtistItemHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistItemHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.artist_item, parent, false)
        return ArtistItemHolder(rootView)
    }

    override fun onBindViewHolder(holder: ArtistItemHolder, position: Int) {
        val attribs = artists[position]

        holder.tvArtistName.text = attribs.name
        holder.tvArtistListeners.text = attribs.listeners

        attribs.imageUriForSize("large")?.let {
            Picasso.get().load(it).into(holder.ivArtistArtwork)
        }

        holder.itemView.setOnClickListener {
            clickHandler.artistSelected(attribs)
        }
    }

    override fun getItemCount(): Int {
        return artists.size
    }

    fun updateItems(newItems: List<ArtistAttribs>) {
        artists.clear()
        artists.addAll(newItems)
        notifyDataSetChanged()
    }
}


class ArtistItemHolder(v: View): RecyclerView.ViewHolder(v) {
    var tvArtistName: TextView = v.findViewById(R.id.tvName)
    var tvArtistListeners: TextView = v.findViewById(R.id.tvListeners)
    var ivArtistArtwork: ImageView = v.findViewById(R.id.ivArtwork)


}