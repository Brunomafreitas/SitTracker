package ipvc.estg.cm

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
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
    private  var markers: List<Marker> = emptyList()
    private lateinit var marker: Marker;

    private lateinit var users: List<User>
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private var continenteLat : Double = 0.0;
    private var continenteLong : Double = 0.0;
 private var distanciaString : String? = " ";
    private  var id : String? = " "
    private  var tipo_id : Int? = 0;
    private var utilizadorAtual : String = " ";
    private lateinit var buttonApaga : Button
    private lateinit var buttonAddMarkerAcidentes : Button
    private lateinit var buttonFiltrarProximos : Button

    private lateinit var buttonMostraTodosMarkers : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa_ocorrencias)
        buttonApaga = findViewById<Button>(R.id.BotaoApagar);
        buttonAddMarkerAcidentes = findViewById<Button>(R.id.buttonAcidentes)
        buttonMostraTodosMarkers = findViewById<Button>(R.id.button3)
        buttonFiltrarProximos = findViewById<Button>(R.id.buttonFiltrar1km)
        continenteLat = 41.7843
        continenteLong = -8.8148
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        createLocationRequest()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                lastLocation = locationResult!!.lastLocation
                var loc = LatLng(lastLocation.latitude, lastLocation.longitude);

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc , 15.0f))
                findViewById<TextView>(R.id.textLngLAt).setText("Lat " + loc.latitude + " - Long" + loc.longitude)
                findViewById<TextView>(R.id.textDistancia).setText("Distancia " + calculateDistance(
                    lastLocation.latitude, lastLocation.longitude,
                    continenteLat, continenteLong).toString());


            }
        }

        buttonApaga.setOnClickListener{
            mMap.clear();
            val request = ServiceBuilder.buildService(endPoints::class.java)
            val call = request.getOcorrencias()
            var position: LatLng

            call.enqueue(object : Callback<List<User>> {
                override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                    if (response.isSuccessful) {
                        users = response.body()!!

                        utilizadorAtual = id.toString();
                        for (user in users) {
                            if(user.tipo == "obras") {
                                if (user.users_id == id) {


                                    if (tipo_id == 0) {
                                        //Toast.makeText(this@MapsActivity, user.lat, Toast.LENGTH_SHORT).show()
                                        if (user.id.toInt() == id?.toInt()) {
                                            position = LatLng(
                                                user.lat.toString().toDouble(),
                                                user.lng.toString().toDouble()
                                            )

                                            if (calculateDistance(
                                                    position.latitude,
                                                    position.longitude,
                                                    lastLocation.latitude,
                                                    lastLocation.longitude
                                                ) < 1000000
                                            ) {
                                                marker = mMap.addMarker(
                                                    MarkerOptions().position(position).title(
                                                        user.nome + " - " + user.titulo + " - " + user.corpo + "Distancia " + "Tipo " + user.tipo  + calculateDistance(
                                                            lastLocation.latitude,
                                                            lastLocation.longitude,
                                                            position.latitude,
                                                            position.longitude
                                                        )
                                                    )
                                                )
                                                markers.toMutableList().add(marker);
                                            }

                                        } else {
                                            position = LatLng(
                                                user.lat.toString().toDouble(),
                                                user.lng.toString().toDouble()
                                            )
                                            if (calculateDistance(
                                                    position.latitude,
                                                    position.longitude,
                                                    lastLocation.latitude,
                                                    lastLocation.longitude
                                                ) < 1000000
                                            ) {
                                                marker = mMap.addMarker(
                                                    MarkerOptions().position(position).title(
                                                        user.nome + " - " + user.titulo + " - " + user.corpo + "Distancia " + "Tipo " + user.tipo  + calculateDistance(
                                                            lastLocation.latitude,
                                                            lastLocation.longitude,
                                                            position.latitude,
                                                            position.longitude
                                                        )
                                                    ).icon(
                                                        BitmapDescriptorFactory.defaultMarker(
                                                            BitmapDescriptorFactory.HUE_GREEN
                                                        )
                                                    )
                                                )
                                                markers.toMutableList().add(marker);
                                            }
                                        }
                                    } else {
                                        if (user.id.toInt() == tipo_id) {
                                            if (user.id.toInt() == id?.toInt()) {
                                                position = LatLng(
                                                    user.lat.toString().toDouble(),
                                                    user.lng.toString().toDouble()
                                                )
                                                if (calculateDistance(
                                                        position.latitude,
                                                        position.longitude,
                                                        lastLocation.latitude,
                                                        lastLocation.longitude
                                                    ) < 1000000
                                                ) {
                                                    marker = mMap.addMarker(
                                                        MarkerOptions().position(position).title(
                                                            user.nome + " - " + user.titulo + " - " + user.corpo + "Distancia " + "Tipo " + user.tipo  + calculateDistance(
                                                                lastLocation.latitude,
                                                                lastLocation.longitude,
                                                                position.latitude,
                                                                position.longitude
                                                            )
                                                        )
                                                    )
                                                    markers.toMutableList().add(marker);
                                                }
                                            } else {
                                                position = LatLng(
                                                    user.lat.toString().toDouble(),
                                                    user.lng.toString().toDouble()
                                                )
                                                if (calculateDistance(
                                                        position.latitude,
                                                        position.longitude,
                                                        lastLocation.latitude,
                                                        lastLocation.longitude
                                                    ) < 1000000
                                                ) {
                                                    marker = mMap.addMarker(
                                                        MarkerOptions().position(position).title(
                                                            user.nome + " - " + user.titulo + " - " + user.corpo + "Distancia " + "Tipo " + user.tipo  + calculateDistance(
                                                                lastLocation.latitude,
                                                                lastLocation.longitude,
                                                                position.latitude,
                                                                position.longitude
                                                            )
                                                        ).icon(
                                                            BitmapDescriptorFactory.defaultMarker(
                                                                BitmapDescriptorFactory.HUE_GREEN
                                                            )
                                                        )
                                                    )
                                                    markers.toMutableList().add(marker);
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    if (tipo_id == 0) {
                                        //Toast.makeText(this@MapsActivity, user.lat, Toast.LENGTH_SHORT).show()
                                        if (user.id.toInt() == id?.toInt()) {
                                            position = LatLng(
                                                user.lat.toString().toDouble(),
                                                user.lng.toString().toDouble()
                                            )
                                            if (calculateDistance(
                                                    position.latitude,
                                                    position.longitude,
                                                    lastLocation.latitude,
                                                    lastLocation.longitude
                                                ) < 1000000
                                            ) {
                                                marker = mMap.addMarker(
                                                    MarkerOptions().position(position).title(
                                                        user.nome + " - " + user.titulo + " - " + user.corpo + "Distancia "  + "Tipo " + user.tipo + calculateDistance(
                                                            lastLocation.latitude,
                                                            lastLocation.longitude,
                                                            position.latitude,
                                                            position.longitude
                                                        )
                                                    )
                                                )
                                                markers.toMutableList().add(marker);
                                            }
                                        } else {
                                            position = LatLng(
                                                user.lat.toString().toDouble(),
                                                user.lng.toString().toDouble()
                                            )
                                            if (calculateDistance(
                                                    position.latitude,
                                                    position.longitude,
                                                    lastLocation.latitude,
                                                    lastLocation.longitude
                                                ) < 1000000
                                            ) {
                                                marker = mMap.addMarker(
                                                    MarkerOptions().position(position).title(
                                                        user.nome + " - " + user.titulo + " - " + user.corpo + "Distancia " + "Tipo " + user.tipo  + calculateDistance(
                                                            lastLocation.latitude,
                                                            lastLocation.longitude,
                                                            position.latitude,
                                                            position.longitude
                                                        )
                                                    ).icon(
                                                        BitmapDescriptorFactory.defaultMarker(
                                                            BitmapDescriptorFactory.HUE_BLUE
                                                        )
                                                    )
                                                )
                                                markers.toMutableList().add(marker);
                                            }
                                        }
                                    } else {
                                        if (user.id.toInt() == tipo_id) {
                                            if (user.id.toInt() == id?.toInt()) {
                                                position = LatLng(
                                                    user.lat.toString().toDouble(),
                                                    user.lng.toString().toDouble()
                                                )
                                                if (calculateDistance(
                                                        position.latitude,
                                                        position.longitude,
                                                        lastLocation.latitude,
                                                        lastLocation.longitude
                                                    ) < 1000000
                                                ) {
                                                    marker = mMap.addMarker(
                                                        MarkerOptions().position(position).title(
                                                            user.nome + " - " + user.titulo + " - " + user.corpo + "Distancia " + "Tipo " + user.tipo  + calculateDistance(
                                                                lastLocation.latitude,
                                                                lastLocation.longitude,
                                                                position.latitude,
                                                                position.longitude
                                                            )
                                                        )
                                                    )
                                                    markers.toMutableList().add(marker);
                                                }
                                            } else {
                                                position = LatLng(
                                                    user.lat.toString().toDouble(),
                                                    user.lng.toString().toDouble()
                                                )
                                                if (calculateDistance(
                                                        position.latitude,
                                                        position.longitude,
                                                        lastLocation.latitude,
                                                        lastLocation.longitude
                                                    ) < 1000000
                                                ) {
                                                    marker = mMap.addMarker(
                                                        MarkerOptions().position(position).title(
                                                            user.nome + " - " + user.titulo + " - " + user.corpo + "Distancia " + "Tipo " + user.tipo  + calculateDistance(
                                                                lastLocation.latitude,
                                                                lastLocation.longitude,
                                                                position.latitude,
                                                                position.longitude
                                                            )
                                                        ).icon(
                                                            BitmapDescriptorFactory.defaultMarker(
                                                                BitmapDescriptorFactory.HUE_BLUE
                                                            )
                                                        )
                                                    )
                                                    markers.toMutableList().add(marker);
                                                }
                                            }
                                        }
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
        buttonAddMarkerAcidentes.setOnClickListener{
            mMap.clear();
            val request = ServiceBuilder.buildService(endPoints::class.java)
            val call = request.getOcorrencias()
            var position: LatLng

            call.enqueue(object : Callback<List<User>> {
                override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                    if (response.isSuccessful) {
                        users = response.body()!!

                        utilizadorAtual = id.toString();
                        for (user in users) {
                            if(user.tipo == "acidente") {
                                if (user.users_id == id) {


                                    if (tipo_id == 0) {
                                        //Toast.makeText(this@MapsActivity, user.lat, Toast.LENGTH_SHORT).show()
                                        if (user.id.toInt() == id?.toInt()) {
                                            position = LatLng(
                                                user.lat.toString().toDouble(),
                                                user.lng.toString().toDouble()
                                            )

                                            if (calculateDistance(
                                                    position.latitude,
                                                    position.longitude,
                                                    lastLocation.latitude,
                                                    lastLocation.longitude
                                                ) < 1000000
                                            ) {
                                                marker = mMap.addMarker(
                                                    MarkerOptions().position(position).title(
                                                        user.nome + " - " + user.titulo + " - " + user.corpo + "Distancia " + "Tipo " + user.tipo  + calculateDistance(
                                                            lastLocation.latitude,
                                                            lastLocation.longitude,
                                                            position.latitude,
                                                            position.longitude
                                                        )
                                                    )
                                                )
                                                markers.toMutableList().add(marker);
                                            }

                                        } else {
                                            position = LatLng(
                                                user.lat.toString().toDouble(),
                                                user.lng.toString().toDouble()
                                            )
                                            if (calculateDistance(
                                                    position.latitude,
                                                    position.longitude,
                                                    lastLocation.latitude,
                                                    lastLocation.longitude
                                                ) < 1000000
                                            ) {
                                                marker = mMap.addMarker(
                                                    MarkerOptions().position(position).title(
                                                        user.nome + " - " + user.titulo + " - " + user.corpo + "Distancia " + "Tipo " + user.tipo  + calculateDistance(
                                                            lastLocation.latitude,
                                                            lastLocation.longitude,
                                                            position.latitude,
                                                            position.longitude
                                                        )
                                                    ).icon(
                                                        BitmapDescriptorFactory.defaultMarker(
                                                            BitmapDescriptorFactory.HUE_GREEN
                                                        )
                                                    )
                                                )
                                                markers.toMutableList().add(marker);
                                            }
                                        }
                                    } else {
                                        if (user.id.toInt() == tipo_id) {
                                            if (user.id.toInt() == id?.toInt()) {
                                                position = LatLng(
                                                    user.lat.toString().toDouble(),
                                                    user.lng.toString().toDouble()
                                                )
                                                if (calculateDistance(
                                                        position.latitude,
                                                        position.longitude,
                                                        lastLocation.latitude,
                                                        lastLocation.longitude
                                                    ) < 1000000
                                                ) {
                                                    marker = mMap.addMarker(
                                                        MarkerOptions().position(position).title(
                                                            user.nome + " - " + user.titulo + " - " + user.corpo + "Distancia " + "Tipo " + user.tipo  + calculateDistance(
                                                                lastLocation.latitude,
                                                                lastLocation.longitude,
                                                                position.latitude,
                                                                position.longitude
                                                            )
                                                        )
                                                    )
                                                    markers.toMutableList().add(marker);
                                                }
                                            } else {
                                                position = LatLng(
                                                    user.lat.toString().toDouble(),
                                                    user.lng.toString().toDouble()
                                                )
                                                if (calculateDistance(
                                                        position.latitude,
                                                        position.longitude,
                                                        lastLocation.latitude,
                                                        lastLocation.longitude
                                                    ) < 1000000
                                                ) {
                                                    marker = mMap.addMarker(
                                                        MarkerOptions().position(position).title(
                                                            user.nome + " - " + user.titulo + " - " + user.corpo + "Distancia " + "Tipo " + user.tipo  + calculateDistance(
                                                                lastLocation.latitude,
                                                                lastLocation.longitude,
                                                                position.latitude,
                                                                position.longitude
                                                            )
                                                        ).icon(
                                                            BitmapDescriptorFactory.defaultMarker(
                                                                BitmapDescriptorFactory.HUE_GREEN
                                                            )
                                                        )
                                                    )
                                                    markers.toMutableList().add(marker);
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    if (tipo_id == 0) {
                                        //Toast.makeText(this@MapsActivity, user.lat, Toast.LENGTH_SHORT).show()
                                        if (user.id.toInt() == id?.toInt()) {
                                            position = LatLng(
                                                user.lat.toString().toDouble(),
                                                user.lng.toString().toDouble()
                                            )
                                            if (calculateDistance(
                                                    position.latitude,
                                                    position.longitude,
                                                    lastLocation.latitude,
                                                    lastLocation.longitude
                                                ) < 1000000
                                            ) {
                                                marker = mMap.addMarker(
                                                    MarkerOptions().position(position).title(
                                                        user.nome + " - " + user.titulo + " - " + user.corpo + "Distancia "  + "Tipo " + user.tipo + calculateDistance(
                                                            lastLocation.latitude,
                                                            lastLocation.longitude,
                                                            position.latitude,
                                                            position.longitude
                                                        )
                                                    )
                                                )
                                                markers.toMutableList().add(marker);
                                            }
                                        } else {
                                            position = LatLng(
                                                user.lat.toString().toDouble(),
                                                user.lng.toString().toDouble()
                                            )
                                            if (calculateDistance(
                                                    position.latitude,
                                                    position.longitude,
                                                    lastLocation.latitude,
                                                    lastLocation.longitude
                                                ) < 1000000
                                            ) {
                                                marker = mMap.addMarker(
                                                    MarkerOptions().position(position).title(
                                                        user.nome + " - " + user.titulo + " - " + user.corpo + "Distancia " + "Tipo " + user.tipo  + calculateDistance(
                                                            lastLocation.latitude,
                                                            lastLocation.longitude,
                                                            position.latitude,
                                                            position.longitude
                                                        )
                                                    ).icon(
                                                        BitmapDescriptorFactory.defaultMarker(
                                                            BitmapDescriptorFactory.HUE_BLUE
                                                        )
                                                    )
                                                )
                                                markers.toMutableList().add(marker);
                                            }
                                        }
                                    } else {
                                        if (user.id.toInt() == tipo_id) {
                                            if (user.id.toInt() == id?.toInt()) {
                                                position = LatLng(
                                                    user.lat.toString().toDouble(),
                                                    user.lng.toString().toDouble()
                                                )
                                                if (calculateDistance(
                                                        position.latitude,
                                                        position.longitude,
                                                        lastLocation.latitude,
                                                        lastLocation.longitude
                                                    ) < 1000000
                                                ) {
                                                    marker = mMap.addMarker(
                                                        MarkerOptions().position(position).title(
                                                            user.nome + " - " + user.titulo + " - " + user.corpo + "Distancia " + "Tipo " + user.tipo  + calculateDistance(
                                                                lastLocation.latitude,
                                                                lastLocation.longitude,
                                                                position.latitude,
                                                                position.longitude
                                                            )
                                                        )
                                                    )
                                                    markers.toMutableList().add(marker);
                                                }
                                            } else {
                                                position = LatLng(
                                                    user.lat.toString().toDouble(),
                                                    user.lng.toString().toDouble()
                                                )
                                                if (calculateDistance(
                                                        position.latitude,
                                                        position.longitude,
                                                        lastLocation.latitude,
                                                        lastLocation.longitude
                                                    ) < 1000000
                                                ) {
                                                    marker = mMap.addMarker(
                                                        MarkerOptions().position(position).title(
                                                            user.nome + " - " + user.titulo + " - " + user.corpo + "Distancia " + "Tipo " + user.tipo  + calculateDistance(
                                                                lastLocation.latitude,
                                                                lastLocation.longitude,
                                                                position.latitude,
                                                                position.longitude
                                                            )
                                                        ).icon(
                                                            BitmapDescriptorFactory.defaultMarker(
                                                                BitmapDescriptorFactory.HUE_BLUE
                                                            )
                                                        )
                                                    )
                                                    markers.toMutableList().add(marker);
                                                }
                                            }
                                        }
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
        buttonMostraTodosMarkers.setOnClickListener{
            // call the service and add markers
            val request = ServiceBuilder.buildService(endPoints::class.java)
            val call = request.getOcorrencias()
            var position: LatLng

            call.enqueue(object : Callback<List<User>> {
                override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                    if (response.isSuccessful) {
                        users = response.body()!!

                        utilizadorAtual = id.toString();
                        for (user in users) {
                            if (user.users_id == id) {


                                if (tipo_id == 0) {
                                    //Toast.makeText(this@MapsActivity, user.lat, Toast.LENGTH_SHORT).show()
                                    if (user.id.toInt() == id?.toInt()) {
                                        position = LatLng(
                                            user.lat.toString().toDouble(),
                                            user.lng.toString().toDouble()
                                        )
                                        if(calculateDistance(position.latitude, position.longitude, lastLocation.latitude, lastLocation.longitude) < 1000000) {
                                            marker =  mMap.addMarker(
                                                MarkerOptions().position(position).title(
                                                    user.nome + " - " + user.titulo + " - " + user.corpo + "Distancia " + "Tipo " + user.tipo  + calculateDistance(
                                                        lastLocation.latitude, lastLocation.longitude,
                                                        position.latitude, position.longitude
                                                    )
                                                )
                                            )
                                            markers.toMutableList().add(marker);
                                        }
                                    } else {
                                        position = LatLng(
                                            user.lat.toString().toDouble(),
                                            user.lng.toString().toDouble()
                                        )
                                        if(calculateDistance(position.latitude, position.longitude, lastLocation.latitude, lastLocation.longitude) < 1000000) {
                                            marker = mMap.addMarker(
                                                MarkerOptions().position(position).title(
                                                    user.nome + " - " + user.titulo + " - " + user.corpo + "Distancia " + "Tipo " + user.tipo  + calculateDistance(
                                                        lastLocation.latitude, lastLocation.longitude,
                                                        position.latitude, position.longitude
                                                    )
                                                ).icon(
                                                    BitmapDescriptorFactory.defaultMarker(
                                                        BitmapDescriptorFactory.HUE_GREEN
                                                    )
                                                )
                                            )
                                            markers.toMutableList().add(marker);
                                        }
                                    }
                                } else {
                                    if (user.id.toInt() == tipo_id) {
                                        if (user.id.toInt() == id?.toInt()) {
                                            position = LatLng(
                                                user.lat.toString().toDouble(),
                                                user.lng.toString().toDouble()
                                            )
                                            if(calculateDistance(position.latitude, position.longitude, lastLocation.latitude, lastLocation.longitude) < 1000000) {
                                                marker =  mMap.addMarker(
                                                    MarkerOptions().position(position).title(
                                                        user.nome + " - " + user.titulo + " - " + user.corpo + "Distancia " + "Tipo " + user.tipo  + calculateDistance(
                                                            lastLocation.latitude,
                                                            lastLocation.longitude,
                                                            position.latitude,
                                                            position.longitude
                                                        )
                                                    )
                                                )
                                                markers.toMutableList().add(marker);
                                            }
                                        } else {
                                            position = LatLng(
                                                user.lat.toString().toDouble(),
                                                user.lng.toString().toDouble()
                                            )
                                            if(calculateDistance(position.latitude, position.longitude, lastLocation.latitude, lastLocation.longitude) < 1000000) {
                                                marker = mMap.addMarker(
                                                    MarkerOptions().position(position).title(
                                                        user.nome + " - " + user.titulo + " - " + user.corpo + "Distancia " + "Tipo " + user.tipo  + calculateDistance(
                                                            lastLocation.latitude,
                                                            lastLocation.longitude,
                                                            position.latitude,
                                                            position.longitude
                                                        )
                                                    ).icon(
                                                        BitmapDescriptorFactory.defaultMarker(
                                                            BitmapDescriptorFactory.HUE_GREEN
                                                        )
                                                    )
                                                )
                                                markers.toMutableList().add(marker);
                                            }
                                        }
                                    }
                                }
                            }else {
                                if (tipo_id == 0) {
                                    //Toast.makeText(this@MapsActivity, user.lat, Toast.LENGTH_SHORT).show()
                                    if (user.id.toInt() == id?.toInt()) {
                                        position = LatLng(
                                            user.lat.toString().toDouble(),
                                            user.lng.toString().toDouble()
                                        )
                                        if(calculateDistance(position.latitude, position.longitude, lastLocation.latitude, lastLocation.longitude) < 1000000) {
                                            marker = mMap.addMarker(
                                                MarkerOptions().position(position).title(
                                                    user.nome + " - " + user.titulo + " - " + user.corpo + "Distancia " + "Tipo " + user.tipo  + calculateDistance(
                                                        lastLocation.latitude, lastLocation.longitude,
                                                        position.latitude, position.longitude
                                                    )
                                                )
                                            )
                                            markers.toMutableList().add(marker);
                                        }
                                    } else {
                                        position = LatLng(
                                            user.lat.toString().toDouble(),
                                            user.lng.toString().toDouble()
                                        )
                                        if(calculateDistance(position.latitude, position.longitude, lastLocation.latitude, lastLocation.longitude) < 1000000) {
                                            marker =  mMap.addMarker(
                                                MarkerOptions().position(position).title(
                                                    user.nome + " - " + user.titulo + " - " + user.corpo + "Distancia " + "Tipo " + user.tipo  + calculateDistance(
                                                        lastLocation.latitude, lastLocation.longitude,
                                                        position.latitude, position.longitude
                                                    )
                                                ).icon(
                                                    BitmapDescriptorFactory.defaultMarker(
                                                        BitmapDescriptorFactory.HUE_BLUE
                                                    )
                                                )
                                            )
                                            markers.toMutableList().add(marker);
                                        }
                                    }
                                } else {
                                    if (user.id.toInt() == tipo_id) {
                                        if (user.id.toInt() == id?.toInt()) {
                                            position = LatLng(
                                                user.lat.toString().toDouble(),
                                                user.lng.toString().toDouble()
                                            )
                                            if(calculateDistance(position.latitude, position.longitude, lastLocation.latitude, lastLocation.longitude) < 1000000) {
                                                marker = mMap.addMarker(
                                                    MarkerOptions().position(position).title(
                                                        user.nome + " - " + user.titulo + " - " + user.corpo + "Distancia "+ "Tipo " + user.tipo  + calculateDistance(
                                                            lastLocation.latitude,
                                                            lastLocation.longitude,
                                                            position.latitude,
                                                            position.longitude
                                                        )
                                                    )
                                                )
                                                markers.toMutableList().add(marker);
                                            }
                                        } else {
                                            position = LatLng(
                                                user.lat.toString().toDouble(),
                                                user.lng.toString().toDouble()
                                            )
                                            if(calculateDistance(position.latitude, position.longitude, lastLocation.latitude, lastLocation.longitude) < 1000000) {
                                                marker = mMap.addMarker(
                                                    MarkerOptions().position(position).title(
                                                        user.nome + " - " + user.titulo + " - " + user.corpo + "Distancia " + "Tipo " + user.tipo  + calculateDistance(
                                                            lastLocation.latitude,
                                                            lastLocation.longitude,
                                                            position.latitude,
                                                            position.longitude
                                                        )
                                                    ).icon(
                                                        BitmapDescriptorFactory.defaultMarker(
                                                            BitmapDescriptorFactory.HUE_BLUE
                                                        )
                                                    )
                                                )
                                                markers.toMutableList().add(marker);
                                            }
                                        }
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
        buttonFiltrarProximos.setOnClickListener{
            mMap.clear();
            val request = ServiceBuilder.buildService(endPoints::class.java)
            val call = request.getOcorrencias()
            var position: LatLng

            call.enqueue(object : Callback<List<User>> {
                override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                    if (response.isSuccessful) {
                        users = response.body()!!

                        utilizadorAtual = id.toString();
                        for (user in users) {
                            if (user.users_id == id) {


                                if (tipo_id == 0) {
                                    //Toast.makeText(this@MapsActivity, user.lat, Toast.LENGTH_SHORT).show()
                                    if (user.id.toInt() == id?.toInt()) {
                                        position = LatLng(
                                            user.lat.toString().toDouble(),
                                            user.lng.toString().toDouble()
                                        )
                                        if(calculateDistance(position.latitude, position.longitude, lastLocation.latitude, lastLocation.longitude) < 1000) {
                                            marker =  mMap.addMarker(
                                                MarkerOptions().position(position).title(
                                                    user.nome + " - " + user.titulo + " - " + user.corpo + "Distancia " + "Tipo " + user.tipo + calculateDistance(
                                                        lastLocation.latitude, lastLocation.longitude,
                                                        position.latitude, position.longitude
                                                    )
                                                )
                                            )
                                            markers.toMutableList().add(marker);
                                        }
                                    } else {
                                        position = LatLng(
                                            user.lat.toString().toDouble(),
                                            user.lng.toString().toDouble()
                                        )
                                        if(calculateDistance(position.latitude, position.longitude, lastLocation.latitude, lastLocation.longitude) < 1000) {
                                            marker = mMap.addMarker(
                                                MarkerOptions().position(position).title(
                                                    user.nome + " - " + user.titulo + " - " + user.corpo + "Distancia "+ "Tipo " + user.tipo  + calculateDistance(
                                                        lastLocation.latitude, lastLocation.longitude,
                                                        position.latitude, position.longitude
                                                    )
                                                ).icon(
                                                    BitmapDescriptorFactory.defaultMarker(
                                                        BitmapDescriptorFactory.HUE_GREEN
                                                    )
                                                )
                                            )
                                            markers.toMutableList().add(marker);
                                        }
                                    }
                                } else {
                                    if (user.id.toInt() == tipo_id) {
                                        if (user.id.toInt() == id?.toInt()) {
                                            position = LatLng(
                                                user.lat.toString().toDouble(),
                                                user.lng.toString().toDouble()
                                            )
                                            if(calculateDistance(position.latitude, position.longitude, lastLocation.latitude, lastLocation.longitude) < 1000) {
                                                marker =  mMap.addMarker(
                                                    MarkerOptions().position(position).title(
                                                        user.nome + " - " + user.titulo + " - " + user.corpo + "Distancia "+ "Tipo " + user.tipo  + calculateDistance(
                                                            lastLocation.latitude,
                                                            lastLocation.longitude,
                                                            position.latitude,
                                                            position.longitude
                                                        )
                                                    )
                                                )
                                                markers.toMutableList().add(marker);
                                            }
                                        } else {
                                            position = LatLng(
                                                user.lat.toString().toDouble(),
                                                user.lng.toString().toDouble()
                                            )
                                            if(calculateDistance(position.latitude, position.longitude, lastLocation.latitude, lastLocation.longitude) < 1000) {
                                                marker = mMap.addMarker(
                                                    MarkerOptions().position(position).title(
                                                        user.nome + " - " + user.titulo + " - " + user.corpo + "Distancia "+ "Tipo " + user.tipo  + calculateDistance(
                                                            lastLocation.latitude,
                                                            lastLocation.longitude,
                                                            position.latitude,
                                                            position.longitude
                                                        )
                                                    ).icon(
                                                        BitmapDescriptorFactory.defaultMarker(
                                                            BitmapDescriptorFactory.HUE_GREEN
                                                        )
                                                    )
                                                )
                                                markers.toMutableList().add(marker);
                                            }
                                        }
                                    }
                                }
                            }else {
                                if (tipo_id == 0) {
                                    //Toast.makeText(this@MapsActivity, user.lat, Toast.LENGTH_SHORT).show()
                                    if (user.id.toInt() == id?.toInt()) {
                                        position = LatLng(
                                            user.lat.toString().toDouble(),
                                            user.lng.toString().toDouble()
                                        )
                                        if(calculateDistance(position.latitude, position.longitude, lastLocation.latitude, lastLocation.longitude) < 1000) {
                                            marker = mMap.addMarker(
                                                MarkerOptions().position(position).title(
                                                    user.nome + " - " + user.titulo + " - " + user.corpo + "Distancia " + "Tipo " + user.tipo + calculateDistance(
                                                        lastLocation.latitude, lastLocation.longitude,
                                                        position.latitude, position.longitude
                                                    )
                                                )
                                            )
                                            markers.toMutableList().add(marker);
                                        }
                                    } else {
                                        position = LatLng(
                                            user.lat.toString().toDouble(),
                                            user.lng.toString().toDouble()
                                        )
                                        if(calculateDistance(position.latitude, position.longitude, lastLocation.latitude, lastLocation.longitude) < 1000) {
                                            marker =  mMap.addMarker(
                                                MarkerOptions().position(position).title(
                                                    user.nome + " - " + user.titulo + " - " + user.corpo + "Distancia " + "Tipo " + user.tipo + calculateDistance(
                                                        lastLocation.latitude, lastLocation.longitude,
                                                        position.latitude, position.longitude
                                                    )
                                                ).icon(
                                                    BitmapDescriptorFactory.defaultMarker(
                                                        BitmapDescriptorFactory.HUE_BLUE
                                                    )
                                                )
                                            )
                                            markers.toMutableList().add(marker);
                                        }
                                    }
                                } else {
                                    if (user.id.toInt() == tipo_id) {
                                        if (user.id.toInt() == id?.toInt()) {
                                            position = LatLng(
                                                user.lat.toString().toDouble(),
                                                user.lng.toString().toDouble()
                                            )
                                            if(calculateDistance(position.latitude, position.longitude, lastLocation.latitude, lastLocation.longitude) < 1000) {
                                                marker = mMap.addMarker(
                                                    MarkerOptions().position(position).title(
                                                        user.nome + " - " + user.titulo + " - " + user.corpo + "Distancia "+ "Tipo " + user.tipo  + calculateDistance(
                                                            lastLocation.latitude,
                                                            lastLocation.longitude,
                                                            position.latitude,
                                                            position.longitude
                                                        )
                                                    )
                                                )
                                                markers.toMutableList().add(marker);
                                            }
                                        } else {
                                            position = LatLng(
                                                user.lat.toString().toDouble(),
                                                user.lng.toString().toDouble()
                                            )
                                            if(calculateDistance(position.latitude, position.longitude, lastLocation.latitude, lastLocation.longitude) < 1000) {
                                                marker = mMap.addMarker(
                                                    MarkerOptions().position(position).title(
                                                        user.nome + " - " + user.titulo + " - " + user.corpo + "Distancia "+ "Tipo " + user.tipo  + calculateDistance(
                                                            lastLocation.latitude,
                                                            lastLocation.longitude,
                                                            position.latitude,
                                                            position.longitude
                                                        )
                                                    ).icon(
                                                        BitmapDescriptorFactory.defaultMarker(
                                                            BitmapDescriptorFactory.HUE_BLUE
                                                        )
                                                    )
                                                )
                                                markers.toMutableList().add(marker);
                                            }
                                        }
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
        val extras = intent.extras
         id = extras?.getString("id")
        tipo_id = extras?.getInt("tipo_id")


    }

    private fun calculateDistance(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Float {

        val results = FloatArray(1);
        Location.distanceBetween(lat1,lng1,lat2,lng2,results)

        return results[0];
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


            R.id.add_ocorrenciaAbre -> {
                val extras = intent.extras
                val id = extras?.getString("id")

                var loc = LatLng(lastLocation.latitude, lastLocation.longitude)
                val intent = Intent(this@mapaOcorrencias, AddOcorrencia::class.java)
                intent.putExtra("lat", loc.latitude.toString())
                intent.putExtra("lng", loc.longitude.toString())
                intent.putExtra("id", id)

                startActivity(intent)

                true
            }

            R.id.logout -> {
                var token = getSharedPreferences("utilizador", Context.MODE_PRIVATE)
                intent.putExtra("loginutilizador", " ")
                var editor = token.edit()
                editor.putString("loginutilizador", " ")

                editor.commit()
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                val intent = Intent(this@mapaOcorrencias, Login::class.java)
                startActivity(intent)
                finish()
                true
            }

            R.id.filtarTipoId -> {

                val intent = Intent(this@mapaOcorrencias, AtividadeFiltrar::class.java)
                intent.putExtra("utilizadorId", utilizadorAtual);
                onPause()
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


        // call the service and add markers
        val request = ServiceBuilder.buildService(endPoints::class.java)
        val call = request.getOcorrencias()
        var position: LatLng

        call.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    users = response.body()!!

                    utilizadorAtual = id.toString();
                    for (user in users) {
                        if (user.users_id == id) {


                            if (tipo_id == 0) {
                                //Toast.makeText(this@MapsActivity, user.lat, Toast.LENGTH_SHORT).show()
                                if (user.id.toInt() == id?.toInt()) {
                                    position = LatLng(
                                        user.lat.toString().toDouble(),
                                        user.lng.toString().toDouble()
                                    )

                                       marker =  mMap.addMarker(
                                            MarkerOptions().position(position).title(
                                                user.nome + " - " + user.titulo + " - " + user.corpo + "Distancia " + "Tipo " + user.tipo + calculateDistance(
                                                    lastLocation.latitude, lastLocation.longitude,
                                                    position.latitude, position.longitude
                                                )
                                            )
                                        )
                                        markers.toMutableList().add(marker);

                                } else {
                                    position = LatLng(
                                        user.lat.toString().toDouble(),
                                        user.lng.toString().toDouble()
                                    )

                                        marker = mMap.addMarker(
                                            MarkerOptions().position(position).title(
                                                user.nome + " - " + user.titulo + " - " + user.corpo + "Distancia "+ "Tipo " + user.tipo  + calculateDistance(
                                                    lastLocation.latitude, lastLocation.longitude,
                                                    position.latitude, position.longitude
                                                )
                                            ).icon(
                                                BitmapDescriptorFactory.defaultMarker(
                                                    BitmapDescriptorFactory.HUE_GREEN
                                                )
                                            )
                                        )
                                        markers.toMutableList().add(marker);

                                }
                            } else {
                                if (user.id.toInt() == tipo_id) {
                                    if (user.id.toInt() == id?.toInt()) {
                                        position = LatLng(
                                            user.lat.toString().toDouble(),
                                            user.lng.toString().toDouble()
                                        )

                                          marker =  mMap.addMarker(
                                                MarkerOptions().position(position).title(
                                                    user.nome + " - " + user.titulo + " - " + user.corpo + "Distancia "+ "Tipo " + user.tipo  + calculateDistance(
                                                        lastLocation.latitude,
                                                        lastLocation.longitude,
                                                        position.latitude,
                                                        position.longitude
                                                    )
                                                )
                                            )
                                            markers.toMutableList().add(marker);

                                    } else {
                                        position = LatLng(
                                            user.lat.toString().toDouble(),
                                            user.lng.toString().toDouble()
                                        )

                                            marker = mMap.addMarker(
                                                MarkerOptions().position(position).title(
                                                    user.nome + " - " + user.titulo + " - " + user.corpo + "Distancia "+ "Tipo " + user.tipo  + calculateDistance(
                                                        lastLocation.latitude,
                                                        lastLocation.longitude,
                                                        position.latitude,
                                                        position.longitude
                                                    )
                                                ).icon(
                                                    BitmapDescriptorFactory.defaultMarker(
                                                        BitmapDescriptorFactory.HUE_GREEN
                                                    )
                                                )
                                            )
                                            markers.toMutableList().add(marker);

                                    }
                                }
                            }
                        }else {
                            if (tipo_id == 0) {
                                //Toast.makeText(this@MapsActivity, user.lat, Toast.LENGTH_SHORT).show()
                                if (user.id.toInt() == id?.toInt()) {
                                    position = LatLng(
                                        user.lat.toString().toDouble(),
                                        user.lng.toString().toDouble()
                                    )

                                       marker = mMap.addMarker(
                                            MarkerOptions().position(position).title(
                                                user.nome + " - " + user.titulo + " - " + user.corpo + "Distancia " + "Tipo " + user.tipo + calculateDistance(
                                                    lastLocation.latitude, lastLocation.longitude,
                                                    position.latitude, position.longitude
                                                )
                                            )
                                        )
                                        markers.toMutableList().add(marker);

                                } else {
                                    position = LatLng(
                                        user.lat.toString().toDouble(),
                                        user.lng.toString().toDouble()
                                    )

                                       marker =  mMap.addMarker(
                                            MarkerOptions().position(position).title(
                                                user.nome + " - " + user.titulo + " - " + user.corpo + "Distancia " + "Tipo " + user.tipo + calculateDistance(
                                                    lastLocation.latitude, lastLocation.longitude,
                                                    position.latitude, position.longitude
                                                )
                                            ).icon(
                                                BitmapDescriptorFactory.defaultMarker(
                                                    BitmapDescriptorFactory.HUE_BLUE
                                                )
                                            )
                                        )
                                        markers.toMutableList().add(marker);

                                }
                            } else {
                                if (user.id.toInt() == tipo_id) {
                                    if (user.id.toInt() == id?.toInt()) {
                                        position = LatLng(
                                            user.lat.toString().toDouble(),
                                            user.lng.toString().toDouble()
                                        )

                                           marker = mMap.addMarker(
                                                MarkerOptions().position(position).title(
                                                    user.nome + " - " + user.titulo + " - " + user.corpo + "Distancia "+ "Tipo " + user.tipo  + calculateDistance(
                                                        lastLocation.latitude,
                                                        lastLocation.longitude,
                                                        position.latitude,
                                                        position.longitude
                                                    )
                                                )
                                            )
                                            markers.toMutableList().add(marker);

                                    } else {
                                        position = LatLng(
                                            user.lat.toString().toDouble(),
                                            user.lng.toString().toDouble()
                                        )

                                            marker = mMap.addMarker(
                                                MarkerOptions().position(position).title(
                                                    user.nome + " - " + user.titulo + " - " + user.corpo + "Distancia "+ "Tipo " + user.tipo  + calculateDistance(
                                                        lastLocation.latitude,
                                                        lastLocation.longitude,
                                                        position.latitude,
                                                        position.longitude
                                                    )
                                                ).icon(
                                                    BitmapDescriptorFactory.defaultMarker(
                                                        BitmapDescriptorFactory.HUE_BLUE
                                                    )
                                                )
                                            )
                                            markers.toMutableList().add(marker);

                                    }
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
        // call the service and add markers

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
