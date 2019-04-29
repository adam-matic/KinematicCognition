package com.example.kinematiccognition
import android.graphics.Path
import android.graphics.PathMeasure
import android.os.Build.VERSION_CODES.N
import android.os.Environment
import android.service.autofill.Validators.not
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction
import java.io.File
import java.io.FileNotFoundException

data class TrajectoryFunction(var xsf: PolynomialSplineFunction,
                              var ysf: PolynomialSplineFunction)

data class CurveXY(var xs: MutableList<Float> = mutableListOf<Float>(),
                   var ys: MutableList<Float> = mutableListOf<Float>())


val sinf = { x: Float -> Math.sin(x.toDouble()).toFloat() }
val cosf = { x: Float -> Math.cos(x.toDouble()).toFloat() }
val expf = { x: Float -> Math.exp(x.toDouble()).toFloat() }

fun curveToPath (curve : CurveXY) : Path {
    val path = Path()
    path.moveTo(curve.xs[0], curve.ys[0])
    for (i in 1 until curve.xs.size) {
        path.lineTo(curve.xs[i], curve.ys[i])
    }
    path.lineTo(curve.xs[0], curve.ys[0])
    return path
}

fun saveCurve(c: CurveXY, filename: String) {
    val filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    val f = File(filePath, filename)

    if (!f.exists()) {
        val writer = f.writer()
        val N = c.xs.size
        writer.use {
            for (i in 0 until N) {
                writer.write("${c.xs[i]} ${c.ys[i]}\n")
            }
        }
        }
    }


object Experiment {
    val practice_duration = 20L
    val experiment_duration = 30L
    val pause_duration = 7L

    var trajectories = mutableMapOf<String, TrajectoryFunction>()
    var paths = mutableMapOf<String, Path>()

    var practiceTasks: TaskSequence
    var experimentTasks : TaskSequence

    init {
        // ellipse
        val curve = PureCurveGenerator(
            x0 = 1700f, y0 = 750f, eps = 1.2f, v = 2f,
            th0 = 0.75f * Math.PI.toFloat(), N = 2f, scale = 450f)
        paths["ellipse"] = curveToPath(curve);
        saveCurve(curve, "EllipsePath.txt")


        // elongated ellipse
        val curve2 = CurveXY()
        var th = 0f
        var dth = 0.01f
        var A = 800f
        var B = 200f
        while (th <= 2f * Math.PI) {
            th += dth
            curve2.xs.add(1920 / 2 + A * cosf(th))
            curve2.ys.add(600 + B * sinf(th))
        }
        paths["ellipse long"] = curveToPath(curve2)
        saveCurve(curve2, "EllipseLongPath.txt")

        // three petal
        val curve3 = PureCurveGenerator(
            x0 = 500f, y0 = 120f, eps = 1.5f, v = (3f / 2f),
            th0 = 0f * Math.PI.toFloat(), N = 4f, scale = 200f)
        paths["flower3"] = curveToPath(curve3)
        saveCurve(curve3, "Flower3Path.txt")

        // four petal
        val curve4 = PureCurveGenerator(
            x0 = 1450f, y0 = 550f, eps = 1.2f, v = (4f / 5f),
            th0 = 0.12f * Math.PI.toFloat(), N = 10f, scale = 120f)
        paths["flower4"] = curveToPath(curve4)
        saveCurve(curve4, "Flower4Path.txt")

        // path "lemniscate"
        val curve5 = CurveXY()
        th = 0f
        dth = 0.01f
        A = 800f
        B = 300f
        while (th <= 4f * Math.PI) {
            th += dth
            curve5.xs.add(1920 / 2 + A * cosf(th * 0.5f))
            curve5.ys.add(600 + B * sinf(th))
        }
        paths["lemniscate"] = curveToPath(curve5)
        saveCurve(curve5, "LemniscatePath.txt")


        // Trajectories

        trajectories["hipo"] = PureTrajectoryGenerator(
                x0 = 1700f, y0 = 750f, eps = 1.2f, v = 2f,
                th0 = 0.75f * Math.PI.toFloat(), N = 40f,
                scale = 450f, beta = 1f / 3f, k = 4f)

        trajectories["hyper"] = PureTrajectoryGenerator(
                x0 = 1700f, y0 = 750f, eps = 1.2f, v = 2f,
                th0 = 0.75f * Math.PI.toFloat(), N = 40f,
                scale = 450f, beta = 1f, k = 4f)

        trajectories["normal"] = PureTrajectoryGenerator(
                x0 = 1700f, y0 = 750f, eps = 1.2f, v = 2f,
                th0 = 0.75f * Math.PI.toFloat(), N = 40f,
                scale = 450f, beta = 2f / 3f,  k = 3f)


        // Tasks

        val traceEllipse1 = Task("trace", "ellipse", duration = practice_duration, saveData = false)
        val trackTarget1 = Task("track", "normal", duration = practice_duration, saveData = false)
        val scribble1 = Task("scribble", "", practice_duration, saveData = false)
        val pause = Task("pause", "", pause_duration, saveData = false)

        practiceTasks = TaskSequence(listOf(traceEllipse1, pause, trackTarget1, pause, scribble1))


        val traceEllipse2 = Task("trace", "ellipse", duration = experiment_duration, saveData = true)
        val traceEllipseLong = Task("trace", "ellipse long", duration = experiment_duration, saveData = true)
        val traceFlower3 = Task("trace", "flower3", duration = experiment_duration, saveData = true)
        val traceFlower4 = Task("trace", "flower4", duration = experiment_duration, saveData = true)
        val traceLemniscate = Task("trace", "lemniscate", duration = experiment_duration, saveData = true)
        val trackHipo = Task("track", "hipo", duration = experiment_duration, saveData = true)
        val trackHyper = Task("track", "hyper", duration = experiment_duration, saveData = true)
        val scribble2 = Task("scribble", "", duration=experiment_duration, saveData = true)
        val endthanks = Task("end", "end", pause_duration)

        experimentTasks = TaskSequence(listOf(traceEllipse2, pause, traceEllipseLong, pause,
            traceFlower3, pause, traceFlower4, pause, traceLemniscate, pause,
            trackHipo, pause, trackHyper, pause, scribble2, endthanks))


    }





}


