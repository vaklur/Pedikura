package com.example.pedikura.logs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.pedikura.DataBaseHandler
import com.example.pedikura.databinding.FragmentLogBinding
import com.example.pedikura.functions.SharedPreferenceFunctions


class LogFragment : Fragment() {

    private var _binding: FragmentLogBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentLogBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = DataBaseHandler(requireContext(), SharedPreferenceFunctions().getUsername(requireContext()).toString())
        var logs = db.readLogs()

        recyclerView = binding.logRV
        attachAdapter(logs)
        toggleRecyclerView(logs)

        binding.logRefreshBTN.setOnClickListener {
            logs = db.readLogs()
            attachAdapter(logs)
        }

    }

    private fun attachAdapter(list: List<Logs>) {
        val searchAdapter = LogsAdapter(list)
        recyclerView.adapter = searchAdapter
    }

    private fun toggleRecyclerView(customerList: List<Logs>) {
        if (customerList.isEmpty()) {
            recyclerView.visibility = View.INVISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
        }
    }
}