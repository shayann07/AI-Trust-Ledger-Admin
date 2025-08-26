package com.trustledger.adminaitrust.fragments.investment_plans

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.trustledger.adminaitrust.R
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.trustledger.adminaitrust.Factories.PlanViewModelFactory
import com.trustledger.adminaitrust.ViewModel.PlanViewModel
import com.trustledger.adminaitrust.adapter.PlansByCategoryAdapter
import com.trustledger.adminaitrust.databinding.FragmentPlansByCategoryBinding
import com.trustledger.adminaitrust.models.PlanModel
import com.trustledger.adminaitrust.repository.PlanRepository
import com.trustledger.adminaitrust.repository.AppDatabase
import java.util.Locale

class PlansByCategoryFragment : Fragment(), PlansByCategoryAdapter.ClickHandler {

    private lateinit var binding: FragmentPlansByCategoryBinding
    private lateinit var adapter: PlansByCategoryAdapter
    private lateinit var viewModel: PlanViewModel
    private var selectedType: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlansByCategoryBinding.inflate(inflater, container, false)
        selectedType = arguments?.getString("type")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        // Initialize the Room database and repository
        val dao = AppDatabase.getInstance(requireContext()).planDao()
        val repository = PlanRepository(dao,requireContext())
        val factory = PlanViewModelFactory(requireActivity().application, repository)
        viewModel = ViewModelProvider(this, factory)[PlanViewModel::class.java]

        viewModel.fetchPlansFromFirebase().observe(viewLifecycleOwner) { plans ->
            selectedType?.let { type ->
                val filteredPlans = plans.filter { it.type.lowercase(Locale.ROOT) == type }
                adapter.updateList(filteredPlans)
            }
        }
//        viewModel.allPlans.observe(viewLifecycleOwner) { plans ->
//            Toast.makeText(requireContext(), "Plans: ${plans.size}", Toast.LENGTH_SHORT).show()
//            selectedType?.let { type ->
//                val filteredPlans = plans.filter { it.type.toLowerCase(Locale.ROOT) == type }
//                adapter.updateList(filteredPlans)
//            }
//        }
        // Optional: refresh Firebase data
        viewModel.refreshData()

    }

    private fun setupRecyclerView() {
        binding.plansRCV.layoutManager = LinearLayoutManager(requireContext())
        adapter = PlansByCategoryAdapter(emptyList(),this)
        binding.plansRCV.adapter = adapter
    }

    override fun onClick(planModel: PlanModel) {
        val bundle = Bundle()
        bundle.putSerializable("plan", planModel)
        findNavController().navigate(R.id.action_plansByCategoryFragment_to_planDetailFragment, bundle)
    }
}
