package com.batara.gtihubuserdicoding.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.batara.gtihubuserdicoding.R
import com.batara.gtihubuserdicoding.UserDetailResponse
import com.batara.gtihubuserdicoding.UsersViewModel
import com.batara.gtihubuserdicoding.ViewModelFactory
import com.batara.gtihubuserdicoding.databinding.ActivityDetailUserBinding
import com.batara.gtihubuserdicoding.ui.adapter.SectionDetailPagerAdapter
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

        Log.d(TAG, "onCreate: $username")

        val sectionsPagerAdapter = SectionDetailPagerAdapter(this)
        binding.viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) {tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val viewModel: UsersViewModel by viewModels {
            factory
        }

        val entity = viewModel.getDetailUsersEntity(username)

        changeIcon(entity.isBookmarked)

        binding.fabFav.setOnClickListener {
            if (entity.isBookmarked){
                viewModel.deleteUsers(entity)
            } else {
                viewModel.saveUsers(entity)
            }
            changeIcon(entity.isBookmarked)
        }

        Log.d(TAG, "onCreate: ${entity.username}")

        viewModel.getDetailUsers(username)
        viewModel.dtlUser.observe(this, { user ->
            setDetailUser(user)
        })
        viewModel.isLoading.observe(this, {
            showLoading(it)
        })
    }

    private fun changeIcon(status: Boolean) {
        val fab = binding.fabFav
        if (status){
            fab.setImageDrawable(ContextCompat.getDrawable(fab.context, R.drawable.ic_fav_fill))
        } else {
            fab.setImageDrawable(ContextCompat.getDrawable(fab.context, R.drawable.ic_fav))
        }
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
        const val EXTRA_STATUS = "extra_status"
        private val TAB_TITLES = intArrayOf(
            R.string.tab_following,
            R.string.tab_followers
        )
        private const val TAG = "DtlUser"
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}