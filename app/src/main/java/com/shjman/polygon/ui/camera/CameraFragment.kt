package com.shjman.polygon.ui.camera

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.shjman.polygon.R
import com.shjman.polygon.databinding.FragmentCameraBinding

class CameraFragment : Fragment(R.layout.fragment_camera) {

    private val binding by viewBinding(FragmentCameraBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}