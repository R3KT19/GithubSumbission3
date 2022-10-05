package com.batara.gtihubuserdicoding.ui.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.batara.gtihubuserdicoding.ui.fragment.BookmarkFragment
import com.batara.gtihubuserdicoding.ui.fragment.UsersFragment

class SectionsPagerAdapter(activity: AppCompatActivity) :FragmentStateAdapter(activity) {
    override fun getItemCount(): Int  = 2

    override fun createFragment(position: Int): Fragment {
        var fragment : Fragment? = null
        when(position){
            0 -> fragment = UsersFragment()
            1 -> fragment = BookmarkFragment()
        }
        return fragment as Fragment
    }

}