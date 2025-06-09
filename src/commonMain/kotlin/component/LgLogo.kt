package component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOutBounce
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import dpInt
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.min


@Composable
fun LgLogo(
    modifier: Modifier = Modifier,
    color: Color = Color.Companion.Red,
) {
    var animationState by remember { mutableStateOf(AnimState.SPINNING) }

    var canvasSize by remember { mutableIntStateOf(0) }
    val strokeWidth = canvasSize / 20F
    val noseHorizontalLength = canvasSize / 6F
    val eyeSize = noseHorizontalLength.dpInt.dp

    val boxRotationX = remember { Animatable(0F)}
    val boxRotationY = remember { Animatable(0F) }
    val boxTranslationX = remember { Animatable(0F) }
    val boxTranslationY = remember { Animatable(0F) }
    val boxRotationZ = remember { Animatable(0F) }

    val eyeTranslationX = remember { Animatable(0F) }
    val eyeTranslationY = remember { Animatable(0F) }
    val eyeRotationX = remember { Animatable(0F) }

    val noseTranslationY = remember { Animatable(0F) }
    val noseTranslationX = remember { Animatable(0F) }

    var changeEyeShape by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .graphicsLayer {
                rotationX = boxRotationX.value
                rotationY = boxRotationY.value
                rotationZ = boxRotationZ.value
                translationX = boxTranslationX.value
                translationY = boxTranslationY.value
                cameraDistance = 50 * density
            }
    ) {
        // G Shape
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .onGloballyPositioned {
                    canvasSize = min(it.size.width, it.size.height)
                }
        ) {
            val radius = (canvasSize - strokeWidth) / 2
            val centerX = size.width / 2
            val centerY = size.height / 2
            drawArc(
                color = color,
                startAngle = 0f,
                sweepAngle = 270f,
                useCenter = false,
                style = Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Companion.Square
                ),
                size = Size(radius * 2, radius * 2),
                topLeft = Offset(
                    centerX - radius,
                    centerY - radius
                )
            )
            drawLine(
                color = color,
                start = Offset(
                    x = centerX + noseHorizontalLength + (strokeWidth / 7),
                    y = centerY
                ),
                end = Offset(centerX * 2 + radius - centerX, centerY),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Companion.Square
            )
        }

        Box(
            modifier = Modifier
                .padding(strokeWidth.dpInt.dp)
                .padding(
                    start = eyeSize,
                    top = eyeSize + (strokeWidth.dpInt / 2).dp
                )
                .graphicsLayer {
                    translationX = eyeTranslationX.value
                    translationY = eyeTranslationY.value
                    rotationX = eyeRotationX.value
                }
                .size(eyeSize * 0.95F)
                .background(
                    color = color,
                    shape = if(changeEyeShape) {
                        RectangleShape
                    } else {
                        CircleShape
                    }
                )
        )

        // Nose
        Canvas(
            modifier = Modifier
                .padding(vertical = (strokeWidth.dpInt * 2).dp)
                .padding(start = (canvasSize / 2).dpInt.dp)
                .graphicsLayer {
                    translationX = noseTranslationX.value
                    translationY = noseTranslationY.value
                }
        ) {
            // Vertical
            drawLine(
                color = color,
                start = Offset(0F, noseHorizontalLength),
                end = Offset(0F, noseHorizontalLength * 4),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Companion.Square
            )
            // Horizontal
            drawLine(
                color = color,
                start = Offset(
                    0F,
                    noseHorizontalLength * 4
                ),
                end = Offset(
                    noseHorizontalLength - (strokeWidth * 6 / 7),
                    noseHorizontalLength * 4
                ),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Companion.Square
            )
        }
    }

    LaunchedEffect(animationState) {
        when (animationState) {
            AnimState.SPINNING -> {
                launch {
                    val boxAnimate = async {
                        boxRotationY.animateTo(
                            targetValue = 0F,
                            animationSpec = keyframes {
                                durationMillis = 3000
                                0F at 0
                                -60F at 250 using LinearOutSlowInEasing
                                750F at 1000
                                720F at 1600
                                720F at 3000
                            },
                        )
                    }
                    val noseAnimate = async {
                        noseTranslationX.animateTo(
                            targetValue = 0F,
                            animationSpec = keyframes {
                                durationMillis = 3000
                                0F at 0
                                -canvasSize / 3F at 150 using LinearOutSlowInEasing
                                -canvasSize / 10F at 650
                                canvasSize / 50F at 1000
                                0F at 1150
                                0F at 3000
                            },
                        )
                    }
                    val eyeAnimate = async {
                        eyeTranslationX.animateTo(
                            targetValue = 0F,
                            animationSpec = keyframes {
                                durationMillis = 3000
                                0f at 0
                                -canvasSize / 4F at 70 using LinearOutSlowInEasing
                                canvasSize / 50F at 1000
                                0F at 1100
                                0F at 3000
                            },
                        )
                    }
                    awaitAll(boxAnimate, noseAnimate, eyeAnimate)
                    animationState = AnimState.BOBBING
                }
            }

            AnimState.BOBBING -> {
                launch {
                    val boxTranslateX = async {
                        boxTranslationX.animateTo(
                            targetValue = 0F,
                            animationSpec = keyframes {
                                durationMillis = 3000
                                0F at 0
                                canvasSize / -30F at 250 using EaseInOutBounce
                                0F at 500
                                canvasSize / 30F at 750 using EaseInOutBounce
                                0F at 1000
                                canvasSize / -30F at 1250 using EaseInOutBounce
                                0F at 1500
                                canvasSize / 30F at 1750 using EaseInOutBounce
                                0F at 2000
                                0F at 3000
                            }
                        )
                    }
                    val boxTranslateY = async {
                        boxTranslationY.animateTo(
                            targetValue = 0F,
                            animationSpec = keyframes {
                                durationMillis = 3000
                                0F at 0
                                canvasSize / -15F at 250 using EaseInOutBounce
                                0F at 500
                                canvasSize / -15F at 750 using EaseInOutBounce
                                0F at 1000
                                canvasSize / -15F at 1250 using EaseInOutBounce
                                0F at 1500
                                canvasSize / -15F at 1750 using EaseInOutBounce
                                0F at 2000
                                0F at 3000
                            }
                        )
                    }
                    val boxRotationZ = async {
                        boxRotationZ.animateTo(
                            targetValue = 0F,
                            animationSpec = keyframes {
                                durationMillis = 3000
                                0F at 0
                                -6F at 250 using EaseInOutBounce
                                0F at 500
                                6F at 750 using EaseInOutBounce
                                0F at 1000
                                -6F at 1250 using EaseInOutBounce
                                0F at 1500
                                6F at 1750 using EaseInOutBounce
                                0F at 2000
                                0F at 3000
                            }
                        )
                    }
                    val eyeTranslateY = async {
                        eyeTranslationY.animateTo(
                            targetValue = 0F,
                            animationSpec = keyframes {
                                durationMillis = 3000
                                0F at 0
                                canvasSize / -15F at 250
                                0F at 500
                                canvasSize / -15F at 750
                                0F at 1000
                                canvasSize / -15F at 1250
                                0F at 1500
                                canvasSize / -15F at 1750
                                0F at 2000
                                0F at 3000
                            }
                        )
                    }
                    val noseTranslateY = async {
                        noseTranslationY.animateTo(
                            targetValue = 0F,
                            animationSpec = keyframes {
                                durationMillis = 3000
                                0F at 0
                                canvasSize / -15F at 250
                                0F at 500
                                canvasSize / -15F at 750
                                0F at 1000
                                canvasSize / -15F at 1250
                                0F at 1500
                                canvasSize / -15F at 1750
                                0F at 2000
                                0F at 3000
                            }
                        )
                    }
                    val eyeRotateX = async {
                        eyeRotationX.animateTo(
                            targetValue = 0F,
                            animationSpec = keyframes {
                                durationMillis = 3000
                                0F at 0
                                0F at 2300
                                85F at 2450 using LinearOutSlowInEasing
                                0f at 3000
                            }
                        )
                    }
                    async {
                        delay(2450)
                        changeEyeShape = true
                        delay(10)
                        changeEyeShape = false
                    }

                    awaitAll(
                        boxTranslateX,
                        boxTranslateY,
                        boxRotationZ,
                        eyeTranslateY,
                        noseTranslateY,
                        eyeRotateX,
                    )
                    delay(900)
                    animationState = AnimState.BOWING
                }
            }

            AnimState.BOWING -> {
                launch {
                    val boxRotationX = async {
                        boxRotationX.animateTo(
                            targetValue = 0F,
                            animationSpec = keyframes {
                                durationMillis = 3000
                                0F at 0
                                25F at 350
                                25F at 950
                                0F at 1500
                                0F at 3000
                            }
                        )
                    }
                    val boxTranslateY = async {
                        boxTranslationY.animateTo(
                            targetValue = 0F,
                            animationSpec = keyframes {
                                durationMillis = 3000
                                0F at 0
                                canvasSize / 10F at 350
                                canvasSize / 10F at 950
                                0F at 1500
                                0F at 3000
                            }
                        )
                    }
                    val eyeRotateX = async {
                        eyeRotationX.animateTo(
                            targetValue = 0F,
                            animationSpec = keyframes {
                                durationMillis = 3000
                                0F at 0
                                70F at 350
                                70F at 950
                                0F at 1500
                                0F at 3000
                            }
                        )
                    }
                    async {
                        delay(350)
                        changeEyeShape = true
                        delay(650)
                        changeEyeShape = false
                    }
                    val eyeTranslateY = async {
                        eyeTranslationY.animateTo(
                            targetValue = 0F,
                            animationSpec = keyframes {
                                durationMillis = 3000
                                0F at 0
                                canvasSize / 6F at 300
                                canvasSize / 6F at 950
                                0F at 1500
                                0F at 3000
                            }
                        )
                    }
                    val noseTranslateY = async {
                        noseTranslationY.animateTo(
                            targetValue = 0F,
                            animationSpec = keyframes {
                                durationMillis = 3000
                                0F at 0
                                canvasSize / 10F at 350
                                canvasSize / 10F at 950
                                0F at 1500
                                0F at 3000
                            }
                        )
                    }

                    awaitAll(
                        boxRotationX,
                        boxTranslateY,
                        eyeRotateX,
                        eyeTranslateY,
                        noseTranslateY
                    )
                    animationState = AnimState.SPINNING
                }
            }
        }
    }
}

enum class AnimState {
    SPINNING,
    BOBBING,
    BOWING,
}