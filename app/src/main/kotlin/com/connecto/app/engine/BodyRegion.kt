package com.connecto.app.engine

/**
 * Represents a single anatomical region on the body.
 */
data class BodyRegion(
    val id: String,
    val commonName: String,
    val medicalName: String,
    val side: Side,
    val orientation: Orientation,
    val layer: BodyLayer,
    // Normalized coordinates (0.0 to 1.0) defining the polygon boundary of this region
    val polygon: List<Pair<Float, Float>>
)

enum class Side {
    LEFT, RIGHT, CENTER, BILATERAL
}

enum class Orientation {
    ANTERIOR, // Front
    POSTERIOR // Back
}

enum class BodyLayer {
    SKIN, MUSCLE, BONE
}

/**
 * Result of a region detection query.
 */
data class DetectionResult(
    val region: BodyRegion,
    val normalizedX: Float,
    val normalizedY: Float
)
