package com.marvelapi.repository

import androidx.paging.PagingSource
import com.marvelapi.model.Character

interface MarvelRepository {

    fun getCharacters() : PagingSource<Int, Character>
}