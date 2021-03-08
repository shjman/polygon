package com.shjman.polygon.ui.home

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import com.shjman.polygon.R
import com.shjman.polygon.common.viewmodel.validation.ValidationResult
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
import java.io.File

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding by viewBinding(FragmentHomeBinding::bind)
    private val viewModel: HomeViewModel by viewModels()
    private val handlerException = CoroutineExceptionHandler { canceledContext, throwable ->
        Timber.e(throwable, "ExceptionHandler canceledContext:$canceledContext")
        binding.progress.visibility = View.GONE
        throwable.message?.let { showMessage(it) }
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
                when (validationResult) {
                    is ValidationResult.Valid -> {
                        binding.carModelLayout.error = null
                        binding.addModel.isEnabled = true
                    }
                    is ValidationResult.NotValid -> {
                        binding.addModel.isEnabled = false
                        validationResult.errorMessageRes?.let {
                            binding.carModelLayout.error = requireContext().resources.getString(it)
                        }
                    }
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
                    showMessage("success")
                }
            }
            .catch { Timber.e(it, "binding.addModel.clicks() error") }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        binding.openRefuelingStatistic.clicks()
            .onEach {
                val checkPhotoPath =
                    File("/storage/emulated/0/Android/media/com.shjman.polygon/Polygon/2021-02-24-00-56-37-764.jpg")
                val bitmap = BitmapFactory.decodeFile(checkPhotoPath.absolutePath)
                checkPhotoPath.let {
                    binding.checkPhoto.setImageBitmap(bitmap)
                }
            }
            .catch { Timber.e(it, "binding.openRefuelingStatistic.clicks() error") }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        binding.addRefueling.clicks()
            .onEach { findNavController().navigate(HomeFragmentDirections.fromHomeToRefueling()) }
            .catch { Timber.e(it, "binding.addRefueling.clicks() error") }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun showMessage(message: String) {
        view?.let { Snackbar.make(it, message, Snackbar.LENGTH_LONG).show() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        uiScope.cancel()
    }
}