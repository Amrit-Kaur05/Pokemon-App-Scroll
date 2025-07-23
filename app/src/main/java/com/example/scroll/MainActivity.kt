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
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler.JSON
import okhttp3.Headers
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var pokemonBall: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        pokemonBall = findViewById(R.id.pokeImg)
        pokemonBall.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.pokeball))

        // making a button to update the pokemon image
        var generateButton = findViewById<Button>(R.id.generate)

        generateButton.setOnClickListener {
            updatePokemon()
        }

    }

    private fun updatePokemon() {

        val client = AsyncHttpClient()

        // getting a random number to use as an id
        val randomID = Random.nextInt(1, 1026).toString()
        Log.d("randomID", randomID)

        client[("https://pokeapi.co/api/v2/pokemon/"+randomID), object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                Log.d("Pokemon", "response successful")

                // updating pokemon image
//                val pokemonImgURL = json.jsonObject.get("sprites").getString("default-front")
                val pokemonImgURL = json.jsonObject.getJSONObject("sprites").getString("front_default")

//                val pokemonImgURL = json.jsonObject.getString("front_default")
//                val spriteUrl = json.jsonObject.sprites.front_default // Get the default front sprite
                Log.d("pokemonImgURL", "pokemon image URL set: $pokemonImgURL")
                val pokemonImageOutput = findViewById<ImageView>(R.id.pokeImg)
                Glide.with(this@MainActivity)
                    .load(pokemonImgURL)
                    .fitCenter()
                    .into(pokemonImageOutput)

                // updating pokemon name
                val pokemonName = json.jsonObject.getString("name").replaceFirstChar { it.titlecase() }
                Log.d("pokemonName", "pokemon name set: $pokemonName")
                val pokemonNameOutput = findViewById<TextView>(R.id.name)
                pokemonNameOutput.text = pokemonName

                // updating pokemon abilities
                var amountAbilities = json.jsonObject.getJSONArray("abilities").length()
                Log.d("pokemonAbilitiesCount", "pokemon ability count: $amountAbilities")
                val pokemonAbilityOutput = findViewById<TextView>(R.id.abilities)
                pokemonAbilityOutput.text = ""
                var allAbilities = "Abilities: \n"

                for (i in 0 until amountAbilities){
                    Log.d("i", "pokemon count: $i")
                    allAbilities = allAbilities + "\n" + pokemonAbilityOutput.getText().toString() + json.jsonObject.getJSONArray("abilities").getJSONObject(i).getJSONObject("ability").getString("name").replaceFirstChar { it.titlecase() }

                }

                pokemonAbilityOutput.text = allAbilities
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
}