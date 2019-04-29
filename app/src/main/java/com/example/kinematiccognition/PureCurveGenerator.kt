package com.example.kinematiccognition


fun PureCurveGenerator (x0: Float = 0f, y0 : Float = 0f, eps: Float = 1.2f,
                        v: Float = 2f, th0 : Float = 0f,
                        N: Float = 5f, scale:Float=200f) : CurveXY
    {

    val xs: MutableList<Float> = mutableListOf<Float>()
    val ys: MutableList<Float> = mutableListOf<Float>()
    var th = th0

    val dtheta = 0.005f

    var x = x0
    var y = y0

    while (th < (th0 + Math.PI.toFloat() * N)) {
        val dx = dtheta * cosf(th) * expf(eps * sinf(v * (th - th0)))
        x += scale * dx
        val dy = dtheta * sinf(th) * expf(eps * sinf(v * (th - th0)))
        y += scale * dy
        th += dtheta
        xs.add(x)
        ys.add(y)
    }

    return CurveXY(xs, ys)
}