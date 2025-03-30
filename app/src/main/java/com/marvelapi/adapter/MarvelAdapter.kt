package com.marvelapi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.marvelapi.database.CharacterEntity
import com.marvelapi.model.CharacterVO
import com.marvelheroesapi.R
import com.marvelheroesapi.databinding.CharacterListItemBinding

class MarvelAdapter(
    private val onCharacterClicked: (CharacterVO) -> Unit,
    private val onFavoriteClicked: (CharacterVO) -> Unit
) :
    PagingDataAdapter<CharacterVO, MarvelAdapter.CharactersViewHolder>(diffCallback) {

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
            holder.bind(it, onCharacterClicked, onFavoriteClicked)
        }
    }

    inner class CharactersViewHolder(private val binding: CharacterListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: CharacterVO,
            onCharacterClick: (CharacterVO) -> Unit,
            onFavoriteClick: (CharacterVO) -> Unit
        ) {
            binding.tvCharacterName.text = item.name
            Glide.with(itemView.context)
                .load(item.thumbnail)
                .fallback(R.drawable.ic_error_image)
                .error(R.drawable.ic_error_image)
                .into(binding.ivCharacterImg)
            binding.root.setOnClickListener { onCharacterClick(item) }
            setFavorite(binding.ivFavorite, onFavoriteClick, item)

        }

    }

    private fun setFavorite(
        view: ImageView,
        onFavoriteClick: (CharacterVO) -> Unit,
        character: CharacterVO
    ) {
        view.apply {
            isSelected = character.isFavorite
            setOnClickListener {
                character.isFavorite = !character.isFavorite
                view.isSelected = character.isFavorite
                onFavoriteClick(character)
            }
        }
    }

    companion object {
        const val CHARACTERS_VIEW_TYPE = 1

        private val diffCallback = object : DiffUtil.ItemCallback<CharacterVO>() {
            override fun areItemsTheSame(
                oldItem: CharacterVO,
                newItem: CharacterVO
            ): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(
                oldItem: CharacterVO,
                newItem: CharacterVO
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}