package com.shjman.polygon.ui.home.refueling

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import com.shjman.polygon.R
import com.shjman.polygon.common.Constants.SIMPLE_COMMON_DATE_FORMAT
import com.shjman.polygon.databinding.FragmentRefuelingBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.view.clicks
import timber.log.Timber
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class RefuelingFragment : Fragment(R.layout.fragment_refueling) {

    private val binding by viewBinding(FragmentRefuelingBinding::bind)
    private val viewModel: RefuelingViewModel by viewModels()
    var photoTakenUri: String? = null
    private val handlerException = CoroutineExceptionHandler { canceledContext, throwable ->
        Timber.e(throwable, "ExceptionHandler canceledContext:$canceledContext")
        binding.progress.visibility = View.GONE
        throwable.message?.let { showMessage(it) }
    }

    private val uiScope = CoroutineScope(Dispatchers.Main + SupervisorJob() + handlerException)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFragmentResultListener(TAKE_PHOTO_REQUEST_KEY) { _, bundle ->
            bundle.getString(TAKEN_PHOTO_URI_BUNDLE_KEY)?.let {
                photoTakenUri = it
                viewModel.photoTakenUri(it)
            }
        }

        val activeViews = listOf(binding.dateValue, binding.mileageLayout, binding.fuelTypeLayout, binding.saveData,
            binding.numberLitersLayout, binding.priceLayout, binding.takeCheckPhoto, binding.addCompletedPhoto)

        binding.takeCheckPhoto.clicks()
            .onEach { findNavController().navigate(RefuelingFragmentDirections.fromRefuelingToCamera()) }
            .catch { Timber.e(it, "binding.takeCheckPhoto.clicks() error") }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        binding.saveData.clicks()
            .combine(viewModel.refuelingDate) { _, zonedDateTime -> zonedDateTime }
            .onEach {
                uiScope.launch {
                    showProgress(activeViews)
                    viewModel.saveData(
                        it.toInstant(),
                        binding.mileageValue.text.toString().toInt(),
                        binding.fuelTypeValue.text.toString(),
                        binding.numberLitersValue.text.toString().toDouble(),
                        binding.priceValue.text.toString().toDouble(),
                        photoTakenUri,
                    )
                    hideProgress(activeViews)
                }
            }
            .catch { Timber.e(it, "binding.saveData.clicks() error") }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.takenPhotoUri
            .onEach {
                binding.photoAddedIcon.visibility = View.VISIBLE
                binding.addCompletedPhoto.text = getString(R.string.refueling_edit_completed_photo)
                binding.takeCheckPhoto.text = getString(R.string.refueling_take_new_check_photo)
            }
            .catch { Timber.e(it, "viewModel.photoTakenUri error") }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.refuelingDate
            .onEach { binding.dateValue.text = it.format(DateTimeFormatter.ofPattern(SIMPLE_COMMON_DATE_FORMAT)) }
            .catch { Timber.e(it, "viewModel.refuelingDate error") }
            .launchIn(viewLifecycleOwner.lifecycleScope)
        viewModel.refuelingDate(ZonedDateTime.now())
    }

    private fun showProgress(activeViews: List<View>) {
        binding.progress.visibility = View.VISIBLE
        activeViews.forEach { it.isEnabled = false }
    }

    private fun hideProgress(activeViews: List<View>) {
        binding.progress.visibility = View.GONE
        activeViews.forEach { it.isEnabled = true }
    }

    private fun showMessage(message: String) {
        view?.let { Snackbar.make(it, message, Snackbar.LENGTH_LONG).show() }
    }

    companion object {
        const val TAKE_PHOTO_REQUEST_KEY = "TAKE_PHOTO_REQUEST_KEY"
        const val TAKEN_PHOTO_URI_BUNDLE_KEY = "TAKEN_PHOTO_URI_BUNDLE_KEY"
    }
}