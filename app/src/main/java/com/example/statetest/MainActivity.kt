package com.example.statetest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.statetest.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel.data.observe(this) {
            Log.d("StateTest", it.toString())
            it.getContentIfNotHandled()?.let { data ->
                binding.progressBar.isVisible = data is Resource.Loading
                if(data is Resource.Error) {
                    Snackbar.make(binding.root, "Error: ${data.throwable!!.message}", Snackbar.LENGTH_LONG).show()
                }
                data.data?.let {
                    binding.textViewResult.text = it.toString()
                }
            }
        }

        binding.buttonGetData.setOnClickListener {
            mainViewModel.getData(false)
        }

        binding.buttonGetDataWithError.setOnClickListener {
            mainViewModel.getData(true)
        }
    }
}