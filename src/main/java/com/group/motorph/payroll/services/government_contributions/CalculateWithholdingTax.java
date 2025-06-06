/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group.motorph.payroll.services.government_contributions;

/**
 *
 * Calculates withholding tax based on monthly earnings using the
 * Philippine BIR's progressive tax rates.
 * 
 * Author: MO-IT103 | CP2 | S1101
 */

public class CalculateWithholdingTax {

    /**
     * Calculates withholding tax based on gross monthly earnings.
     * Uses the Philippine progressive tax rate schedule for monthly income.
     *
     * @param monthlyEarnings The gross monthly earnings amount
     * @return The calculated withholding tax amount
     */
    public static double calculateWithholdingTax(double monthlyEarnings) {

        double tax;

        // Compute tax based on BIR TRAIN Law monthly brackets
        if (monthlyEarnings <= 20833) {
            tax = 0.0;
        } else if (monthlyEarnings <= 33333) {
            tax = (monthlyEarnings - 20833) * 0.20;
        } else if (monthlyEarnings <= 66667) {
            tax = 2500 + ((monthlyEarnings - 33333) * 0.25);
        } else if (monthlyEarnings <= 166667) {
            tax = 10833 + ((monthlyEarnings - 66667) * 0.30);
        } else if (monthlyEarnings <= 666667) {
            tax = 40833.33 + ((monthlyEarnings - 166667) * 0.32);
        } else {
            tax = 200833.33 + ((monthlyEarnings - 666667) * 0.35);
        }

        return tax;
    }
}
