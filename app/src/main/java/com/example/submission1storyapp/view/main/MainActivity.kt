package com.example.submission1storyapp.view.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submission1storyapp.adapter.StateLoadAdapter
import com.example.submission1storyapp.adapter.StoryListAdapter
import com.example.submission1storyapp.data.database.Entities
import com.example.submission1storyapp.databinding.ActivityMainBinding
import com.example.submission1storyapp.view.Map.MapsActivity
import com.example.submission1storyapp.view.ViewModelFactory
import com.example.submission1storyapp.view.addstory.AddStoryActivity
import com.example.submission1storyapp.view.detailstory.DetailStoryActivity
import com.example.submission1storyapp.view.login.LoginActivity

class MainActivity : AppCompatActivity(), StoryListAdapter.OnItemClickListener {

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvListstory.layoutManager = LinearLayoutManager(this)

        getData()

        setupAction()
    }

    private fun getData() {
        val adapter = StoryListAdapter(this)
        binding.rvListstory.adapter = adapter.withLoadStateFooter(
            footer = StateLoadAdapter {
                adapter.retry()
            }
        )
        viewModel.storyPage.observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }


    private fun setupAction() {
        binding.imgLogout.setOnClickListener {
            viewModel.logout()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
        }

        binding.imgMaps.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onItemClick(item: Entities) {
        val id = item.idStory
        val intent = Intent(this, DetailStoryActivity::class.java)
        intent.putExtra(DetailStoryActivity.EXTRA_NAME, id)
        startActivity(intent)
    }

    companion object {

        fun start(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }
}