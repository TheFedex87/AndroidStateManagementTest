package com.example.statetest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.asLiveData
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

//        mainViewModel.data.observe(this) {
//            Log.d("StateTest", it.toString())
//            it.getContentIfNotHandled()?.let { data ->
//                binding.progressBar.isVisible = data is MainViewModelState.Loading
//                if(data is MainViewModelState.Error) {
//                    Snackbar.make(binding.root, "Error: ${data.message}", Snackbar.LENGTH_LONG).show()
//                }
//                if(data is MainViewModelState.State2) {
//                    Snackbar.make(binding.root, "Emitted event 2", Snackbar.LENGTH_LONG).show()
//                }
//                data.data?.let {
//                    binding.textViewResult.text = it.toString()
//                }
//            }
//        }

//        mainViewModel.stateManager.state.observe(this) {
//            binding.progressBar.isVisible = it is MainViewModelState.Loading
//            it?.data?.let {
//                binding.textViewResult.text = it.toString()
//            }
//        }
//        mainViewModel.stateManager.event.observe(this) {
//            when(it) {
//                is MainViewModelState.Error -> {
//                    //binding.progressBar.isVisible = false
//                    Snackbar.make(binding.root, "Error: ${it.message}", Snackbar.LENGTH_LONG).show()
//                }
//                is MainViewModelState.State2 -> {
//                    Snackbar.make(binding.root, "Emitted event 2", Snackbar.LENGTH_LONG).show()
//                }
//                else -> {
//                    // Any other state is managed as State and not as Event
//                }
//            }
//        }

        mainViewModel.stateManager.singleStateEvent.observe(this) {
            when (val event = it?.event?.getContentIfNotHandled()) {
                is MainViewModelState.Error -> {
                    //binding.progressBar.isVisible = false
                    Snackbar.make(binding.root, "Error: ${event.message}", Snackbar.LENGTH_LONG).show()
                }
                is MainViewModelState.State2 -> {
                    Snackbar.make(binding.root, "Emitted event 2", Snackbar.LENGTH_LONG).show()
                }
                else -> {
                    // Any other state is managed as State and not as Event
                }
            }

            val state = it?.state
            binding.progressBar.isVisible = state is MainViewModelState.Loading
            state?.data?.let {
                binding.textViewResult.text = it.toString()
            }
        }

        binding.apply {
            buttonGetData.setOnClickListener {
                mainViewModel.getData(false)
            }

            buttonGetDataWithError.setOnClickListener {
                mainViewModel.getData(true)
            }

            buttonEmitEvent.setOnClickListener {
                mainViewModel.emitValue()
            }
        }
    }
}