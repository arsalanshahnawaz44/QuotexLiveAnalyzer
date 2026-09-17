import android.app.Activity
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {

    private val bg = Color.rgb(5, 8, 13)
    private val panel = Color.rgb(15, 20, 28)
    private val purple = Color.rgb(130, 80, 255)
    private val green = Color.rgb(0, 220, 120)
    private val red = Color.rgb(255, 75, 85)
    private val white = Color.WHITE
    private val gray = Color.rgb(150, 160, 175)

    private lateinit var chart: CandleChart
    private lateinit var signal: TextView
    private lateinit var status: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = LinearLayout(this)
        root.orientation = LinearLayout.VERTICAL
        root.setBackgroundColor(bg)
        root.setPadding(20, 20, 20, 20)

        root.addView(text("● LIVE   QUOTEX ANALYZER", 21f, green, true))

        status = text("Ready — select pair and timeframe", 12f, gray)
        status.setPadding(0, 8, 0, 12)
        root.addView(status)

        // LIVE / OTC buttons
        val modeRow = LinearLayout(this)
        modeRow.orientation = LinearLayout.HORIZONTAL

        val liveButton = button("LIVE")
        val otcButton = button("OTC")

        modeRow.addView(
            liveButton,
            LinearLayout.LayoutParams(0, 55, 1f).apply {
                setMargins(0, 0, 8, 0)
            }
        )

        modeRow.addView(
            otcButton,
            LinearLayout.LayoutParams(0, 55, 1f)
        )

        root.addView(modeRow)

        liveButton.setOnClickListener {
            status.text = "LIVE mode selected"
        }

        otcButton.setOnClickListener {
            status.text = "OTC mode selected"
        }

        // Pair buttons
        val pairTitle = text("PAIR", 13f, gray, true)
        pairTitle.setPadding(0, 14, 0, 6)
        root.addView(pairTitle)

        val pairRow = LinearLayout(this)
        pairRow.orientation = LinearLayout.HORIZONTAL

        val eur = button("EUR/USD")
        val gbp = button("GBP/USD")
        val jpy = button("USD/JPY")

        pairRow.addView(eur, LinearLayout.LayoutParams(0, 55, 1f))
        pairRow.addView(gbp, LinearLayout.LayoutParams(0, 55, 1f))
        pairRow.addView(jpy, LinearLayout.LayoutParams(0, 55, 1f))

        root.addView(pairRow)

        eur.setOnClickListener {
            status.text = "EUR/USD selected"
        }

        gbp.setOnClickListener {
            status.text = "GBP/USD selected"
        }

        jpy.setOnClickListener {
            status.text = "USD/JPY selected"
        }

        // Timeframes
        val tfTitle = text("TIMEFRAME", 13f, gray, true)
        tfTitle.setPadding(0, 14, 0, 6)
        root.addView(tfTitle)

        val tfRow = LinearLayout(this)
        tfRow.orientation = LinearLayout.HORIZONTAL

        val one = button("1M")
        val two = button("2M")
        val five = button("5M")
        val ten = button("10M")

        tfRow.addView(one, LinearLayout.LayoutParams(0, 50, 1f))
        tfRow.addView(two, LinearLayout.LayoutParams(0, 50, 1f))
        tfRow.addView(five, LinearLayout.LayoutParams(0, 50, 1f))
        tfRow.addView(ten, LinearLayout.LayoutParams(0, 50, 1f))

        root.addView(tfRow)

        one.setOnClickListener { status.text = "Timeframe: 1 minute" }
        two.setOnClickListener { status.text = "Timeframe: 2 minutes" }
        five.setOnClickListener { status.text = "Timeframe: 5 minutes" }
        ten.setOnClickListener { status.text = "Timeframe: 10 minutes" }

        // Chart
        chart = CandleChart(this)

        root.addView(
            chart,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                430
            ).apply {
                setMargins(0, 14, 0, 0)
            }
        )

        root.addView(
            text("TECHNICAL ANALYSIS", 17f, white, true).apply {
                setPadding(0, 14, 0, 8)
            }
        )

        val indicators = LinearLayout(this)
        indicators.orientation = LinearLayout.VERTICAL
        indicators.setBackgroundColor(panel)
        indicators.setPadding(18, 10, 18, 10)

        indicators.addView(text("EMA 20 / 50     TREND: WAITING", 14f, gray))
        indicators.addView(text("RSI              --", 14f, white))
        indicators.addView(text("MACD             --", 14f, gray))
        indicators.addView(text("BOLLINGER        --", 14f, gray))
        indicators.addView(text("SUPPORT          --", 14f, gray))
        indicators.addView(text("RESISTANCE       --", 14f, gray))

        root.addView(indicators)

        root.addView(
            text("NEXT CANDLE SIGNAL", 17f, white, true).apply {
                setPadding(0, 14, 0, 8)
            }
        )

        signal = TextView(this)
        signal.text = "WAITING FOR DATA"
        signal.textSize = 22f
        signal.setTextColor(gray)
        signal.gravity = Gravity.CENTER
        signal.setBackgroundColor(panel)
        signal.setPadding(10, 20, 10, 20)

        root.addView(signal)

        root.addView(
            text(
                "Signal will be calculated after market data is connected.",
                12f,
                gray
            ).apply {
                setPadding(0, 8, 0, 8)
            }
        )

        // ANALYZE button
        val analyze = button("ANALYZE")
        analyze.textSize = 19f
        analyze.setTextColor(white)

        root.addView(
            analyze,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                65
            )
        )

        analyze.setOnClickListener {
            analyzeMarket()
        }

        setContentView(root)
    }

    private fun analyzeMarket() {
        status.text = "Analyzing current chart data..."

        // Current build intentionally does NOT fake a prediction.
        signal.text = "DATA REQUIRED"
        signal.setTextColor(gray)

        status.postDelayed({
            status.text =
                "No live market feed connected yet"
        }, 700)
    }

    private fun button(label: String): Button {
        return Button(this).apply {
            text = label
            textSize = 14f
            setTextColor(white)
            setBackgroundColor(purple)
            isAllCaps = false
        }
    }

    private fun text(
        value: String,
        size: Float,
        color: Int,
        bold: Boolean = false
    ): TextView {
        return TextView(this).apply {
            text = value
            textSize = size
            setTextColor(color)

            if (bold) {
                setTypeface(
                    typeface,
                    android.graphics.Typeface.BOLD
                )
            }
        }
    }

    class CandleChart(context: android.content.Context) : View(context) {

        private val green = Color.rgb(0, 220, 120)
        private val red = Color.rgb(255, 75, 85)
        private val blue = Color.rgb(80, 160, 255)

        private val paint =
            Paint(Paint.ANTI_ALIAS_FLAG)

        private val candles = floatArrayOf(
            0.68f, 0.64f, 0.70f, 0.61f, 0.58f,
            0.63f, 0.55f, 0.51f, 0.57f, 0.48f,
            0.44f, 0.49f, 0.41f, 0.38f, 0.43f,
            0.35f, 0.31f, 0.36f, 0.28f, 0.24f
        )

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)

            canvas.drawColor(Color.rgb(9, 13, 19))

            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 1f
            paint.color = Color.rgb(35, 45, 58)

            for (i in 1..5) {
                val y = height * i / 6f
                canvas.drawLine(
                    0f, y,
                    width.toFloat(), y,
                    paint
                )
            }

            for (i in 1..5) {
                val x = width * i / 6f
                canvas.drawLine(
                    x, 0f,
                    x, height.toFloat(),
                    paint
                )
            }

            val candleWidth = width / 27f

            for (i in candles.indices) {

                val x =
                    candleWidth * (i * 1.3f + 2f)

                val close = candles[i]
                val open =
                    if (i == 0)
                        0.73f
                    else
                        candles[i - 1]

                val high =
                    minOf(open, close) - 0.035f

                val low =
                    maxOf(open, close) + 0.035f

                paint.color =
                    if (close > open)
                        green
                    else
                        red

                paint.strokeWidth = 3f

                canvas.drawLine(
                    x,
                    high * height,
                    x,
                    low * height,
                    paint
                )

                paint.style = Paint.Style.FILL

                canvas.drawRect(
                    x - candleWidth / 2,
                    minOf(open, close) * height,
                    x + candleWidth / 2,
                    maxOf(open, close) * height,
                    paint
                )
            }

            // EMA-style visual line
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 4f
            paint.color = blue

            val path = android.graphics.Path()
            path.moveTo(
                0f,
                height * 0.67f
            )

            for (i in 1..width step 25) {
                val y =
                    height *
                        (0.67f -
                         i.toFloat() /
                         width * 0.38f)

                path.lineTo(
                    i.toFloat(),
                    y
                )
            }

            canvas.drawPath(path, paint)

            paint.style = Paint.Style.FILL
        }
    }
}
