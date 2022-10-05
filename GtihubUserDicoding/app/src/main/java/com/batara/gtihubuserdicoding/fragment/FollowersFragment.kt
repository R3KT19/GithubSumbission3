package com.batara.gtihubuserdicoding.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.batara.gtihubuserdicoding.MainViewModel
import com.batara.gtihubuserdicoding.UsersResponseItem
import com.batara.gtihubuserdicoding.activity.DetailUserActivity
import com.batara.gtihubuserdicoding.adapter.ListFollowerAdapter
import com.batara.gtihubuserdicoding.databinding.FragmentFollowersBinding

class FollowersFragment : Fragment() {
    private var _binding: FragmentFollowersBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowersBinding.inflate(inflater, container, false)
        showFollowersList()
        return binding.root
    }

    private fun showFollowersList() {
        val layoutManager = LinearLayoutManager(activity)
        binding.rvFollowers.layoutManager = layoutManager
        val username : String = (activity as DetailUserActivity).getUsername()

        val mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MainViewModel::class.java)

        mainViewModel.getFollowersList(username)
        mainViewModel.listUser.observe(viewLifecycleOwner, {user ->
            setUserDetailData(user)
        })

        mainViewModel.isLoading.observe(viewLifecycleOwner, {
            showLoading(it)
        })

    }

    private fun setUserDetailData(userData: List<UsersResponseItem>) {
        val listFollower = ArrayList<UsersResponseItem>()
        for (user in userData) {
            listFollower.add(user)
        }
        val adapter = ListFollowerAdapter(listFollower)
        binding.rvFollowers.setHasFixedSize(true)
        binding.rvFollowers.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}