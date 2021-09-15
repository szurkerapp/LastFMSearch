package hu.zsoltkiss.lastfmsearch.ext

import android.content.Context
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.setDefaultDivider(context: Context) {
    val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
    addItemDecoration(decoration)
}