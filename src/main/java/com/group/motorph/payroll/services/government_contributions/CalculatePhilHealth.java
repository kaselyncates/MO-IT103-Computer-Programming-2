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

public class CalculatePhilHealth {
    /**
     * Calculates PhilHealth contribution based on gross monthly pay.
     * The contribution is calculated as 3% of gross pay, split equally between
     * employer and employee (employee pays half).
     *
     * @param grossMonthlyPay The gross monthly pay amount
     * @return The calculated PhilHealth contribution amount (employee share)
     */
    public static double calculatePhilHealth(double grossMonthlyPay) {
        // 3% monthly contribution, split 50/50 between employer and employee
        return (grossMonthlyPay * 0.03) / 2;
    }
    
}
