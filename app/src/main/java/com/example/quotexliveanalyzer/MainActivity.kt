import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.TextView

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val screen = TextView(this)
        screen.text = "QUOTEX LIVE ANALYZER\n\nLAUNCH TEST OK"
        screen.textSize = 24f
        screen.setTextColor(Color.WHITE)
        screen.setBackgroundColor(Color.rgb(5, 8, 13))
        screen.gravity = Gravity.CENTER

        setContentView(screen)
    }
}
