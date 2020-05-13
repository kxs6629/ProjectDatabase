/**
 * @author : Kenny Scott
 * @version : 4/22/2020
 * @desc : Presentation layer for project database. This is a new
 * presentation layer that utilizes a UI instead of a console
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ProjectUILayer extends JFrame{
    //attributes
    private String facultyName;
    private String emailAddr;
    private String dept;
    private String startDate;
    private String summary;
    private String studentName;
    private String projectName;
    private String year;
    private String major;
    private String userType;

    private JTextArea console;
    private ProjectDataLayer pdl;
    private JScrollPane verticalScroll;

    /**
     * Constructor for the UI. Presents the user
     * with a console and action buttons
     */
    public ProjectUILayer(){
        pdl = new ProjectDataLayer();
        pdl.connect();

        setTitle("Project Database");

        JPanel north = new JPanel();
        JLabel jln= new JLabel();
        jln.setText("Project Database Overlay");
        north.add(jln);
        add(north, BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setLayout(new BorderLayout());
        console = new JTextArea(20,50);
        verticalScroll = new JScrollPane(console);
        console.setEnabled(false);
        console.append("Welcome to the project database overlay! Please login to get started");
        console.setDisabledTextColor(Color.BLACK);

        JLabel jlc = new JLabel("Console Log: ");
        jlc.setAlignmentX(CENTER_ALIGNMENT);
        center.add(jlc,BorderLayout.NORTH);
        center.add(verticalScroll,BorderLayout.CENTER);

        add(center, BorderLayout.CENTER);

        JPanel east = new JPanel();

        east.setLayout(new BoxLayout(east,BoxLayout.PAGE_AXIS));
        JLabel loginMsg = new JLabel("Login or View as Guest:");
        east.add(loginMsg);

        //login button
        JButton loginBtn = new JButton("Login");
        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame temp = new JFrame();
                String login = JOptionPane.showInputDialog(temp,"Enter Email to Sign In: ");
                String status = pdl.login(login);
                if(status.equals("Student") || status.equals("Faculty")){
                    userType = status;
                    console.append("\nWelcome "+status+"!");
                    updateButtons(east);
                }
                else{
                    console.append("\n No match found, continuing as guest");
                    userType = "Public";
                    updateButtons(east);
                }
            }
        });

        //guest button that allows public users to access the database (with limited functionality
        JButton guestBtn = new JButton("Guest");
        guestBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userType = "Public";
                console.append("\n Welcome Guest!");
                updateButtons(east);
            }
        });

        east.add(loginBtn);
        east.add(guestBtn);

        add(east,BorderLayout.EAST);

        JPanel south = new JPanel();

        // button to clear console log
        JButton testBtn = new JButton("Clear Log");
        testBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                console.setText("");
            }
        });

        // allows user to exit the program
        JButton exitBtn = new JButton("Exit");
        exitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pdl.close();
                System.exit(0);
            }
        });
        south.add(testBtn);
        south.add(exitBtn);

        add(south,BorderLayout.SOUTH);

        getContentPane().setBackground(Color.PINK);
        pack();
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    /**
     * Updates the east panel to contain buttons accessible to a user type
     * @param jp panel to be updated
     */
    public void updateButtons(JPanel jp){
        getContentPane().remove(jp);
        JPanel replacement = new JPanel();
        replacement.setLayout(new BoxLayout(replacement,BoxLayout.PAGE_AXIS));
        JLabel jl = new JLabel("Select an Action");
        replacement.add(jl);
        JLabel role = new JLabel("Role: "+userType);
        replacement.add(role);

        //button to list all projects from the database in the console
        JButton viewProject = new JButton("View Projects");
        viewProject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> list = pdl.listProject();
                console.append("\n Project List:");
                for(String title: list){
                    console.append("\n"+title);
                }
            }
        });
        replacement.add(viewProject);

        //list all staff members of a desired project
        JButton viewStaff = new JButton("View Project Staff");
        viewStaff.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String project = JOptionPane.showInputDialog("Enter Project Name: ");
                if(project == null){
                    console.append("\nMissing field detected");
                }
                else{
                    ArrayList<String> staffList = pdl.viewStaff(project);
                    if (staffList.size() > 0) {
                        console.append("\n Staff list for " + project + ":");
                        for(String name : staffList) {
                            console.append("\n" + name);
                        }
                    }
                    else{
                        console.append("\nNo results for "+project);
                    }
                }
            }
        });
        replacement.add(viewStaff);

        //describe a desired project
        JButton descProject = new JButton("Describe Project");
        descProject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String projectName = JOptionPane.showInputDialog("Enter Project Name");
                if(projectName == null){
                    console.append("\nMissing Field detected");
                }
                else {
                    String result = pdl.descProject(projectName);
                    console.append("\n" + result);
                }
            }
        });
        replacement.add(descProject);

        //get the interest of a person
        JButton getInterest = new JButton("Describe Person's Interest");
        getInterest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                studentName = JOptionPane.showInputDialog("Name of person to describe: ");
                if(studentName == null){
                    console.append("\n Missing field detected");
                }else{
                    ArrayList<String> interestList = pdl.getInterest(studentName);
                    if(interestList.size() == 0){
                        console.append("\n No results found for "+studentName);
                    }
                    else{
                        console.append("\n List of Interest for "+studentName);
                        for(String interest : interestList){
                            console.append("\n"+interest);
                        }
                    }
                }
            }
        });
        replacement.add(getInterest);

        //get the keywords of a person
        JButton getKeyword = new JButton("Describe Person's Keywords");
        getKeyword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                studentName = JOptionPane.showInputDialog("Name of person to describe: ");
                if(studentName == null){
                    console.append("\n Missing field detected");
                }else{
                    ArrayList<String> keywordList = pdl.getKeywords(studentName);
                    if(keywordList.size() == 0){
                        console.append("\n No results found for "+studentName);
                    }
                    else{
                        console.append("\n List of Interest for "+studentName);
                        for(String keyword : keywordList){
                            console.append("\n"+keyword);
                        }
                    }
                }
            }
        });
        replacement.add(getKeyword);

        //Enables more functionality if the user is a faculty member
        if(userType.equals("Faculty")){
            //remove a project from the database
            JButton removeProject = new JButton("Delete a Project");
            removeProject.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    projectName = JOptionPane.showInputDialog("Name of project to be deleted (THIS CANNOT BE UNDONE): ");
                    if(projectName == null){
                        console.append("\nNo action taken, missing field detected");
                    }else{
                        pdl.removeProject(projectName);
                        console.append("\nDeleted "+projectName+" from database");
                    }
                }
            });
            replacement.add(removeProject);

            //add a project to the database
            JButton createProject = new JButton("Create a Project");
            createProject.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    projectName = JOptionPane.showInputDialog("Project Name: ");
                    summary = JOptionPane.showInputDialog("A short summary of the project: ");
                    startDate = JOptionPane.showInputDialog("Start Date (yyyy-mm-dd): ");
                    facultyName = JOptionPane.showInputDialog("Faculty Member Leading Project: ");
                    if(facultyName == null || projectName == null || summary == null || startDate == null){
                        console.append("\nMissing fields detected");
                    }else {
                        pdl.createProject(facultyName, projectName, summary, startDate);
                        console.append("\n Added "+projectName+" to database!");
                    }
                }
            });
            replacement.add(createProject);

            //update a desired project's status
            JButton updateProject = new JButton("Update Project Status");
            updateProject.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    projectName = JOptionPane.showInputDialog("Name of project to be updated: ");
                    int result = JOptionPane.showConfirmDialog(null, "Has the project been completed?");
                    switch (result) {
                        case JOptionPane.YES_OPTION:
                            result = 1;
                            startDate = JOptionPane.showInputDialog("Date of most recent progress(yyyy-mm-dd): ");
                            if(startDate == null || projectName == null){
                                console.append("\n Missing fields detected");
                            }else {
                                pdl.updateProject(result, startDate, projectName);
                                console.append("\n"+projectName+" updated!");
                            }
                            break;
                        case JOptionPane.NO_OPTION:
                            result = 0;
                            if(startDate == null || projectName == null){
                                console.append("\n Missing fields detected");
                            }else {
                                pdl.updateProject(result, startDate, projectName);
                                console.append("\n"+projectName+" updated!");
                            }
                            break;
                        case JOptionPane.CANCEL_OPTION:
                            console.append("\nUpdate cancelled");
                            break;
                    }
                }
            });
            replacement.add(updateProject);

            //add a student to a project staff list
            JButton addStudentProject = new JButton("Add Student to Project");
            addStudentProject.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    projectName = JOptionPane.showInputDialog("What Project?");
                    studentName = JOptionPane.showInputDialog("Name of Student to Add?");
                    if(projectName == null || studentName == null){
                        console.append("\n Missing field(s) detected");
                    }else {
                        pdl.addStaff(projectName, studentName);
                    }
                }
            });
            replacement.add(addStudentProject);

            // remove a student from a project staff list
            JButton removeStudentProject = new JButton("Remove Student from Project");
            removeStudentProject.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    projectName = JOptionPane.showInputDialog("What Project?");
                    studentName = JOptionPane.showInputDialog("Name of Student to Remove?");
                    if(studentName == null || projectName == null){
                        console.append("\n Missing field(s) detected");
                    }
                    else{
                        pdl.removeStudent(projectName,studentName);
                    }
                }
            });
            replacement.add(removeStudentProject);

            //add a student to the database
            JButton addStudent = new JButton("Add New Student");
            addStudent.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    studentName = JOptionPane.showInputDialog("New Student Name: ");
                    emailAddr = JOptionPane.showInputDialog("Student Email Address: ");
                    major = JOptionPane.showInputDialog("Student's Major: ");
                    year = JOptionPane.showInputDialog("What year? (1-4)");

                    if(studentName == null || emailAddr == null || major == null || year == null){
                        console.append("\n Empty field(s) detected, unable to add new entry");
                    }else{
                        pdl.addStudent(studentName,emailAddr,major,year);
                        console.append("\n"+studentName+" added to database!");
                    }
                }
            });
            replacement.add(addStudent);

            //add a faculty member to the database
            JButton addFacultyMember = new JButton("Add Faculty Member");
            addFacultyMember.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    facultyName = JOptionPane.showInputDialog("New Member Name?");
                    emailAddr = JOptionPane.showInputDialog("Email Address?");
                    dept = JOptionPane.showInputDialog("Department?");
                    if(facultyName == null || emailAddr == null || dept == null){
                        console.append("\n Empty field(s) detected, unable to add new entry");
                    }
                    else {
                        pdl.addFaculty(facultyName, emailAddr, dept);
                        console.append("\n Added " + facultyName + " to database!");
                    }
                }
            });
            replacement.add(addFacultyMember);
        }

        add(replacement,BorderLayout.EAST);
        validate();
        repaint();
    }

    public static void main(String[] args){
        new ProjectUILayer();
    }
}
