package com.marvelapi.repository.impl

import CharactersPagingSource
import com.marvelapi.repository.MarvelRepository
import com.marvelapi.services.MarvelCharactersService

class MarvelRepositoryImpl(private val marvelCharactersService: MarvelCharactersService) :
    MarvelRepository {

    override fun getCharacters(query: String?) = CharactersPagingSource(
        fetchCharacters = {
            marvelCharactersService.getCharacters(query)
        }
    )
}