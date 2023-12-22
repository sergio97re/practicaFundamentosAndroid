package com.example.practicafundandroid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.practicafundandroid.databinding.FragmentDetailBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    val heroViewModel: SharedViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater)

        viewLifecycleOwner.lifecycleScope.launch {
            heroViewModel.uiState.collect{
                binding.tvNameHeroDetail.text = heroViewModel.HeroeSeleccionado.name
                Picasso.get().load(heroViewModel.HeroeSeleccionado.photo).into(binding.ivHeroDetail)
                binding.pbLife.max = heroViewModel.HeroeSeleccionado.vidamax
                binding.pbLife.progress = heroViewModel.HeroeSeleccionado.vidaactual
                binding.tvTotal.text = "${heroViewModel.HeroeSeleccionado.vidaactual}%"
            }
        }
        buttonsOnClick()
        return binding.root
    }

    private fun buttonsOnClick() {
        binding.cureButton.setOnClickListener {
            heroViewModel.actionOnClickButton(binding.cureButton.tag.toString())
        }
        binding.hitButton.setOnClickListener {
            heroViewModel.actionOnClickButton((binding.hitButton.tag.toString()))
        }
    }
}