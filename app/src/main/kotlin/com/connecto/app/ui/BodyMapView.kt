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
 * Interactive body map view.
 * Supports Tap Mode, Paint Mode, and Multi-Select Mode.
 */
class BodyMapView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    // ─── Mode ─────────────────────────────────────────────────────────────────
    enum class InteractionMode { TAP, PAINT, MULTI_SELECT }
    enum class AnatomicalLayer { SKIN, MUSCLES, ORGANS, SKELETAL }

    var interactionMode: InteractionMode = InteractionMode.TAP
    var currentOrientation: Orientation = Orientation.ANTERIOR
        set(value) {
            field = value
            invalidate()
        }
    var currentLayer: AnatomicalLayer = AnatomicalLayer.SKIN
        set(value) {
            field = value
            invalidate()
        }

    private val detections = mutableListOf<DetectionResult>()
    private val paintPoints = mutableListOf<PointF>()

    // ─── Listener ─────────────────────────────────────────────────────────────
    var onRegionSelected: ((List<DetectionResult>) -> Unit)? = null

    // ─── Paint Objects ────────────────────────────────────────────────────────
    private val gridPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.argb(30, 0, 220, 255)
        style = Paint.Style.STROKE
        strokeWidth = 0.5f
    }

    private val regionPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.argb(70, 0, 200, 255)
        style = Paint.Style.FILL
    }

    private val regionStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.argb(200, 0, 220, 255)
        style = Paint.Style.STROKE
        strokeWidth = 2f
    }

    private val selectedFillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.argb(140, 0, 255, 220)
        style = Paint.Style.FILL
    }

    private val markerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.argb(255, 0, 255, 220)
        style = Paint.Style.FILL
    }

    private val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 28f
        typeface = Typeface.DEFAULT_BOLD
        setShadowLayer(4f, 0f, 0f, Color.argb(255, 0, 200, 255))
    }

    private val paintTrailPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.argb(100, 0, 255, 180)
        style = Paint.Style.STROKE
        strokeWidth = 20f
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }

    private val paintPath = Path()
    private var pulseRadius = 10f
    private var pulseGrowing = true

    // Paints
    private val bodyPaint = Paint().apply {
        color = Color.parseColor("#1A3A4A")
        style = Paint.Style.STROKE
        strokeWidth = 2f
        isAntiAlias = true
    }

    private val scanLinePaint = Paint().apply {
        color = Color.RED
        strokeWidth = 3f
        setShadowLayer(10f, 0f, 0f, Color.RED)
    }

    private var scanLineY = 0f
    private val scanAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
        duration = 3000
        repeatCount = ValueAnimator.INFINITE
        repeatMode = ValueAnimator.REVERSE
        interpolator = LinearInterpolator()
        addUpdateListener {
            scanLineY = it.animatedValue as Float
            invalidate()
        }
    }

    init {
        scanAnimator.start()
    }

    // ─── Drawing ──────────────────────────────────────────────────────────────

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val w = width.toFloat()
        val h = height.toFloat()

        drawBodySilhouettes(canvas, w, h)
        drawDetections(canvas, w, h)
        drawScanningLine(canvas, w, h)
    }

    private fun drawBodySilhouettes(canvas: Canvas, w: Float, h: Float) {
        val regions = BodyMap.getRegions(currentOrientation)
        
        // Change body color based on layer
        val layerColor = when(currentLayer) {
            AnatomicalLayer.SKIN -> "#1A3A4A"
            AnatomicalLayer.MUSCLES -> "#4A1A1A"
            AnatomicalLayer.ORGANS -> "#1A4A3A"
            AnatomicalLayer.SKELETAL -> "#4A4A4A"
        }
        bodyPaint.color = Color.parseColor(layerColor)

        regions.forEach { region ->
            val path = Path()
            region.polygon.forEachIndexed { i, p ->
                val px = p.first * w
                val py = p.second * h
                if (i == 0) path.moveTo(px, py) else path.lineTo(px, py)
            }
            path.close()
            canvas.drawPath(path, bodyPaint)
            
            // Draw stylized internal "structure" if not skin
            if (currentLayer != AnatomicalLayer.SKIN) {
                drawLayerDetails(canvas, region, w, h)
            }
        }
    }

    private fun drawLayerDetails(canvas: Canvas, region: BodyRegion, w: Float, h: Float) {
        // Simplified procedural detail drawing for internal layers
        bodyPaint.strokeWidth = 1f
        bodyPaint.alpha = 50
        val bounds = region.getBounds()
        val cx = bounds.centerX() * w
        val cy = bounds.centerY() * h
        canvas.drawCircle(cx, cy, 10f, bodyPaint)
        bodyPaint.strokeWidth = 2f
        bodyPaint.alpha = 255
    }

    private fun drawScanningLine(canvas: Canvas, w: Float, h: Float) {
        val y = scanLineY * h
        canvas.drawLine(0f, y, w, y, scanLinePaint)
    }

    private fun drawGrid(canvas: Canvas) {
        val cols = 10
        val rows = 20
        val cellW = width.toFloat() / cols
        val cellH = height.toFloat() / rows
        for (i in 0..cols) canvas.drawLine(i * cellW, 0f, i * cellW, height.toFloat(), gridPaint)
        for (i in 0..rows) canvas.drawLine(0f, i * cellH, width.toFloat(), i * cellH, gridPaint)
    }

    private fun drawRegionOutlines(canvas: Canvas) {
        val regions = BodyMap.getRegions(currentOrientation)
        for (region in regions) {
            val path = regionToPath(region.polygon)
            canvas.drawPath(path, regionPaint)
            canvas.drawPath(path, regionStrokePaint)
        }
    }

    private fun drawDetections(canvas: Canvas, w: Float, h: Float) {
        for (result in detections) {
            val path = regionToPath(result.region.polygon)
            canvas.drawPath(path, selectedFillPaint)
        }
    }

    private fun drawMarkersAndLabels(canvas: Canvas) {
        for (result in selectedRegions) {
            val centroid = getCentroid(result.region.polygon)
            val cx = centroid.first * width
            val cy = centroid.second * height

            // Pulse circle
            val glowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.argb(80, 0, 255, 220)
                style = Paint.Style.FILL
            }
            canvas.drawCircle(cx, cy, pulseRadius + 6f, glowPaint)
            canvas.drawCircle(cx, cy, pulseRadius, markerPaint)

            // Connector line to label
            val labelX = cx + 40f
            val labelY = cy - 20f
            canvas.drawLine(cx, cy, labelX, labelY, markerPaint)

            // Label
            val label = result.region.commonName
            canvas.drawText(label, labelX + 4f, labelY, labelPaint)
        }
    }

    private fun animatePulse() {
        if (selectedRegions.isNotEmpty()) {
            if (pulseGrowing) {
                pulseRadius += 0.5f
                if (pulseRadius > 14f) pulseGrowing = false
            } else {
                pulseRadius -= 0.5f
                if (pulseRadius < 8f) pulseGrowing = true
            }
            postInvalidateDelayed(16)
        } else {
            pulseRadius = 10f
        }
    }

    // ─── Touch Handling ───────────────────────────────────────────────────────

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val nx = event.x / width
        val ny = event.y / height

        when (interactionMode) {
            InteractionMode.TAP -> handleTap(nx, ny, event)
            InteractionMode.PAINT -> handlePaint(nx, ny, event)
            InteractionMode.MULTI_SELECT -> handleTap(nx, ny, event) // same as tap for now
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
        val px = nx * width
        val py = ny * height

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                paintPath.moveTo(px, py)
                paintPoints.clear()
                paintPoints.add(PointF(nx, ny))
            }
            MotionEvent.ACTION_MOVE -> {
                paintPath.lineTo(px, py)
                paintPoints.add(PointF(nx, ny))
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                val results = AnatomicalEngine.detectFromPaint(paintPoints, currentOrientation)
                selectedRegions.clear()
                selectedRegions.addAll(results)
                paintPath.reset()
                paintPoints.clear()
                invalidate()
                onRegionSelected?.invoke(selectedRegions.toList())
            }
        }
    }

    // ─── Public API ───────────────────────────────────────────────────────────

    fun clearSelections() {
        selectedRegions.clear()
        paintPath.reset()
        paintPoints.clear()
        pulseRadius = 10f
        invalidate()
    }

    fun undoLastSelection() {
        if (selectedRegions.isNotEmpty()) {
            selectedRegions.removeAt(selectedRegions.size - 1)
            invalidate()
            onRegionSelected?.invoke(selectedRegions.toList())
        }
    }

    fun getSelections(): List<DetectionResult> = selectedRegions.toList()

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private fun regionToPath(polygon: List<Pair<Float, Float>>): Path {
        val path = Path()
        if (polygon.isEmpty()) return path
        path.moveTo(polygon[0].first * width, polygon[0].second * height)
        for (i in 1 until polygon.size) {
            path.lineTo(polygon[i].first * width, polygon[i].second * height)
        }
        path.close()
        return path
    }

    private fun getCentroid(polygon: List<Pair<Float, Float>>): Pair<Float, Float> {
        val cx = polygon.map { it.first }.average().toFloat()
        val cy = polygon.map { it.second }.average().toFloat()
        return cx to cy
    }
}
