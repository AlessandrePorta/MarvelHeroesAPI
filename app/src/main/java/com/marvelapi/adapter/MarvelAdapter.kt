package com.marvelapi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.marvelapi.services.response.CharactersResponse
import com.marvelheroesapi.databinding.CharacterListItemBinding

class MarvelAdapter(
    private val onMovieClicked: (CharactersResponse) -> Unit
) :
    PagingDataAdapter<CharactersResponse, MarvelAdapter.CharactersViewHolder>(diffCallback) {

    override fun getItemViewType(position: Int) = CHARACTERS_VIEW_TYPE

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CharactersViewHolder {
        val view =
            CharacterListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CharactersViewHolder(view)
    }

    override fun onBindViewHolder(holder: CharactersViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it, onMovieClicked)
        }
    }

    inner class CharactersViewHolder(private val binding: CharacterListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: CharactersResponse,
            onCharacterClick: (CharactersResponse) -> Unit
        ) {
            binding.tvMovieName.text = item.name
            Glide.with(itemView.context)
                .load(item.thumbnail?.path+item.thumbnail?.extension)
                .into(binding.ivMovieImg)
            binding.root.setOnClickListener { onCharacterClick(item) }
        }

    }

    companion object {
        const val CHARACTERS_VIEW_TYPE = 1

        private val diffCallback = object : DiffUtil.ItemCallback<CharactersResponse>() {
            override fun areItemsTheSame(oldItem: CharactersResponse, newItem: CharactersResponse): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: CharactersResponse, newItem: CharactersResponse): Boolean {
                return oldItem == newItem
            }
        }
    }
}