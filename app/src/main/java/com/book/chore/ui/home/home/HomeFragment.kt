package com.book.chore.ui.login.home.ui.home

import android.content.Intent
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
import com.book.chore.data.User.UserManager
import com.book.chore.databinding.HomeFragmentBinding
import com.book.chore.ui.home.home.adapters.ChoreServicesAdapter
import com.book.chore.ui.home.home.adapters.OnItemClickListener
import com.bumptech.glide.Glide
import java.lang.Exception
import com.book.chore.ui.services.ServicesActivity


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
                        //Add code to filter the task doers
                        val i = Intent(context, ServicesActivity::class.java)
                        startActivity(i)
                    }
                }
            })
            binding.servicesList.layoutManager = GridLayoutManager(context, 2)
            binding.servicesList.adapter = adapter
        }
        setProfileThumbnail()
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
                            if (userProfilePic == "") {
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
            Toast.makeText(activity, resources.getString(R.string.errProfileThumbnail), Toast.LENGTH_SHORT).show()
        }
    }
}
