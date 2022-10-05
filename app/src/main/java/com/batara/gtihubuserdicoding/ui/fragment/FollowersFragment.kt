package com.batara.gtihubuserdicoding.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.batara.gtihubuserdicoding.UsersResponseItem
import com.batara.gtihubuserdicoding.UsersViewModel
import com.batara.gtihubuserdicoding.ViewModelFactory
import com.batara.gtihubuserdicoding.ui.activity.DetailUserActivity
import com.batara.gtihubuserdicoding.databinding.FragmentFollowersBinding
import com.batara.gtihubuserdicoding.ui.adapter.ListFollowAdapter

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

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: UsersViewModel by viewModels {
            factory
        }

        viewModel.getFollowersList(username)
        viewModel.listUser.observe(viewLifecycleOwner, {user ->
            setUserDetailData(user)
        })

        viewModel.isLoading.observe(viewLifecycleOwner, {
            showLoading(it)
        })

    }

    private fun setUserDetailData(userData: List<UsersResponseItem>) {
        val listFollower = ArrayList<UsersResponseItem>()
        for (user in userData) {
            listFollower.add(user)
        }
        val adapter = ListFollowAdapter(listFollower)
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