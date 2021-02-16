package com.shjman.polygon.ui.home.refueling

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.shjman.polygon.R
import com.shjman.polygon.databinding.FragmentRefuelingBinding
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.view.clicks
import timber.log.Timber

class RefuelingFragment : Fragment(R.layout.fragment_refueling) {

    private val binding by viewBinding(FragmentRefuelingBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.takeCheckPhoto.clicks()
            .onEach { findNavController().navigate(RefuelingFragmentDirections.fromRefuelingToCamera()) }
            .catch { Timber.e(it, "binding.takeCheckPhoto.clicks() error") }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }
}