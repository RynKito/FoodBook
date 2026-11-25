package com.example.foodbook.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.foodbook.R
import com.example.foodbook.data.model.MealDetail
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
                    val meta = listOf(
                        m.time.takeIf { it.isNotBlank() }?.let { "⏱ $it" },
                        m.servings.takeIf { it.isNotBlank() }?.let { "👥 $it" },
                        m.level.takeIf { it.isNotBlank() }?.let { "Độ khó: $it" }
                    ).filterNotNull().joinToString("  •  ")
                    binding.tvMeta.text = meta
                    binding.tvMeta.visibility = if (meta.isBlank()) View.GONE else View.VISIBLE
                    binding.tvTip.text = "💡 Mẹo: ${m.tip}"
                    binding.tvTip.visibility = if (m.tip.isBlank()) View.GONE else View.VISIBLE
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

        binding.btnShare.setOnClickListener {
            (viewModel.detail.value as? Resource.Success)?.data?.let { shareRecipe(it) }
        }

        binding.btnFav.setOnClickListener {
            (viewModel.detail.value as? Resource.Success)?.data?.let { viewModel.toggleFavorite(it, isFav) }
        }
    }

    /** Gửi công thức dạng văn bản qua Zalo, Messenger, Gmail... bằng Intent hệ thống. */
    private fun shareRecipe(m: MealDetail) {
        val meta = listOf(
            m.time.takeIf { it.isNotBlank() }?.let { "⏱ $it" },
            m.servings.takeIf { it.isNotBlank() }?.let { "👥 $it" },
            m.level.takeIf { it.isNotBlank() }?.let { "Độ khó: $it" }
        ).filterNotNull().joinToString("  •  ")
        val text = buildString {
            appendLine("🍽 ${m.name}")
            if (meta.isNotBlank()) appendLine(meta)
            appendLine()
            appendLine("🛒 NGUYÊN LIỆU")
            m.ingredients.forEach { appendLine("• $it") }
            appendLine()
            appendLine("👩‍🍳 CÁCH LÀM")
            appendLine(m.instructions)
            if (m.tip.isNotBlank()) {
                appendLine()
                appendLine("💡 Mẹo: ${m.tip}")
            }
            appendLine()
            append("— Gửi từ ứng dụng FoodBook")
        }
        val send = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, m.name)
            putExtra(Intent.EXTRA_TEXT, text)
        }
        startActivity(Intent.createChooser(send, "Chia sẻ công thức qua"))
    }

    override fun onDestroyView() { _binding = null; super.onDestroyView() }
}
