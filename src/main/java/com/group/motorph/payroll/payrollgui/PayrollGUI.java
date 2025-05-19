/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group.motorph.payroll.payrollgui;

import com.group.motorph.payroll.models.EmployeeData;
import com.group.motorph.payroll.models.TimeLogDetails;
import com.group.motorph.payroll.models.WeeklyTotals;
import com.group.motorph.payroll.services.data_loader.LoadEmployeeData;
import com.group.motorph.payroll.services.data_loader.LoadTimeSheet;
import com.group.motorph.payroll.services.payroll_calculations.PayrollCalculations;
import com.group.motorph.payroll.services.payroll_calculations.CalculateAndDisplay;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 *
 * @author KC
 */
public class PayrollGUI extends JFrame {

    private final ArrayList<EmployeeData> employeeData = new ArrayList<>();
    private final ArrayList<TimeLogDetails> timeSheet = new ArrayList<>();
    private final ArrayList<WeeklyTotals> weeklyTotals = new ArrayList<>();

    private JTextField idField;
    private JTextArea resultArea;

    private static final String EMPLOYEE_DATA_PATH = Paths.get("src", "main", "java", "com", "group", "motorph", "resources", "employee-data.tsv").toString();
    private static final String ATTENDANCE_RECORD_PATH = Paths.get("src", "main", "java", "com", "group", "motorph", "resources", "attendance-record.csv").toString();
    private static final String SSS_TABLE_PATH = Paths.get("src", "main", "java", "com", "group", "motorph", "resources", "sss-contribution-table.tsv").toString();

    public PayrollGUI() {
        setTitle("MotorPH Payroll System");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        idField = new JTextField(15);
        JButton calculateButton = new JButton("Calculate Payroll");
        topPanel.add(new JLabel("Enter Employee ID:"));
        topPanel.add(idField);
        topPanel.add(calculateButton);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        calculateButton.addActionListener(e -> calculatePayroll());

        setVisible(true);
    }

    private void calculatePayroll() {
        // Clear previous data
        employeeData.clear();
        timeSheet.clear();
        weeklyTotals.clear();

        String employeeId = idField.getText().trim();
        if (employeeId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an Employee ID.");
            return;
        }

        try {
            LoadEmployeeData.loadEmployeeData(employeeId, EMPLOYEE_DATA_PATH, employeeData);
            LoadTimeSheet.loadTimeSheet(employeeId, ATTENDANCE_RECORD_PATH, timeSheet);
            PayrollCalculations.calculateWeeklyTotals(timeSheet, weeklyTotals);

            if (employeeData.isEmpty()) {
                resultArea.setText("No employee found with ID: " + employeeId);
                return;
            }

            StringBuilder output = new StringBuilder();
            for (EmployeeData emp : employeeData) {
                output.append("Employee ID: ").append(emp.employeeId).append("\n");
                output.append("Name: ").append(emp.firstName).append(" ").append(emp.lastName).append("\n");
                output.append("Birthday: ").append(emp.birthday).append("\n");
                output.append("Position: ").append(emp.position).append("\n");
                output.append("Status: ").append(emp.status).append("\n\n");
            }

            output.append(CalculateAndDisplay.getSalaryBreakdown(weeklyTotals, employeeData, SSS_TABLE_PATH));

            resultArea.setText(output.toString());

        } catch (Exception ex) {
            resultArea.setText("Error calculating payroll: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PayrollGUI::new);
    }
}