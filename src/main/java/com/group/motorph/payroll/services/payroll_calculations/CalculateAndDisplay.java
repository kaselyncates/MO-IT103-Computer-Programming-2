/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author MO-IT103 | CP2 | S1101
 * 
 */

package com.group.motorph.payroll.services.payroll_calculations;

import com.group.motorph.payroll.models.EmployeeData;
import com.group.motorph.payroll.models.MonthlyTotals;
import java.util.ArrayList;
import com.group.motorph.payroll.services.government_contributions.CalculatePagIbig;
import com.group.motorph.payroll.services.government_contributions.CalculatePhilHealth;
import com.group.motorph.payroll.services.government_contributions.CalculateSss;
import com.group.motorph.payroll.services.government_contributions.CalculateWithholdingTax;

/**
 * Utility class to generate salary breakdown text for GUI use.
 */
public class CalculateAndDisplay {

    public static String getSalaryBreakdown(ArrayList<MonthlyTotals> monthlyTotals, ArrayList<EmployeeData> employeeData, String sssFilePath) {
        StringBuilder output = new StringBuilder();

        for (MonthlyTotals month : monthlyTotals) {
            for (EmployeeData data : employeeData) {
                double monthlyAllowances = PayrollCalculations.calculateMonthlyAllowances(data);
                double overtimePay = PayrollCalculations.calculateOvertimePay(month.getOverTime(), data.hourlyRate);
                double monthlyEarnings = (month.getHoursWorked() * data.hourlyRate) + overtimePay;
                double grossMonthPay = monthlyEarnings + monthlyAllowances;
                double sss = CalculateSss.calculateSss(grossMonthPay, sssFilePath);
                double pagIbig = CalculatePagIbig.calculatePagIbig(grossMonthPay);
                double philHealth = CalculatePhilHealth.calculatePhilHealth(grossMonthPay);
                double tax = CalculateWithholdingTax.calculateWithholdingTax(monthlyEarnings);
                double totalDeductions = sss + pagIbig + philHealth + tax;
                double netMonthPay = grossMonthPay - totalDeductions;

                output.append("Pay Period: ").append(month.getPayPeriodStart()).append(" to ").append(month.getPayPeriodEnd()).append("\n\n");
                output.append(String.format("Regular Hours Worked: %.2f\n", month.getHoursWorked()));
                output.append(String.format("Overtime Hours: %.2f\n", month.getOverTime()));
                output.append(String.format("Gross Pay: PHP %.2f\n", grossMonthPay));
                output.append(String.format(" - SSS: PHP %.2f\n", sss));
                output.append(String.format(" - PhilHealth: PHP %.2f\n", philHealth));
                output.append(String.format(" - Pag-IBIG: PHP %.2f\n", pagIbig));
                output.append(String.format(" - Tax: PHP %.2f\n\n", tax));
                output.append(String.format("Net Pay: PHP %.2f\n\n", netMonthPay));
            }
        }
        return output.toString();
    }
}
