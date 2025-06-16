/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author KC
 */

package com.group.motorph.payroll.utils;

import com.opencsv.CSVWriter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TSVtoCSVConverter {

    public static void convert(String tsvPath, String csvPath) {
        try (
            BufferedReader reader = new BufferedReader(new FileReader(tsvPath));
            CSVWriter writer = new CSVWriter(new FileWriter(csvPath))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Split using tab character
                String[] columns = line.split("\t");
                writer.writeNext(columns);
            }
            System.out.println("Converted TSV to CSV successfully.");
        } catch (IOException e) {
            System.err.println("Error during conversion: " + e.getMessage());
        }
    }
}

