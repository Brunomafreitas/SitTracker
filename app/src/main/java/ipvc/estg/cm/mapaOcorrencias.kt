package ipvc.estg.cm

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.location.*
import ipvc.estg.cm.api.ServiceBuilder
import ipvc.estg.cm.api.User
import ipvc.estg.cm.api.endPoints
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("DEPRECATION")
class mapaOcorrencias : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

   private lateinit var locationRequest: LocationRequest
   private lateinit var locationCallback : LocationCallback


    private lateinit var users: List<User>
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private val LOCATION_PERMISSION_REQUEST_CODE = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa_ocorrencias)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations){
                    // Update UI with location data
                    // ...
                }
            }
        }
        // call the service and add markers
        val request = ServiceBuilder.buildService(endPoints::class.java)
        val call = request.getUsers()
        var position: LatLng
        createLocationRequest()
        call.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    users = response.body()!!
                    val extras = intent.extras
                    val id = extras?.getString("id")
                    val tipo_id = extras?.getInt("tipo_id")


                    for (user in users) {

                        if (tipo_id == 0) {
                            //Toast.makeText(this@MapsActivity, user.lat, Toast.LENGTH_SHORT).show()
                            if (user.id.toInt() == id?.toInt()) {
                                position = LatLng(
                                    user.lat.toString().toDouble(),
                                    user.lng.toString().toDouble()
                                )
                                mMap.addMarker(MarkerOptions().position(position).title(user.name))
                            } else {
                                position = LatLng(
                                    user.lat.toString().toDouble(),
                                    user.lng.toString().toDouble()
                                )
                                mMap.addMarker(
                                    MarkerOptions().position(position).title(user.name).icon(
                                        BitmapDescriptorFactory.defaultMarker(
                                            BitmapDescriptorFactory.HUE_GREEN
                                        )
                                    )
                                )
                            }
                        } else {
                            if (user.tipo_id.toInt() == tipo_id) {
                                if (user.id.toInt() == id?.toInt()) {
                                    position = LatLng(
                                        user.lat.toString().toDouble(),
                                        user.lng.toString().toDouble()
                                    )
                                    mMap.addMarker(MarkerOptions().position(position).title(user.name ))
                                } else {
                                    position = LatLng(
                                        user.lat.toString().toDouble(),
                                        user.lng.toString().toDouble()
                                    )
                                    mMap.addMarker(
                                        MarkerOptions().position(position).title(user.name).icon(
                                            BitmapDescriptorFactory.defaultMarker(
                                                BitmapDescriptorFactory.HUE_GREEN
                                            )
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Toast.makeText(this@mapaOcorrencias, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })




    }
    private fun createLocationRequest() {
        locationRequest = LocationRequest()
        // interval specifies the rate at which your app will like to receive updates. locationRequest.interval = 10000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
       /* val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        */
        setUpMap()
    }

    fun setUpMap() {                    // Minha localização atual
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        } else {
            // 1
            mMap.isMyLocationEnabled = true
            // 2
            fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                // Got last known location. In some rare situations this can be null.
                // 3
                if (location != null) {
                    lastLocation = location
                    //Toast.makeText(this@MapsActivity, lastLocation.toString(), Toast.LENGTH_SHORT).show()
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
                }
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.mapsmenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val request = ServiceBuilder.buildService(endPoints::class.java)
        //val call = request.getUsers()
        val replyIntent = Intent()
        return when (item.itemId) {


            R.id.add_ocorrencia -> {
                val extras = intent.extras
                val id = extras?.getString("id")

                var loc = LatLng(lastLocation.latitude, lastLocation.longitude)
                val intent = Intent(this@mapaOcorrencias, AddOcorrencia::class.java)
                intent.putExtra("lat", loc.latitude.toString())
                intent.putExtra("lng", loc.longitude.toString())
                intent.putExtra("id", id)
                //finish()
                startActivity(intent)

                true
            }

            R.id.logout -> {
                var token = getSharedPreferences("utilizador", Context.MODE_PRIVATE)
                intent.putExtra("utilizador", " ")
                var editor = token.edit()
                editor.putString("loginutilizador", " ")
                editor.commit()
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                //finish()
                val intent = Intent(this@mapaOcorrencias, Login::class.java)
                startActivity(intent)
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) { ActivityCompat.requestPermissions(this,
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null /* Looper */) }



    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    public override fun onResume()
    {
        super.onResume()
        startLocationUpdates()
    }

    public override fun onRestart() {
        super.onRestart()
        val extras = intent.extras
        val id = extras?.getString("id")
        val tipo_id = extras?.getString("tipo_id")
        val intent = Intent(this@mapaOcorrencias, mapaOcorrencias::class.java)
        intent.putExtra("id", id)

        startActivity(intent)
        finish()

    }
}
