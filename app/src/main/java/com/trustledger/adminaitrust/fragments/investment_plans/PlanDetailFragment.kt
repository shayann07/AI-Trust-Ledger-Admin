package com.trustledger.adminaitrust.fragments.investment_plans

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.trustledger.adminaitrust.Factories.PlanViewModelFactory
import com.trustledger.adminaitrust.ViewModel.PlanViewModel
import com.trustledger.adminaitrust.databinding.FragmentPlanDetailBinding
import com.trustledger.adminaitrust.models.PlanModel
import com.trustledger.adminaitrust.repository.AppDatabase
import com.trustledger.adminaitrust.repository.PlanRepository

class PlanDetailFragment : Fragment() {

    private var _binding: FragmentPlanDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: PlanViewModel
    private lateinit var database: AppDatabase
    private var selectedType = "Stocks"
    private var planModel: PlanModel? = null // Nullable now to check edit/add mode

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlanDetailBinding.inflate(inflater, container, false)
        planModel = arguments?.getSerializable("plan") as? PlanModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = AppDatabase.getInstance(requireContext())
        val planDao = database.planDao()
        val planRepository = PlanRepository(planDao, requireContext())

        viewModel = ViewModelProvider(
            this,
            PlanViewModelFactory(requireActivity().application, planRepository)
        )[PlanViewModel::class.java]

        setupSpinner()

        // Check if in edit mode
        if (planModel != null) {
            binding.etPlanName.setText(planModel!!.planName)
            binding.etMinAmount.setText(planModel!!.minAmount.toString())
            binding.etPlanDays.setText(planModel!!.planDays.toString())
            binding.etDailyPercentage.setText(planModel!!.dailyPercentage.toString())
            selectedType = planModel!!.type // update selectedType to current type
        }

        setupClickListener()
    }

    private fun setupSpinner() {
        val typeList = listOf("Stocks", "Medicine", "Forex")
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            typeList
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerType.adapter = adapter

        val selectedIndex = typeList.indexOf(planModel?.type ?: "Stocks")
        if (selectedIndex != -1) {
            binding.spinnerType.setSelection(selectedIndex)
            selectedType = typeList[selectedIndex]
        }

        binding.spinnerType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                selectedType = parent?.getItemAtPosition(pos).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupClickListener() {
        binding.btnConfirm.setOnClickListener {
            val name = binding.etPlanName.text.toString()
            val minAmount = binding.etMinAmount.text.toString().toIntOrNull() ?: 0
            val days = binding.etPlanDays.text.toString().toIntOrNull() ?: 0
            val daily = binding.etDailyPercentage.text.toString().toFloatOrNull() ?: 0f

            if (name.isEmpty() || minAmount == 0 || days == 0 || daily == 0f ) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val plan = PlanModel(
                id = planModel?.id ?: 0, // If editing, use existing ID
                docId = planModel?.docId ?: "",
                type = selectedType,
                planName = name,
                minAmount = minAmount,
                planDays = days,
                dailyPercentage = daily
                // timestamp = System.currentTimeMillis() if needed
            )

            context?.let {
                if (planModel == null) {
                    viewModel.insertPlan(plan, it)
                    Toast.makeText(it, "Plan Saved!", Toast.LENGTH_SHORT).show()
                } else {

                    viewModel.updatePlan(plan)
                    Toast.makeText(it, "Plan Updated!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
