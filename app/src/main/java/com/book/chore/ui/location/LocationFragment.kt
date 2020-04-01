package com.book.chore.ui.location

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.book.chore.R
import com.book.chore.utils.LocationHelper
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import kotlinx.android.synthetic.main.fragment_location.*
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [LocationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LocationFragment : Fragment(), PlaceSelectionListener {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var autocompleteSupportFragment: AutocompleteSupportFragment
    private var location = ""
    private val PERMISSION_ID = 42
    private val googleApiKey = "AIzaSyCBywthrPsR7bN8yHeTqIpHr6XCKAbz43g"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!Places.isInitialized()) {
            getContext()?.let { Places.initialize(it, googleApiKey, Locale.US) };
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (!Places.isInitialized()) {
            getContext()?.let { Places.initialize(it, googleApiKey, Locale.US) };
        }

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this.requireActivity())

        autocompleteSupportFragment = places_autocomplete_edit_text as AutocompleteSupportFragment
        autocompleteSupportFragment.setCountries(LocationHelper.COUNTRY_SUPPORTED)
        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ADDRESS))
        autocompleteSupportFragment.setHint("Enter the location here")
        autocompleteSupportFragment.setOnPlaceSelectedListener(this)

        val locationButton2 = getActivity()?.findViewById<Button>(R.id.locationButton)
        locationButton2?.setOnClickListener {
            if (checkPermissions()) {
                if (isLocationEnabled()) {
                    fusedLocationProviderClient.lastLocation.addOnCompleteListener(this.requireActivity()) { task ->
                        var location: Location? = task.result
                        if (location != null) {
                            getCityName(location)
                        };
                    }
                } else {
                    Toast.makeText(this.requireContext(), "Turn on location", Toast.LENGTH_LONG)
                        .show()
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(intent)
                }
            } else {
                requestPermissions()
            }
        }

        return inflater.inflate(R.layout.fragment_location, container, false)
    }


    override fun onPlaceSelected(p0: Place) {
        Log.i("place:", p0.address.toString())

        location = p0.address.toString()
        Handler().postDelayed({
            autocompleteSupportFragment.setText(location);
        }, 50)
    }

    override fun onError(p0: Status) {}

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager =
            this.requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this.requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Granted. Start getting the location information
            }
        }
    }

    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this.requireActivity())
        fusedLocationProviderClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location = locationResult.lastLocation
        }
    }

    private fun getCityName(location: Location) {

        var geoCoder: Geocoder
        geoCoder = Geocoder(this.requireContext(), Locale.CANADA)

        var list: List<Address> = geoCoder.getFromLocation(location.latitude, location.longitude, 1)
        Log.i("address list", list.toString())

        var fullAddress = list.get(0).getAddressLine(0)
        Log.i("fullAddress", fullAddress)

        var city = list.get(0).locality
        Log.i("city", city)

        Handler().postDelayed({
            autocompleteSupportFragment.setText(fullAddress);
        }, 50)
    }
}