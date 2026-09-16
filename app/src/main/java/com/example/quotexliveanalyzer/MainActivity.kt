package com.example.quotexliveanalyzer

import android.app.Activity
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {

    private val bg = Color.rgb(5, 8, 13)
    private val panel = Color.rgb(15, 20, 28)
    private val green = Color.rgb(0, 220, 120)
    private val red = Color.rgb(255, 75, 85)
    private val white = Color.WHITE
    private val gray = Color.rgb(150, 160, 175)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = LinearLayout(this)
        root.orientation = LinearLayout.VERTICAL
        root.setBackgroundColor(bg)
        root.setPadding(20, 20, 20, 20)

        root.addView(
            text("QUOTEX LIVE ANALYZER", 22f, white, true)
        )

        root.addView(
            text("LIVE        OTC", 15f, green, true).apply {
                setPadding(0, 18, 0, 18)
            }
        )

        val pair = LinearLayout(this)
        pair.setBackgroundColor(panel)
        pair.setPadding(18, 14, 18, 14)

        pair.addView(
            text("EUR / USD", 18f, white, true),
            LinearLayout.LayoutParams(0, 60, 1f)
        )

        pair.addView(
            text("1 MIN", 16f, green, true).apply {
                gravity = Gravity.CENTER
            },
            LinearLayout.LayoutParams(100, 60)
        )

        root.addView(pair)

        root.addView(
            text("● LIVE MARKET", 13f, green, true).apply {
                setPadding(0, 16, 0, 8)
            }
        )

        root.addView(
            CandleChart(this),
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                520
            )
        )

        root.addView(
            text("TECHNICAL ANALYSIS", 17f, white, true).apply {
                setPadding(0, 18, 0, 10)
            }
        )

        val indicators = LinearLayout(this)
        indicators.orientation = LinearLayout.VERTICAL
        indicators.setBackgroundColor(panel)
        indicators.setPadding(18, 12, 18, 12)

        indicators.addView(
            text("EMA 20 / 50       TREND: BULLISH", 14f, green)
        )
        indicators.addView(
            text("RSI                 61.4", 14f, white)
        )
        indicators.addView(
            text("MACD               POSITIVE", 14f, green)
        )
        indicators.addView(
            text("BOLLINGER          MID-UPPER", 14f, white)
        )
        indicators.addView(
            text("SUPPORT            1.08420", 14f, gray)
        )
        indicators.addView(
            text("RESISTANCE         1.08510", 14f, gray)
        )

        root.addView(indicators)

        root.addView(
            text("NEXT CANDLE SIGNAL", 17f, white, true).apply {
                setPadding(0, 18, 0, 8)
            }
        )

        val signal = TextView(this)
        signal.text = "UP  •  78% CONFIDENCE"
        signal.textSize = 25f
        signal.setTextColor(green)
        signal.gravity = Gravity.CENTER
        signal.setBackgroundColor(panel)
        signal.setPadding(10, 28, 10, 28)

        root.addView(signal)

        root.addView(
            text(
                "Analyzing trend, momentum, volatility and price structure...",
                12f,
                gray
            ).apply {
                setPadding(0, 10, 0, 0)
            }
        )

        setContentView(root)
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

        private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)

            canvas.drawColor(Color.rgb(9, 13, 19))

            paint.strokeWidth = 1f
            paint.color = Color.rgb(35, 45, 58)

            for (i in 1..5) {
                val y = height * i / 6f
                canvas.drawLine(
                    0f,
                    y,
                    width.toFloat(),
                    y,
                    paint
                )
            }

            for (i in 1..5) {
                val x = width * i / 6f
                canvas.drawLine(
                    x,
                    0f,
                    x,
                    height.toFloat(),
                    paint
                )
            }

            val candles = listOf(
                0.68f, 0.64f, 0.70f, 0.61f, 0.58f,
                0.63f, 0.55f, 0.51f, 0.57f, 0.48f,
                0.44f, 0.49f, 0.41f, 0.38f, 0.43f,
                0.35f, 0.31f, 0.36f, 0.28f, 0.24f
            )

            val candleWidth = width / 27f

            for (i in candles.indices) {

                val x = candleWidth * (i * 1.3f + 2f)

                val close = candles[i]
                val open = if (i == 0) 0.73f else candles[i - 1]

                val high = minOf(open, close) - 0.035f
                val low = maxOf(open, close) + 0.035f

                paint.color =
                    if (close > open) green else red

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

            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 4f
            paint.color = Color.rgb(80, 160, 255)

            val path = Path()
            path.moveTo(0f, height * 0.67f)

            for (i in 1..width step 25) {
                val y =
                    height * (0.67f - i.toFloat() / width * 0.38f)

                path.lineTo(i.toFloat(), y)
            }

            canvas.drawPath(path, paint)

            paint.style = Paint.Style.FILL
        }
    }
}
