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

    // ✅ FULLY IMPLEMENTED VERSION TO VIEW ALL EMPLOYEES
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
}
