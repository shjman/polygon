package com.shjman.polygon.ui.splash

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.shjman.polygon.R
import com.shjman.polygon.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.view.clicks
import timber.log.Timber

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {

    private val binding by viewBinding(FragmentSplashBinding::bind)
    private val viewModel by viewModels<SplashViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.launchMatrix.clicks()
            .onEach { startActivity(Intent(context, Class.forName("com.shjman.android_academy.MatrixEffectActivity"))) }
            .catch { cause -> Timber.e(cause, " error while startActivity(intent)") }
            .launchIn(lifecycleScope)

        viewModel.navigationDelay
            .onEach {
                delay(1000)
                findNavController().navigate(SplashFragmentDirections.fromSplashToHome())
            }
            .catch { Timber.e("error while handle viewModel.navigationDelay") }
            .launchIn(lifecycleScope)
    }
}