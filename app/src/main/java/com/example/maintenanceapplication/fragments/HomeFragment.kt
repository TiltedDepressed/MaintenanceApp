package com.example.maintenanceapplication.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.maintenanceapplication.activities.CreateRequestActivity
import com.example.maintenanceapplication.adapters.RequestsAdapter
import com.example.maintenanceapplication.databinding.FragmentHomeBinding
import com.example.maintenanceapplication.viewModel.HomeViewModel
import com.google.gson.JsonObject


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private lateinit var sharePreference: SharedPreferences

    private lateinit var editor: SharedPreferences.Editor

    private lateinit var requestAdapter: RequestsAdapter

    private lateinit var homeViewModel: HomeViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharePreference = this.requireActivity().getSharedPreferences("MY_PRE", Context.MODE_PRIVATE)
        editor = sharePreference.edit()
        requestAdapter = RequestsAdapter()
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]



        val id = sharePreference.getString("USER_ID","").toString()
        val token = sharePreference.getString("TOKEN","").toString()

        val data = JsonObject()
        data.addProperty("token",token)
        homeViewModel.getRequestsByUserId(id, data)

        homeViewModel.observerRequestListLiveData().observe(this){requestList->
            requestAdapter.setRequestList(requestList)
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        buttons()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareRecyclerView()
    }

    private fun prepareRecyclerView() {
        binding.recyclerView.apply {
            adapter = requestAdapter
        }
    }

    private fun buttons() {
        binding.plusImageView.setOnClickListener {
            val intent = Intent(this@HomeFragment.requireActivity(),CreateRequestActivity::class.java)
            startActivity(intent)
        }
    }
}