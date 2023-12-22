package com.example.practicafundandroid

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.practicafundandroid.databinding.ItemHeroBinding
import com.squareup.picasso.Picasso

interface onClickLayoutItem{
    fun onClick(hero: Hero)
}

class HeroAdapter(
    private val heroList: List<Hero>,
    private val callback: onClickLayoutItem
): RecyclerView.Adapter<HeroAdapter.HerosListFragmentViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HerosListFragmentViewHolder {
        val binding = ItemHeroBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HerosListFragmentViewHolder(binding, callback)
    }

    override fun onBindViewHolder(
        holder: HerosListFragmentViewHolder,
        position: Int
    ) {
        holder.showHeros(heroList[position])
    }

    override fun getItemCount(): Int {
        return heroList.size
    }


    class HerosListFragmentViewHolder(
        private var item: ItemHeroBinding, private val callback: onClickLayoutItem
    ): RecyclerView.ViewHolder(item.root){
            fun showHeros(hero: Hero){
                item.tvName.text = hero.name
                Picasso.get().load(hero.photo).into(item.ivItem)
                item.pBList.max = hero.vidamax
                item.pBList.progress = hero.vidaactual
                item.heroItem.setOnClickListener{
                    callback.onClick(hero)
                }

                if(hero.isDead){
                    item.ivItem.alpha = 0.6F
                    item.ivItem.isClickable = false
                }else{
                    item.ivItem.foreground = ContextCompat.getDrawable(item.ivItem.context, R.drawable.gradient)
                    item.ivItem.alpha = 1.0F
                    item.heroItem.isClickable = true
                }
            }
        }

}