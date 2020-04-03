package com.book.chore.ui.login.home.ui.home

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.book.chore.R
import com.book.chore.data.ChoreServices.ChoreService
import com.book.chore.data.ChoreServices.ServiceManager
import com.book.chore.data.User.UserManager
import com.book.chore.databinding.HomeFragmentBinding
import com.book.chore.sharedmodel.SharedViewModel
import com.book.chore.ui.home.home.adapters.ChoreServicesAdapter
import com.book.chore.ui.home.home.adapters.OnItemClickListener
import com.book.chore.ui.location.LocationFragment
import com.book.chore.ui.services.ServicesActivity
import com.book.chore.utils.ChoreConstants
import com.bumptech.glide.Glide


class HomeFragment : Fragment() {

    private lateinit var binding: HomeFragmentBinding
    private lateinit var viewModel: SharedViewModel
    private var city = "default"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        /**
         *Loading and initializing the [LocationFragment] fragment
         */
        childFragmentManager.beginTransaction().add(LocationFragment(), "locationFragment").commit()

        /**
         * Executing default services
         */
        fetchServices()
        setProfileThumbnail()

        /**
         * Initializing [SharedViewModel] instance
         */
        viewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
        viewModel.getSharedData()
            ?.observe(viewLifecycleOwner, object : Observer<MutableMap<String, Any>> {
                override fun onChanged(t: MutableMap<String, Any>?) {
                    if (t != null) {
                        city = t.get(ChoreConstants.AppConstant.SERVICE_CITY) as String
                        Log.i("Location changed: ", city)
                        fetchServices()
                    }
                }
            })

        return binding.root
    }

    private fun setProfileThumbnail() {
        try {
            if (UserManager().isUserLoggedIn()) {
                UserManager().fetchUserDataById(UserManager().loggedInUserId()) {
                    if (it == null) {
                        binding.imgProfilePicHome.setImageDrawable(resources.getDrawable(R.drawable.ic_account_box_black_24dp))
                    } else {
                        with(it) {
                            if (userProfilePic == "null") {
                                binding.imgProfilePicHome.setImageDrawable(resources.getDrawable(R.drawable.ic_account_box_black_24dp))
                            } else {
                                Glide.with(this@HomeFragment).load(userProfilePic)
                                    .into(binding.imgProfilePicHome)
                            }
                        }
                    }
                }
            } else {
                binding.imgProfilePicHome.setImageDrawable(resources.getDrawable(R.drawable.ic_account_box_black_24dp))
            }
        } catch (e: Exception) {
            Toast.makeText(
                activity,
                resources.getString(R.string.errProfileThumbnail),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun fetchServices() {
        if (city == "default") {
            ServiceManager().fetchAvailableServices {
                Log.i("service list", it.toString())
                val adapter = ChoreServicesAdapter(it, object : OnItemClickListener {
                    override fun onItemClick(item: ChoreService?) {

                        val builder = AlertDialog.Builder(this@HomeFragment.requireContext())
                        builder.setTitle(ChoreConstants.AlertConstant.SERVICE_SELECT_LOCATION_TITLE)
                        builder.setMessage(ChoreConstants.AlertConstant.SERVICE_SELECT_LOCATION_MESSAGE)
                        builder.setPositiveButton(ChoreConstants.AlertConstant.OKAY_BUTTON) { dialog, which ->
                        }
                        val alert = builder.create()
                        alert.setTitle(ChoreConstants.AlertConstant.SERVICE_SELECT_LOCATION_TITLE)
                        alert.show()

                    }
                })
                binding.servicesList.layoutManager = GridLayoutManager(context, 2)
                binding.servicesList.adapter = adapter
            }
        } else {
            Log.i("Fetching services for: ", city)
//          ServiceManager().fetchAvailableServicesWithCity(city) {
//                Log.i("service list with city", it.toString())
//
//                if(it.isEmpty()){
//                    Toast.makeText(getContext(),"Services not found for $city",Toast.LENGTH_SHORT).show()
//                }
//
//                val adapter = ChoreServicesAdapter(it, object : OnItemClickListener {
//                    override fun onItemClick(item: ChoreService?) {
//                        if (item != null) {
//                            Toast.makeText(context, item.serviceName, Toast.LENGTH_LONG).show()
//                            //Add code to filter the task doers
//                            val i = Intent(context, ServicesActivity::class.java)
//                            i.putExtra(ChoreConstants.AppConstant.SERVICE_CITY, city)
//                            startActivity(i)
//                        }
//                    }
//                })
//                binding.servicesList.layoutManager = GridLayoutManager(context, 2)
//                binding.servicesList.adapter = adapter
//            }


            var servicesList = ServiceManager().fetchAvailableServicesWithCity(city) {}
            val adapter = ChoreServicesAdapter(servicesList, object : OnItemClickListener {
                override fun onItemClick(item: ChoreService?) {

                    if (servicesList.isEmpty()) {
                        Toast.makeText(
                            getContext(),
                            "Services not found for $city",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    if (item != null) {
                        Toast.makeText(context, item.serviceName, Toast.LENGTH_LONG).show()
                        //Add code to filter the task doers
                        val i = Intent(context, ServicesActivity::class.java)
                        i.putExtra(ChoreConstants.AppConstant.SERVICE_CITY, city)
                        i.putExtra("serviceType",item.serviceName)
                        startActivity(i)
                    }
                }
            })
            binding.servicesList.layoutManager = GridLayoutManager(context, 2)
            binding.servicesList.adapter = adapter

        }
    }
}
