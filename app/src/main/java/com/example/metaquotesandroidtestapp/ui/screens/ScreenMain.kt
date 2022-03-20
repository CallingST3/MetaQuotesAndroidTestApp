package com.example.metaquotesandroidtestapp.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.metaquotesandroidtestapp.R
import com.example.metaquotesandroidtestapp.databinding.ScreenMainBinding
import com.example.metaquotesandroidtestapp.extensions.Extensions.invisible
import com.example.metaquotesandroidtestapp.logic.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ScreenMain : Fragment() {

    private lateinit var binding: ScreenMainBinding
    private lateinit var adapter: ArrayAdapter<String>

    private val viewModel: MainViewModel by viewModels()

    //region ******************** OVERRIDE *********************************************************

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = ScreenMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    //endregion OVERRIDE

    //region ******************** INIT *************************************************************

    private fun init() {
        initViews()
        collect()
    }

    private fun initViews() {
        adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_activated_1)
        binding.listView.adapter = adapter
        binding.listView.setOnItemClickListener { _, _, _, _ ->
            binding.buttonCopy.invisible(binding.listView.checkedItemCount == 0)
        }

        binding.buttonStart.setOnClickListener {
            if(binding.editUrl.text.isEmpty() || binding.editFilter.text.isEmpty()) toast(getString(R.string.main_screen_error_text_empty))
            else viewModel.loadData(binding.editUrl.text.toString(), binding.editFilter.text.toString())
        }

        binding.buttonCopy.setOnClickListener {
            viewModel.copy(binding.listView.checkedItemPositions, adapter)
            toast(getString(R.string.main_screen_copied_to_clipboard))
        }
    }

    private fun collect() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.source.collect { event ->
                when(event) {
                    is MainViewModel.Event.Result -> {
                        adapter.add(event.result)
                    }
                    is MainViewModel.Event.Failure -> {
                        binding.buttonStart.text = getString(R.string.main_screen_button_start)
                        binding.loader.invisible(true)
                        toastNoEmpty(event.errorText, getString(R.string.main_screen_error_data))
                    }
                    is MainViewModel.Event.Complete -> {
                        binding.buttonStart.text = getString(R.string.main_screen_button_start)
                        binding.loader.invisible(true)
                    }
                    is MainViewModel.Event.Loading -> {
                        adapter.clear()
                        binding.buttonStart.text = getString(R.string.main_screen_button_stop)
                        binding.loader.invisible(false)
                        binding.buttonCopy.invisible(true)
                        binding.listView.clearChoices()
                    }
                }
            }
        }
    }

    //endregion INIT

    //region ******************** HELPERS **********************************************************

    private fun toast(text: String) {
        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()
    }

    private fun toastNoEmpty(text1: String?, text2: String) {
        val text = if(text1.isNullOrEmpty()) text2 else text1
        toast(text)
    }

    //endregion HELPERS
}