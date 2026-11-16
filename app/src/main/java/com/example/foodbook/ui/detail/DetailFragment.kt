package com.example.foodbook.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.foodbook.R
import com.example.foodbook.databinding.FragmentDetailBinding
import com.example.foodbook.util.Resource

class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailViewModel by viewModels()
    private var isFav = false

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _binding = FragmentDetailBinding.inflate(i, c, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val mealId = requireArguments().getString("mealId").orEmpty()
        viewModel.load(mealId)

        viewModel.detail.observe(viewLifecycleOwner) { state ->
            binding.progress.visibility = if (state is Resource.Loading) View.VISIBLE else View.GONE
            binding.content.visibility = if (state is Resource.Success) View.VISIBLE else View.GONE
            binding.tvError.visibility = if (state is Resource.Error) View.VISIBLE else View.GONE
            when (state) {
                is Resource.Success -> {
                    val m = state.data
                    binding.tvName.text = m.name
                    binding.tvInfo.text = "${m.category} • ${m.area}"
                    binding.tvIngredients.text = m.ingredients.joinToString("\n") { "• $it" }
                    binding.tvInstructions.text = m.instructions
                    Glide.with(this).load(m.thumb.ifBlank { null })
                        .placeholder(R.drawable.bg_placeholder).fallback(R.drawable.bg_placeholder).error(R.drawable.bg_placeholder)
                        .centerCrop().into(binding.ivThumb)
                }
                is Resource.Error -> binding.tvError.text = state.message
                else -> Unit
            }
        }

        viewModel.isFavorite(mealId).observe(viewLifecycleOwner) { fav ->
            isFav = fav
            binding.btnFav.text = if (fav) getString(R.string.remove_favorite) else getString(R.string.add_favorite)
        }

        binding.btnFav.setOnClickListener {
            (viewModel.detail.value as? Resource.Success)?.data?.let { viewModel.toggleFavorite(it, isFav) }
        }
    }

    override fun onDestroyView() { _binding = null; super.onDestroyView() }
}
