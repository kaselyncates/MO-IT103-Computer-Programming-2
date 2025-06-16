/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.motorphpayroll;

/**
 *
 * @author KC
 */

import com.opencsv.CSVReader;
import java.io.FileReader;

public class CSVTest {
    public static void main(String[] args) {
        String path = "src/main/java/com/group/motorph/resources/employee-data.tsv"; // Adjust if needed

        try (CSVReader reader = new CSVReader(new FileReader(path))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                for (String cell : line) {
                    System.out.print(cell + " | ");
                }
                System.out.println();
            }
        } catch (Exception e) {
            System.err.println("Error reading CSV: " + e.getMessage());
        }
    }
}
