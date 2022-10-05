package com.batara.gtihubuserdicoding.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.batara.gtihubuserdicoding.UsersViewModel
import com.batara.gtihubuserdicoding.ViewModelFactory
import com.batara.gtihubuserdicoding.data.local.entity.UsersEntity
import com.batara.gtihubuserdicoding.data.repository.Result
import com.batara.gtihubuserdicoding.databinding.FragmentUsersBinding
import com.batara.gtihubuserdicoding.ui.activity.DetailUserActivity
import com.batara.gtihubuserdicoding.ui.adapter.ListUserAdapter

class UsersFragment : Fragment() {

    private var _binding: FragmentUsersBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUsersBinding.inflate(layoutInflater, container, false)
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

        binding?.rvUser?.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = usersAdapter
        }

        showUserList(viewModel, usersAdapter)

        usersAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback{
            override fun onItemClickClicked(data: UsersEntity) {
                moveWithUsername(data)
            }
        })

        binding?.inputLayout?.setEndIconOnClickListener {
            val username : String = binding?.edSearch?.text.toString()
            if (username.isEmpty()) {
                showUserList(viewModel, usersAdapter)
            }else {
                showSearchUser(viewModel, usersAdapter, username)
            }
        }
    }

    private fun showUserList(viewModel: UsersViewModel, usersAdapter: ListUserAdapter) {
        viewModel.getUsers().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding?.progressBar?.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding?.progressBar?.visibility = View.GONE
                        val usersData = result.data as MutableList<UsersEntity>
                        Log.d(TAG, "showUserList: ${usersData.size}")
                        usersAdapter.submitList(usersData){
                            usersAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
                                override fun onItemRangeInserted(
                                    positionStart: Int,
                                    itemCount: Int
                                ) {
                                    (binding?.rvUser?.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(positionStart, 0)
                                }
                            })
                        }
                        usersAdapter.notifyDataSetChanged()
                    }
                    is Result.Error -> {
                        binding?.progressBar?.visibility = View.GONE
                        Toast.makeText(
                            context,
                            "Terjadi kesalahan " + result.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun showSearchUser(
        viewModel: UsersViewModel,
        usersAdapter: ListUserAdapter,
        username: String
    ) {
        viewModel.getSearchUsers(username).observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding?.progressBar?.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding?.progressBar?.visibility = View.GONE
                        val newsData = result.data as MutableList<UsersEntity>
                        usersAdapter.submitList(newsData){
                            usersAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
                                override fun onItemRangeInserted(
                                    positionStart: Int,
                                    itemCount: Int
                                ) {
                                    (binding?.rvUser?.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(positionStart, 0)
                                }
                            })
                        }
                        usersAdapter.notifyDataSetChanged()
                    }
                    is Result.Error -> {
                        binding?.progressBar?.visibility = View.GONE
                        Toast.makeText(
                            context,
                            "Terjadi kesalahan " + result.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
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

    companion object{
        private const val TAG = "UsersFragment"
    }
}