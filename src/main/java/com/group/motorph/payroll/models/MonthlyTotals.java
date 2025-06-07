/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author MO-IT103 | CP2 | S1101
 * 
 */

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

    // Getters
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
