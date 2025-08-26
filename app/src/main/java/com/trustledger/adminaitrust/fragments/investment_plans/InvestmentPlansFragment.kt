package com.trustledger.adminaitrust.fragments.investment_plans

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.trustledger.adminaitrust.R
import com.trustledger.adminaitrust.databinding.FragmentInvestmentPlansBinding

class InvestmentPlansFragment : Fragment() {
    private lateinit var binding: FragmentInvestmentPlansBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInvestmentPlansBinding.inflate(inflater, container, false)

        binding.floatingButton.setOnClickListener {
            findNavController().navigate(R.id.action_investmentPlansFragment_to_planDetailFragment)
        }

        val bundle = Bundle()
        binding.btnforex.setOnClickListener {
            bundle.putString("type", "forex")
            findNavController().navigate(R.id.action_investmentPlansFragment_to_plansByCategoryFragment, bundle)
        }
        binding.btnStocks.setOnClickListener {
            bundle.putString("type", "stocks")
            findNavController().navigate(R.id.action_investmentPlansFragment_to_plansByCategoryFragment, bundle)
        }
        binding.btnMedicine.setOnClickListener {
            bundle.putString("type", "medicine")
            findNavController().navigate(R.id.action_investmentPlansFragment_to_plansByCategoryFragment, bundle)
        }
        return binding.root
    }
}
