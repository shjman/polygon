package com.shjman.polygon.ui.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.shjman.polygon.R
import com.shjman.polygon.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.view.clicks
import reactivecircus.flowbinding.android.widget.textChanges
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding by viewBinding(FragmentHomeBinding::bind)
    private val viewModel: HomeViewModel by viewModels()
    private val handlerException = CoroutineExceptionHandler { canceledContext, throwable ->
        Timber.e(throwable, "ExceptionHandler canceledContext:$canceledContext")
        binding.progress.visibility = View.GONE
        Toast.makeText(requireContext(), throwable.message, Toast.LENGTH_LONG).show()
    }
    private val uiScope = CoroutineScope(Dispatchers.Main + SupervisorJob() + handlerException)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.carModel.textChanges()
            .skipInitialValue()
            .debounce(400L)
            .onEach { viewModel.carModelValidation.valueChanged(it.toString()) }
            .catch { Timber.e(it, "binding.carModel.textChanges() error") }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.carModelValidation.result
            .onEach { validationResult ->
                validationResult.errorMessageRes?.let {
                    binding.carModelLayout.error = requireContext().resources.getString(it)
                }
            }
            .catch { Timber.e(it, "viewModel.carModelValidation.result error") }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        binding.addModel.clicks()
            .onEach {
                uiScope.launch {
                    binding.progress.visibility = View.VISIBLE
                    viewModel.onAddModelClicked()
                    binding.progress.visibility = View.GONE
                    Toast.makeText(requireContext(), "success", Toast.LENGTH_LONG).show()
                }
            }
            .catch { Timber.e(it, "binding.addModel.clicks() error") }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        uiScope.cancel()
    }
}