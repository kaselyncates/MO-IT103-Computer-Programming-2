/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author KC
 */

package com.group.motorph.payroll.services.data_writer;

import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import com.group.motorph.payroll.models.EmployeeData;


/**
 * Utility class for writing employee data to a TSV (tab-separated values) file.
 * This class uses OpenCSV's CSVWriter with custom settings to avoid quotation marks
 * and escape characters, and uses tab as a delimiter.
 */
public class EmployeeFileWriter {

    /**
     * Appends a new employee record to the specified employee data file.
     *
     * @param filePath The path to the employee data file (should be .tsv format).
     * @param fields   The array of employee details to be written as a row.
     * @throws IOException If an I/O error occurs while writing to the file.
     */
    public static void appendEmployee(String filePath, String[] fields) throws IOException {
        try (CSVWriter writer = new CSVWriter(
                new FileWriter(filePath, true),           // Append mode
                '\t',                                      // Tab delimiter for TSV
                CSVWriter.NO_QUOTE_CHARACTER,              // No quote characters
                CSVWriter.NO_ESCAPE_CHARACTER,             // No escape characters
                System.lineSeparator()                     // Use platform-specific new line
        )) {
            writer.writeNext(fields);
        }
    }
    
    public static void overwriteEmployeeData(String filePath, List<EmployeeData> employees) throws IOException {
    try (CSVWriter writer = new CSVWriter(new FileWriter(filePath, false), '\t',
            CSVWriter.NO_QUOTE_CHARACTER,
            CSVWriter.DEFAULT_ESCAPE_CHARACTER,
            CSVWriter.DEFAULT_LINE_END)) {

      

        for (EmployeeData emp : employees) {
            String[] row = {
                emp.getEmployeeId(),
                emp.getLastName(),
                emp.getFirstName(),
                emp.getBirthday(),
                emp.getAddress(),
                emp.getPhoneNumber(),
                emp.getSssNumber(),
                emp.getPhilHealthNumber(),
                emp.getPagIbigNumber(),
                emp.getTinNumber(),
                emp.getStatus(),
                emp.getPosition(),
                emp.getSupervisor(),
                String.format("%.2f", emp.getBasicSalary()),
                String.format("%.2f", emp.getRiceSubsidy()),
                String.format("%.2f", emp.getPhoneAllowance()),
                String.format("%.2f", emp.getClothingAllowance()),
                String.format("%.2f", emp.getHourlyRate()),
                String.format("%.2f", emp.getGrossSalary())
            };
            writer.writeNext(row);
        }
    }
}
}



