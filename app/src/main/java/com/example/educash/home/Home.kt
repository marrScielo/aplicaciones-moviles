package com.example.educash.home

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.educash.R
import com.example.educash.book.ThirdFragment
import com.example.educash.homee.homeFragment
import com.example.educash.profile.SecondFragment
import com.example.educash.search.FirstFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class Home : AppCompatActivity() {

    private val homeFragment = homeFragment()
    private val firstFragment = FirstFragment()
    private val secondFragment = SecondFragment()
    private val thirdFragment = ThirdFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        val prefs = getSharedPreferences("auth", MODE_PRIVATE)
        val nombre = prefs.getString("nombre", null)
            ?: intent.getStringExtra("nombre")
            ?: "Usuario"


        homeFragment.arguments = (homeFragment.arguments ?: Bundle()).apply {
            putString("nombre", nombre)
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Carga inicial solo una vez
        if (savedInstanceState == null) {
            loadFragment(homeFragment)
        }

        bottomNav.setOnItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.homeFragment   -> { loadFragment(homeFragment);   true }
                R.id.thirdFragment  -> { loadFragment(thirdFragment);  true }
                R.id.FirstFragment  -> { loadFragment(firstFragment);  true } // ojo: mayúscula
                R.id.SecondFragment -> { loadFragment(secondFragment); true } // ojo: mayúscula
                else -> false
            }
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.frame_container)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, 0, bars.right, bars.bottom)
            insets
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_container, fragment)
            .commit()
    }
}
