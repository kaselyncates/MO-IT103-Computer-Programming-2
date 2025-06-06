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
        setTitle("MotorPH Payroll System");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top panel: input and buttons
        JPanel topPanel = new JPanel();
        idField = new JTextField(15);
        JButton calculateButton = new JButton("Calculate Payroll");
        JButton viewAllButton = new JButton("View All Employees");

        topPanel.add(new JLabel("Enter Employee ID:"));
        topPanel.add(idField);
        topPanel.add(calculateButton);
        topPanel.add(viewAllButton);

        // Output area
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Action listeners
        calculateButton.addActionListener(e -> calculatePayroll());
        viewAllButton.addActionListener(e -> viewAllEmployees());

        setVisible(true);
    }

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PayrollGUI::new);
    }
}
