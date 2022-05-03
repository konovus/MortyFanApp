package com.konovus.simplemortyr.ui.mainScreen

import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging3.PagingDataEpoxyController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.konovus.simplemortyr.R
import com.konovus.simplemortyr.databinding.ItemMortyCharacterBinding
import com.konovus.simplemortyr.entity.Character
import com.konovus.simplemortyr.util.ViewBindingKotlinModel

class CharactersEpoxyController : PagingDataEpoxyController<Character>(){


    override fun buildItemModel(currentPosition: Int, item: Character?): EpoxyModel<*> {
        return CharacterItemEpoxyModel(
            character = item!!,
            onClick = {

            }
        ).id(item.id)
    }

    data class CharacterItemEpoxyModel(
        val character: Character,
        val onClick: (Character) -> Unit
    ): ViewBindingKotlinModel<ItemMortyCharacterBinding>(R.layout.item_morty_character) {

        override fun ItemMortyCharacterBinding.bind() {
            Glide.with(image)
                .load(character.image)
                .transition(
                    DrawableTransitionOptions.withCrossFade())
                .placeholder(R.color.light_gray)
                .into(image)
            name.text = character.name
        }
    }
}