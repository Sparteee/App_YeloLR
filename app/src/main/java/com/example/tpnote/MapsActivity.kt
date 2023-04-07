package com.example.tpnote

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.tpnote.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
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
        val station : Station? = intent.getParcelableExtra<Station>("stationSeul") // Récupération des données passé pour l'objet station seul
        if(station != null){ // Vérification si il est null pour éviter les interférences entre le bouton et le listView
                val markerStation = LatLng(station.lat, station.long) // Création du marker à placer
                mMap.addMarker(MarkerOptions().position(markerStation).title(station.nom).snippet("${station.nbVelos} / ${station.nbEmplacements} places")) // Ajout du marqueur avec son titre et le sous titre
                mMap.moveCamera(CameraUpdateFactory.newLatLng(markerStation)) // Mouvement de caméra pour être placé sur le marqueur
            }

        val allStations = intent.getParcelableArrayListExtra<Station>("allStations") // Récupération des données passé pour la liste de stations
        var moyenneLat = 0.0
        var moyenneLong = 0.0
        var cpt = 0
        if (allStations != null) { // Vérification si il est null pour éviter les interférences entre le bouton et le listView
            for(objetstation in allStations){ // Boucle pour récupérer chaque objet de la liste
                moyenneLat += objetstation.lat // Somme des latitudes
                moyenneLong += objetstation.long  // Sommes des longitudes
                val markerAllStation = LatLng(objetstation.lat, objetstation.long) // Chaque tour de boucle, création d'un marqueur avec les données correspondant à chaque station présent dans la liste
                mMap.addMarker(MarkerOptions().position(markerAllStation).title(objetstation.nom).snippet("${objetstation.nbVelos} / ${objetstation.nbEmplacements} places")) // Ajout du marqueur avec son titre et le sous titre
                cpt++ // Compteur
            }
            val moyenneMarqueur = LatLng(moyenneLat/cpt,moyenneLong/cpt) // Marqueur moyen non affiché pour avoir une caméra centré
            mMap.moveCamera(CameraUpdateFactory.newLatLng(moyenneMarqueur)) // Move caméra sur le marqueur moyen pour avoir caméra centré sur tous les points

        }
    }
}