package com.konovus.simplemortyr.ui.pagerScreen

import android.app.SharedElementCallback
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.transition.TransitionInflater
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.konovus.simplemortyr.R
import com.konovus.simplemortyr.ui.detailsScreen.DetailsFragment
import com.konovus.simplemortyr.ui.mainScreen.MainFragment.Companion.characters
import com.konovus.simplemortyr.ui.mainScreen.MainFragment.Companion.currentPosition
import com.konovus.simplemortyr.ui.mainScreen.MainFragment.Companion.rvPos
import com.konovus.simplemortyr.ui.mainScreen.MainViewModel
import com.konovus.simplemortyr.ui.mainScreen.TAG
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PagerFragment : Fragment(R.layout.pager_fragment) {

    private lateinit var viewPager: ViewPager2
    private val viewModel: MainViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.pager_fragment, container, false)

        viewPager = view.findViewById(R.id.viewPager)
        viewPager.adapter = MyPagerAdapter(this)

        viewPager.setCurrentItem(currentPosition, false)
        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                currentPosition = position
            }
        })

        prepareTransitions()
        if(savedInstanceState == null)
            postponeEnterTransition()

        return view
    }

    private fun prepareTransitions() {
        sharedElementEnterTransition = TransitionInflater.from(context)
            .inflateTransition(R.transition.image_shared_element_transition)
        // A similar mapping is set at the ImagePagerFragment with a setEnterSharedElementCallback.
        setEnterSharedElementCallback(object : SharedElementCallback(){
            override fun onMapSharedElements(
                names: MutableList<String>,
                sharedElements: MutableMap<String, View>
            ) {
                val currentFragment = viewPager[0]
                sharedElements[names[0]] = currentFragment.findViewById(R.id.image_details);
            }
        })
    }

    private inner class MyPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = characters.size
        override fun createFragment(position: Int): Fragment = DetailsFragment.newInstance(
            characters[position])

    }
}