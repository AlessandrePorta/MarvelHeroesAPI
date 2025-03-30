package com.marvelapi

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import com.marvelapi.adapter.LoadListAdapter
import com.marvelapi.adapter.MarvelAdapter
import com.marvelapi.model.CharacterVO
import com.marvelapi.viewmodel.MarvelViewModel
import com.marvelheroesapi.R
import com.marvelheroesapi.databinding.FragmentMainBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MarvelFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val marvelAdapter = MarvelAdapter(::navigateToCharacterDetails, ::onFavoriteClick)
    private val marvelViewModel: MarvelViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        (activity as MainActivity).getToolbarShow()
        setupAdapter()
        getCharacters()
        setupMenu()
        getListState()
        setRetryButton()
    }

    private fun getCharacters() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                marvelViewModel.pagingDataFlow.collect { pagingData ->
                    marvelAdapter.submitData(pagingData)
                }
            }
        }
    }

    private fun getListState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                marvelAdapter.loadStateFlow.collectLatest { state ->
                    binding.tvEmpty.isVisible =
                        state.append is LoadState.NotLoading && state.append.endOfPaginationReached && marvelAdapter.itemCount < 1
                    binding.groupError.isVisible =
                        state.refresh is LoadState.Error && marvelAdapter.itemCount < 1
                }
            }
        }
    }

    private fun setupAdapter() {
        with(binding.rvContainer) {
            val concatAdapter = marvelAdapter.withLoadStateFooter(
                footer = LoadListAdapter { marvelAdapter.retry() }
            )
            layoutManager = setupLayoutManager(concatAdapter)
            adapter = concatAdapter
        }
    }

    private fun setupLayoutManager(concatAdapter: ConcatAdapter): GridLayoutManager {
        return GridLayoutManager(requireContext(), FULL_SPAN_COUNT).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (concatAdapter.getItemViewType(position) == MarvelAdapter.CHARACTERS_VIEW_TYPE) {
                        FULL_SPAN_COUNT
                    } else {
                        SINGLE_SPAN_COUNT
                    }
                }
            }
        }
    }

    private fun setupMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.search_menu, menu)
                val searchItem = menu.findItem(R.id.search)
                val searchView = searchItem.actionView as SearchView

                searchView.isIconified = false
                searchView.requestFocus()
                searchView.queryHint = "Search"

                val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.showSoftInput(searchView, InputMethodManager.SHOW_IMPLICIT)
                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)

                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        if (!query.isNullOrEmpty()) {
                            marvelViewModel.search(query)
                        }
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        marvelViewModel.search(newText)
                        return true
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.search -> true
                    else -> false
                }
            }
        }, viewLifecycleOwner)
    }

    private fun setRetryButton() {
        binding.btnRetry.setOnClickListener {
            getCharacters()
        }
    }

    private fun onFavoriteClick(character: CharacterVO) {
        viewLifecycleOwner.lifecycleScope.launch {
            marvelViewModel.onFavoriteClick(character)
        }
    }

    private fun navigateToCharacterDetails(character: CharacterVO) {
        findNavController().navigate(
            MarvelFragmentDirections.actionMarvelFragmentToDescriptionFragment(
                id = character.id.toString(),
                name = character.name ?: "",
                description = character.description ?: "",
                favorite = character.isFavorite.toString(),
                img = character.thumbnail ?: "",
                resourceuri = character.resourceURI ?: ""
            )
        )
    }

    companion object {
        private const val FULL_SPAN_COUNT = 2
        private const val SINGLE_SPAN_COUNT = 1
    }

    override fun onResume() {
        super.onResume()
        marvelAdapter.refresh()
    }
}