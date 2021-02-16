package com.shjman.polygon.ui.home.refueling

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.shjman.polygon.R
import com.shjman.polygon.databinding.FragmentRefuelingBinding

class RefuelingFragment : Fragment(R.layout.fragment_refueling) {

    private val binding by viewBinding(FragmentRefuelingBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}