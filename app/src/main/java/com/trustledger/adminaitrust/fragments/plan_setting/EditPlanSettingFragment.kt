package com.trustledger.adminaitrust.fragments.plan_setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.trustledger.adminaitrust.Factories.PlanViewModelFactory
import com.trustledger.adminaitrust.R
import com.trustledger.adminaitrust.ViewModel.PlanViewModel
import com.trustledger.adminaitrust.databinding.FragmentEditPlanSettingBinding
import com.trustledger.adminaitrust.databinding.FragmentPlanSettingBinding
import com.trustledger.adminaitrust.models.TeamSettings
import com.trustledger.adminaitrust.repository.AppDatabase
import com.trustledger.adminaitrust.repository.PlanRepository

class EditPlanSettingFragment : Fragment() {
    private var _binding: FragmentEditPlanSettingBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: PlanViewModel
    private lateinit var database: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditPlanSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val teamSettings = arguments?.getSerializable("teamSettings") as? TeamSettings

        database = AppDatabase.getInstance(requireContext())
        val planDao = database.planDao()
        val planRepository = PlanRepository(planDao,requireContext())
        viewModel = ViewModelProvider(
            this,
            PlanViewModelFactory(requireActivity().application, planRepository)
        )[PlanViewModel::class.java]

        if (teamSettings != null) {
            binding.level.setText(teamSettings.level.toString())
            binding.profitPercentage.setText(teamSettings.profitPercentage.toString())
            binding.requiredMembers.setText(teamSettings.requiredMembers.toString())
        }
        binding.btnConfirm.setOnClickListener {
            val level = binding.level.text.toString().trim()
            val profitPercentage = binding.profitPercentage.text.toString().trim()
            val requiredMembers = binding.requiredMembers.text.toString().trim()
            if (level.isEmpty() && profitPercentage.isEmpty() && requiredMembers.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val updatedTeamSettings = TeamSettings(
                id = teamSettings?.id ?: 0,
                docId = teamSettings?.docId ?: "",
                level = level.toInt(),
                profitPercentage = profitPercentage.toDouble(),
                requiredMembers = requiredMembers.toInt()
            )
            viewModel.updateTeamSetting(updatedTeamSettings)
            Toast.makeText(requireContext(), "Level Updated", Toast.LENGTH_SHORT).show()
            findNavController().navigate(
                R.id.homeFragment,
                null,
                NavOptions.Builder()
                    .setPopUpTo(R.id.nav_graph, true)
                    .build()
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}