package com.batara.gtihubuserdicoding.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.batara.gtihubuserdicoding.adapter.ListUserAdapter
import com.batara.gtihubuserdicoding.MainViewModel
import com.batara.gtihubuserdicoding.UsersResponseItem
import com.batara.gtihubuserdicoding.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showRecyclerList()
    }

    private fun showRecyclerList() {
        val layoutManager = LinearLayoutManager(this@MainActivity)
        binding.rvUser.layoutManager = layoutManager
        binding.rvUser.setHasFixedSize(true)

        val mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MainViewModel::class.java)
        mainViewModel.getUser()
        mainViewModel.listUser.observe(this, {user ->
            setUserData(user)
        })

        binding.inputLayout.setEndIconOnClickListener(){
            val username:String = binding.edSearch.text.toString()
            if (username.isEmpty()) {
                mainViewModel.getUser()
                mainViewModel.listUser.observe(this, {user ->
                    setUserData(user)
                })
            }
            mainViewModel.getSearch(username)
            mainViewModel.listUser.observe(this, {user ->
                setUserData(user)
            })
        }

        mainViewModel.isLoading.observe(this, {
            showLoading(it)
        })
    }

    private fun setUserData(userData: List<UsersResponseItem>) {
        val listUser = ArrayList<UsersResponseItem>()
        for (user in userData) {
            listUser.add(user)
        }
        val adapter = ListUserAdapter(listUser)
        binding.rvUser.adapter = adapter
        adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback{
            override fun onItemClickClicked(data: UsersResponseItem) {
                moveWithUsername(data)
            }
        })
    }

    private fun moveWithUsername(data: UsersResponseItem) {
        val moveWithUsernameIntent = Intent(this@MainActivity, DetailUserActivity::class.java)
        moveWithUsernameIntent.putExtra(DetailUserActivity.EXTRA_USERNAME, data.login)
        startActivity(moveWithUsernameIntent)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}