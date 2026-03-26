package com.connecto.app.ui

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import com.connecto.app.engine.*

/**
 * Advanced Interactive Body Map with HUD Features.
 * Supports Tap, Paint, Multi-Select and multi-layer anatomical views.
 */
class BodyMapView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    enum class InteractionMode { TAP, PAINT, MULTI_SELECT }
    enum class AnatomicalLayer { SKIN, MUSCLES, ORGANS, SKELETAL }

    var interactionMode: InteractionMode = InteractionMode.TAP
    var currentOrientation: Orientation = Orientation.ANTERIOR
        set(value) { field = value; invalidate() }
    var currentLayer: AnatomicalLayer = AnatomicalLayer.SKIN
        set(value) { field = value; invalidate() }

    private val selectedRegions = mutableListOf<DetectionResult>()
    private val paintPoints = mutableListOf<PointF>()
    private val paintPath = Path()

    var onRegionSelected: ((List<DetectionResult>) -> Unit)? = null

    // ─── Paints ───────────────────────────────────────────────────────────────
    private val bodyStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 2f
    }

    private val regionFillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private val selectedFillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.argb(160, 0, 255, 204)
        style = Paint.Style.FILL
    }

    private val scanLinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        strokeWidth = 4f
        setShadowLayer(15f, 0f, 0f, Color.RED)
    }

    private val paintTrailPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.argb(120, 0, 255, 180)
        style = Paint.Style.STROKE
        strokeWidth = 15f
        strokeCap = Paint.Cap.ROUND
    }

    private val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 24f
        typeface = Typeface.MONOSPACE
    }

    // ─── Animations ───────────────────────────────────────────────────────────
    private var scanLineY = 0f
    private val scanAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
        duration = 4000
        repeatCount = ValueAnimator.INFINITE
        repeatMode = ValueAnimator.REVERSE
        interpolator = LinearInterpolator()
        addUpdateListener {
            scanLineY = it.animatedValue as Float
            invalidate()
        }
    }

    private var pulseRadius = 10f
    private var pulseGrowing = true

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null) // for shadow layer
        scanAnimator.start()
    }

    // ─── Drawing ──────────────────────────────────────────────────────────────
    override fun onDraw(canvas: Canvas) {
        val w = width.toFloat()
        val h = height.toFloat()

        drawBody(canvas, w, h)
        drawSelections(canvas, w, h)
        
        if (paintPoints.isNotEmpty()) {
            canvas.drawPath(paintPath, paintTrailPaint)
        }

        drawScanningLine(canvas, w, h)
        animatePulse()
    }

    private fun drawBody(canvas: Canvas, w: Float, h: Float) {
        val regions = BodyMap.getRegions(currentOrientation)
        
        val (strokeColor, fillAlpha) = when(currentLayer) {
            AnatomicalLayer.SKIN -> "#00FFFF" to 30
            AnatomicalLayer.MUSCLES -> "#FF3333" to 50
            AnatomicalLayer.ORGANS -> "#33FF33" to 60
            AnatomicalLayer.SKELETAL -> "#FFFFFF" to 40
        }
        
        bodyStrokePaint.color = Color.parseColor(strokeColor)
        regionFillPaint.color = Color.parseColor(strokeColor)
        regionFillPaint.alpha = fillAlpha

        regions.forEach { region ->
            val path = regionToPath(region.polygon, w, h)
            canvas.drawPath(path, regionFillPaint)
            canvas.drawPath(path, bodyStrokePaint)
            
            if (currentLayer != AnatomicalLayer.SKIN) {
                drawInternalDetail(canvas, region, w, h)
            }
        }
    }

    private fun drawInternalDetail(canvas: Canvas, region: BodyRegion, w: Float, h: Float) {
        val bounds = region.getBounds()
        val cx = bounds.centerX() * w
        val cy = bounds.centerY() * h
        bodyStrokePaint.strokeWidth = 1f
        canvas.drawCircle(cx, cy, 8f, bodyStrokePaint)
        bodyStrokePaint.strokeWidth = 2f
    }

    private fun drawSelections(canvas: Canvas, w: Float, h: Float) {
        selectedRegions.forEach { result ->
            val path = regionToPath(result.region.polygon, w, h)
            canvas.drawPath(path, selectedFillPaint)
            
            // Draw marker & label
            val center = getCentroid(result.region.polygon)
            val cx = center.first * w
            val cy = center.second * h
            canvas.drawCircle(cx, cy, pulseRadius, bodyStrokePaint)
            canvas.drawText("NODE_${result.region.id}", cx + 15f, cy - 15f, labelPaint)
        }
    }

    private fun drawScanningLine(canvas: Canvas, w: Float, h: Float) {
        val y = scanLineY * h
        canvas.drawLine(0f, y, w, y, scanLinePaint)
        // Add a subtle glow area
        val glowPaint = Paint(scanLinePaint).apply { alpha = 40; strokeWidth = 20f }
        canvas.drawLine(0f, y, w, y, glowPaint)
    }

    private fun animatePulse() {
        if (selectedRegions.isNotEmpty()) {
            if (pulseGrowing) {
                pulseRadius += 0.4f
                if (pulseRadius > 14f) pulseGrowing = false
            } else {
                pulseRadius -= 0.4f
                if (pulseRadius < 8f) pulseGrowing = true
            }
            invalidate()
        }
    }

    // ─── Touch Handling ───────────────────────────────────────────────────────
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val nx = event.x / width
        val ny = event.y / height

        when (interactionMode) {
            InteractionMode.TAP -> handleTap(nx, ny, event)
            InteractionMode.PAINT -> handlePaint(nx, ny, event)
            InteractionMode.MULTI_SELECT -> handleTap(nx, ny, event)
        }
        return true
    }

    private fun handleTap(nx: Float, ny: Float, event: MotionEvent) {
        if (event.action == MotionEvent.ACTION_UP) {
            val result = AnatomicalEngine.detectRegion(nx, ny, currentOrientation) ?: return
            if (interactionMode == InteractionMode.MULTI_SELECT) {
                val existing = selectedRegions.find { it.region.id == result.region.id }
                if (existing != null) selectedRegions.remove(existing)
                else selectedRegions.add(result)
            } else {
                selectedRegions.clear()
                selectedRegions.add(result)
            }
            invalidate()
            onRegionSelected?.invoke(selectedRegions.toList())
        }
    }

    private fun handlePaint(nx: Float, ny: Float, event: MotionEvent) {
        val px = event.x; val py = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> { paintPath.reset(); paintPath.moveTo(px, py); paintPoints.clear(); paintPoints.add(PointF(nx, ny)) }
            MotionEvent.ACTION_MOVE -> { paintPath.lineTo(px, py); paintPoints.add(PointF(nx, ny)); invalidate() }
            MotionEvent.ACTION_UP -> {
                val results = AnatomicalEngine.detectFromPaint(paintPoints, currentOrientation)
                selectedRegions.addAll(results.filter { r -> selectedRegions.none { it.region.id == r.region.id } })
                paintPath.reset(); paintPoints.clear()
                invalidate()
                onRegionSelected?.invoke(selectedRegions.toList())
            }
        }
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────
    private fun regionToPath(poly: List<Pair<Float, Float>>, w: Float, h: Float): Path {
        val path = Path()
        poly.forEachIndexed { i, p ->
            if (i == 0) path.moveTo(p.first * w, p.second * h)
            else path.lineTo(p.first * w, p.second * h)
        }
        path.close()
        return path
    }

    private fun getCentroid(poly: List<Pair<Float, Float>>): Pair<Float, Float> {
        return poly.map { it.first }.average().toFloat() to poly.map { it.second }.average().toFloat()
    }

    fun clearSelections() { selectedRegions.clear(); invalidate(); onRegionSelected?.invoke(emptyList()) }
    fun undoLastSelection() { if (selectedRegions.isNotEmpty()) { selectedRegions.removeAt(selectedRegions.size - 1); invalidate(); onRegionSelected?.invoke(selectedRegions.toList()) } }
    fun getSelections(): List<DetectionResult> = selectedRegions.toList()
}
