package com.movie.moviebazaar.helper.recyclerview.holder

import android.view.View

interface ItemClickListener {
    fun onClick(view: View?, position: Int, isLongClick: Boolean)
}
