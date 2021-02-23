package com.shjman.polygon.ui.home.refueling

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.shjman.polygon.R
import com.shjman.polygon.common.Constants.SIMPLE_COMMON_DATE_FORMAT
import com.shjman.polygon.databinding.FragmentRefuelingBinding
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.view.clicks
import timber.log.Timber
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class RefuelingFragment : Fragment(R.layout.fragment_refueling) {

    private val binding by viewBinding(FragmentRefuelingBinding::bind)
    private val viewModel: RefuelingViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFragmentResultListener(TAKE_PHOTO_REQUEST_KEY) { _, bundle ->
            bundle.getString(TAKEN_PHOTO_URI_BUNDLE_KEY)?.let { viewModel.photoTakenUri(it) }
        }

        binding.dateValue.text = ZonedDateTime.now().format(DateTimeFormatter.ofPattern(SIMPLE_COMMON_DATE_FORMAT))

        binding.takeCheckPhoto.clicks()
            .onEach { findNavController().navigate(RefuelingFragmentDirections.fromRefuelingToCamera()) }
            .catch { Timber.e(it, "binding.takeCheckPhoto.clicks() error") }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.photoTakenUri
            .onEach {
                binding.photoAddedIcon.visibility = View.VISIBLE
                binding.addCompletedPhoto.text = getString(R.string.refueling_edit_completed_photo)
                binding.takeCheckPhoto.text = getString(R.string.refueling_take_new_check_photo)
            }
            .catch { Timber.e(it, "viewModel.photoTakenUri error") }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    companion object {
        const val TAKE_PHOTO_REQUEST_KEY = "TAKE_PHOTO_REQUEST_KEY"
        const val TAKEN_PHOTO_URI_BUNDLE_KEY = "TAKEN_PHOTO_URI_BUNDLE_KEY"
    }
}