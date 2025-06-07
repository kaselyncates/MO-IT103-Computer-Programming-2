/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group.motorph.payroll.models;

/**
 *
 * @author MO-IT103 | CP2 | S1101
 * 
 */

/**
 * The EmployeeData class stores and manages employee information in the MotorPH
 * payroll system. It contains personal details and compensation information for
 * each employee.
*
 */
public class EmployeeData {

    // Employee identification and personal information
    /**
     * Unique identifier assigned to each employee within the organization. This
     * ID is used as the primary reference in all payroll calculations and
     * reports.
     */
    public String employeeId;

    /**
     * Employee's legal last name as it appears on official documents.
     */
    public String lastName;

    /**
     * Employee's legal first name as it appears on official documents.
     */
    public String firstName;

    /**
     * Employee's date of birth in the format YYYY-MM-DD. This is used for age
     * verification and benefits eligibility.
     */
    public String birthday;

    // Employment details
    /**
     * Current employment status of the employee. Valid values include "Regular"
     * and "Probationary". This status affects benefit eligibility and job
     * security.
     */
    public String status;

    /**
     * Employee's current job title or position within the company. This
     * determines salary range and responsibilities.
     */
    public String position;

    // Employee benefits and compensation details
    /**
     * Monthly rice subsidy amount in Philippine Pesos. This is a standard
     * benefit provided to eligible employees.
     */
    public double riceSubsidy;

    /**
     * Monthly phone allowance in Philippine Pesos. This amount is provided to
     * cover work-related communication expenses.
     */
    public double phoneAllowance;

    /**
     * Monthly clothing allowance in Philippine Pesos. This is provided to
     * maintain professional appearance according to company standards.
     */
    public double clothingAllowance;

    /**
     * The employee's base pay rate per hour in Philippine Pesos. This rate is
     * used to calculate regular wages before deductions and benefits.
     */
    public double hourlyRate;

    // The residential address of the employee
    public String address;
    
    // The name or ID of the employee's immediate supervisor
    public String supervisor;
    
    // The contact phone number of the employee
    public String phoneNumber;
    
    // Tax Identification Number assigned by the government
    public String tin;
    
    // Social Security System number for employee contributions
    public String sss;
    
    // PhilHealth identification number for health insurance
    public String philHealth;
    
    // Pag-IBIG Fund membership number for housing savings
    public String pagIbig;
    
    // The employee's base monthly salary before any additions or deductions
    public double basicSalary;
    
    // The total salary including basic salary and all allowances before deductions
    public double grossSalary;
    
    /**
     * Constructs a new EmployeeData object with the specified parameters. This
     * constructor initializes all fields with the provided values.
     *
     * @param employeeId The unique identifier for the employee
     * @param lastName The employee's last name
     * @param firstName The employee's first name
     * @param birthday The employee's date of birth
     * @param status The employee's employment status
     * @param position The employee's job position or title
     * @param riceSubsidy The monthly rice subsidy amount
     * @param phoneAllowance The monthly phone allowance amount
     * @param clothingAllowance The monthly clothing allowance amount
     * @param hourlyRate The employee's hourly pay rate
     */
    
    public EmployeeData(String employeeId, String lastName, String firstName, String birthday, String status, String position,
            double riceSubsidy, double phoneAllowance, double clothingAllowance, double hourlyRate) {
        this.employeeId = employeeId;
        this.lastName = lastName;
        this.firstName = firstName;
        this.birthday = birthday;
        this.status = status;
        this.position = position;
        this.riceSubsidy = riceSubsidy;
        this.phoneAllowance = phoneAllowance;
        this.clothingAllowance = clothingAllowance;
        this.hourlyRate = hourlyRate;
    }
    
    public EmployeeData(String employeeId, String lastName, String firstName, String birthday,
                    String address, String status, String position, String supervisor,
                    String phoneNumber, String tin, String sss, String philHealth, String pagIbig,
                    double riceSubsidy, double phoneAllowance, double clothingAllowance,
                    double hourlyRate, double basicSalary, double grossSalary) {

        this.employeeId = employeeId;
        this.lastName = lastName;
        this.firstName = firstName;
        this.birthday = birthday;
        this.address = address;
        this.status = status;
        this.position = position;
        this.supervisor = supervisor;
        this.phoneNumber = phoneNumber;
        this.tin = tin;
        this.sss = sss;
        this.philHealth = philHealth;
        this.pagIbig = pagIbig;
        this.riceSubsidy = riceSubsidy;
        this.phoneAllowance = phoneAllowance;
        this.clothingAllowance = clothingAllowance;
        this.hourlyRate = hourlyRate;
        this.basicSalary = basicSalary;
        this.grossSalary = grossSalary;
    }

    public String viewEmployeeRecord() {
    return String.format(
        "Employee ID: %s\n" +
        "Name: %s %s\n" +
        "Birthday: %s\n" +
        "Address: %s\n" +
        "Status: %s\n" +
        "Position: %s\n" +
        "Supervisor: %s\n" +
        "Phone Number: %s\n" +
        "TIN: %s\n" +
        "SSS: %s\n" +
        "PhilHealth: %s\n" +
        "Pag-IBIG: %s\n" +
        "Rice Subsidy: %.2f\n" +
        "Phone Allowance: %.2f\n" +
        "Clothing Allowance: %.2f\n" +
        "Hourly Rate: %.2f\n" +
        "Basic Salary: %.2f\n" +
        "Gross Salary: %.2f",
        employeeId, firstName, lastName, birthday, address, status, position, supervisor,
        phoneNumber, tin, sss, philHealth, pagIbig, riceSubsidy, phoneAllowance, clothingAllowance,
        hourlyRate, basicSalary, grossSalary
    );
}

    /**
     * Returns a string representation of the EmployeeData object. This method
     * formats all employee information into a readable string that can be used
     * for logging, debugging, or display purposes.
     *
     * @return A formatted string containing all employee data fields
     */
    @Override
    public String toString() {
        return String.format("Employee ID: %s, Last Name: %s, First Name: %s, Birthday: %s, Status: %s, Position: %s, Rice Subsidy: %.2f, Phone Allowance: %.2f, Clothing Allowance: %.2f, Hourly Rate: %.2f",
                employeeId, lastName, firstName, birthday, status, position, riceSubsidy, phoneAllowance, clothingAllowance, hourlyRate);
    }
    
    
}
