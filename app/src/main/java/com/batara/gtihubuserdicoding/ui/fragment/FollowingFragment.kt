package com.batara.gtihubuserdicoding.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.batara.gtihubuserdicoding.UsersResponseItem
import com.batara.gtihubuserdicoding.UsersViewModel
import com.batara.gtihubuserdicoding.ViewModelFactory
import com.batara.gtihubuserdicoding.ui.activity.DetailUserActivity
import com.batara.gtihubuserdicoding.ui.adapter.ListFollowAdapter
import com.batara.gtihubuserdicoding.databinding.FragmentFollowingBinding

class FollowingFragment : Fragment() {
    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        showFollowingList()
        return binding.root
    }

    private fun showFollowingList() {
        val layoutManager = LinearLayoutManager(activity)
        binding.rvFollowing.layoutManager = layoutManager
        val username : String = (activity as DetailUserActivity).getUsername()

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: UsersViewModel by viewModels {
            factory
        }

        viewModel.getFollowingList(username)
        viewModel.listUser.observe(viewLifecycleOwner, {user ->
            setUserDetailData(user)
        })

        viewModel.isLoading.observe(viewLifecycleOwner, {
            showLoading(it)
        })

    }

    private fun setUserDetailData(userData: List<UsersResponseItem>) {
        val listFollowing = ArrayList<UsersResponseItem>()
        for (user in userData) {
            listFollowing.add(user)
        }
        val adapter = ListFollowAdapter(listFollowing)
        binding.rvFollowing.setHasFixedSize(true)
        binding.rvFollowing.adapter = adapter
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