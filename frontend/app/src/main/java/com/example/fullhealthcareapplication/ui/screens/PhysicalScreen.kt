package com.example.fullhealthcareapplication.ui.screens

import android.graphics.PointF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.SnapPosition.Center
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fullhealthcareapplication.ui.components.AppBar
import android.graphics.Paint
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import com.example.fullhealthcareapplication.ui.components.StepCounterBarGraph
import kotlin.random.Random

@Composable
fun PhysicalScreen(
    toHome: () -> Unit
){
    val horizontalGradientBrush = Brush.horizontalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.tertiaryContainer
        )
    )
    val physicalDescription = "Cardio fitness, or cardiorespiratory fitness, is your body's ability to take in, circulate and use oxygen. This process relies on vital parts of the body such as your heart and lungs. Therefore, aerobic exercises such as running or high-intensity interval training are great for your fitness."
    AppBar(
        title = "Physical",
        toHome = {toHome()}
    )
    Box(
        modifier = Modifier
            .padding(top = 110.dp)
            .clip(RoundedCornerShape(bottomStart = 38.dp, bottomEnd = 38.dp))
            .height(20.dp)
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.secondaryContainer)
    )
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 150.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Card (
            modifier = Modifier
                .height(160.dp)
                .fillMaxWidth(0.8f),
            shape = RoundedCornerShape(32.dp)
        ){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(horizontalGradientBrush, alpha = 0.5f)
                    .height(100.dp)
                    .fillMaxWidth(0.8f)
            ){
                Column (
                    modifier = Modifier
                        .padding(start = 30.dp, end = 30.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(
                        text = "About Physical",
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = physicalDescription,
                        fontSize = 12.sp,
                        lineHeight = 14.sp
                    )
                }
            }
        }
        val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
        val stepCounts = listOf(3000, 5000, 7000, 4000, 6000, 8000, 2000) // Fake data

//        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
//            Text(
//                text = "Weekly Step Count",
//                textAlign = TextAlign.Start,
//                fontSize = 20.sp,
//                fontWeight = FontWeight.Bold
//            )
//            Spacer(modifier = Modifier.height(16.dp))
//            StepCounterBarGraph(stepCounts = stepCounts, days = daysOfWeek)
//        }
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, start = 24.dp, end = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "Weekly Step Count",
                textAlign = TextAlign.Start,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Row (
            modifier = Modifier
                .padding(start = 24.dp, end = 24.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            StepCounterBarGraph(stepCounts = stepCounts, days = daysOfWeek)
        }
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, start = 24.dp, end = 24.dp, bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "Today",
                textAlign = TextAlign.Start,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Row (
            modifier = Modifier
                .padding(start = 24.dp, end = 24.dp, bottom = 5.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                "Calories Burnt",
                fontSize = 12.sp,
                lineHeight = 14.sp)
            Text(
                "200",
                fontSize = 12.sp,
                lineHeight = 14.sp
            )
        }
        Row (
            modifier = Modifier
                .padding(start = 24.dp, end = 24.dp, bottom = 5.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                "Step Count",
                fontSize = 12.sp,
                lineHeight = 14.sp
            )
            Text(
                "4000",
                fontSize = 12.sp,
                lineHeight = 14.sp
            )
        }
        Row (
            modifier = Modifier
                .padding(start = 24.dp, end = 24.dp, bottom = 5.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                "Distance",
                fontSize = 12.sp,
                lineHeight = 14.sp
            )
            Text(
                "700",
                fontSize = 12.sp,
                lineHeight = 14.sp
            )
        }
    }
}

data class GraphAppearance(
    val graphColor: Color,
    val graphAxisColor: Color,
    val graphThickness: Float,
    val iscolorAreaUnderChart: Boolean,
    val colorAreaUnderChart: Color,
    val isCircleVisible: Boolean,
    val circleColor: Color,
    val backgroundColor: Color
)

@Composable
fun Graph(
    modifier : Modifier,
    xValues: List<Int>,
    yValues: List<Int>,
    points: List<Float>,
    paddingSpace: Dp,
    verticalStep: Int,
    graphAppearance: GraphAppearance
) {
    val controlPoints1 = mutableListOf<PointF>()
    val controlPoints2 = mutableListOf<PointF>()
    val coordinates = mutableListOf<PointF>()
    val density = LocalDensity.current
    val textPaint = remember(density) {
        Paint().apply {
            color = graphAppearance.graphAxisColor.toArgb()
            textAlign = Paint.Align.CENTER
            textSize = density.run { 12.sp.toPx() }
        }
    }

    Box(
        modifier = modifier
            .background(graphAppearance.backgroundColor)
            .padding(horizontal = 8.dp, vertical = 12.dp),
        contentAlignment = Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight(0.7f),
        ) {
            val xAxisSpace = (size.width - paddingSpace.toPx()) / xValues.size
            val yAxisSpace = size.height / yValues.size
            /** placing x axis points */
            for (i in xValues.indices) {
                drawContext.canvas.nativeCanvas.drawText(
                    "${xValues[i]}",
                    xAxisSpace * (i + 1),
                    size.height - 30,
                    textPaint
                )
            }
            /** placing y axis points */
            for (i in yValues.indices) {
                drawContext.canvas.nativeCanvas.drawText(
                    "${yValues[i]}",
                    paddingSpace.toPx() / 2f,
                    size.height - yAxisSpace * (i + 1),
                    textPaint
                )
            }
            /** placing our x axis points */
            for (i in points.indices) {
                val x1 = xAxisSpace * xValues[i]
                val y1 = size.height - (yAxisSpace * (points[i]/verticalStep.toFloat()))
                coordinates.add(PointF(x1,y1))
                /** drawing circles to indicate all the points */
                if (graphAppearance.isCircleVisible){
                    drawCircle(
                        color = graphAppearance.circleColor,
                        radius = 10f,
                        center = Offset(x1,y1)
                    )
                }
            }
            /** calculating the connection points */
            for (i in 1 until coordinates.size) {
                controlPoints1.add(PointF((coordinates[i].x + coordinates[i - 1].x) / 2, coordinates[i - 1].y))
                controlPoints2.add(PointF((coordinates[i].x + coordinates[i - 1].x) / 2, coordinates[i].y))
            }
            /** drawing the path */
            val stroke = Path().apply {
                reset()
                moveTo(coordinates.first().x, coordinates.first().y)
                for (i in 0 until coordinates.size - 1) {
                    cubicTo(
                        controlPoints1[i].x,controlPoints1[i].y,
                        controlPoints2[i].x,controlPoints2[i].y,
                        coordinates[i + 1].x,coordinates[i + 1].y
                    )
                }
            }
            /** filling the area under the path */
            val fillPath = android.graphics.Path(stroke.asAndroidPath())
                .asComposePath()
                .apply {
                    lineTo(xAxisSpace * xValues.last(), size.height - yAxisSpace)
                    lineTo(xAxisSpace, size.height - yAxisSpace)
                    close()
                }
            if (graphAppearance.iscolorAreaUnderChart){
                drawPath(
                    fillPath,
                    brush = Brush.verticalGradient(
                        listOf(
                            graphAppearance.colorAreaUnderChart,
                            Color.Transparent,
                        ),
                        endY = size.height - yAxisSpace
                    ),
                )
            }
            drawPath(
                stroke,
                color = graphAppearance.graphColor,
                style = Stroke(
                    width = graphAppearance.graphThickness,
                    cap = StrokeCap.Round
                )
            )
        }
    }
}