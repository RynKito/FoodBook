package com.example.foodbook.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodbook.R
import com.example.foodbook.databinding.FragmentHomeBinding
import com.example.foodbook.util.Resource
import com.google.android.material.chip.Chip

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private val adapter = MealAdapter { meal ->
        findNavController().navigate(R.id.detailFragment, bundleOf("mealId" to meal.id))
    }

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(i, c, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.rvMeals.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvMeals.adapter = adapter

        // Setup Chips (Món Việt, Món Âu, Món Trung, Món Nhật)
        viewModel.categories.forEach { name ->
            val chip = Chip(requireContext()).apply {
                id = View.generateViewId()
                text = name
                isCheckable = true
                isChecked = (name == viewModel.currentCategory)
                setOnClickListener { viewModel.load(c = name) }
            }
            binding.chipGroup.addView(chip)
        }

        // Setup Dropdown (Tất cả món, Món thịt, Hải sản, Món chay, Tráng miệng)
        val spinnerAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            viewModel.dietOptions
        )
        binding.spinnerFilter.setAdapter(spinnerAdapter)
        binding.spinnerFilter.setText(viewModel.currentDietLabel, false)

        binding.spinnerFilter.setOnItemClickListener { _, _, position, _ ->
            val selectedDiet = viewModel.dietOptions[position]
            viewModel.load(dietLabel = selectedDiet)
        }

        binding.etSearch.setOnEditorActionListener { _, _, _ ->
            viewModel.search(binding.etSearch.text.toString()); true
        }

        viewModel.meals.observe(viewLifecycleOwner) { state ->
            binding.progress.visibility = if (state is Resource.Loading) View.VISIBLE else View.GONE
            binding.tvMessage.visibility = View.GONE
            when (state) {
                is Resource.Success -> {
                    adapter.submitList(state.data)
                    if (state.data.isEmpty()) showMessage("Không tìm thấy món nào")
                }
                is Resource.Error -> { adapter.submitList(emptyList()); showMessage(state.message) }
                else -> Unit
            }
        }
        if (viewModel.meals.value == null) viewModel.load()
    }

    private fun showMessage(text: String) {
        binding.tvMessage.text = text
        binding.tvMessage.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        binding.rvMeals.adapter = null
        _binding = null
        super.onDestroyView()
    }
}
