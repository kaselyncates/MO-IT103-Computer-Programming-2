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
import com.opencsv.CSVWriter;
import java.io.FileWriter;
import javax.swing.table.DefaultTableModel;
import com.group.motorph.payroll.services.data_writer.EmployeeFileWriter;
import com.group.motorph.payroll.models.EmployeeData;
import com.group.motorph.payroll.utilities.EmployeeDefaults;


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
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // -------------------- TOP PANEL --------------------
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        // --- Row 1
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        idField = new JTextField(15);
        row1.add(new JLabel("Enter Employee ID:"));
        row1.add(idField);

        // --- Row 2
        JPanel row2 = new JPanel(new GridLayout(1, 3, 10, 0));
        JButton viewRecordButton = new JButton("View Record");
        JButton calculateButton = new JButton("Calculate Payroll");
        JButton viewAllButton = new JButton("View All Employees");
        row2.add(viewRecordButton);
        row2.add(calculateButton);
        row2.add(viewAllButton);

        // --- Row 3
        JPanel row3 = new JPanel(new GridLayout(1, 3, 10, 0)); 
        JButton newEmployeeButton = new JButton("New Employee");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        row3.add(newEmployeeButton);
        row3.add(updateButton);
        row3.add(deleteButton);

        // --- Assemble all rows into the topPanel ---
        topPanel.add(row1);
        topPanel.add(Box.createVerticalStrut(10)); 
        topPanel.add(row2);
        topPanel.add(Box.createVerticalStrut(10)); 
        topPanel.add(row3);

        // --- Add to main layout ---
        add(topPanel, BorderLayout.NORTH);


        
        // -------------------- CENTER DISPLAY AREA --------------------
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
        
         // -------------------- BUTTON ACTIONS --------------------
        calculateButton.addActionListener(e -> calculatePayroll());
        viewRecordButton.addActionListener(e -> viewEmployeeRecord());
        viewAllButton.addActionListener(e -> viewAllEmployees());
        newEmployeeButton.addActionListener(e -> openNewEmployeeForm());
        updateButton.addActionListener(e -> openUpdateForm());
        deleteButton.addActionListener(e -> deleteSelectedEmployee());


    setVisible(true);
}

    //----------CALCULATE PAYROLL FOR SPECIFIC EMPLOYEE----------
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

        CardLayout cl = (CardLayout)((JPanel)getContentPane().getComponent(1)).getLayout();
        cl.show((JPanel)getContentPane().getComponent(1), "TEXT");

        resultArea.setText(output.toString());

    } catch (Exception ex) {
        resultArea.setText("Error calculating payroll: " + ex.getMessage());
    }
}


    //----------VIEW ALL EMPLOYEE RECORDS----------
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


    
    //----------VIEW SPECIFIC EMPLOYEE RECORDS----------
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


    //----------VIEW SPECIFIC EMPLOYEE IN NEW FRAME----------
    private void showEmployeeDetailsInNewFrame(EmployeeData emp) {
    JFrame detailsFrame = new JFrame("Employee Details - " + emp.employeeId);
    detailsFrame.setSize(900, 600);
    detailsFrame.setLocationRelativeTo(this);
    detailsFrame.setLayout(new BorderLayout());

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

    JTextArea detailsArea = new JTextArea();
    detailsArea.setEditable(false);
    JScrollPane leftScrollPane = new JScrollPane(detailsArea);

    JTextArea payrollArea = new JTextArea();
    payrollArea.setEditable(false);
    JScrollPane rightScrollPane = new JScrollPane(payrollArea);

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

    
    //----------ADD NEW EMPLOYEE----------
    private void openNewEmployeeForm() {
    JFrame formFrame = new JFrame("Add New Employee");
    formFrame.setSize(400, 600);
    formFrame.setLocationRelativeTo(this);
    formFrame.setLayout(new GridLayout(0, 2, 5, 5));

    // Create input fields
    JTextField idField = new JTextField();
    JTextField lastNameField = new JTextField();
    JTextField firstNameField = new JTextField();
    JTextField birthdayField = new JTextField();
    JTextField addressField = new JTextField();
    JTextField supervisorField = new JTextField();
    JTextField phoneField = new JTextField();
    JTextField tinField = new JTextField();
    JTextField sssField = new JTextField();
    JTextField philHealthField = new JTextField();
    JTextField pagIbigField = new JTextField();
    JTextField basicSalaryField = new JTextField();
    
    // Dropdowns for status and position
    String[] statusOptions = { "Regular", "Probationary" };
    JComboBox<String> statusBox = new JComboBox<>(statusOptions);

    String[] positionOptions = {
        "Account Manager", "Account Rank and File", "Account Team Leader", "Accounting Head",
        "Chief Finance Officer", "Chief Marketing Officer", "Customer Service and Relations",
        "HR Manager", "HR Rank and File", "HR Team Leader", "IT Operations and Systems",
        "Payroll Manager", "Payroll Rank and File", "Payroll Team Leader",
        "Sales & Marketing", "Supply Chain and Logistics"
    };
    JComboBox<String> positionBox = new JComboBox<>(positionOptions);

    // Add all fields to the form
    formFrame.add(new JLabel("Employee ID")); formFrame.add(idField);
    formFrame.add(new JLabel("Last Name")); formFrame.add(lastNameField);
    formFrame.add(new JLabel("First Name")); formFrame.add(firstNameField);
    formFrame.add(new JLabel("Birthday (YYYY-MM-DD)")); formFrame.add(birthdayField);
    formFrame.add(new JLabel("Address")); formFrame.add(addressField);
    formFrame.add(new JLabel("Phone Number")); formFrame.add(phoneField);
    formFrame.add(new JLabel("SSS")); formFrame.add(sssField);
    formFrame.add(new JLabel("PhilHealth")); formFrame.add(philHealthField);
    formFrame.add(new JLabel("TIN")); formFrame.add(tinField);
    formFrame.add(new JLabel("Pag-IBIG")); formFrame.add(pagIbigField);
    formFrame.add(new JLabel("Status")); formFrame.add(statusBox);
    formFrame.add(new JLabel("Position")); formFrame.add(positionBox);
    formFrame.add(new JLabel("Supervisor")); formFrame.add(supervisorField);  
    formFrame.add(new JLabel("Basic Salary")); formFrame.add(basicSalaryField);

    JButton submitButton = new JButton("Submit");
    formFrame.add(new JLabel());
    formFrame.add(submitButton);

    submitButton.addActionListener(e -> {
    try {
        String position = (String) positionBox.getSelectedItem();
        double basicSalary = Double.parseDouble(basicSalaryField.getText());
        double rice = 1500;
        double phone = EmployeeDefaults.getPhoneAllowance(position);
        double clothing = EmployeeDefaults.getClothingAllowance(position);

        double hourly = basicSalary / 22 / 8; // 22 working days × 8 hours/day
        double gross = basicSalary + rice + phone + clothing;

        String[] newRow = {
        idField.getText(), 
        lastNameField.getText(),      
        firstNameField.getText(),     
        birthdayField.getText(),      
        addressField.getText(),       
        phoneField.getText(),         
        sssField.getText(),           
        philHealthField.getText(),  
        tinField.getText(),
        pagIbigField.getText(),                  
        (String) statusBox.getSelectedItem(), 
        position,                     
        supervisorField.getText(),    
        String.format("%.2f", basicSalary), 
        String.format("%.2f", rice),        
        String.format("%.2f", phone),       
        String.format("%.2f", clothing),    
        String.format("%.2f", gross),       
        String.format("%.2f", hourly)       
};

        EmployeeData newEmployee = new EmployeeData(
        idField.getText(),
        lastNameField.getText(),
        firstNameField.getText(),
        birthdayField.getText(),
        addressField.getText(),
        (String) statusBox.getSelectedItem(),
        (String) positionBox.getSelectedItem(),
        supervisorField.getText(),
        phoneField.getText(),
        tinField.getText(),
        sssField.getText(),
        philHealthField.getText(),
        pagIbigField.getText(),
        rice, phone, clothing, hourly, basicSalary, gross
);

        EmployeeFileWriter.appendEmployee(EMPLOYEE_DATA_PATH, newRow);


        JOptionPane.showMessageDialog(formFrame, "Employee added successfully!");
        formFrame.dispose();

        // Refresh JTable
        refreshEmployeeTable();

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(formFrame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
});

    formFrame.setVisible(true);
}

    
    //----------UPDATE EMPLOYEE RECORDS----------
    private void openUpdateForm() {
    int selectedRow = employeeTable.getSelectedRow();
    if (selectedRow == -1) return; // no selection

    // Get values from table
    String id = employeeTable.getValueAt(selectedRow, 0).toString();
    String lastName = employeeTable.getValueAt(selectedRow, 1).toString();
    String firstName = employeeTable.getValueAt(selectedRow, 2).toString();
    String sss = employeeTable.getValueAt(selectedRow, 3).toString();
    String philHealth = employeeTable.getValueAt(selectedRow, 4).toString();
    String tin = employeeTable.getValueAt(selectedRow, 5).toString();
    String pagIbig = employeeTable.getValueAt(selectedRow, 6).toString();

    // Create frame
    JFrame updateFrame = new JFrame("Update Employee - " + id);
    updateFrame.setSize(400, 400);
    updateFrame.setLayout(new GridLayout(0, 2, 5, 5));
    updateFrame.setLocationRelativeTo(this);

    // Create fields
    JTextField lastNameField = new JTextField(lastName);
    JTextField firstNameField = new JTextField(firstName);
    JTextField sssField = new JTextField(sss);
    JTextField philHealthField = new JTextField(philHealth);
    JTextField tinField = new JTextField(tin);
    JTextField pagIbigField = new JTextField(pagIbig);

    updateFrame.add(new JLabel("Last Name:")); updateFrame.add(lastNameField);
    updateFrame.add(new JLabel("First Name:")); updateFrame.add(firstNameField);
    updateFrame.add(new JLabel("SSS:")); updateFrame.add(sssField);
    updateFrame.add(new JLabel("PhilHealth:")); updateFrame.add(philHealthField);
    updateFrame.add(new JLabel("TIN:")); updateFrame.add(tinField);
    updateFrame.add(new JLabel("Pag-IBIG:")); updateFrame.add(pagIbigField);

    JButton saveButton = new JButton("Save");
    updateFrame.add(new JLabel()); updateFrame.add(saveButton);

    saveButton.addActionListener(ae -> {
        try {
            ArrayList<EmployeeData> allEmployees = new ArrayList<>();
            LoadEmployeeData.loadAllEmployees(EMPLOYEE_DATA_PATH, allEmployees);

            for (EmployeeData emp : allEmployees) {
                if (emp.getEmployeeId().equals(id)) {
                    emp.setLastName(lastNameField.getText());
                    emp.setFirstName(firstNameField.getText());
                    emp.setSssNumber(sssField.getText());
                    emp.setPhilHealthNumber(philHealthField.getText());
                    emp.setTinNumber(tinField.getText());
                    emp.setPagIbigNumber(pagIbigField.getText());
                    break;
                }
            }

            EmployeeFileWriter.overwriteEmployeeData(EMPLOYEE_DATA_PATH, allEmployees);
            JOptionPane.showMessageDialog(updateFrame, "Employee updated.");
            updateFrame.dispose();
            refreshEmployeeTable(); // Refresh table in main GUI

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(updateFrame, "Error: " + ex.getMessage());
        }
    });

    updateFrame.setVisible(true);
}


    //----------DELETE EMPLOYEE RECORDS----------
    private void deleteSelectedEmployee() {
    int selectedRow = employeeTable.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Please select an employee to delete.");
        return;
    }

    String employeeIdToDelete = (String) employeeTable.getValueAt(selectedRow, 0); // Column 0 = Employee ID

    int confirm = JOptionPane.showConfirmDialog(
        this,
        "Are you sure you want to delete employee ID: " + employeeIdToDelete + "?",
        "Confirm Deletion",
        JOptionPane.YES_NO_OPTION
    );

    if (confirm != JOptionPane.YES_OPTION) {
        return;
    }

    try {
        ArrayList<EmployeeData> allEmployees = new ArrayList<>();
        LoadEmployeeData.loadAllEmployees(EMPLOYEE_DATA_PATH, allEmployees);

        // Filter out the employee to delete
        allEmployees.removeIf(emp -> emp.getEmployeeId().equals(employeeIdToDelete));

        // Overwrite the CSV with the updated list
        EmployeeFileWriter.overwriteEmployeeData(EMPLOYEE_DATA_PATH, allEmployees);

        JOptionPane.showMessageDialog(this, "Employee deleted successfully.");
        refreshEmployeeTable(); // Reload JTable

    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error deleting employee: " + ex.getMessage());
    }
}

    
    //----------REFRESH TABLE----------
    private void refreshEmployeeTable() {
    try {
        ArrayList<EmployeeData> allEmployees = new ArrayList<>();
        LoadEmployeeData.loadAllEmployees(EMPLOYEE_DATA_PATH, allEmployees);

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[] {
            "Employee ID", "Last Name", "First Name", "SSS", "PhilHealth", "TIN", "Pag-IBIG"
        });

        for (EmployeeData emp : allEmployees) {
            model.addRow(new Object[] {
                emp.getEmployeeId(),
                emp.getLastName(),
                emp.getFirstName(),
                emp.getSssNumber(),           
                emp.getPhilHealthNumber(),
                emp.getTinNumber(),
                emp.getPagIbigNumber()
            });
        }

        employeeTable.setModel(model);

    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Failed to load employee table.");
    }
}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PayrollGUI::new);
    }
}
