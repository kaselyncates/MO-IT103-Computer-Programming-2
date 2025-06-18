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
import com.group.motorph.payroll.utilities.ParseNumber;
import com.opencsv.CSVReader;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderBuilder;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import com.opencsv.exceptions.CsvValidationException;

public class LoadEmployeeData {

    public static void loadEmployeeData(String targetEmployeeId, String employeeDataPath, ArrayList<EmployeeData> employeeData) {
        try (CSVReader reader = new CSVReaderBuilder(new FileReader(employeeDataPath))
                .withCSVParser(new CSVParserBuilder().withSeparator('\t').build())
                .build()) {

            reader.readNext(); // skip header
            String[] fields;
            while ((fields = reader.readNext()) != null) {
                if (!fields[0].equals(targetEmployeeId)) continue;

                employeeData.add(new EmployeeData(
                        fields[0], // ID
                        fields[1], // Last Name
                        fields[2], // First Name
                        fields[3], // Birthday
                        fields[10], // Status
                        fields[11], // Position
                        ParseNumber.parseDouble(fields[14]), // Rice Subsidy
                        ParseNumber.parseDouble(fields[15]), // Phone Allowance
                        ParseNumber.parseDouble(fields[16]), // Clothing Allowance
                        ParseNumber.parseDouble(fields[18])  // Hourly Rate
                ));
            }
        } catch (IOException | CsvValidationException e) {
            System.err.println("Error reading employee data file: " + e.getMessage());
        }
    }

    public static void loadAllEmployees(String employeeDataPath, ArrayList<EmployeeData> allEmployees) {
    try (CSVReader reader = new CSVReaderBuilder(new FileReader(employeeDataPath))
            .withCSVParser(new CSVParserBuilder().withSeparator('\t').build())
            .build()) {

        reader.readNext(); // skip header
        String[] fields;
        while ((fields = reader.readNext()) != null) {
            EmployeeData emp = new EmployeeData(
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

            allEmployees.add(emp);
        }
    } catch (IOException | CsvValidationException e) {
        System.err.println("Error loading all employee data: " + e.getMessage());
    }
}


    public static EmployeeData getFullEmployeeDetails(String targetEmployeeId, String employeeDataPath) {
        try (CSVReader reader = new CSVReaderBuilder(new FileReader(employeeDataPath))
                .withCSVParser(new CSVParserBuilder().withSeparator('\t').build())
                .build()) {

            reader.readNext(); // skip header
            String[] fields;
            while ((fields = reader.readNext()) != null) {
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
                        ParseNumber.parseDouble(fields[14]),
                        ParseNumber.parseDouble(fields[15]),
                        ParseNumber.parseDouble(fields[16]),
                        ParseNumber.parseDouble(fields[18]),
                        ParseNumber.parseDouble(fields[13]), // Basic
                        ParseNumber.parseDouble(fields[17])  // Gross
                );
            }
        } catch (IOException | CsvValidationException e) {
            System.err.println("Error loading full employee data: " + e.getMessage());
        }

        return null;
    }
    
    public static List<EmployeeData> getAllEmployees(String filePath) throws IOException, CsvValidationException
 {
    List<EmployeeData> employees = new ArrayList<>();

    try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
        String[] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            if (nextLine.length < 18) continue; // Skip invalid lines

            EmployeeData emp = new EmployeeData(
                nextLine[0], nextLine[1], nextLine[2], nextLine[3], nextLine[4],
                nextLine[5], nextLine[6], nextLine[7], nextLine[8], nextLine[9],
                nextLine[10], nextLine[11], nextLine[12],
                Double.parseDouble(nextLine[13]),
                Double.parseDouble(nextLine[14]),
                Double.parseDouble(nextLine[15]),
                Double.parseDouble(nextLine[16]),
                Double.parseDouble(nextLine[17])
            );
            employees.add(emp);
        }
    }

    return employees;
}

}
