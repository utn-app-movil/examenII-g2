package cr.ac.utn.appmovil.rooms

import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


abstract class wils_BaseActivity : AppCompatActivity() {
    
    protected lateinit var tvMessage: TextView
    protected fun showMessage(message: String) {
        tvMessage.text = message
        tvMessage.visibility = View.VISIBLE
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}