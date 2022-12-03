package com.niceplaces.niceplaces.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.CancelableCallback
import com.google.android.gms.maps.GoogleMap.OnCameraMoveListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.ClusterManager.*
import com.niceplaces.niceplaces.Const
import com.niceplaces.niceplaces.R
import com.niceplaces.niceplaces.adapters.MarkerInfoAdapter
import com.niceplaces.niceplaces.adapters.PlacesAdapter
import com.niceplaces.niceplaces.controllers.PrefsController
import com.niceplaces.niceplaces.dao.DaoPlaces
import com.niceplaces.niceplaces.models.GeoPoint
import com.niceplaces.niceplaces.models.MyClusterItem
import com.niceplaces.niceplaces.models.Place
import com.niceplaces.niceplaces.utils.*
import org.apache.commons.collections4.CollectionUtils
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, OnCameraMoveListener,
        OnClusterClickListener<MyClusterItem?>, OnClusterInfoWindowClickListener<MyClusterItem?>,
        OnClusterItemClickListener<MyClusterItem?>, OnClusterItemInfoWindowClickListener<MyClusterItem> {

    private val mContext: Context = this
    private val mActivity: Activity = this
    private var mMap: GoogleMap? = null
    private var mPlaces: List<Place>? = null
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mPlacesAdapter: PlacesAdapter
    private var mCurrentPosition: LatLng? = null
    private var mMapMode: MapMode? = null
    private lateinit var dialogPosLoading: AlertDialog
    private var mClusterManager: ClusterManager<MyClusterItem>? = null
    private var myPositionAnchored = true
    private var moveToMyPositionClicked = true
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    private lateinit var prefs: PrefsController

    private enum class MapMode {
        ROAD, SATELLITE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(AppUtils.tag, "onCreate() called")
        setContentView(R.layout.activity_maps)
        supportActionBar!!.hide()
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        mRecyclerView = findViewById(R.id.listview)
        val imageViewSearchButton = findViewById<ImageView>(R.id.imageview_search_button)
        val imageViewSettingsButton = findViewById<ImageView>(R.id.imageview_map_settings_button)
        imageViewSearchButton.setOnClickListener { view -> // Check if no view has focus:
            val focusView = mActivity.currentFocus
            if (focusView != null) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
            val editTextSearch = findViewById<EditText>(R.id.edittext_search)
            val pattern = editTextSearch.text.toString().toLowerCase(Locale.getDefault())
            val filteredPlaces: List<Place> = ArrayList(mPlaces)
            CollectionUtils.filter(filteredPlaces) { `object` ->
                val res = `object`.mName.toLowerCase(Locale.getDefault()).contains(pattern)
                // TODO causa NullPointerException
                try {
                    // object.mMarker.setVisible(res);
                } catch (e: NullPointerException) {

                }
                res
            }
            updatePlacesListView(filteredPlaces)
        }
        imageViewSettingsButton.setOnClickListener {
            val intent = Intent(mContext, SettingsActivity::class.java)
            mContext.startActivity(intent)
        }
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        prefs = PrefsController(mContext)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        Log.i(AppUtils.tag, "onMapReady() called");
        mMap = googleMap
        mMapMode = MapMode.ROAD
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 0)
        } /*else if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 0)
        }*/ else {
            Log.i("Permission", "Granted")
            setupLocationListener()
        }
        val imageViewMapMode = findViewById<ImageView>(R.id.imageview_map_mode)
        imageViewMapMode.setOnClickListener {
            when (mMapMode) {
                MapMode.ROAD -> {
                    mMap!!.mapType = GoogleMap.MAP_TYPE_SATELLITE
                    imageViewMapMode.setImageResource(R.drawable.road)
                    mMapMode = MapMode.SATELLITE
                }
                MapMode.SATELLITE -> {
                    mMap!!.mapType = GoogleMap.MAP_TYPE_NORMAL
                    imageViewMapMode.setImageResource(R.drawable.mountains)
                    mMapMode = MapMode.ROAD
                }
                else -> {}
            }
        }
        val imageViewAnchor = findViewById<ImageView>(R.id.imageview_map_position)
        imageViewAnchor.setOnClickListener {
            moveToMyPositionClicked = true
            if (!myPositionAnchored) {
                mMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(
                        CameraPosition.Builder().target(mCurrentPosition).tilt(DEFAULT_TILT.toFloat()).zoom(prefs.zoom).build()),
                        object : CancelableCallback {
                            override fun onFinish() {
                                moveToMyPositionClicked = false
                            }

                            override fun onCancel() {}
                        })
                imageViewAnchor.setImageResource(R.drawable.crosshairs_gps)
                myPositionAnchored = true
            }
        }
        mMap!!.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
        mMap!!.isBuildingsEnabled = false
        mMap!!.uiSettings.isZoomControlsEnabled = true
        mMap!!.uiSettings.isMyLocationButtonEnabled = true
        mMap!!.setOnCameraMoveListener(this)
        mClusterManager = ClusterManager(this, mMap)
        val algorithm = NonHierarchicalDistanceBasedAlgorithm<MyClusterItem>()
        algorithm.maxDistanceBetweenClusteredItems = 100 // 100dp
        mClusterManager!!.algorithm = algorithm
        mClusterManager!!.renderer = MyRenderer(this, mMap, mClusterManager)
        mClusterManager!!.setOnClusterClickListener(this)
        mClusterManager!!.setOnClusterInfoWindowClickListener(this)
        mClusterManager!!.setOnClusterItemClickListener(this)
        mClusterManager!!.setOnClusterItemInfoWindowClickListener(this)
        mMap!!.setInfoWindowAdapter(mClusterManager!!.markerManager)
        mClusterManager!!.markerCollection.setInfoWindowAdapter(MarkerInfoAdapter(this, this))
        // Cambiare l'ordine di queste istruzioni potrebbe causare il blocco del
        // clustering in fase di cambio di zooom
        mMap!!.setOnCameraIdleListener(mClusterManager)
        mMap!!.setOnMarkerClickListener(mClusterManager)
        mMap!!.setOnInfoWindowClickListener(mClusterManager)
        val myPosition = LatLng(prefs.storedLocation.latitude, prefs.storedLocation.longitude)
        mMap!!.moveCamera(CameraUpdateFactory.newCameraPosition(
                CameraPosition.Builder().target(myPosition).tilt(DEFAULT_TILT.toFloat()).zoom(prefs.zoom).build()))
        mPlacesAdapter = mMap?.let { PlacesAdapter(this, it) }!!
        mRecyclerView.adapter = mPlacesAdapter
        mRecyclerView.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false)
    }

    override fun onCameraMove() {
        val zoom = mMap!!.cameraPosition.zoom
        prefs.zoom = zoom
        var cameraCenter = mMap!!.cameraPosition.target
        if (cameraCenter == null) {
            cameraCenter = LatLng(prefs.storedLocation.latitude, prefs.storedLocation.longitude)
        }
        Log.i(AppUtils.tag, "LATLNG: " + cameraCenter.latitude + ", " +
                cameraCenter.longitude)
        val imageViewAnchor = findViewById<ImageView>(R.id.imageview_map_position)
        try {
            val centeredToMyPosition = cameraCenter.latitude == mCurrentPosition!!.latitude &&
                    cameraCenter.longitude == mCurrentPosition!!.longitude
            if (!moveToMyPositionClicked) {
                if (centeredToMyPosition) {
                    imageViewAnchor.setImageResource(R.drawable.crosshairs_gps)
                } else {
                    imageViewAnchor.setImageResource(R.drawable.crosshairs)
                }
            }
            myPositionAnchored = centeredToMyPosition
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            0 -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setupLocationListener()
                } else {
                    //
                }
            }
        }
    }

    private fun setupLocationListener() {
        dialogPosLoading = AlertDialog.Builder(this@MapsActivity).create()
        dialogPosLoading.setTitle(R.string.location_loading)
        val inflater = layoutInflater
        val view = inflater.inflate(R.layout.dialog_load_location, null)
        dialogPosLoading.setView(view)
        dialogPosLoading.show()
        val dialogPosDisabled = AlertDialog.Builder(this@MapsActivity).create()
        dialogPosDisabled.setTitle(R.string.location_disabled)
        dialogPosDisabled.setMessage(getString(R.string.impossible_load_location))
        dialogPosDisabled.setButton(AlertDialog.BUTTON_NEUTRAL, "OK"
        ) { dialog, which -> dialog.dismiss() }
        try {
            locationListener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    Log.i(AppUtils.tag, "Location update " + location.latitude.toString() + " " + location.longitude.toString())
                    prefs.storedLocation = GeoPoint(location.latitude, location.longitude)
                    sendNearestPlacesRequest(location)
                }

                override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {
                    Log.i("OnProvider", "StatusChanged")
                }

                override fun onProviderEnabled(s: String) {
                    Log.i("OnProviderEnabled", s)
                }

                override fun onProviderDisabled(s: String) {
                    Log.i("OnProviderDisabled", s)
                    val oneProviderAvailable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                    if (!oneProviderAvailable) {
                        val storedLocation = prefs.storedLocation
                        sendNearestPlacesRequest(storedLocation.latitude, storedLocation.longitude)
                        if (!dialogPosDisabled.isShowing) {
                            dialogPosLoading.dismiss()
                            dialogPosDisabled.show()
                        }
                    }
                }
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                prefs.locationRefreshTime.toLong(), prefs.locationRefreshDistance.toFloat(), locationListener)
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                prefs.locationRefreshTime.toLong(), prefs.locationRefreshDistance.toFloat(), locationListener)
            var location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            Log.i("getLastKnownLocation", java.lang.Boolean.toString(location == null))
            if (location == null) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                Log.i("getLastKnownLocation2", java.lang.Boolean.toString(location == null))
            }
            if (location != null) {
                // TODO genera crash
                //updateLocation(location);
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    fun sendNearestPlacesRequest(lat: Double, lon: Double) {
        val daoPlaces = DaoPlaces(mContext)
        daoPlaces.getNearest(lat, lon, object : MyRunnable() {
            override fun run() {
                Log.i(AppUtils.tag, "Data retrieved")
                mPlaces = places
                updateLocation(lat, lon)
                dialogPosLoading!!.dismiss()
            }
        }, object : MyRunnable() {
            override fun run() {}
        })
    }

    fun sendNearestPlacesRequest(location: Location) {
        sendNearestPlacesRequest(location.latitude, location.longitude)
    }

    fun updateLocation(location: Location) {
        val longitude = location.longitude
        val latitude = location.latitude
        updateLocation(latitude, longitude)
    }

    fun updateLocation(latitude: Double, longitude: Double) {
        val results = FloatArray(1)
        Log.i(AppUtils.tag, "New location: $latitude, $longitude")
        val myPosition = LatLng(latitude, longitude)
        mMap!!.clear()
        mClusterManager!!.clearItems()
        mMap!!.addMarker(MarkerOptions().position(myPosition).title("Tu sei qui!")
                .icon(ImageUtils.bitmapDescriptorFromDrawable(this, R.drawable.marker_position)))
        if (myPositionAnchored) {
            mMap!!.moveCamera(CameraUpdateFactory.newCameraPosition(
                    CameraPosition.Builder().target(myPosition).tilt(DEFAULT_TILT.toFloat()).zoom(prefs.zoom).build()))
        }
        mCurrentPosition = myPosition
        for (i in mPlaces!!.indices) {
            // Calcola le distanze in linea d'aria
            Location.distanceBetween(latitude, longitude, mPlaces!![i].mLatitude, mPlaces!![i].mLongitude, results)
            val place = mPlaces!![i]
            place.mDistance = results[0].toDouble()
            val clusterItem = MyClusterItem(mPlaces!![i])
            place.mClusterItem = clusterItem
            mClusterManager!!.addItem(clusterItem)
        }
        mClusterManager!!.cluster()
        Collections.sort(mPlaces) { g1, g2 -> (g1.mDistance - g2.mDistance).toInt() }
        updatePlacesListView(mPlaces)
    }

    private fun updatePlacesListView(places: List<Place>?) {
        mPlacesAdapter.submitList(places)
        val TVCounter = findViewById<TextView>(R.id.places_counter)
        TVCounter.text = getString(R.string.places_in_radius_of, String.format("%.2f", prefs.distanceRadius), places!!.size)
    }

    override fun onPause() {
        super.onPause()
        Log.i(AppUtils.tag, "onPause() called")
        try {
            locationManager.removeUpdates(locationListener)
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        Log.i(AppUtils.tag, "onResume() called")
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                if (mCurrentPosition != null) {
                    val location = Location(LocationManager.GPS_PROVIDER)
                    location.latitude = mCurrentPosition!!.latitude
                    location.longitude = mCurrentPosition!!.longitude
                    sendNearestPlacesRequest(location)
                }
        }
    }

    override fun onClusterClick(cluster: Cluster<MyClusterItem?>): Boolean {
        return false
    }

    override fun onClusterItemClick(myClusterItem: MyClusterItem?): Boolean {
        return false
    }

    override fun onClusterInfoWindowClick(cluster: Cluster<MyClusterItem?>) {}
    override fun onClusterItemInfoWindowClick(myClusterItem: MyClusterItem) {
        val intent = Intent(mActivity, PlaceDetailsActivity::class.java)
        intent.putExtra(Const.PLACE_ID, myClusterItem.place.iD)
        mActivity.startActivity(intent)
    }

    companion object {
        @JvmField
        var DEFAULT_TILT = 60
    }
}