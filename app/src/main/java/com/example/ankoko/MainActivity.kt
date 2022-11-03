package com.example.ankoko

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.ankoko.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // toolbar 를 설정한다.
        setSupportActionBar(binding.mainToolbar)
        title = "메모앱"
    }

    // option 메뉴를 설정한다
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            // 추가 버튼
            R.id.main_menu_add -> {
                val memoAddIntent = Intent(this, MemoAddActivity::class.java)
                startActivity(memoAddIntent)
            }
        }

        return super.onOptionsItemSelected(item)
    }
}