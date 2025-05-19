/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group.motorph.payroll.services.payroll_calculations;

import com.group.motorph.payroll.models.EmployeeData;
import com.group.motorph.payroll.models.WeeklyTotals;
import java.util.ArrayList;
import com.group.motorph.payroll.ui.ConsoleUI;
import com.group.motorph.payroll.services.government_contributions.CalculatePagIbig;
import com.group.motorph.payroll.services.government_contributions.CalculatePhilHealth;
import com.group.motorph.payroll.services.government_contributions.CalculateSss;
import com.group.motorph.payroll.services.government_contributions.CalculateWithholdingTax;

/**
 *
 * @author MO-IT103 | CP2 | S1101
 * 
 */


public class CalculateAndDisplay {
    
    /**
     * Calculates and displays salary details for each pay period.This includes
     * earnings, deductions, and net pay calculations.
     * @param weeklyTotals The list where the weekly totals are being stored
     * @param employeeData The list where the employee data are being stored
     * @param sssFilePath The file path for SSS contribution range table
     */
    public static void calculateAndDisplaySalary(ArrayList<WeeklyTotals> weeklyTotals, ArrayList<EmployeeData> employeeData, String sssFilePath) {

        // Process each week in the data
        for (WeeklyTotals week : weeklyTotals) {

            // Process employee data (usually just one employee)
            for (EmployeeData data : employeeData) {

                // Calculate weekly allowances from monthly values
                // Monthly allowances are divided by 4 to get weekly equivalent
                double weeklyAllowances = PayrollCalculations.calculateWeeklyAllowances(data);

                // Calculate overtime pay with 25% premium rate
                // Overtime is paid at 1.25 times the regular hourly rate
                double overtimePay = PayrollCalculations.calculateOvertimePay(week.overTime, data.hourlyRate);

                // Calculate regular weekly earnings (hours worked * hourly rate)
                // Plus overtime pay for total earnings
                double weeklyEarnings = (week.hoursWorked * data.hourlyRate) + overtimePay;

                // Calculate gross pay (earnings + allowances)
                double grossWeekPay = weeklyEarnings + weeklyAllowances;

                // Calculate mandatory government deductions
                double sss = CalculateSss.calculateSss(grossWeekPay, sssFilePath);
                double pagIbig = CalculatePagIbig.calculatePagIbig(grossWeekPay);
                double philHealth = CalculatePhilHealth.calculatePhilHealth(grossWeekPay);
                double tax = CalculateWithholdingTax.calculateWithholdingTax(weeklyEarnings);

                // Calculate net pay after all deductions
                double totalDeductions = sss + pagIbig + philHealth + tax;
                double netWeekPay = grossWeekPay - totalDeductions;

                // Display results in a formatted manner
                ConsoleUI.displayWeeklySalary(week, data, sss, pagIbig, philHealth, tax, grossWeekPay, netWeekPay);
            }
        }
    }
    
    public static String getSalaryBreakdown(ArrayList<WeeklyTotals> weeklyTotals, ArrayList<EmployeeData> employeeData, String sssFilePath) {
    StringBuilder output = new StringBuilder();

    for (WeeklyTotals week : weeklyTotals) {
        for (EmployeeData data : employeeData) {
            double weeklyAllowances = PayrollCalculations.calculateWeeklyAllowances(data);
            double overtimePay = PayrollCalculations.calculateOvertimePay(week.overTime, data.hourlyRate);
            double weeklyEarnings = (week.hoursWorked * data.hourlyRate) + overtimePay;
            double grossWeekPay = weeklyEarnings + weeklyAllowances;

            double sss = CalculateSss.calculateSss(grossWeekPay, sssFilePath);
            double pagIbig = CalculatePagIbig.calculatePagIbig(grossWeekPay);
            double philHealth = CalculatePhilHealth.calculatePhilHealth(grossWeekPay);
            double tax = CalculateWithholdingTax.calculateWithholdingTax(weeklyEarnings);

            double totalDeductions = sss + pagIbig + philHealth + tax;
            double netWeekPay = grossWeekPay - totalDeductions;

            output.append("Pay Period: ").append(week.payPeriodStart).append(" to ").append(week.payPeriodEnd).append("\n");
            output.append(String.format("Regular Hours Worked: %.2f\n", week.hoursWorked));
            output.append(String.format("Overtime Hours: %.2f\n", week.overTime));
            output.append(String.format("Gross Pay: PHP %.2f\n", grossWeekPay));
            output.append(String.format(" - SSS: PHP %.2f\n", sss));
            output.append(String.format(" - PhilHealth: PHP %.2f\n", philHealth));
            output.append(String.format(" - Pag-IBIG: PHP %.2f\n", pagIbig));
            output.append(String.format(" - Tax: PHP %.2f\n", tax));
            output.append(String.format("Net Pay: PHP %.2f\n\n", netWeekPay));
        }
    }

    return output.toString();
}

}
