package app.twentyhours.moviesbooking

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculateCentroid
import androidx.compose.foundation.gestures.calculateCentroidSize
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateRotation
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChanged
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.twentyhours.moviesbooking.ui.theme.MoviesBookingTheme
import kotlin.math.abs

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoviesBookingTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        MainContent()
                    }
                }
            }
        }
    }
}

@Composable
fun MainContent(modifier: Modifier = Modifier) {
    val layout2: List<SeatsRowData> = listOf(
        SeatsRowData(rowName = "A", numberSeats = 22, voidSeats = setOf(5, 20)),
        SeatsRowData(rowName = "B", numberSeats = 22, voidSeats = setOf(5, 20)),
        SeatsRowData(rowName = "C", numberSeats = 22, voidSeats = setOf(5, 20)),
        SeatsRowData(
            rowName = "D",
            numberSeats = 22,
            voidSeats = setOf(5, 20),
            seatType = SeatType.VIP
        ),
        SeatsRowData(
            rowName = "E",
            numberSeats = 22,
            voidSeats = setOf(5, 20),
            seatType = SeatType.VIP
        ),
        SeatsRowData(
            rowName = "F",
            numberSeats = 22,
            voidSeats = setOf(5, 20),
            seatType = SeatType.VIP
        ),
        SeatsRowData(
            rowName = "G",
            numberSeats = 22,
            voidSeats = setOf(5, 20),
            seatType = SeatType.VIP
        ),
        SeatsRowData(
            rowName = "H",
            numberSeats = 22,
            voidSeats = setOf(5, 20),
            seatType = SeatType.VIP
        ),
        SeatsRowData(
            rowName = "I",
            numberSeats = 22,
            voidSeats = setOf(5, 20),
            seatType = SeatType.VIP
        ),
        SeatsRowData(
            rowName = "J",
            numberSeats = 22,
            voidSeats = setOf(5, 20),
            seatType = SeatType.VIP
        ),
        SeatsRowData(
            rowName = "K",
            numberSeats = 22,
            voidSeats = setOf(1),
            seatType = SeatType.COUPLE
        )
    )

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(100.dp))
        SeatsLayout(layout2, true)
    }
}

@Composable
fun SeatsLayout(seats: List<SeatsRowData>, isReverse: Boolean = false) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    BoxWithConstraints {
        val onGesture = { _: Offset, offsetChange: Offset, zoomChange: Float, _: Float,
                          _: PointerInputChange, changes: List<PointerInputChange> ->
            scale = (scale * zoomChange).coerceIn(1f, 3f)

            val extraWidth = (scale - 1) * constraints.maxWidth
            val extraHeight = (scale - 1) * 200

            val maxX = extraWidth / 2
            val maxY = extraHeight / 2

            offset = Offset(
                x = (offset.x + scale * offsetChange.x).coerceIn(-maxX, maxX),
                y = (offset.y + scale * offsetChange.y).coerceIn(-maxY, maxY)
            )

            if (changes.size > 1) {
                changes.forEach { it.consume() }
            }
        }

        Row(modifier = Modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                translationX = offset.x
                translationY = offset.y
            }
            .pointerInput(Unit) {
                detectTransformGestures(
                    pass = PointerEventPass.Initial,
                    onGesture = onGesture
                )
            }) {
            val direction = if (isReverse) LayoutDirection.Rtl else LayoutDirection.Ltr

            CompositionLocalProvider(LocalLayoutDirection provides direction) {
                Column(horizontalAlignment = Alignment.Start) {
                    seats.forEach {
                        SeatsRow(it)
                    }
                }
            }
        }
    }
}

@Composable
fun SeatsRow(seatsRowData: SeatsRowData) {
    var remainingSeats = seatsRowData.numberSeats
    var currentSeat = 1
    Row(horizontalArrangement = Arrangement.End) {
        while (remainingSeats > 0) {
            if (seatsRowData.voidSeats.contains(currentSeat)) {
                VoidSeatItem()
                currentSeat++
            } else {
                SeatItem(
                    rowName = seatsRowData.rowName,
                    number = seatsRowData.numberSeats - remainingSeats + 1,
                    seatType = seatsRowData.seatType
                )
                currentSeat++
                remainingSeats--
            }
        }
    }
}

@Composable
fun SeatItem(
    rowName: String,
    number: Int,
    seatType: SeatType = SeatType.NORM
) {
    var isSelected by remember { mutableStateOf(false) }
    var background by remember { mutableStateOf(Color.Green) }
    background = when (seatType) {
        SeatType.NORM -> Color.Green
        SeatType.VIP -> Color.Red
        SeatType.COUPLE -> Color.Cyan
    }
    if (isSelected) {
        background = Color.Gray
    }
    Box(
        modifier = Modifier
            .padding(1.dp)
            .size(width = 12.dp, height = 12.dp)
            .background(background)
            .clickable { isSelected = !isSelected },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$rowName$number",
            style = TextStyle(fontSize = 6.sp)
        )
    }
}

@Composable
fun VoidSeatItem() {
    Box(
        modifier = Modifier
            .padding(1.dp)
            .size(width = 12.dp, height = 12.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MoviesBookingTheme {
        MainContent()
    }
}

/**
 * Source: https://stackoverflow.com/a/76021552/23770482
 */
suspend fun PointerInputScope.detectTransformGestures(
    panZoomLock: Boolean = false,
    consume: Boolean = true,
    pass: PointerEventPass = PointerEventPass.Main,
    onGestureStart: (PointerInputChange) -> Unit = {},
    onGesture: (
        centroid: Offset,
        pan: Offset,
        zoom: Float,
        rotation: Float,
        mainPointer: PointerInputChange,
        changes: List<PointerInputChange>
    ) -> Unit,
    onGestureEnd: (PointerInputChange) -> Unit = {}
) {
    awaitEachGesture {
        var rotation = 0f
        var zoom = 1f
        var pan = Offset.Zero
        var pastTouchSlop = false
        val touchSlop = viewConfiguration.touchSlop
        var lockedToPanZoom = false

        // Wait for at least one pointer to press down, and set first contact position
        val down: PointerInputChange = awaitFirstDown(
            requireUnconsumed = false,
            pass = pass
        )
        onGestureStart(down)

        var pointer = down
        // Main pointer is the one that is down initially
        var pointerId = down.id

        do {
            val event = awaitPointerEvent(pass = pass)

            // If any position change is consumed from another PointerInputChange
            // or pointer count requirement is not fulfilled
            val canceled =
                event.changes.any { it.isConsumed }

            if (!canceled) {

                // Get pointer that is down, if first pointer is up
                // get another and use it if other pointers are also down
                // event.changes.first() doesn't return same order
                val pointerInputChange =
                    event.changes.firstOrNull { it.id == pointerId }
                        ?: event.changes.first()

                // Next time will check same pointer with this id
                pointerId = pointerInputChange.id
                pointer = pointerInputChange

                val zoomChange = event.calculateZoom()
                val rotationChange = event.calculateRotation()
                val panChange = event.calculatePan()

                if (!pastTouchSlop) {
                    zoom *= zoomChange
                    rotation += rotationChange
                    pan += panChange

                    val centroidSize = event.calculateCentroidSize(useCurrent = false)
                    val zoomMotion = abs(1 - zoom) * centroidSize
                    val rotationMotion =
                        abs(rotation * kotlin.math.PI.toFloat() * centroidSize / 180f)
                    val panMotion = pan.getDistance()

                    if (zoomMotion > touchSlop ||
                        rotationMotion > touchSlop ||
                        panMotion > touchSlop
                    ) {
                        pastTouchSlop = true
                        lockedToPanZoom = panZoomLock && rotationMotion < touchSlop
                    }
                }

                if (pastTouchSlop) {
                    val centroid = event.calculateCentroid(useCurrent = false)
                    val effectiveRotation = if (lockedToPanZoom) 0f else rotationChange
                    if (effectiveRotation != 0f ||
                        zoomChange != 1f ||
                        panChange != Offset.Zero
                    ) {
                        onGesture(
                            centroid,
                            panChange,
                            zoomChange,
                            effectiveRotation,
                            pointer,
                            event.changes
                        )
                    }

                    if (consume) {
                        event.changes.forEach {
                            if (it.positionChanged()) {
                                it.consume()
                            }
                        }
                    }
                }
            }
        } while (!canceled && event.changes.any { it.pressed })
        onGestureEnd(pointer)
    }
}