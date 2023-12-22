package com.example.practicafundandroid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.practicafundandroid.databinding.FragmentHerosListBinding
import kotlinx.coroutines.launch


class HerosListFragment : Fragment(),onClickLayoutItem {

    private lateinit var binding: FragmentHerosListBinding
    private val viewShareModel: SharedViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHerosListBinding.inflate(inflater)
        setCureAllHeros()

        val heroAdapter = HeroAdapter(viewShareModel.herosList, this)
        binding.rvHerosList.layoutManager = GridLayoutManager(binding.rvHerosList.context, 2)
        binding.rvHerosList.adapter = heroAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewShareModel.uiState.collect{
                heroAdapter.notifyDataSetChanged()
            }
        }

        return binding.root

    }

    override fun onClick(hero: Hero) {
        viewShareModel.selectedHeroToDetail(hero)
    }

    private fun setCureAllHeros(){
        binding.cureButtonList.setOnClickListener {
            viewShareModel.cureAllHeros()
            Toast.makeText(binding.cureButtonList.context, "Todos los personajes han sido curados", Toast.LENGTH_LONG).show()
        }
    }

}