package com.batara.gtihubuserdicoding.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import com.batara.gtihubuserdicoding.MainViewModel
import com.batara.gtihubuserdicoding.R
import com.batara.gtihubuserdicoding.UserDetailResponse
import com.batara.gtihubuserdicoding.adapter.SectionsPagerAdapter
import com.batara.gtihubuserdicoding.databinding.ActivityDetailUserBinding
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val username : String = getUsername()

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        binding.viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) {tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        val mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MainViewModel::class.java)
        mainViewModel.getDetailUser(username)
        mainViewModel.dtlUser.observe(this, {user ->
            setDetailUser(user)
        })

        mainViewModel.isLoading.observe(this, {
            showLoading(it)
        })
    }

    private fun setDetailUser(user: UserDetailResponse){
        Glide.with(this@DetailUserActivity)
            .load(user.avatarUrl)
            .circleCrop()
            .into(binding.imgDtlAvatar)

        binding.tvUsernameDetail.text = user.login
        binding.tvRealNameDetail.text = user.name
        binding.tvDtlFollowing.text = user.following.toString()
        binding.tvDtlFollowers.text = user.followers.toString()
        binding.tvDtlRepository.text = user.publicRepos.toString()
        binding.tvDtlCompany.text = user.company
        binding.tvDtlLocation.text = user.location
    }

    fun getUsername(): String {
        return intent.getStringExtra(EXTRA_USERNAME).toString()
    }

    companion object{
        const val EXTRA_USERNAME = "extra_username"
        private val TAB_TITLES = intArrayOf(
            R.string.tab_followers,
            R.string.tab_following
        )
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}