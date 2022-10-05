package com.batara.gtihubuserdicoding.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.batara.gtihubuserdicoding.UsersViewModel
import com.batara.gtihubuserdicoding.ViewModelFactory
import com.batara.gtihubuserdicoding.data.local.entity.UsersEntity
import com.batara.gtihubuserdicoding.ui.adapter.ListUserAdapter
import com.batara.gtihubuserdicoding.databinding.FragmentBookmarkBinding
import com.batara.gtihubuserdicoding.ui.activity.DetailUserActivity


class BookmarkFragment : Fragment() {

    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookmarkBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: UsersViewModel by viewModels {
            factory
        }
        val usersAdapter = ListUserAdapter() { user ->
            if (user.isBookmarked){
                viewModel.deleteUsers(user)
            } else {
                viewModel.saveUsers(user)
            }
        }

        usersAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback{
            override fun onItemClickClicked(data: UsersEntity) {
                moveWithUsername(data)
            }
        })

        viewModel.getBookmarkedUsers().observe(viewLifecycleOwner) { bookmarkedUsers ->
            binding?.progressBar?.visibility = View.GONE
            usersAdapter.submitList(bookmarkedUsers)
        }

        binding?.rvFollowers?.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = usersAdapter
        }
    }

    private fun moveWithUsername(data: UsersEntity) {
        val moveWithUsernameIntent = Intent(activity, DetailUserActivity::class.java)
        moveWithUsernameIntent.putExtra(DetailUserActivity.EXTRA_USERNAME, data.username)
        moveWithUsernameIntent.putExtra(DetailUserActivity.EXTRA_STATUS, data.isBookmarked)
        startActivity(moveWithUsernameIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}