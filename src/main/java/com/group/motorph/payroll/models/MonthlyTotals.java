package com.group.motorph.payroll.models;

import java.time.LocalDate;

public class MonthlyTotals {
    private String employeeId;
    private String firstName;
    private String lastName;
    private int month;
    private int year;

    private double hoursWorked;
    private double overTime;
    private double regularPay;
    private double overtimePay;
    private double allowances;
    private double totalPay;

    private LocalDate payPeriodStart;
    private LocalDate payPeriodEnd;

    // Constructor
    public MonthlyTotals(String employeeId, String firstName, String lastName,
                         int month, int year,
                         double hoursWorked, double overTime,
                         double regularPay, double overtimePay,
                         double allowances, double totalPay,
                         LocalDate payPeriodStart, LocalDate payPeriodEnd) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.month = month;
        this.year = year;
        this.hoursWorked = hoursWorked;
        this.overTime = overTime;
        this.regularPay = regularPay;
        this.overtimePay = overtimePay;
        this.allowances = allowances;
        this.totalPay = totalPay;
        this.payPeriodStart = payPeriodStart;
        this.payPeriodEnd = payPeriodEnd;
    }

    // Getters (add these if missing)
    public double getHoursWorked() {
        return hoursWorked;
    }

    public double getOverTime() {
        return overTime;
    }

    public LocalDate getPayPeriodStart() {
        return payPeriodStart;
    }

    public LocalDate getPayPeriodEnd() {
        return payPeriodEnd;
    }
}
