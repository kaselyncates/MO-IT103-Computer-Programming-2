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

import com.group.motorph.payroll.models.TimeLogDetails;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Locale;

public class LoadTimeSheet {

    // Date formatter for "MM/dd/yyyy"
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    // Time formatter for "H:mm"
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("H:mm");

    // Store working hours
    private static final LocalTime STORE_OPENING_TIME = LocalTime.of(8, 0);
    private static final LocalTime STORE_CLOSING_TIME = LocalTime.of(17, 0);

    // 10-minute grace period
    private static final Duration GRACE_PERIOD = Duration.ofMinutes(10);
    private static final double GRACE_PERIOD_HOURS = GRACE_PERIOD.toMinutes() / 60.0;

    /**
     * Load time logs for a specific employee from a CSV attendance file.
     *
     * @param targetEmployeeId     The employee ID to load data for.
     * @param attendanceRecordPath Path to the CSV file.
     * @param timeSheet            The ArrayList to populate with parsed records.
     */
    public static void loadTimeSheet(String targetEmployeeId, String attendanceRecordPath, ArrayList<TimeLogDetails> timeSheet) {
        try (CSVReader reader = new CSVReader(new FileReader(attendanceRecordPath))) {
            String[] fields;
            reader.readNext(); // Skip header row

            while ((fields = reader.readNext()) != null) {
                String employeeId = fields[0];

                if (!employeeId.equals(targetEmployeeId)) {
                    continue; // Skip if not the target employee
                }

                String lastName = fields[1];
                String firstName = fields[2];
                LocalDate date = LocalDate.parse(fields[3], DATE_FORMATTER);
                LocalTime logIn = LocalTime.parse(fields[4], TIME_FORMATTER);
                LocalTime logOut = LocalTime.parse(fields[5], TIME_FORMATTER);

                double hoursWorked;
                double overTime = 0;

                Duration lateDuration = Duration.between(STORE_OPENING_TIME, logIn);
                double lateHours = lateDuration.toMinutes() / 60.0;

                if (lateHours <= GRACE_PERIOD_HOURS) {
                    Duration workedDuration = Duration.between(STORE_OPENING_TIME, logOut);
                    hoursWorked = (workedDuration.toMinutes() / 60.0) - 1; // subtract lunch
                    if (hoursWorked > 8.0) {
                        overTime = hoursWorked - 8.0;
                    }
                } else {
                    Duration workedDuration = Duration.between(logIn, STORE_CLOSING_TIME);
                    hoursWorked = (workedDuration.toMinutes() / 60.0) - 1; // subtract lunch
                }

                int weekNumber = date.get(WeekFields.of(Locale.getDefault()).weekOfYear());

                TimeLogDetails entry = new TimeLogDetails(
                        employeeId, lastName, firstName, date,
                        logIn, logOut, hoursWorked, overTime, weekNumber
                );

                timeSheet.add(entry);
            }

        } catch (IOException | CsvValidationException e) {
            System.err.println("Error reading time log CSV: " + e.getMessage());
        }
    }
}
