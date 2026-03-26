package com.connecto.app.engine

/**
 * Complete anatomical body map for both front and back views.
 * Coordinates are normalized (0.0 - 1.0) relative to the body image dimensions.
 * X=0 is left edge, X=1 is right edge, Y=0 is top, Y=1 is bottom.
 *
 * NOTE: "Left/Right" here refers to the PATIENT's left/right.
 */
object BodyMap {

    // ─── ANTERIOR (FRONT) REGIONS ────────────────────────────────────────────

    val anteriorRegions: List<BodyRegion> = listOf(

        // HEAD
        BodyRegion(
            id = "head_center", commonName = "Head", medicalName = "Cranium",
            side = Side.CENTER, orientation = Orientation.ANTERIOR, layer = BodyLayer.BONE,
            polygon = listOf(0.35f to 0.00f, 0.65f to 0.00f, 0.70f to 0.09f, 0.65f to 0.13f, 0.35f to 0.13f, 0.30f to 0.09f)
        ),

        // FACE
        BodyRegion(
            id = "face_center", commonName = "Face", medicalName = "Facial Region",
            side = Side.CENTER, orientation = Orientation.ANTERIOR, layer = BodyLayer.SKIN,
            polygon = listOf(0.37f to 0.04f, 0.63f to 0.04f, 0.65f to 0.13f, 0.35f to 0.13f)
        ),

        // NECK
        BodyRegion(
            id = "neck_center", commonName = "Neck", medicalName = "Cervical Region",
            side = Side.CENTER, orientation = Orientation.ANTERIOR, layer = BodyLayer.SKIN,
            polygon = listOf(0.40f to 0.13f, 0.60f to 0.13f, 0.58f to 0.19f, 0.42f to 0.19f)
        ),

        // LEFT SHOULDER (patient's left = image's right)
        BodyRegion(
            id = "left_shoulder", commonName = "Left Shoulder", medicalName = "Left Deltoid Region",
            side = Side.LEFT, orientation = Orientation.ANTERIOR, layer = BodyLayer.MUSCLE,
            polygon = listOf(0.58f to 0.19f, 0.72f to 0.22f, 0.74f to 0.30f, 0.62f to 0.28f)
        ),

        // RIGHT SHOULDER
        BodyRegion(
            id = "right_shoulder", commonName = "Right Shoulder", medicalName = "Right Deltoid Region",
            side = Side.RIGHT, orientation = Orientation.ANTERIOR, layer = BodyLayer.MUSCLE,
            polygon = listOf(0.42f to 0.19f, 0.28f to 0.22f, 0.26f to 0.30f, 0.38f to 0.28f)
        ),

        // CHEST
        BodyRegion(
            id = "chest_center", commonName = "Chest", medicalName = "Thoracic Region",
            side = Side.CENTER, orientation = Orientation.ANTERIOR, layer = BodyLayer.BONE,
            polygon = listOf(0.38f to 0.19f, 0.62f to 0.19f, 0.64f to 0.38f, 0.36f to 0.38f)
        ),

        // LEFT UPPER ARM
        BodyRegion(
            id = "left_upper_arm", commonName = "Left Upper Arm", medicalName = "Left Brachial Region",
            side = Side.LEFT, orientation = Orientation.ANTERIOR, layer = BodyLayer.MUSCLE,
            polygon = listOf(0.62f to 0.28f, 0.74f to 0.30f, 0.76f to 0.46f, 0.64f to 0.44f)
        ),

        // RIGHT UPPER ARM
        BodyRegion(
            id = "right_upper_arm", commonName = "Right Upper Arm", medicalName = "Right Brachial Region",
            side = Side.RIGHT, orientation = Orientation.ANTERIOR, layer = BodyLayer.MUSCLE,
            polygon = listOf(0.38f to 0.28f, 0.26f to 0.30f, 0.24f to 0.46f, 0.36f to 0.44f)
        ),

        // ABDOMEN
        BodyRegion(
            id = "abdomen_center", commonName = "Abdomen", medicalName = "Abdominal Region",
            side = Side.CENTER, orientation = Orientation.ANTERIOR, layer = BodyLayer.SKIN,
            polygon = listOf(0.36f to 0.38f, 0.64f to 0.38f, 0.62f to 0.55f, 0.38f to 0.55f)
        ),

        // LEFT FOREARM
        BodyRegion(
            id = "left_forearm", commonName = "Left Forearm", medicalName = "Left Antebrachial Region",
            side = Side.LEFT, orientation = Orientation.ANTERIOR, layer = BodyLayer.MUSCLE,
            polygon = listOf(0.64f to 0.44f, 0.76f to 0.46f, 0.78f to 0.61f, 0.66f to 0.59f)
        ),

        // RIGHT FOREARM
        BodyRegion(
            id = "right_forearm", commonName = "Right Forearm", medicalName = "Right Antebrachial Region",
            side = Side.RIGHT, orientation = Orientation.ANTERIOR, layer = BodyLayer.MUSCLE,
            polygon = listOf(0.36f to 0.44f, 0.24f to 0.46f, 0.22f to 0.61f, 0.34f to 0.59f)
        ),

        // LEFT HAND
        BodyRegion(
            id = "left_hand", commonName = "Left Hand", medicalName = "Left Manus",
            side = Side.LEFT, orientation = Orientation.ANTERIOR, layer = BodyLayer.BONE,
            polygon = listOf(0.66f to 0.59f, 0.78f to 0.61f, 0.80f to 0.69f, 0.68f to 0.67f)
        ),

        // RIGHT HAND
        BodyRegion(
            id = "right_hand", commonName = "Right Hand", medicalName = "Right Manus",
            side = Side.RIGHT, orientation = Orientation.ANTERIOR, layer = BodyLayer.BONE,
            polygon = listOf(0.34f to 0.59f, 0.22f to 0.61f, 0.20f to 0.69f, 0.32f to 0.67f)
        ),

        // PELVIS / GROIN
        BodyRegion(
            id = "pelvis_center", commonName = "Pelvis", medicalName = "Pelvic Region",
            side = Side.CENTER, orientation = Orientation.ANTERIOR, layer = BodyLayer.BONE,
            polygon = listOf(0.38f to 0.55f, 0.62f to 0.55f, 0.60f to 0.62f, 0.40f to 0.62f)
        ),

        // LEFT THIGH
        BodyRegion(
            id = "left_thigh", commonName = "Left Thigh", medicalName = "Left Femoral Region",
            side = Side.LEFT, orientation = Orientation.ANTERIOR, layer = BodyLayer.MUSCLE,
            polygon = listOf(0.52f to 0.62f, 0.62f to 0.62f, 0.61f to 0.77f, 0.51f to 0.77f)
        ),

        // RIGHT THIGH
        BodyRegion(
            id = "right_thigh", commonName = "Right Thigh", medicalName = "Right Femoral Region",
            side = Side.RIGHT, orientation = Orientation.ANTERIOR, layer = BodyLayer.MUSCLE,
            polygon = listOf(0.38f to 0.62f, 0.48f to 0.62f, 0.49f to 0.77f, 0.39f to 0.77f)
        ),

        // LEFT KNEE
        BodyRegion(
            id = "left_knee", commonName = "Left Knee", medicalName = "Left Patellar Region",
            side = Side.LEFT, orientation = Orientation.ANTERIOR, layer = BodyLayer.BONE,
            polygon = listOf(0.51f to 0.77f, 0.61f to 0.77f, 0.61f to 0.82f, 0.51f to 0.82f)
        ),

        // RIGHT KNEE
        BodyRegion(
            id = "right_knee", commonName = "Right Knee", medicalName = "Right Patellar Region",
            side = Side.RIGHT, orientation = Orientation.ANTERIOR, layer = BodyLayer.BONE,
            polygon = listOf(0.39f to 0.77f, 0.49f to 0.77f, 0.49f to 0.82f, 0.39f to 0.82f)
        ),

        // LEFT LOWER LEG
        BodyRegion(
            id = "left_lower_leg", commonName = "Left Lower Leg", medicalName = "Left Anterior Tibial Region",
            side = Side.LEFT, orientation = Orientation.ANTERIOR, layer = BodyLayer.BONE,
            polygon = listOf(0.51f to 0.82f, 0.61f to 0.82f, 0.60f to 0.95f, 0.52f to 0.95f)
        ),

        // RIGHT LOWER LEG
        BodyRegion(
            id = "right_lower_leg", commonName = "Right Lower Leg", medicalName = "Right Anterior Tibial Region",
            side = Side.RIGHT, orientation = Orientation.ANTERIOR, layer = BodyLayer.BONE,
            polygon = listOf(0.39f to 0.82f, 0.49f to 0.82f, 0.48f to 0.95f, 0.40f to 0.95f)
        ),

        // LEFT FOOT
        BodyRegion(
            id = "left_foot", commonName = "Left Foot", medicalName = "Left Pedal Region",
            side = Side.LEFT, orientation = Orientation.ANTERIOR, layer = BodyLayer.BONE,
            polygon = listOf(0.51f to 0.95f, 0.63f to 0.95f, 0.64f to 1.00f, 0.50f to 1.00f)
        ),

        // RIGHT FOOT
        BodyRegion(
            id = "right_foot", commonName = "Right Foot", medicalName = "Right Pedal Region",
            side = Side.RIGHT, orientation = Orientation.ANTERIOR, layer = BodyLayer.BONE,
            polygon = listOf(0.36f to 0.95f, 0.49f to 0.95f, 0.50f to 1.00f, 0.35f to 1.00f)
        )
    )

    // ─── POSTERIOR (BACK) REGIONS ────────────────────────────────────────────

    val posteriorRegions: List<BodyRegion> = listOf(

        BodyRegion(
            id = "back_head", commonName = "Back of Head", medicalName = "Occipital Region",
            side = Side.CENTER, orientation = Orientation.POSTERIOR, layer = BodyLayer.BONE,
            polygon = listOf(0.35f to 0.00f, 0.65f to 0.00f, 0.68f to 0.10f, 0.32f to 0.10f)
        ),

        BodyRegion(
            id = "back_neck", commonName = "Back of Neck", medicalName = "Posterior Cervical Region",
            side = Side.CENTER, orientation = Orientation.POSTERIOR, layer = BodyLayer.MUSCLE,
            polygon = listOf(0.40f to 0.10f, 0.60f to 0.10f, 0.58f to 0.19f, 0.42f to 0.19f)
        ),

        BodyRegion(
            id = "upper_back_left", commonName = "Left Upper Back", medicalName = "Left Scapular Region",
            side = Side.LEFT, orientation = Orientation.POSTERIOR, layer = BodyLayer.MUSCLE,
            polygon = listOf(0.50f to 0.19f, 0.65f to 0.19f, 0.66f to 0.38f, 0.50f to 0.38f)
        ),

        BodyRegion(
            id = "upper_back_right", commonName = "Right Upper Back", medicalName = "Right Scapular Region",
            side = Side.RIGHT, orientation = Orientation.POSTERIOR, layer = BodyLayer.MUSCLE,
            polygon = listOf(0.35f to 0.19f, 0.50f to 0.19f, 0.50f to 0.38f, 0.34f to 0.38f)
        ),

        BodyRegion(
            id = "lower_back_center", commonName = "Lower Back", medicalName = "Lumbar Region",
            side = Side.CENTER, orientation = Orientation.POSTERIOR, layer = BodyLayer.MUSCLE,
            polygon = listOf(0.38f to 0.38f, 0.62f to 0.38f, 0.62f to 0.55f, 0.38f to 0.55f)
        ),

        BodyRegion(
            id = "left_glute", commonName = "Left Buttock", medicalName = "Left Gluteal Region",
            side = Side.LEFT, orientation = Orientation.POSTERIOR, layer = BodyLayer.MUSCLE,
            polygon = listOf(0.50f to 0.55f, 0.62f to 0.55f, 0.62f to 0.65f, 0.50f to 0.65f)
        ),

        BodyRegion(
            id = "right_glute", commonName = "Right Buttock", medicalName = "Right Gluteal Region",
            side = Side.RIGHT, orientation = Orientation.POSTERIOR, layer = BodyLayer.MUSCLE,
            polygon = listOf(0.38f to 0.55f, 0.50f to 0.55f, 0.50f to 0.65f, 0.38f to 0.65f)
        ),

        BodyRegion(
            id = "left_hamstring", commonName = "Left Hamstring", medicalName = "Left Posterior Femoral Region",
            side = Side.LEFT, orientation = Orientation.POSTERIOR, layer = BodyLayer.MUSCLE,
            polygon = listOf(0.51f to 0.65f, 0.62f to 0.65f, 0.61f to 0.80f, 0.51f to 0.80f)
        ),

        BodyRegion(
            id = "right_hamstring", commonName = "Right Hamstring", medicalName = "Right Posterior Femoral Region",
            side = Side.RIGHT, orientation = Orientation.POSTERIOR, layer = BodyLayer.MUSCLE,
            polygon = listOf(0.38f to 0.65f, 0.49f to 0.65f, 0.49f to 0.80f, 0.39f to 0.80f)
        ),

        BodyRegion(
            id = "left_calf", commonName = "Left Calf", medicalName = "Left Sural Region",
            side = Side.LEFT, orientation = Orientation.POSTERIOR, layer = BodyLayer.MUSCLE,
            polygon = listOf(0.51f to 0.82f, 0.61f to 0.82f, 0.60f to 0.95f, 0.52f to 0.95f)
        ),

        BodyRegion(
            id = "right_calf", commonName = "Right Calf", medicalName = "Right Sural Region",
            side = Side.RIGHT, orientation = Orientation.POSTERIOR, layer = BodyLayer.MUSCLE,
            polygon = listOf(0.39f to 0.82f, 0.49f to 0.82f, 0.48f to 0.95f, 0.40f to 0.95f)
        )
    )

    fun getRegions(orientation: Orientation): List<BodyRegion> =
        if (orientation == Orientation.ANTERIOR) anteriorRegions else posteriorRegions
}
