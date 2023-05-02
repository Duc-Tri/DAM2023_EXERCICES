package com.dam2023.zelda.util;

/**
 * Copie de fonctions de la classe Math de Java 8 (car Android utilise seulement Java 7)
 */
public class Math
{
    /**
     * Returns the largest (closest to positive infinity)
     * {@code int} value that is less than or equal to the algebraic quotient.
     * There is one special case, if the dividend is the
     * {@linkplain Integer#MIN_VALUE Integer.MIN_VALUE} and the divisor is {@code -1},
     * then integer overflow occurs and
     * the result is equal to the {@code Integer.MIN_VALUE}.
     * <p/>
     * Normal integer division operates under the round to zero rounding mode
     * (truncation).  This operation instead acts under the round toward
     * negative infinity (floor) rounding mode.
     * The floor rounding mode gives different results than truncation
     * when the exact result is negative.
     * <ul>
     * <li>If the signs of the arguments are the same, the results of
     * {@code floorDiv} and the {@code /} operator are the same.  <br>
     * For example, {@code floorDiv(4, 3) == 1} and {@code (4 / 3) == 1}.</li>
     * <li>If the signs of the arguments are different,  the quotient is negative and
     * {@code floorDiv} returns the integer less than or equal to the quotient
     * and the {@code /} operator returns the integer closest to zero.<br>
     * For example, {@code floorDiv(-4, 3) == -2},
     * whereas {@code (-4 / 3) == -1}.
     * </li>
     * </ul>
     * <p/>
     * @param x the dividend
     * @param y the divisor
     * @return the largest (closest to positive infinity)
     * {@code int} value that is less than or equal to the algebraic quotient.
     * @throws ArithmeticException if the divisor {@code y} is zero
     * @since 1.8
     */
    public static int floorDiv(int x, int y)
    {
        int r = x / y;
        // if the signs are different and modulo not zero, round down
        if ((x ^ y) < 0 && (r * y != x))
        {
            r--;
        }
        return r;
    }
}
