package com.example.scroll

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler.JSON
import okhttp3.Headers
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var pokeImgList: MutableList<String>
    private lateinit var pokeTitleList: MutableList<String>
    private lateinit var pokeDescList: MutableList<String>
    private lateinit var rvPoke: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        rvPoke = findViewById<RecyclerView>(R.id.pokemon_list)
        pokeImgList = mutableListOf()
        pokeTitleList = mutableListOf()
        pokeDescList = mutableListOf()
        updatePokemon()

    }

    private fun updatePokemon() {

        val client = AsyncHttpClient()

        for (i in 0 until 20) {
            // getting a random number to use as an id
            val randomID = Random.nextInt(1, 1026).toString()
            Log.d("randomID", randomID)

            client[("https://pokeapi.co/api/v2/pokemon/" + randomID), object :
                JsonHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                    Log.d("Pokemon", "response successful")

                    // getting pokemon image and adding to list
                    val pokemonImgURL =
                        json.jsonObject.getJSONObject("sprites").getString("front_default")
                    pokeImgList.add(pokemonImgURL)

                    // getting pokemon name and adding to list
                    val pokemonName =
                        json.jsonObject.getString("name").replaceFirstChar { it.titlecase() }
                    pokeTitleList.add(pokemonName)

                    // updating pokemon abilities and adding to list
                    var amountAbilities = json.jsonObject.getJSONArray("abilities").length()
                    var allAbilities = "Abilities: \n"
                    for (i in 0 until amountAbilities) {
                        Log.d("i", "pokemon count: $i")
                        allAbilities =
                            allAbilities + "\n" + json.jsonObject.getJSONArray("abilities")
                                .getJSONObject(i).getJSONObject("ability").getString("name")
                                .replaceFirstChar { it.titlecase() }

                    }
                    pokeDescList.add(allAbilities)



                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Headers?,
                    errorResponse: String,
                    throwable: Throwable?
                ) {
                    Log.d("Pokemon Error", errorResponse)
                }
            }]
        }

        // using adapter and lists
        val adapter = PokeAdapter(pokeImgList, pokeTitleList, pokeDescList)
        rvPoke.adapter = adapter
        rvPoke.layoutManager = LinearLayoutManager(this@MainActivity)
    }
}