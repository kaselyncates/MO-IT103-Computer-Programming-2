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
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    // Group logs by YearMonth (e.g., June 2025)
    Map<YearMonth, List<TimeLogDetails>> groupedByMonth = timeSheet.stream()
        .collect(Collectors.groupingBy(log -> YearMonth.from(log.date)));

    for (Map.Entry<YearMonth, List<TimeLogDetails>> entry : groupedByMonth.entrySet()) {
        YearMonth ym = entry.getKey();
        List<TimeLogDetails> logsInMonth = entry.getValue();

        double totalHoursWorked = logsInMonth.stream()
            .mapToDouble(log -> log.hoursWorked)
            .sum();

        double totalOvertime = logsInMonth.stream()
            .mapToDouble(log -> log.overTime)
            .sum();

        double regularPay = totalHoursWorked * emp.hourlyRate;
        double overtimePay = calculateOvertimePay(totalOvertime, emp.hourlyRate);
        double allowances = calculateMonthlyAllowances(emp);
        double totalPay = regularPay + overtimePay + allowances;

        LocalDate periodStart = logsInMonth.stream()
            .map(log -> log.date)
            .min(LocalDate::compareTo)
            .orElse(ym.atDay(1));

        LocalDate periodEnd = logsInMonth.stream()
            .map(log -> log.date)
            .max(LocalDate::compareTo)
            .orElse(ym.atEndOfMonth());

        MonthlyTotals totals = new MonthlyTotals(
            emp.employeeId, emp.firstName, emp.lastName,
            ym.getMonthValue(), ym.getYear(),
            totalHoursWorked, totalOvertime,
            regularPay, overtimePay,
            allowances, totalPay,
            periodStart, periodEnd
        );

        monthlytotals.add(totals);
    }
}
}