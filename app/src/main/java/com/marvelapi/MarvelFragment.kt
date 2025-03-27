package com.marvelapi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import com.marvelapi.adapter.LoadListAdapter
import com.marvelapi.adapter.MarvelAdapter
import com.marvelapi.adapter.MarvelAdapter.Companion.CHARACTERS_VIEW_TYPE
import com.marvelapi.model.Character
import com.marvelapi.services.response.CharactersResponse
import com.marvelapi.viewmodel.MarvelViewModel
import com.marvelheroesapi.R
import com.marvelheroesapi.databinding.FragmentMainBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MarvelFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    private val marvelAdapter = MarvelAdapter(::navigateToCharacterDetails)

    private val marvelViewModel : MarvelViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
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
    }

    private fun getCharacters() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                marvelViewModel.pagingDataFlow.collect {
                    marvelAdapter.submitData(it)
                }
            }
        }
    }

    private fun setupAdapter() {
        with(binding.rvContainer) {
            val concatAdapter = marvelAdapter.withLoadStateFooter(
                footer = LoadListAdapter {
                    marvelAdapter.retry()
                }
            )
            layoutManager = setupLayoutManager(concatAdapter)
            adapter = concatAdapter
        }
    }

    private fun setupLayoutManager(concatAdapter: ConcatAdapter): GridLayoutManager {
        return GridLayoutManager(requireContext(), FULL_SPAN_COUNT).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (concatAdapter.getItemViewType(position) == CHARACTERS_VIEW_TYPE) {
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
                menu.clear()
                menuInflater.inflate(R.menu.search_menu, menu)
                val searchItem = menu.findItem(R.id.search)
                val searchView = searchItem.actionView as SearchView
                searchItem.setIcon(R.drawable.ic_search)
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
//                        marvelViewModel.search(newText)
                        return true
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.search -> {
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner)
    }

    private fun navigateToCharacterDetails(character: CharactersResponse) {
//        findNavController()
//            .navigate(
//                MarvelFragmentDirections(
//                    character.id.toString()
//                )
//            )
    }

    companion object {
        private const val FULL_SPAN_COUNT = 2
        private const val SINGLE_SPAN_COUNT = 1
    }
}