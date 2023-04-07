package com.example.tpnote

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {
    val listeStations : ArrayList<Station> = ArrayList<Station>() // Liste qui va accueillir tous les objets Stations
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listStationsVelos() // Appel de la fonction récupération des données
    }

    // Fonction pour la requête de récupération des données
    fun listStationsVelos() {

        val client = OkHttpClient() // Module pour faire pouvoir faire l'appel à la requête
        val request = Request.Builder()
            .url("https://api.agglo-larochelle.fr/production/opendata/api/records/1.0/search/dataset=yelo___disponibilite_des_velos_en_libre_service&facet=station_nom")
            .header("Authorization", "a237e560-ca4b-4059-86c6-4f9111b6ae7a")
            .build() // Construction de la requête


        client.newCall(request).enqueue(object : Callback { // Appel de l'API
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    // Méthode onResponse qui récupère le retour d'appel à l'API
                    val jsonArray = JSONObject(response.body!!.string()).getJSONArray("records") // Récupération uniquement d'une partie des informations surtout celle qui nous intéressent


                    for(i in 0 until jsonArray.length()){

                        listeStations.add( // Ajout dans la liste
                            // Création de l'objet Station avec tous les paramètres récupéré dans l'API
                            Station(
                                jsonArray.getJSONObject(i).getJSONObject("fields").getInt("nombre_emplacements"),
                                jsonArray.getJSONObject(i).getJSONObject("fields").getInt("velos_disponibles"),
                                jsonArray.getJSONObject(i).getJSONObject("fields").getString("station_nom"),
                                jsonArray.getJSONObject(i).getJSONObject("fields").getDouble("station_longitude"),
                                jsonArray.getJSONObject(i).getJSONObject("fields").getDouble("station_latitude"))
                        )
                    }

                    this@MainActivity.runOnUiThread(java.lang.Runnable {
                            val listViewVelo = findViewById<ListView>(R.id.listViewVelo)
                            val buttonlisten = findViewById<Button>(R.id.button)

                            listViewVelo.adapter = ArrayAdapter<Station>(
                                    this@MainActivity,
                                    android.R.layout.simple_list_item_1,
                                    listeStations) // Affichage des stations dans le listView avec la liste des stations

                        listViewVelo.setOnItemClickListener { parent, _, position, _ -> // Listener sur le listview
                                val objetStation = parent.getItemAtPosition(position) as Station // Récupération de l'objet cliqué

                            val intent : Intent = Intent(this@MainActivity, MapsActivity::class.java).apply {
                                putExtra("stationSeul" , objetStation) // Passage de l'objet dnas l'activité Maps via Intent
                            }
                            startActivity(intent) // Lancement de l'activité
                        }

                        buttonlisten.setOnClickListener{ // Listener sur le button

                            val intentAllStations : Intent = Intent(this@MainActivity,MapsActivity::class.java).apply {
                                putParcelableArrayListExtra("allStations" , listeStations) // Passage de la liste des stations
                            }
                            startActivity(intentAllStations) // Lancement de l'activité

                        }


                    })
                }
            }
        })
    }
}