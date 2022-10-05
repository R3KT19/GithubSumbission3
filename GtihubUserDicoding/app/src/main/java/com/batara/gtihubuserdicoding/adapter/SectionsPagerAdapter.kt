package com.batara.gtihubuserdicoding.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.batara.gtihubuserdicoding.fragment.FollowersFragment
import com.batara.gtihubuserdicoding.fragment.FollowingFragment

class SectionsPagerAdapter(activity: AppCompatActivity) :FragmentStateAdapter(activity) {
    override fun getItemCount(): Int  = 2

    override fun createFragment(position: Int): Fragment {
        var fragment : Fragment? = null
        when(position){
            0 -> fragment = FollowersFragment()
            1 -> fragment = FollowingFragment()
        }
        return fragment as Fragment
    }

}