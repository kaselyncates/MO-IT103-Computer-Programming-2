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
import com.group.motorph.payroll.models.TimeLogDetails;
import java.util.ArrayList;

public class PayrollCalculations {
    /**
     * Calculates the employee's fixed monthly allowance.
     *
     * @param data The employee's data
     * @return The allowance amount
     */
    static double calculateMonthlyAllowances(EmployeeData data) {
        return data.riceSubsidy + data.phoneAllowance + data.clothingAllowance;
    }

    /**
     * Calculates overtime pay at 125% of the hourly rate.
     *
     * @param overTime    Total overtime hours
     * @param hourlyRate  The employee's hourly rate
     * @return The overtime pay
     */
    static double calculateOvertimePay(double overTime, double hourlyRate) {
        return overTime * hourlyRate * 1.25;
    }

    /**
     * Calculates and aggregates monthly totals from a time sheet and employee data.
     *
     * @param timeSheet       List of time log entries for the employee
     * @param monthlytotals   List where the calculated totals will be stored
     * @param emp             Employee data object for salary and allowance details
     */
    public static void calculateMonthlyTotals(
        ArrayList<TimeLogDetails> timeSheet,
        ArrayList<MonthlyTotals> monthlytotals,
        EmployeeData emp) {

    if (timeSheet.isEmpty() || emp == null) return;

    int month = timeSheet.get(0).date.getMonthValue();
    int year = timeSheet.get(0).date.getYear();

    double totalHoursWorked = 0;
    double totalOvertime = 0;

    for (TimeLogDetails log : timeSheet) {
        totalHoursWorked += log.hoursWorked;
        totalOvertime += log.overTime;
    }

    double regularPay = totalHoursWorked * emp.hourlyRate;
    double overtimePay = calculateOvertimePay(totalOvertime, emp.hourlyRate);
    double allowances = calculateMonthlyAllowances(emp);
    double totalPay = regularPay + overtimePay + allowances;

    
    java.time.LocalDate periodStart = java.time.LocalDate.of(year, month, 1);
    java.time.LocalDate periodEnd = periodStart.withDayOfMonth(periodStart.lengthOfMonth());

    
    MonthlyTotals totals = new MonthlyTotals(
        emp.employeeId, emp.firstName, emp.lastName,
        month, year,
        totalHoursWorked, totalOvertime,
        regularPay, overtimePay,
        allowances, totalPay,
        periodStart, periodEnd
    );

    monthlytotals.add(totals);
}
}