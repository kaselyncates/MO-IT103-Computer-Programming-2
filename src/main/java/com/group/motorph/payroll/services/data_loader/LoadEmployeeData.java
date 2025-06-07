/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author MO-IT103 | CP2 | S1101
 * 
 */

package com.group.motorph.payroll.services.data_loader;

import com.group.motorph.payroll.models.EmployeeData;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import com.group.motorph.payroll.utilities.ParseNumber;


public class LoadEmployeeData {

    public static void loadEmployeeData(String targetEmployeeId, String employeeDataPath, ArrayList<EmployeeData> employeeData) {
        try (BufferedReader br = new BufferedReader(new FileReader(employeeDataPath))) {
            br.readLine(); // Skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split("\t");
                String employeeId = fields[0];
                if (!employeeId.equals(targetEmployeeId)) continue;

                String lastName = fields[1];
                String firstName = fields[2];
                String birthday = fields[3];
                String status = fields[10];
                String position = fields[11];
                double riceSubsidy = ParseNumber.parseDouble(fields[14]);
                double phoneAllowance = ParseNumber.parseDouble(fields[15]);
                double clothingAllowance = ParseNumber.parseDouble(fields[16]);
                double hourlyRate = ParseNumber.parseDouble(fields[18]);

                EmployeeData entry = new EmployeeData(employeeId, lastName, firstName, birthday, status, position,
                        riceSubsidy, phoneAllowance, clothingAllowance, hourlyRate);

                employeeData.add(entry);
            }
        } catch (IOException e) {
            System.err.println("Error reading employee data file: " + e.getMessage());
        }
    }

    // TO VIEW ALL EMPLOYEES
    public static void loadAllEmployees(String employeeDataPath, ArrayList<EmployeeData> allEmployees) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(employeeDataPath))) {
            br.readLine(); // skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split("\t");

                String employeeId = fields[0];
                String lastName = fields[1];
                String firstName = fields[2];
                String birthday = fields[3];
                String status = fields[10];
                String position = fields[11];
                double riceSubsidy = ParseNumber.parseDouble(fields[14]);
                double phoneAllowance = ParseNumber.parseDouble(fields[15]);
                double clothingAllowance = ParseNumber.parseDouble(fields[16]);
                double hourlyRate = ParseNumber.parseDouble(fields[18]);

                EmployeeData entry = new EmployeeData(employeeId, lastName, firstName, birthday, status, position,
                        riceSubsidy, phoneAllowance, clothingAllowance, hourlyRate);

                allEmployees.add(entry);
            }
        }
    }
    
    public static EmployeeData getFullEmployeeDetails(String targetEmployeeId, String employeeDataPath) {
    try (BufferedReader br = new BufferedReader(new FileReader(employeeDataPath))) {
        br.readLine(); // skip header
        String line;

        while ((line = br.readLine()) != null) {
            String[] fields = line.split("\t");
            if (!fields[0].equals(targetEmployeeId)) continue;

            return new EmployeeData(
                fields[0], // ID
                fields[1], // Last Name
                fields[2], // First Name
                fields[3], // Birthday
                fields[4], // Address
                fields[10], // Status
                fields[11], // Position
                fields[12], // Supervisor
                fields[5], // Phone
                fields[8], // TIN
                fields[6], // SSS
                fields[7], // PhilHealth
                fields[9], // Pag-IBIG
                ParseNumber.parseDouble(fields[14]), // Rice Subsidy
                ParseNumber.parseDouble(fields[15]), // Phone Allowance
                ParseNumber.parseDouble(fields[16]), // Clothing Allowance
                ParseNumber.parseDouble(fields[18]), // Hourly Rate
                ParseNumber.parseDouble(fields[13]), // Basic Salary
                ParseNumber.parseDouble(fields[17])  // Gross Salary
            );
        }
    } catch (IOException e) {
        System.err.println("Error loading full employee data: " + e.getMessage());
    }
    return null; // if not found or error
}

}
