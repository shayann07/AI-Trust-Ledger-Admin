package com.trustledger.adminaitrust.fragments.plan_setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trustledger.adminaitrust.R
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.trustledger.adminaitrust.Factories.PlanViewModelFactory
import com.trustledger.adminaitrust.Utils
import com.trustledger.adminaitrust.ViewModel.PlanViewModel
import com.trustledger.adminaitrust.adapter.TeamSettingsAdapter
import com.trustledger.adminaitrust.databinding.FragmentPlanSettingBinding
import com.trustledger.adminaitrust.models.TeamSettings
import com.trustledger.adminaitrust.repository.AppDatabase
import com.trustledger.adminaitrust.repository.PlanRepository

class PlanSettingFragment : Fragment(), TeamSettingsAdapter.ClickHandler {
    private var _binding: FragmentPlanSettingBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: PlanViewModel
    private lateinit var database: AppDatabase
    private lateinit var teamSettingsAdapter: TeamSettingsAdapter
    private lateinit var utils: Utils


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlanSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        utils = Utils(requireContext())
        database = AppDatabase.getInstance(requireContext())
        val planDao = database.planDao()
        val planRepository = PlanRepository(planDao, requireContext())
        viewModel = ViewModelProvider(
            this,
            PlanViewModelFactory(requireActivity().application, planRepository)
        )[PlanViewModel::class.java]
        setupRecyclerView()
        utils.startLoadingAnimation()
        viewModel.fetchTeamSettingsFromRoom().observe(viewLifecycleOwner) { teamSettings ->
            val sortedList = teamSettings.sortedBy { it.level }
            utils.endLoadingAnimation()
            teamSettingsAdapter.updateData(sortedList)
        }

        binding.floatingButton.setOnClickListener {
            findNavController().navigate(R.id.action_planSettingFragment_to_editPlanSettingFragment)
        }
    }

    private fun setupRecyclerView() {
        binding.planSettingRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        teamSettingsAdapter = TeamSettingsAdapter(emptyList(), this)
        binding.planSettingRecyclerView.adapter = teamSettingsAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onEditClick(teamSettings: TeamSettings) {
        val bundle = Bundle()
        bundle.putSerializable("teamSettings", teamSettings)
        findNavController().navigate(
            R.id.action_planSettingFragment_to_editPlanSettingFragment,
            bundle
        )
    }
}
