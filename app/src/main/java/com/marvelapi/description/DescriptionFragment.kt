package com.marvelapi.description

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.marvelapi.model.CharacterVO
import com.marvelapi.viewmodel.MarvelViewModel
import com.marvelheroesapi.databinding.FragmentDescriptionBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class DescriptionFragment : Fragment() {

    private lateinit var binding: FragmentDescriptionBinding

    private val args by navArgs<DescriptionFragmentArgs>()

    private val marvelViewModel: MarvelViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentDescriptionBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        retrieveArgs()
    }

    private fun init() {
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun retrieveArgs() = with(binding) {
        var isFavorite: Boolean
        tvCharacterName.text = args.name
        if (args.description.isEmpty()) {
            tvCharacterDescription.text = "No description available"
        } else {
            tvCharacterDescription.text = args.description
        }
        isFavorite = if (args.favorite == "true") {
            ivFavorite.isSelected = true
            true
        } else {
            ivFavorite.isSelected = false
            false
        }
        ivShare.setOnClickListener {
            val textToShare = "Check out this character from Marvel Comics!"

            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, textToShare)
                type = "text/plain"
            }

            startActivity(Intent.createChooser(shareIntent, "Share via"))
        }
        ivFavorite.setOnClickListener {
            isFavorite = !isFavorite
            ivFavorite.isSelected = isFavorite
            viewLifecycleOwner.lifecycleScope.launch {
                marvelViewModel.onFavoriteClick(
                    CharacterVO(
                        args.id.toInt(),
                        args.name,
                        args.description,
                        args.img,
                        isFavorite = isFavorite,
                        resourceURI = args.resourceuri
                    )
                )
            }
        }

        Glide.with(requireContext()).load(args.img).into(ivCharacterImg)
    }
}