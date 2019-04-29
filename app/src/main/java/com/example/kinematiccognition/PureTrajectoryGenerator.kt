package com.example.kinematiccognition

import org.apache.commons.math3.analysis.interpolation.SplineInterpolator
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction


fun PureTrajectoryGenerator(x0    : Float =   0f,     y0 : Float = 0f,
                            eps   : Float = 1.2f,      v : Float = 2f,
                            th0   : Float =   0f,      N : Float = 5f,
                            scale : Float = 200f,   beta : Float = 2f/3f,
                            k     : Float = 0.7f ): TrajectoryFunction {

    val xs: MutableList<Double> = mutableListOf<Double>()
    val ys: MutableList<Double> = mutableListOf<Double>()
    val ths: MutableList<Double> = mutableListOf<Double>()
    val xsf : PolynomialSplineFunction
    val ysf : PolynomialSplineFunction

    var th = th0.toDouble()
    val dtheta = 0.005
    var x = x0.toDouble()
    var y = y0.toDouble()

    while (th < (th0 + Math.PI * N)) {
        val dx = dtheta * Math.cos(th) * Math.exp(eps * Math.sin(v * (th - th0)))
        x += scale * dx
        val dy = dtheta * Math.sin(th) * Math.exp(eps * Math.sin(v * (th - th0)))
        y += scale * dy
        th += dtheta
        xs.add(x)
        ys.add(y)
        ths.add(th)
    }

    val R_exp_beta : MutableList<Double> = mutableListOf<Double>()

    for (i in 0 until ths.size) R_exp_beta.add(Math.exp(beta * eps * Math.sin(v.toDouble() * (ths[i] - th0))))

    var t = 0.0
    val t_theta: MutableList<Double> = mutableListOf<Double>()
    for (i in 0 until ths.size) {
        t += dtheta * R_exp_beta[i] / k
        t_theta.add(t)
    }

    val s = SplineInterpolator()

    xsf = s.interpolate (t_theta.toDoubleArray(), xs.toDoubleArray())
    ysf = s.interpolate (t_theta.toDoubleArray(), ys.toDoubleArray())


    return TrajectoryFunction(xsf, ysf)
}

