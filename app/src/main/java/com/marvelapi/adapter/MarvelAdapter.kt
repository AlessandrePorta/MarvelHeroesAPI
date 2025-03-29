package com.marvelapi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.marvelapi.database.CharacterEntity
import com.marvelapi.model.CharacterVO
import com.marvelheroesapi.databinding.CharacterListItemBinding

class MarvelAdapter(
    private val onCharacterClicked: (CharacterVO) -> Unit
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
            holder.bind(it, onCharacterClicked)
        }
    }

    inner class CharactersViewHolder(private val binding: CharacterListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: CharacterVO,
            onCharacterClick: (CharacterVO) -> Unit
        ) {
            binding.tvCharacterName.text = item.name
            Glide.with(itemView.context)
                .load(item.thumbnail)
                .into(binding.ivCharacterImg)
            binding.root.setOnClickListener { onCharacterClick(item) }
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