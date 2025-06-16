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
import java.util.List;


public class PayrollGUI extends JFrame {

    private final ArrayList<EmployeeData> employeeData = new ArrayList<>();
    private final ArrayList<TimeLogDetails> timeSheet = new ArrayList<>();
    private final ArrayList<MonthlyTotals> monthlytotals = new ArrayList<>();

    private JTextField idField;
    private JTextArea resultArea;
    
    private JTable employeeTable;
    private JScrollPane tableScrollPane;
    private String selectedEmployeeId = null;  // stores the selected Employee ID


    private static final String EMPLOYEE_DATA_PATH = Paths.get("src", "main", "java", "com", "group", "motorph", "resources", "employee-data.tsv").toString();
    private static final String ATTENDANCE_RECORD_PATH = Paths.get("src", "main", "java", "com", "group", "motorph", "resources", "attendance-record.csv").toString();
    private static final String SSS_TABLE_PATH = Paths.get("src", "main", "java", "com", "group", "motorph", "resources", "sss-contribution-table.tsv").toString();

    
    public PayrollGUI() {
        // Main window title and size
        setTitle("MotorPH Payroll System");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top panel: user inputs employee id number
        JPanel topPanel = new JPanel();
        idField = new JTextField(15);
        topPanel.add(new JLabel("Enter Employee ID:"));
        topPanel.add(idField);
       
        //to view specific employee's complete details
        JButton viewRecordButton = new JButton("View Employee Record");
        topPanel.add(viewRecordButton);
        
        //to calculate a specific employee's payroll
        JButton calculateButton = new JButton("Calculate Payroll");
        topPanel.add(calculateButton);
        
        //to view all employee details
        JButton viewAllButton = new JButton("View All Employees");
        topPanel.add(viewAllButton);

       // Output area: text and table views
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        JScrollPane textScrollPane = new JScrollPane(resultArea);

        employeeTable = new JTable(); // initially empty
        tableScrollPane = new JScrollPane(employeeTable);

        // Use CardLayout to switch between text and table
        JPanel displayPanel = new JPanel(new CardLayout());
        displayPanel.add(textScrollPane, "TEXT");
        displayPanel.add(tableScrollPane, "TABLE");

        add(topPanel, BorderLayout.NORTH);
        add(displayPanel, BorderLayout.CENTER);

        // Add button actions
        calculateButton.addActionListener(e -> calculatePayroll());
        viewRecordButton.addActionListener(e -> viewEmployeeRecord());
        viewAllButton.addActionListener(e -> viewAllEmployees());
        
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

        // Prompt for month using dropdown menu
        String[] months = {
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
            };

        JComboBox<String> monthComboBox = new JComboBox<>(months);
            int option = JOptionPane.showConfirmDialog(
            this,
            monthComboBox,
            "Select Month",
            JOptionPane.OK_CANCEL_OPTION
            );

        if (option != JOptionPane.OK_OPTION) {
            resultArea.setText("No month selected.");
            return;
            }

        String selectedMonth = (String) monthComboBox.getSelectedItem();

        // Filter monthly totals by selected month
        ArrayList<MonthlyTotals> filteredMonthlyTotals = new ArrayList<>();
        for (MonthlyTotals mt : monthlytotals) {
            if (mt.getMonthName().equalsIgnoreCase(selectedMonth)) {
                filteredMonthlyTotals.add(mt);
            }
        }

        // Display employee details
        StringBuilder output = new StringBuilder();
        for (EmployeeData emp : employeeData) {
            output.append("Employee ID: ").append(emp.employeeId).append("\n");
            output.append("Name: ").append(emp.firstName).append(" ").append(emp.lastName).append("\n");
            output.append("Birthday: ").append(emp.birthday).append("\n");
            output.append("Position: ").append(emp.position).append("\n");
            output.append("Status: ").append(emp.status).append("\n\n");
        }

        if (filteredMonthlyTotals.isEmpty()) {
            resultArea.setText("No payroll records found for " + selectedMonth + ".");
            return;
        }

        output.append("Payroll for Month: ").append(selectedMonth).append("\n\n");
        output.append(CalculateAndDisplay.getSalaryBreakdown(filteredMonthlyTotals, employeeData, SSS_TABLE_PATH));

        resultArea.setText(output.toString());

    } catch (Exception ex) {
        resultArea.setText("Error calculating payroll: " + ex.getMessage());
    }
}


    //view all employee records
    private void viewAllEmployees() {
    ArrayList<EmployeeData> allEmployees = new ArrayList<>();
    LoadEmployeeData.loadAllEmployees(EMPLOYEE_DATA_PATH, allEmployees);

    if (allEmployees.isEmpty()) {
        // Show text view and display message
        CardLayout cl = (CardLayout)((JPanel)getContentPane().getComponent(1)).getLayout();
        cl.show((JPanel)getContentPane().getComponent(1), "TEXT");
        resultArea.setText("No employee records found.");
        return;
    }

    // Define table column names
    String[] columnNames = {"Employee ID", "Last Name", "First Name", "SSS", "PhilHealth", "TIN", "Pag-IBIG"};

    // Fill table rows
    String[][] rowData = new String[allEmployees.size()][7];
    for (int i = 0; i < allEmployees.size(); i++) {
        EmployeeData emp = allEmployees.get(i);
        rowData[i][0] = emp.employeeId;
        rowData[i][1] = emp.lastName;
        rowData[i][2] = emp.firstName;
        rowData[i][3] = emp.sss;
        rowData[i][4] = emp.philHealth;
        rowData[i][5] = emp.tin;
        rowData[i][6] = emp.pagIbig;
    }

    // Create the JTable
    employeeTable = new JTable(rowData, columnNames);
    employeeTable.setFillsViewportHeight(true);
    
    // Track selected row in the table
    employeeTable.getSelectionModel().addListSelectionListener(e -> {
    int selectedRow = employeeTable.getSelectedRow();
    if (selectedRow >= 0) {
        selectedEmployeeId = (String) employeeTable.getValueAt(selectedRow, 0); // column 0 = Employee ID
    }
});

    // Put the table inside the scroll pane
    tableScrollPane.setViewportView(employeeTable);

    // Show the table view
    CardLayout cl = (CardLayout)((JPanel)getContentPane().getComponent(1)).getLayout();
    cl.show((JPanel)getContentPane().getComponent(1), "TABLE");
}


    
    //view a specific employee record
    private void viewEmployeeRecord() {
    employeeData.clear();
    String employeeId = idField.getText().trim();

    // Fallback to selectedEmployeeId if text field is empty
    if (employeeId.isEmpty()) {
        if (selectedEmployeeId != null && !selectedEmployeeId.isEmpty()) {
            employeeId = selectedEmployeeId;
            idField.setText(employeeId);  // Optional: update the text field for clarity
        } else {
            JOptionPane.showMessageDialog(this, "Please enter or select an Employee ID.");
            return;
        }
    }

    try {
        // Load full employee details
        EmployeeData emp = LoadEmployeeData.getFullEmployeeDetails(employeeId, EMPLOYEE_DATA_PATH);

        if (emp == null) {
            resultArea.setText("No employee found with ID: " + employeeId);
            return;
        }

        showEmployeeDetailsInNewFrame(emp);

    } catch (Exception ex) {
        resultArea.setText("Error loading employee record: " + ex.getMessage());
    }
}


    private void showEmployeeDetailsInNewFrame(EmployeeData emp) {
    JFrame detailsFrame = new JFrame("Employee Details - " + emp.employeeId);
    detailsFrame.setSize(900, 600);
    detailsFrame.setLocationRelativeTo(this);
    detailsFrame.setLayout(new BorderLayout());

    // Top Panel: Month Selector + Button
    JPanel topPanel = new JPanel();
    String[] months = {
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    };
    JComboBox<String> monthComboBox = new JComboBox<>(months);
    JButton computeButton = new JButton("Compute Salary");

    topPanel.add(new JLabel("Select Month:"));
    topPanel.add(monthComboBox);
    topPanel.add(computeButton);

    // Left Text Area (Employee Details)
    JTextArea detailsArea = new JTextArea();
    detailsArea.setEditable(false);
    JScrollPane leftScrollPane = new JScrollPane(detailsArea);

    // Right Text Area (Monthly Payroll)
    JTextArea payrollArea = new JTextArea();
    payrollArea.setEditable(false);
    JScrollPane rightScrollPane = new JScrollPane(payrollArea);

    // Panel to hold the two areas side by side
    JPanel centerPanel = new JPanel(new GridLayout(1, 2));
    centerPanel.add(leftScrollPane);
    centerPanel.add(rightScrollPane);

    // Base output for employee details
    StringBuilder baseOutput = new StringBuilder();
    baseOutput.append("Employee ID: ").append(emp.employeeId).append("\n");
    baseOutput.append("Name: ").append(emp.firstName).append(" ").append(emp.lastName).append("\n");
    baseOutput.append("Birthday: ").append(emp.birthday).append("\n");
    baseOutput.append("Address: ").append(emp.address).append("\n");
    baseOutput.append("Phone Number: ").append(emp.phoneNumber).append("\n\n");

    baseOutput.append("Status: ").append(emp.status).append("\n");
    baseOutput.append("Position: ").append(emp.position).append("\n");
    baseOutput.append("Supervisor: ").append(emp.supervisor).append("\n\n");

    baseOutput.append("TIN: ").append(emp.tin).append("\n");
    baseOutput.append("SSS: ").append(emp.sss).append("\n");
    baseOutput.append("PhilHealth: ").append(emp.philHealth).append("\n");
    baseOutput.append("Pag-IBIG: ").append(emp.pagIbig).append("\n\n");

    baseOutput.append(String.format("Rice Subsidy: PHP %.2f\n", emp.riceSubsidy));
    baseOutput.append(String.format("Phone Allowance: PHP %.2f\n", emp.phoneAllowance));
    baseOutput.append(String.format("Clothing Allowance: PHP %.2f\n\n", emp.clothingAllowance));

    baseOutput.append(String.format("Hourly Rate: PHP %.2f\n", emp.hourlyRate));
    baseOutput.append(String.format("Basic Salary: PHP %.2f\n", emp.basicSalary));
    baseOutput.append(String.format("Gross Salary: PHP %.2f\n\n", emp.grossSalary));

    detailsArea.setText(baseOutput.toString());

    // Compute salary on button click
    computeButton.addActionListener(e -> {
        String selectedMonth = (String) monthComboBox.getSelectedItem();
        ArrayList<TimeLogDetails> timeSheet = new ArrayList<>();
        ArrayList<MonthlyTotals> monthlyTotals = new ArrayList<>();

        try {
            LoadTimeSheet.loadTimeSheet(emp.employeeId, ATTENDANCE_RECORD_PATH, timeSheet);
            PayrollCalculations.calculateMonthlyTotals(timeSheet, monthlyTotals, emp);

            ArrayList<MonthlyTotals> filtered = new ArrayList<>();
            for (MonthlyTotals mt : monthlyTotals) {
                if (mt.getMonthName().equalsIgnoreCase(selectedMonth)) {
                    filtered.add(mt);
                }
            }

            if (!filtered.isEmpty()) {
                ArrayList<EmployeeData> singleEmployeeList = new ArrayList<>(List.of(emp));
                String breakdown = CalculateAndDisplay.getSalaryBreakdown(filtered, singleEmployeeList, SSS_TABLE_PATH);
                payrollArea.setText("Payroll for " + selectedMonth + ":\n\n" + breakdown);
            } else {
                payrollArea.setText("No payroll data available for " + selectedMonth + ".");
            }

        } catch (Exception ex) {
            payrollArea.setText("Error computing salary: " + ex.getMessage());
        }
    });

    detailsFrame.add(topPanel, BorderLayout.NORTH);
    detailsFrame.add(centerPanel, BorderLayout.CENTER);
    detailsFrame.setVisible(true);
}



    public static void main(String[] args) {
        SwingUtilities.invokeLater(PayrollGUI::new);
    }
}
