package app.twentyhours.moviesbooking

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.twentyhours.moviesbooking.ui.theme.MoviesBookingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoviesBookingTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainContent(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MainContent(modifier: Modifier = Modifier) {
    val layout1: List<SeatsRowData> = listOf(
        SeatsRowData(rowName = "A", numberSeats = 13, voidSeats = setOf(1)),
        SeatsRowData(rowName = "B", numberSeats = 14, voidSeats = setOf(1)),
        SeatsRowData(rowName = "C", numberSeats = 14, voidSeats = setOf(1)),
        SeatsRowData(
            rowName = "D",
            numberSeats = 14,
            voidSeats = setOf(1),
            seatType = SeatType.VIP
        ),
        SeatsRowData(
            rowName = "E",
            numberSeats = 14,
            voidSeats = setOf(1),
            seatType = SeatType.VIP
        ),
        SeatsRowData(
            rowName = "F",
            numberSeats = 14,
            voidSeats = setOf(1),
            seatType = SeatType.VIP
        ),
        SeatsRowData(
            rowName = "F",
            numberSeats = 14,
            voidSeats = setOf(1),
            seatType = SeatType.VIP
        ),
        SeatsRowData(
            rowName = "G",
            numberSeats = 14,
            voidSeats = setOf(1),
            seatType = SeatType.VIP
        ),
        SeatsRowData(
            rowName = "H",
            numberSeats = 14,
            voidSeats = setOf(1),
            seatType = SeatType.VIP
        ),
        SeatsRowData(rowName = "D", numberSeats = 18, seatType = SeatType.COUPLE),
    )
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
        val state = rememberTransformableState { zoomChange, offsetChange, _ ->
            scale = (scale * zoomChange).coerceIn(1f, 3f)
            
            val extraWidth = (scale - 1) * constraints.maxWidth
            val extraHeight = (scale - 1) * 200

            val maxX = extraWidth / 2
            val maxY = extraHeight / 2

            offset = Offset(
                x = (offset.x + scale * offsetChange.x).coerceIn(-maxX, maxX),
                y = (offset.y + scale * offsetChange.y).coerceIn(-maxY, maxY)
            )
        }

        Row(modifier = Modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                translationX = offset.x
                translationY = offset.y
            }
            .transformable(state)) {
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
    val background = when (seatType) {
        SeatType.NORM -> Color.Gray
        SeatType.VIP -> Color.Red
        SeatType.COUPLE -> Color.Cyan
    }
    Box(
        modifier = Modifier
            .padding(1.dp)
            .size(width = 12.dp, height = 12.dp)
            .background(background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$rowName$number", style = TextStyle(
                fontSize = 6.sp
            )
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