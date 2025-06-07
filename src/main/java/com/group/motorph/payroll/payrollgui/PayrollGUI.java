/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author MO-IT103 | CP2 | S1101
 * 
 */

package com.group.motorph.payroll.payrollgui;

import com.group.motorph.payroll.models.EmployeeData;
import com.group.motorph.payroll.models.TimeLogDetails;
import com.group.motorph.payroll.models.MonthlyTotals;
import com.group.motorph.payroll.services.data_loader.LoadEmployeeData;
import com.group.motorph.payroll.services.data_loader.LoadTimeSheet;
import com.group.motorph.payroll.services.payroll_calculations.PayrollCalculations;
import com.group.motorph.payroll.services.payroll_calculations.CalculateAndDisplay;
import javax.swing.*;
import java.awt.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.io.IOException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField; 
import javax.swing.JButton;    

public class PayrollGUI extends JFrame {

    private final ArrayList<EmployeeData> employeeData = new ArrayList<>();
    private final ArrayList<TimeLogDetails> timeSheet = new ArrayList<>();
    private final ArrayList<MonthlyTotals> monthlytotals = new ArrayList<>();

    private JTextField idField;
    private JTextArea resultArea;

    private static final String EMPLOYEE_DATA_PATH = Paths.get("src", "main", "java", "com", "group", "motorph", "resources", "employee-data.tsv").toString();
    private static final String ATTENDANCE_RECORD_PATH = Paths.get("src", "main", "java", "com", "group", "motorph", "resources", "attendance-record.csv").toString();
    private static final String SSS_TABLE_PATH = Paths.get("src", "main", "java", "com", "group", "motorph", "resources", "sss-contribution-table.tsv").toString();

    public PayrollGUI() {
        // Main window title and size
        setTitle("MotorPH Payroll System");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top panel: input and buttons
        JPanel topPanel = new JPanel();
        idField = new JTextField(15);
        JButton calculateButton = new JButton("Calculate Payroll");
        JButton viewAllButton = new JButton("View All Employees");
        JButton viewRecordButton = new JButton("View Employee Record");
        
        topPanel.add(new JLabel("Enter Employee ID:"));
        topPanel.add(idField);
        topPanel.add(viewRecordButton);
        topPanel.add(calculateButton);
        topPanel.add(viewAllButton);

        // Output area
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Add button actions
        calculateButton.addActionListener(e -> calculatePayroll());
        viewRecordButton.addActionListener(e -> viewEmployeeRecord());
        viewAllButton.addActionListener(e -> viewAllEmployees());
        viewRecordButton.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        String empIdInput = idField.getText().trim();

        if (!empIdInput.isEmpty()) {
            try {
                // Load the employee data using your new method
                EmployeeData emp = LoadEmployeeData.getFullEmployeeDetails(empIdInput, "src/data/EmployeeData.tsv");

                if (emp != null) {
                    resultArea.setText(emp.viewEmployeeRecord()); // Display full record
                } else {
                    resultArea.setText("Employee ID not found.");
                }

            } catch (Exception ex) {
                resultArea.setText("Error loading employee data: " + ex.getMessage());
            }
        } else {
            resultArea.setText("Please enter an Employee ID.");
        }
    }
});


        setVisible(true);
    }

    //Calculate Payroll of a specific employee
    private void calculatePayroll() {
        employeeData.clear();
        timeSheet.clear();
        monthlytotals.clear();

        String employeeId = idField.getText().trim();
        if (employeeId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an Employee ID.");
            return;
        }

        try {
            LoadEmployeeData.loadEmployeeData(employeeId, EMPLOYEE_DATA_PATH, employeeData);

            if (employeeData.isEmpty()) {
                resultArea.setText("No employee found with ID: " + employeeId);
                return;
            }

            LoadTimeSheet.loadTimeSheet(employeeId, ATTENDANCE_RECORD_PATH, timeSheet);
            PayrollCalculations.calculateMonthlyTotals(timeSheet, monthlytotals, employeeData.get(0));

            StringBuilder output = new StringBuilder();
            for (EmployeeData emp : employeeData) {
                output.append("Employee ID: ").append(emp.employeeId).append("\n");
                output.append("Name: ").append(emp.firstName).append(" ").append(emp.lastName).append("\n");
                output.append("Birthday: ").append(emp.birthday).append("\n");
                output.append("Position: ").append(emp.position).append("\n");
                output.append("Status: ").append(emp.status).append("\n\n");
            }

            output.append(CalculateAndDisplay.getSalaryBreakdown(monthlytotals, employeeData, SSS_TABLE_PATH));
            resultArea.setText(output.toString());

        } catch (Exception ex) {
            resultArea.setText("Error calculating payroll: " + ex.getMessage());
        }
    }

    //view all employee records
    private void viewAllEmployees() {
        try {
            ArrayList<EmployeeData> allEmployees = new ArrayList<>();
            LoadEmployeeData.loadAllEmployees(EMPLOYEE_DATA_PATH, allEmployees);

            if (allEmployees.isEmpty()) {
                resultArea.setText("No employee records found.");
                return;
            }

            StringBuilder output = new StringBuilder("All Employees:\n\n");
            for (EmployeeData emp : allEmployees) {
                output.append("Employee ID: ").append(emp.employeeId).append("\n");
                output.append("Name: ").append(emp.firstName).append(" ").append(emp.lastName).append("\n");
                output.append("Birthday: ").append(emp.birthday).append("\n");
                output.append("Position: ").append(emp.position).append("\n");
                output.append("Status: ").append(emp.status).append("\n");
                output.append("--------------------------\n");
            }

            resultArea.setText(output.toString());

        } catch (IOException e) {
            resultArea.setText("Error loading employee data: " + e.getMessage());
        }
    }
    
    //view a specific employee record
    private void viewEmployeeRecord() {
    employeeData.clear();
    String employeeId = idField.getText().trim();

    if (employeeId.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please enter an Employee ID.");
        return;
    }

    try {
        // Load full employee details
        EmployeeData emp = LoadEmployeeData.getFullEmployeeDetails(employeeId, EMPLOYEE_DATA_PATH);

        if (emp == null) {
            resultArea.setText("No employee found with ID: " + employeeId);
            return;
        }

        StringBuilder output = new StringBuilder();

        output.append("Employee ID: ").append(emp.employeeId).append("\n");
        output.append("Name: ").append(emp.firstName).append(" ").append(emp.lastName).append("\n");
        output.append("Birthday: ").append(emp.birthday).append("\n");
        output.append("Address: ").append(emp.address).append("\n");
        output.append("Phone Number: ").append(emp.phoneNumber).append("\n\n");
        
        output.append("Status: ").append(emp.status).append("\n");
        output.append("Position: ").append(emp.position).append("\n");
        output.append("Supervisor: ").append(emp.supervisor).append("\n\n");
        
        output.append("TIN: ").append(emp.tin).append("\n");
        output.append("SSS: ").append(emp.sss).append("\n");
        output.append("PhilHealth: ").append(emp.philHealth).append("\n");
        output.append("Pag-IBIG: ").append(emp.pagIbig).append("\n\n");
        
        output.append(String.format("Rice Subsidy: PHP %.2f\n", emp.riceSubsidy));
        output.append(String.format("Phone Allowance: PHP %.2f\n", emp.phoneAllowance));
        output.append(String.format("Clothing Allowance: PHP %.2f\n\n", emp.clothingAllowance));
        
        output.append(String.format("Hourly Rate: PHP %.2f\n", emp.hourlyRate));
        output.append(String.format("Basic Salary: PHP %.2f\n\n", emp.basicSalary));
        
        output.append(String.format("Gross Salary: PHP %.2f\n\n", emp.grossSalary));

        resultArea.setText(output.toString());

    } catch (Exception ex) {
        resultArea.setText("Error loading employee record: " + ex.getMessage());
    }
}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PayrollGUI::new);
    }
}
