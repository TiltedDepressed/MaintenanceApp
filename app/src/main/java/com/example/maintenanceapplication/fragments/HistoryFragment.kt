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
import com.example.maintenanceapplication.activities.FavoritesDetailActivity
import com.example.maintenanceapplication.adapters.FavoritesAdapter
import com.example.maintenanceapplication.databinding.FragmentHistoryBinding
import com.example.maintenanceapplication.viewModel.HistoryViewModel
import com.google.gson.JsonObject

class HistoryFragment : Fragment() {


    private lateinit var binding: FragmentHistoryBinding

    private lateinit var sharePreference: SharedPreferences

    private lateinit var editor: SharedPreferences.Editor

    private lateinit var historyViewModel: HistoryViewModel

    private lateinit var favoritesAdapter: FavoritesAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharePreference = this.requireActivity().getSharedPreferences("MY_PRE", Context.MODE_PRIVATE)
        editor = sharePreference.edit()
        historyViewModel = ViewModelProvider(this)[HistoryViewModel::class.java]
        favoritesAdapter = FavoritesAdapter()
        val id = sharePreference.getString("USER_ID","").toString()
        val token = sharePreference.getString("TOKEN","").toString()
        val data = JsonObject()
        data.addProperty("token",token)
        historyViewModel.favoritesByUserId(id, data)
        historyViewModel.observerRequestListLiveData().observe(this){favoritesList->
            favoritesAdapter.setRequestList(favoritesList)
        }


    }

    private fun prepareRecyclerView() {
        binding.recyclerView.apply {
            adapter = favoritesAdapter
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        prepareRecyclerView()
        favoritesAdapter.onItemClick = {
            val intent = Intent(this@HistoryFragment.requireActivity(),FavoritesDetailActivity::class.java)
            intent.putExtra("FAVORITES_ID",it.id)
            intent.putExtra("REQUEST_ID",it.request)
            startActivity(intent)
        }
        return binding.root
    }

}