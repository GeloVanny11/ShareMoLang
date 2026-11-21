package com.hop.share

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.SweepGradient
import android.graphics.Typeface
import android.view.MotionEvent
import android.view.View
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.min
import kotlin.math.sin

/** Minimal radar of nearby phones. Tap a phone to send to it. */
class RadarView(ctx: Context) : View(ctx) {
    var peers: List<MainActivity.Peer> = emptyList()
        set(value) { field = value; invalidate() }
    var onTap: (MainActivity.Peer) -> Unit = {}

    private val d = resources.displayMetrics.density
    private val accent = Color.parseColor("#2B5CFF")
    private val ink = Color.parseColor("#111827")
    private val ring = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE; strokeWidth = 1.2f * d; color = Color.parseColor("#E5E7EB")
    }
    private val sweep = Paint(Paint.ANTI_ALIAS_FLAG)
    private val fill = Paint(Paint.ANTI_ALIAS_FLAG)
    private val stroke = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.STROKE; strokeWidth = 2f * d; color = accent }
    private val txt = Paint(Paint.ANTI_ALIAS_FLAG).apply { textAlign = Paint.Align.CENTER }
    private var angle = 0f
    private val spots = ArrayList<Triple<MainActivity.Peer, Float, Float>>()

    override fun onSizeChanged(w: Int, h: Int, ow: Int, oh: Int) {
        sweep.shader = SweepGradient(
            w / 2f, h / 2f,
            intArrayOf(Color.argb(0, 43, 92, 255), Color.argb(70, 43, 92, 255)),
            floatArrayOf(0.8f, 1f)
        )
    }

    override fun onDraw(c: Canvas) {
        val cx = width / 2f
        val cy = height / 2f
        val r = min(cx, cy) - 6 * d
        for (k in 1..3) c.drawCircle(cx, cy, r * k / 3, ring)
        c.save(); c.rotate(angle, cx, cy); c.drawCircle(cx, cy, r, sweep); c.restore()

        fill.color = accent
        c.drawCircle(cx, cy, 24 * d, fill)
        txt.color = Color.WHITE; txt.textSize = 12 * d; txt.typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
        c.drawText("You", cx, cy + 4.5f * d, txt)

        spots.clear()
        val n = peers.size
        peers.forEachIndexed { i, p ->
            val a = i.toDouble() / n * 2 * PI - PI / 2
            val rr = if (n > 3) r * (if (i % 2 == 0) 0.5f else 0.8f) else r * 0.62f
            val x = cx + (rr * cos(a)).toFloat()
            val y = cy + (rr * sin(a)).toFloat()
            fill.color = Color.WHITE
            c.drawCircle(x, y, 24 * d, fill)
            c.drawCircle(x, y, 24 * d, stroke)
            txt.textSize = 20 * d
            c.drawText("\uD83D\uDCF1", x, y + 7 * d, txt)
            txt.textSize = 12 * d; txt.color = ink
            c.drawText(p.name.take(16), x, y + 42 * d, txt)
            spots.add(Triple(p, x, y))
        }
        angle = (angle + 1.4f) % 360f
        postInvalidateOnAnimation()
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        if (e.action == MotionEvent.ACTION_UP) {
            spots.firstOrNull { hypot(it.second - e.x, it.third - e.y) < 40 * d }?.let {
                onTap(it.first); performClick()
            }
        }
        return true
    }

    override fun performClick(): Boolean = super.performClick()
}
