package com.konovus.simplemortyr.util

import android.content.Context
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.first

inline fun SearchView.onQueryTextChanged(
    searchView: SearchView?,
    crossinline listener: (String) -> Unit
) {
    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            searchView?.clearFocus()
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            listener(newText.orEmpty())
            return true
        }
    })
}

inline fun MenuItem.onActionExpanded(crossinline listener: (String) -> Unit) {
    this.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
        override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
            return true
        }

        override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
            listener("")
            return true
        }
    })
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

suspend fun saveDataStore(key: String, value: String, context: Context) {
    val dataStoreKey = stringPreferencesKey(key)
    context.dataStore.edit { settings ->
        settings[dataStoreKey] = value
    }
}

suspend fun readDataStore(key: String, context: Context) : String? {
    val dataStoreKey = stringPreferencesKey(key)
    val preferences = context.dataStore.data.first()
    return preferences[dataStoreKey]
}



inline fun myItemTouchHelper(recyclerView: RecyclerView, crossinline listener: (Int) -> Unit) {
    ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
        0,
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            listener(viewHolder.adapterPosition)
        }
    }).attachToRecyclerView(recyclerView)
}