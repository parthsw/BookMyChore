package com.book.chore.ui.login.home.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.book.chore.R
import com.book.chore.data.ChoreServices.ChoreService
import com.book.chore.data.ChoreServices.ServiceManager
import com.book.chore.databinding.HomeFragmentBinding
import com.book.chore.ui.home.home.adapters.ChoreServicesAdapter
import com.book.chore.ui.home.home.adapters.OnItemClickListener


class HomeFragment : Fragment() {

    private lateinit var binding: HomeFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        ServiceManager().fetchAvailableServices {

            val adapter = ChoreServicesAdapter(it, object : OnItemClickListener {
                override fun onItemClick(item: ChoreService?) {
                    if (item != null) {
                        Toast.makeText(context, item.serviceName, Toast.LENGTH_LONG).show()
                    }
                }
            })
            binding.servicesList.layoutManager = GridLayoutManager(context, 2)
            binding.servicesList.adapter = adapter
        }
        return binding.root
    }
}