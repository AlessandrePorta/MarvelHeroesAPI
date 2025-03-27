package com.marvelapi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.marvelheroesapi.databinding.LoadListItemBinding

class LoadListAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<LoadListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = LoadListItemBinding.inflate(inflater, parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        loadState: LoadState
    ) = holder.bind(loadState)

    inner class ViewHolder(
        private val binding: LoadListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(loadState: LoadState) = with(binding) {
            progressBarLoadingMore.isVisible = loadState is LoadState.Loading
            textTryAgain.isVisible = loadState is LoadState.Error
            textTryAgain.setOnClickListener { retry() }
        }
    }
}