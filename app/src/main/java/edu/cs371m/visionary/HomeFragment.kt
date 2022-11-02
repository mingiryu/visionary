package edu.cs371m.visionary

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import edu.cs371m.visionary.databinding.RecyclerViewBinding


class HomeFragment: Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private var _binding: RecyclerViewBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    // Set up the adapter
    private fun initAdapter(binding: RecyclerViewBinding) : RowAdapter {
        val postRowAdapter = RowAdapter(viewModel)
        val recyclerView = binding.recyclerView
        val itemDecoration = DividerItemDecoration(recyclerView.context, LinearLayoutManager.VERTICAL)

        recyclerView.adapter = postRowAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(itemDecoration)

        return postRowAdapter
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RecyclerViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(javaClass.simpleName, "onViewCreated")
        // XXX Write me
        val adapter = initAdapter(binding)
    }
}