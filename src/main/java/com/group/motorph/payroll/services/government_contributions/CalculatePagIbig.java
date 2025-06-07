/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author MO-IT103 | CP2 | S1101
 * 
 */

package com.group.motorph.payroll.services.government_contributions;


public class CalculatePagIbig {

    /**
     * Calculates Pag-IBIG contribution based on gross monthly pay.
     * The contribution rate varies based on the pay amount.
     *
     * @param grossMonthlyPay The gross monthly pay amount
     * @return The calculated Pag-IBIG contribution amount
     */
    public static double calculatePagIbig(double grossMonthlyPay) {

        // Monthly thresholds
        double lowerThreshold = 1000;
        double upperThreshold = 1500;

        if (grossMonthlyPay >= lowerThreshold && grossMonthlyPay <= upperThreshold) {
            // 1% contribution for pay within thresholds
            return grossMonthlyPay * 0.01;
        } else if (grossMonthlyPay > upperThreshold) {
            // 2% contribution for pay above upper threshold
            return grossMonthlyPay * 0.02;
        } else {
            // No contribution for pay below lower threshold
            return 0;
        }
    }
}
