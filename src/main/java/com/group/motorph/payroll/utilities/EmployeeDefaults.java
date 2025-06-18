/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author KC
 */

//DEFAULYS FOR CREATING NEW EMPLOYEE
package com.group.motorph.payroll.utilities;

public class EmployeeDefaults {

    // Returns the default phone allowance for the given position
    public static double getPhoneAllowance(String position) {
        switch (position) {
            case "Chief Finance Officer":
            case "Chief Marketing Officer":
                return 2000;
            case "IT Operations and Systems":
            case "HR Manager":
            case "Accounting Head":
            case "Payroll Manager":
            case "Account Manager":
            case "Sales & Marketing":
            case "Supply Chain and Logistics":
            case "Customer Service and Relations":
                return 1000;
            case "HR Team Leader":
            case "Payroll Team Leader":
            case "Account Team Leader":
                return 800;
            default:
                return 500;
        }
    }

    // Returns the default clothing allowance for the given position
    public static double getClothingAllowance(String position) {
        switch (position) {
            case "Chief Finance Officer":
            case "Chief Marketing Officer":
            case "IT Operations and Systems":
            case "HR Manager":
            case "Accounting Head":
            case "Payroll Manager":
            case "Account Manager":
            case "Sales & Marketing":
            case "Supply Chain and Logistics":
            case "Customer Service and Relations":
                return 1000;
            case "HR Team Leader":
            case "Payroll Team Leader":
            case "Account Team Leader":
                return 800;
            default:
                return 500;
        }
    }
}
