package com.example.game.engine

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import com.example.game.model.BikeSpec
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun DrRidingCanvas(
    modifier: Modifier = Modifier,
    playerX: Float, // -1.0 to 1.0 on road
    steeringAngleDegrees: Float, // -60 to +60
    speedKmh: Float,
    roadScrollOffset: Float,
    currentIntersectionZ: Float, // Distance ahead in meters
    trafficLight: TrafficLightState,
    cityVehicles: List<CityVehicle>,
    bikeSpec: BikeSpec,
    bikeColorHex: Long,
    cameraMode: CameraViewMode,
    isBlinkingLeft: Boolean,
    isBlinkingRight: Boolean,
    isBraking: Boolean,
    targetParkingZ: Float? = null // Parking box distance
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        val horizonY = height * 0.42f
        val horizonX = (width / 2f) - (steeringAngleDegrees * 0.4f)

        // 1. SKY & CITY SKYLINE
        drawDrDrivingSkyline(
            width = width,
            height = height,
            horizonX = horizonX,
            horizonY = horizonY
        )

        // 2. CITY STREET, SIDEWALKS & CROSSWALKS
        drawCityAvenue(
            width = width,
            height = height,
            horizonX = horizonX,
            horizonY = horizonY,
            roadScrollOffset = roadScrollOffset,
            intersectionZ = currentIntersectionZ,
            trafficLight = trafficLight,
            targetParkingZ = targetParkingZ
        )

        // 3. 3D TRAFFIC VEHICLES (Sorted farthest to closest)
        val sortedVehicles = cityVehicles.sortedByDescending { it.distanceZ }
        for (v in sortedVehicles) {
            drawDrDrivingTraffic(
                vehicle = v,
                width = width,
                height = height,
                horizonX = horizonX,
                horizonY = horizonY
            )
        }

        // 4. PLAYER MOTORCYCLE / COCKPIT
        when (cameraMode) {
            CameraViewMode.FIRST_PERSON_COCKPIT -> {
                drawDrRidingCockpit(
                    width = width,
                    height = height,
                    steeringAngle = steeringAngleDegrees,
                    speedKmh = speedKmh,
                    bikeColor = Color(bikeColorHex),
                    isBlinkingLeft = isBlinkingLeft,
                    isBlinkingRight = isBlinkingRight,
                    isBraking = isBraking
                )
            }
            CameraViewMode.THIRD_PERSON_CHASE -> {
                drawDrRidingThirdPerson(
                    width = width,
                    height = height,
                    playerX = playerX,
                    steeringAngle = steeringAngleDegrees,
                    speedKmh = speedKmh,
                    bikeColor = Color(bikeColorHex),
                    isBlinkingLeft = isBlinkingLeft,
                    isBlinkingRight = isBlinkingRight,
                    isBraking = isBraking
                )
            }
        }

        // 5. REARVIEW MIRROR (TOP CENTER)
        drawRearviewMirror(
            width = width,
            height = height,
            cityVehicles = cityVehicles,
            speedKmh = speedKmh
        )
    }
}

// -------------------------------------------------------------
// DRAWING ROUTINES: DR. DRIVING AESTHETIC
// -------------------------------------------------------------

private fun DrawScope.drawDrDrivingSkyline(
    width: Float,
    height: Float,
    horizonX: Float,
    horizonY: Float
) {
    // Bright clear blue daytime sky gradient
    drawRect(
        brush = Brush.verticalGradient(
            colors = listOf(Color(0xFF5BA4E6), Color(0xFFC7E2FA)),
            startY = 0f,
            endY = horizonY
        ),
        size = Size(width, horizonY)
    )

    // Soft white clouds
    val cloudColor = Color(0xF0FFFFFF)
    drawCircle(cloudColor, radius = 28f, center = Offset(width * 0.22f, horizonY * 0.35f))
    drawCircle(cloudColor, radius = 38f, center = Offset(width * 0.30f, horizonY * 0.30f))
    drawCircle(cloudColor, radius = 26f, center = Offset(width * 0.38f, horizonY * 0.36f))

    drawCircle(cloudColor, radius = 32f, center = Offset(width * 0.75f, horizonY * 0.40f))
    drawCircle(cloudColor, radius = 42f, center = Offset(width * 0.84f, horizonY * 0.34f))

    // Modern clean pastel skyscrapers along the avenue (Dr. Driving signature look)
    val buildingCount = 10
    val bWidth = width / buildingCount
    for (i in 0..buildingCount) {
        val bx = i * bWidth
        val bHeight = when (i % 4) {
            0 -> horizonY * 0.45f
            1 -> horizonY * 0.70f
            2 -> horizonY * 0.35f
            else -> horizonY * 0.58f
        }
        val buildingColor = when (i % 3) {
            0 -> Color(0xFFD6E2E9)
            1 -> Color(0xFFC3D4DF)
            else -> Color(0xFFB5C9D7)
        }
        drawRect(
            color = buildingColor,
            topLeft = Offset(bx, bHeight),
            size = Size(bWidth + 1f, horizonY - bHeight)
        )

        // Building windows (neat grid)
        val windowCols = 3
        val windowRows = 5
        val wW = (bWidth / (windowCols + 1)) * 0.6f
        val wH = ((horizonY - bHeight) / (windowRows + 1)) * 0.5f
        for (r in 1..windowRows) {
            for (c in 1..windowCols) {
                val wx = bx + c * (bWidth / (windowCols + 1))
                val wy = bHeight + r * ((horizonY - bHeight) / (windowRows + 1))
                drawRect(
                    color = Color(0x66FFFFFF),
                    topLeft = Offset(wx, wy),
                    size = Size(wW, wH)
                )
            }
        }
    }
}

private fun DrawScope.drawCityAvenue(
    width: Float,
    height: Float,
    horizonX: Float,
    horizonY: Float,
    roadScrollOffset: Float,
    intersectionZ: Float,
    trafficLight: TrafficLightState,
    targetParkingZ: Float?
) {
    val bottomRoadWidth = width * 1.45f
    val topRoadWidth = width * 0.14f

    val roadBottomLeft = (width / 2f) - (bottomRoadWidth / 2f)
    val roadBottomRight = (width / 2f) + (bottomRoadWidth / 2f)

    val roadTopLeft = horizonX - (topRoadWidth / 2f)
    val roadTopRight = horizonX + (topRoadWidth / 2f)

    // Sidewalks / Trees grass border
    drawRect(
        color = Color(0xFF88C057), // Fresh green grass border
        topLeft = Offset(0f, horizonY),
        size = Size(width, height - horizonY)
    )

    // Sidewalk slabs
    val sidewalkExtraW = 55f
    val leftSidewalk = Path().apply {
        moveTo(roadTopLeft - 10f, horizonY)
        lineTo(roadTopLeft, horizonY)
        lineTo(roadBottomLeft, height)
        lineTo(roadBottomLeft - sidewalkExtraW, height)
        close()
    }
    drawPath(leftSidewalk, Color(0xFFD4D4D8))

    val rightSidewalk = Path().apply {
        moveTo(roadTopRight, horizonY)
        lineTo(roadTopRight + 10f, horizonY)
        lineTo(roadBottomRight + sidewalkExtraW, height)
        lineTo(roadBottomRight, height)
        close()
    }
    drawPath(rightSidewalk, Color(0xFFD4D4D8))

    // Main Asphalt Avenue
    val avenuePath = Path().apply {
        moveTo(roadTopLeft, horizonY)
        lineTo(roadTopRight, horizonY)
        lineTo(roadBottomRight, height)
        lineTo(roadBottomLeft, height)
        close()
    }
    drawPath(avenuePath, Color(0xFF374151)) // Smooth dark asphalt

    // Center DOUBLE SOLID YELLOW LINE (Separating opposing traffic)
    val segments = 22
    for (i in 0 until segments) {
        val t1 = (i.toFloat() / segments)
        val t2 = ((i + 1).toFloat() / segments)

        val y1 = horizonY + (height - horizonY) * (t1 * t1)
        val y2 = horizonY + (height - horizonY) * (t2 * t2)

        val cX1 = horizonX + ((width / 2f) - horizonX) * (t1 * t1)
        val cX2 = horizonX + ((width / 2f) - horizonX) * (t2 * t2)

        val yellowW = 2.5f + 8f * (t1 * t1)

        // Double yellow stripes
        drawLine(
            color = Color(0xFFFFD600),
            start = Offset(cX1 - yellowW * 0.7f, y1),
            end = Offset(cX2 - yellowW * 0.7f, y2),
            strokeWidth = yellowW * 0.45f
        )
        drawLine(
            color = Color(0xFFFFD600),
            start = Offset(cX1 + yellowW * 0.7f, y1),
            end = Offset(cX2 + yellowW * 0.7f, y2),
            strokeWidth = yellowW * 0.45f
        )
    }

    // Lane Dashed Dividers (White) on player's side
    val laneRatio = 0.50f
    for (i in 0 until segments step 2) {
        val t1 = (i.toFloat() / segments)
        val t2 = ((i + 1).toFloat() / segments)

        val scrollShift = (roadScrollOffset * 0.06f) % 2f
        val shiftedT1 = (t1 + scrollShift * 0.04f).coerceIn(0f, 1f)
        val shiftedT2 = (t2 + scrollShift * 0.04f).coerceIn(0f, 1f)

        val y1 = horizonY + (height - horizonY) * (shiftedT1 * shiftedT1)
        val y2 = horizonY + (height - horizonY) * (shiftedT2 * shiftedT2)

        val currentRoadW = topRoadWidth + (bottomRoadWidth - topRoadWidth) * (shiftedT1 * shiftedT1)
        val currentCenterX = horizonX + ((width / 2f) - horizonX) * (shiftedT1 * shiftedT1)

        val lineX = currentCenterX + (laneRatio * currentRoadW * 0.28f)
        val lineWidth = 2.5f + 10f * (shiftedT1 * shiftedT1)

        drawLine(
            color = Color.White.copy(alpha = 0.9f),
            start = Offset(lineX, y1),
            end = Offset(lineX, y2),
            strokeWidth = lineWidth,
            cap = StrokeCap.Round
        )
    }

    // INTERSECTION CROSSWALK & TRAFFIC LIGHT
    if (intersectionZ in 5f..160f) {
        val normZ = (intersectionZ / 160f).coerceIn(0f, 1f)
        val depthScale = (1f - normZ)
        val projY = horizonY + (height * 0.88f - horizonY) * (depthScale * depthScale)

        val interRoadW = (topRoadWidth + (bottomRoadWidth - topRoadWidth) * (depthScale * depthScale))
        val interCenterX = horizonX + ((width / 2f) - horizonX) * (depthScale * depthScale)

        // White Stop Line
        drawLine(
            color = Color.White,
            start = Offset(interCenterX - interRoadW * 0.5f, projY - 10f),
            end = Offset(interCenterX + interRoadW * 0.5f, projY - 10f),
            strokeWidth = 6f * depthScale
        )

        // Zebra Pedestrian Stripes
        val stripes = 12
        val sWidth = interRoadW / stripes
        for (s in 0 until stripes step 2) {
            val sx = interCenterX - (interRoadW * 0.5f) + s * sWidth
            drawRect(
                color = Color.White.copy(alpha = 0.85f),
                topLeft = Offset(sx, projY),
                size = Size(sWidth * 0.8f, 22f * depthScale)
            )
        }

        // Roadside Traffic Light Pole on Right
        val poleX = interCenterX + (interRoadW * 0.54f)
        val poleY = projY - 70f * depthScale
        val boxW = 20f * depthScale
        val boxH = 48f * depthScale

        // Metal pole
        drawLine(
            color = Color(0xFF4B5563),
            start = Offset(poleX + boxW / 2f, projY),
            end = Offset(poleX + boxW / 2f, poleY),
            strokeWidth = 4f * depthScale
        )

        // Signal Box
        drawRoundRect(
            color = Color(0xFF1F2937),
            topLeft = Offset(poleX, poleY),
            size = Size(boxW, boxH),
            cornerRadius = CornerRadius(4f, 4f)
        )

        // Lights: Red, Yellow, Green
        val lightR = boxW * 0.35f
        val redColor = if (trafficLight == TrafficLightState.RED) Color(0xFFFF1744) else Color(0x33FF1744)
        val yellowColor = if (trafficLight == TrafficLightState.YELLOW) Color(0xFFFFD600) else Color(0x33FFD600)
        val greenColor = if (trafficLight == TrafficLightState.GREEN) Color(0xFF00E676) else Color(0x3300E676)

        drawCircle(redColor, radius = lightR, center = Offset(poleX + boxW / 2f, poleY + boxH * 0.20f))
        drawCircle(yellowColor, radius = lightR, center = Offset(poleX + boxW / 2f, poleY + boxH * 0.50f))
        drawCircle(greenColor, radius = lightR, center = Offset(poleX + boxW / 2f, poleY + boxH * 0.80f))
    }

    // TARGET GREEN PARKING BAY (For Parking / Destination missions)
    if (targetParkingZ != null && targetParkingZ in 5f..160f) {
        val normZ = (targetParkingZ / 160f).coerceIn(0f, 1f)
        val depthScale = (1f - normZ)
        val projY = horizonY + (height * 0.86f - horizonY) * (depthScale * depthScale)

        val pRoadW = (topRoadWidth + (bottomRoadWidth - topRoadWidth) * (depthScale * depthScale))
        val pCenterX = horizonX + ((width / 2f) - horizonX) * (depthScale * depthScale)

        val bayW = 60f * depthScale
        val bayH = 75f * depthScale
        val bayX = pCenterX + (pRoadW * 0.25f)

        // Glowing Green Parking Box
        drawRoundRect(
            color = Color(0x4400E676),
            topLeft = Offset(bayX - bayW / 2f, projY - bayH / 2f),
            size = Size(bayW, bayH),
            cornerRadius = CornerRadius(8f, 8f)
        )
        drawRoundRect(
            color = Color(0xFF00E676),
            topLeft = Offset(bayX - bayW / 2f, projY - bayH / 2f),
            size = Size(bayW, bayH),
            cornerRadius = CornerRadius(8f, 8f),
            style = Stroke(width = 3f * depthScale)
        )
    }
}

private fun DrawScope.drawDrDrivingTraffic(
    vehicle: CityVehicle,
    width: Float,
    height: Float,
    horizonX: Float,
    horizonY: Float
) {
    val normZ = (vehicle.distanceZ / 180f).coerceIn(0f, 1f)
    val depthScale = (1f - normZ).coerceIn(0.06f, 1.0f)
    val projScale = depthScale * depthScale

    val screenY = horizonY + (height * 0.84f - horizonY) * projScale

    val bottomRoadWidth = width * 1.45f
    val topRoadWidth = width * 0.14f
    val currentRoadWidth = topRoadWidth + (bottomRoadWidth - topRoadWidth) * projScale
    val currentCenterX = horizonX + ((width / 2f) - horizonX) * projScale

    val screenX = currentCenterX + (vehicle.lane * currentRoadWidth * 0.24f)

    val carW = (if (vehicle.type.isHeavyTruck) 80f else 54f) * projScale * 2.1f
    val carH = (if (vehicle.type.isHeavyTruck) 115f else 46f) * projScale * 2.1f

    // Shadow
    drawOval(
        color = Color.Black.copy(alpha = 0.55f),
        topLeft = Offset(screenX - carW * 0.52f, screenY - carH * 0.1f),
        size = Size(carW * 1.04f, carH * 0.35f)
    )

    // Body
    val bodyColor = Color(vehicle.type.color)
    val bodyPath = Path().apply {
        moveTo(screenX - carW * 0.42f, screenY - carH)
        lineTo(screenX + carW * 0.42f, screenY - carH)
        lineTo(screenX + carW * 0.48f, screenY - carH * 0.15f)
        lineTo(screenX - carW * 0.48f, screenY - carH * 0.15f)
        close()
    }
    drawPath(bodyPath, bodyColor)

    // Windshield
    val glassPath = Path().apply {
        moveTo(screenX - carW * 0.34f, screenY - carH * 0.88f)
        lineTo(screenX + carW * 0.34f, screenY - carH * 0.88f)
        lineTo(screenX + carW * 0.38f, screenY - carH * 0.52f)
        lineTo(screenX - carW * 0.38f, screenY - carH * 0.52f)
        close()
    }
    drawPath(glassPath, Color(0xFF1E293B))

    // Taillights
    val lightW = carW * 0.18f
    val lightH = carH * 0.16f
    val lightY = screenY - carH * 0.44f

    val lightColor = if (vehicle.isBraking) Color(0xFFFF1744) else Color(0xFFCC1111)

    // Left light
    drawRect(color = lightColor, topLeft = Offset(screenX - carW * 0.44f, lightY), size = Size(lightW, lightH))
    // Right light
    drawRect(color = lightColor, topLeft = Offset(screenX + carW * 0.44f - lightW, lightY), size = Size(lightW, lightH))

    // Turn indicator blinkers (Dr. Driving signature!)
    val isBlinkingNow = (System.currentTimeMillis() / 250) % 2 == 0L
    if (vehicle.blinkerLeft && isBlinkingNow) {
        drawCircle(Color(0xFFFF9800), radius = lightW * 0.8f, center = Offset(screenX - carW * 0.44f, lightY + lightH / 2f))
    }
    if (vehicle.blinkerRight && isBlinkingNow) {
        drawCircle(Color(0xFFFF9800), radius = lightW * 0.8f, center = Offset(screenX + carW * 0.44f - lightW / 2f, lightY + lightH / 2f))
    }
}

private fun DrawScope.drawDrRidingCockpit(
    width: Float,
    height: Float,
    steeringAngle: Float,
    speedKmh: Float,
    bikeColor: Color,
    isBlinkingLeft: Boolean,
    isBlinkingRight: Boolean,
    isBraking: Boolean
) {
    val cockpitY = height * 0.70f
    val centerX = width / 2f

    // 1. Sleek Motorcycle Handlebars with Clip-on Grips (Rotating with steering wheel!)
    rotate(degrees = steeringAngle * 0.35f, pivot = Offset(centerX, cockpitY + 30f)) {
        // Main chrome handlebar pipe
        drawLine(
            color = Color(0xFF71717A),
            start = Offset(centerX - width * 0.42f, cockpitY),
            end = Offset(centerX + width * 0.42f, cockpitY),
            strokeWidth = 14f,
            cap = StrokeCap.Round
        )

        // Left & Right Textured Rubber Grips
        drawRoundRect(
            color = Color(0xFF18181B),
            topLeft = Offset(centerX - width * 0.46f, cockpitY - 12f),
            size = Size(width * 0.14f, 26f),
            cornerRadius = CornerRadius(6f, 6f)
        )
        drawRoundRect(
            color = Color(0xFF18181B),
            topLeft = Offset(centerX + width * 0.32f, cockpitY - 12f),
            size = Size(width * 0.14f, 26f),
            cornerRadius = CornerRadius(6f, 6f)
        )

        // Front Brake Lever (Aluminum)
        drawLine(
            color = if (isBraking) Color(0xFFFF1744) else Color(0xFFA1A1AA),
            start = Offset(centerX + width * 0.30f, cockpitY - 6f),
            end = Offset(centerX + width * 0.44f, cockpitY - 16f),
            strokeWidth = 6f,
            cap = StrokeCap.Round
        )

        // Center Triple Tree Clamp with Bike Color Accent
        drawRoundRect(
            color = Color(0xFF27272A),
            topLeft = Offset(centerX - 45f, cockpitY - 16f),
            size = Size(90f, 32f),
            cornerRadius = CornerRadius(8f, 8f)
        )
        drawCircle(
            color = bikeColor,
            radius = 12f,
            center = Offset(centerX, cockpitY)
        )
    }

    // 2. Dr. Driving Instrument Cluster / Dashboard (Fixed at bottom center)
    val dashW = width * 0.48f
    val dashH = 92f
    val dashX = centerX - dashW / 2f
    val dashY = cockpitY + 28f

    // Outer Bezel
    drawRoundRect(
        color = Color(0xFF18181B),
        topLeft = Offset(dashX, dashY),
        size = Size(dashW, dashH),
        cornerRadius = CornerRadius(14f, 14f),
        style = Fill
    )
    drawRoundRect(
        color = Color(0xFF52525B),
        topLeft = Offset(dashX, dashY),
        size = Size(dashW, dashH),
        cornerRadius = CornerRadius(14f, 14f),
        style = Stroke(width = 2.5f)
    )

    // Left Turn Signal Arrow ◀ (Blinking)
    val blinkerBlink = (System.currentTimeMillis() / 250) % 2 == 0L
    val leftArrowColor = if (isBlinkingLeft && blinkerBlink) Color(0xFF00E676) else Color(0x3300E676)
    drawCircle(leftArrowColor, radius = 10f, center = Offset(dashX + 22f, dashY + 24f))

    // Right Turn Signal Arrow ▶ (Blinking)
    val rightArrowColor = if (isBlinkingRight && blinkerBlink) Color(0xFF00E676) else Color(0x3300E676)
    drawCircle(rightArrowColor, radius = 10f, center = Offset(dashX + dashW - 22f, dashY + 24f))

    // Speedometer Gauge Arc (0 to 180 km/h)
    val speedRatio = (speedKmh / 160f).coerceIn(0.05f, 1.0f)
    drawRoundRect(
        color = if (speedKmh > 80f) Color(0xFFFF9800) else Color(0xFF00E5FF),
        topLeft = Offset(dashX + 16f, dashY + 48f),
        size = Size((dashW - 32f) * speedRatio, 8f),
        cornerRadius = CornerRadius(4f, 4f)
    )
}

private fun DrawScope.drawDrRidingThirdPerson(
    width: Float,
    height: Float,
    playerX: Float,
    steeringAngle: Float,
    speedKmh: Float,
    bikeColor: Color,
    isBlinkingLeft: Boolean,
    isBlinkingRight: Boolean,
    isBraking: Boolean
) {
    val bikeCenterX = (width / 2f) + (playerX * width * 0.36f)
    val baseBikeY = height * 0.82f

    // Bike lean based on steering angle
    val lean = (steeringAngle * 0.4f).coerceIn(-28f, 28f)

    rotate(degrees = lean, pivot = Offset(bikeCenterX, baseBikeY)) {
        // Shadow
        drawOval(
            color = Color.Black.copy(alpha = 0.55f),
            topLeft = Offset(bikeCenterX - 32f, baseBikeY + 8f),
            size = Size(64f, 18f)
        )

        // Rear Tire
        drawRoundRect(
            color = Color(0xFF18181B),
            topLeft = Offset(bikeCenterX - 18f, baseBikeY - 30f),
            size = Size(36f, 52f),
            cornerRadius = CornerRadius(10f, 10f)
        )

        // Tail Cowl
        val tailPath = Path().apply {
            moveTo(bikeCenterX - 24f, baseBikeY - 42f)
            lineTo(bikeCenterX + 24f, baseBikeY - 42f)
            lineTo(bikeCenterX + 14f, baseBikeY - 72f)
            lineTo(bikeCenterX - 14f, baseBikeY - 72f)
            close()
        }
        drawPath(tailPath, bikeColor)

        // Taillight
        val tailLightColor = if (isBraking) Color(0xFFFF1744) else Color(0xFFCC1111)
        drawRoundRect(
            color = tailLightColor,
            topLeft = Offset(bikeCenterX - 12f, baseBikeY - 50f),
            size = Size(24f, 8f),
            cornerRadius = CornerRadius(3f, 3f)
        )

        // Turn Indicators
        val isBlink = (System.currentTimeMillis() / 250) % 2 == 0L
        if (isBlinkingLeft && isBlink) {
            drawCircle(Color(0xFFFF9800), radius = 6f, center = Offset(bikeCenterX - 28f, baseBikeY - 48f))
        }
        if (isBlinkingRight && isBlink) {
            drawCircle(Color(0xFFFF9800), radius = 6f, center = Offset(bikeCenterX + 28f, baseBikeY - 48f))
        }

        // Rider (Helmet & Jacket)
        val riderY = baseBikeY - 75f
        drawOval(color = Color(0xFF27272A), topLeft = Offset(bikeCenterX - 22f, riderY - 26f), size = Size(44f, 38f))
        drawOval(color = Color(0xFFFAFAFA), topLeft = Offset(bikeCenterX - 14f, riderY - 52f), size = Size(28f, 30f))
        drawRoundRect(color = Color(0xFF18181B), topLeft = Offset(bikeCenterX - 10f, riderY - 42f), size = Size(20f, 8f), cornerRadius = CornerRadius(2f, 2f))
    }
}

private fun DrawScope.drawRearviewMirror(
    width: Float,
    height: Float,
    cityVehicles: List<CityVehicle>,
    speedKmh: Float
) {
    // Dr. Driving signature: Clean wide top-center rearview mirror!
    val mirrorW = width * 0.44f
    val mirrorH = 50f
    val mirrorX = (width / 2f) - (mirrorW / 2f)
    val mirrorY = 22f

    // Mirror Frame
    drawRoundRect(
        color = Color(0xFF18181B),
        topLeft = Offset(mirrorX, mirrorY),
        size = Size(mirrorW, mirrorH),
        cornerRadius = CornerRadius(10f, 10f)
    )
    drawRoundRect(
        color = Color(0xFF71717A),
        topLeft = Offset(mirrorX, mirrorY),
        size = Size(mirrorW, mirrorH),
        cornerRadius = CornerRadius(10f, 10f),
        style = Stroke(width = 2f)
    )

    // Mirror Glass (Road reflection behind)
    drawRoundRect(
        brush = Brush.verticalGradient(
            colors = listOf(Color(0xFF93C5FD), Color(0xFF4B5563)),
            startY = mirrorY,
            endY = mirrorY + mirrorH
        ),
        topLeft = Offset(mirrorX + 3f, mirrorY + 3f),
        size = Size(mirrorW - 6f, mirrorH - 6f),
        cornerRadius = CornerRadius(8f, 8f)
    )

    // Center divider in mirror
    drawLine(
        color = Color(0xFFFFD600),
        start = Offset(width / 2f, mirrorY + 18f),
        end = Offset(width / 2f, mirrorY + mirrorH - 6f),
        strokeWidth = 2f
    )

    // Cars behind you reflected in mirror
    val behindCars = cityVehicles.filter { it.distanceZ < 25f }
    for (car in behindCars) {
        val relDist = (25f - car.distanceZ).coerceIn(2f, 25f)
        val carScale = (relDist / 25f)
        val rX = (width / 2f) + (car.lane * 25f * carScale)
        val rY = mirrorY + mirrorH * 0.65f
        drawRect(
            color = Color(car.type.color),
            topLeft = Offset(rX - 8f * carScale, rY - 6f * carScale),
            size = Size(16f * carScale, 12f * carScale)
        )
    }
}
