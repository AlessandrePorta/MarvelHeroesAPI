package com.marvelapi.repository.impl

import com.marvelapi.paging.CharactersPagingSource
import com.marvelapi.repository.MarvelRepository
import com.marvelapi.services.MarvelCharactersService

class MarvelRepositoryImpl(private val marvelCharactersService: MarvelCharactersService) :
    MarvelRepository {

    override fun getCharacters() = CharactersPagingSource(
        fetchCharacters = {
            marvelCharactersService.getCharacters(it).dataContainer
        }
    )
}