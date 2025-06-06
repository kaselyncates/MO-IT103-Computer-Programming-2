/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group.motorph.payroll.services.government_contributions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import com.group.motorph.payroll.utilities.ParseNumber;

/**
 * Calculates SSS contribution based on gross monthly pay.
 * This uses the SSS contribution table to determine the appropriate contribution amount.
 * 
 * @param grossMonthlyPay The gross monthly pay amount
 * @param sssFilePath The file path of the SSS table
 * @return The calculated SSS contribution amount
 */
public class CalculateSss {

    public static double calculateSss(double grossMonthlyPay, String sssFilePath) {
        double sss = 0.0;

        try (BufferedReader br = new BufferedReader(new FileReader(sssFilePath))) {
            // Skip header
            br.readLine();

            String line;

            // Process each line in the SSS contribution table
            while ((line = br.readLine()) != null) {
                // Split line by tab character to get individual fields
                String[] fields = line.split("\t");

                // Parse salary range and contribution from the table
                int compensationRangeFrom = ParseNumber.parseInt(fields[0]);
                int compensationRangeTo = ParseNumber.parseInt(fields[2]);
                double contribution = ParseNumber.parseDouble(fields[3]);

                // Check if gross monthly pay falls within this compensation range
                if (grossMonthlyPay >= compensationRangeFrom && grossMonthlyPay <= compensationRangeTo) {
                    sss = contribution;
                    break;
                }
            }

            // Special case: If pay exceeds maximum SSS compensation bracket
            if (grossMonthlyPay > 24750) {
                sss = 1125.00;
            }

        } catch (IOException e) {
            System.err.println("Error reading SSS table file: " + e.getMessage());
        }

        return sss;
    }
    
}

