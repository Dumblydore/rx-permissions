package me.mauricee.rxPermissions.demo

import android.Manifest
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import io.reactivex.disposables.CompositeDisposable

import kotlinx.android.synthetic.main.activity_main.*
import me.mauricee.rxpermission.requestPermissions

class MainActivity : AppCompatActivity() {
private val dis = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            dis.add(this.requestPermissions(Manifest.permission.CAMERA).all().subscribe { allAccepted ->
                Snackbar.make(view, "All accepted $allAccepted", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
