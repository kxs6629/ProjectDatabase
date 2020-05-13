/**
 * @author : Kenny Scott
 * @version : 4/15/2020
 * @desc : DataLayer class for project database
 */
import java.sql.*;
import java.util.ArrayList;

public class ProjectDataLayer {
    //attributes
    private Statement s;
    private PreparedStatement stmt;
    private String sql;
    private String stuId;
    private String facultyId;
    private String projectId;
    private ResultSet rs;

    //final attributes
    private final String URI;
    private final String DRIVER;
    private final String USER;
    private final String PASSWORD;
    private Connection conn;

    //Constructor for data layer class
    public ProjectDataLayer(){
        URI = "jdbc:mysql://localhost/project_db?autoReconnect=true&useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        DRIVER = "com.mysql.cj.jdbc.Driver";
        USER = "root";
        PASSWORD = "student";
    }

    /**
     * Connects to the database
     * @return true if connection is successful, false if not
     */
    public boolean connect() {
        conn = null;
        try  {
            Class.forName (DRIVER);
        }
        catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
            return false;
        }

        try  {
            conn = DriverManager.getConnection(URI, USER, PASSWORD);
            return true;
        }
        catch(SQLException sqle) {
            return false;
        }// end of catch
    }// end of connect()

    /**
     * Closes connection to the database
     * @return true if disconnected, false if unable to disconnect
     */
    public boolean close() {
        try {
            conn.close();
            return true;
        }//end of try
        catch(SQLException sqle) {
            return false;
        }//end of catch
    }// end of method close

    /**
     * Lists all the projects in the database
     */
    public ArrayList<String> listProject(){
        ArrayList<String> projectList = new ArrayList<>();
        try{
            s = conn.createStatement();
            sql = "SELECT title FROM project";
            rs = s.executeQuery(sql);
            while(rs.next()){
                projectList.add(rs.getString(1));
            }
        }catch(SQLException sqle){
            System.out.println("Error getting project list");
        }
        return projectList;
    }

    /**
     * List the description for a specified project.
     * If no results come up in search, the user is
     * notified that no results occurred
     * @param _project : name of project to search for
     */
    public String descProject(String _project){
        String desc= "";
        try{
            s = conn.createStatement();
            sql = "SELECT summary,ongoing FROM project WHERE title='"+_project+"'";
            rs = s.executeQuery(sql);
            if(rs.next()){
                desc = "Description: "+rs.getString(1);
            }
            else{
                desc = "No results found for "+_project;
            }
        }
        catch (SQLException sqle){
            System.out.println("Error in pulling data summary for "+_project);
        }
        return desc;
    }

    /**
     * Add a staff member to a project
     * @param project : title of project to be updated
     * @param student : name of student to be added
     */
    public void addStaff(String project, String student){
        try{
            stuId="";
            projectId="";

            // get the student's id
            s = conn.createStatement();
            sql = "SELECT personId FROM person WHERE name = '"+student+"'";
            rs = s.executeQuery(sql);
            if (rs.next() == false){
                System.out.println("No results found for "+student);
            } else{
                stuId = rs.getString(1);
            }

            // get the project id
            s = conn.createStatement();
            sql = "select idProject from project where title = '"+project+"'";
            rs = s.executeQuery(sql);
            if (rs.next() == false){
                System.out.println("No results found for "+project);
            } else{
                projectId = rs.getString(1);
            }
            // insert student into StudentList
            sql = "INSERT INTO `project_db`.`StudentList` (`Student_Person_personId`, `Project_idProject`) VALUES (?, ?)";
            System.out.println(stuId+" "+projectId);
            stmt = conn.prepareStatement(sql);
            stmt.setString(1,stuId);
            stmt.setString(2,projectId);
            stmt.executeUpdate();

            System.out.println("Added "+student+" to "+project);
        }
        catch(SQLException sqle){
            System.out.println("Error adding "+student+" to "+project);
        }
    }

    /**
     * Remove a student from a project
     * @param project name of project to change
     * @param student name of student to remove from project
     */
    public void removeStudent(String project, String student){
        try{
            stuId="";


            // get the student's id
            s = conn.createStatement();
            sql = "SELECT personId FROM person WHERE name = '"+student+"'";
            rs = s.executeQuery(sql);
            if (rs.next() == false){
                System.out.println("No results found for "+student);
            } else{
                stuId = rs.getString(1);
            }
            // insert student into StudentList
            sql = "DELETE FROM studentList WHERE student_person_personid = ?;";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1,stuId);
            stmt.executeUpdate();

            System.out.println("Removed "+student+" from "+project);
        }
        catch(SQLException sqle){
            System.out.println("Error adding "+student+" to "+project);
        }
    }

    /**
     * Search and print the staff list for a specified project
     * @param _project name of project to search for
     */
    public ArrayList<String> viewStaff(String _project){
        ArrayList<String> staffList = new ArrayList<>();
        try{
            s = conn.createStatement();
            sql = "SELECT idProject FROM project where title = '"+_project+"'";
            rs = s.executeQuery(sql);
            if(rs.next() == false){
                System.out.println("No results found for "+_project);
            }
            else{
                String id = rs.getString(1);
                s = conn.createStatement();
                sql = "SELECT DISTINCT person.name FROM person,project,faculty WHERE Project.idProject = "+id+" AND person.personid = project.faculty_person_personid";
                rs = s.executeQuery(sql);
                if(rs.next()) {
                    staffList.add("Project Lead: " + rs.getString(1));
                }
                s = conn.createStatement();
                sql = "SELECT person.name FROM person,project,StudentList,student WHERE Project.idProject = "+id+ " AND StudentList.Project_idProject="+ id+" AND StudentList.student_person_personid = student.person_personId AND person.personid = Student.person_personid";
                rs = s.executeQuery(sql);
                while(rs.next() == true){
                    staffList.add(rs.getString(1));
                }
            }
        }
        catch(SQLException sqle){
            System.out.println("Error in pulling data from database");
            sqle.printStackTrace();
        }
        return staffList;
    }


    /**
     * Create and insert a project into the database
     * @param facultyName : name of faculty leading project
     * @param projectName : title of project
     * @param summary : description of project
     * @param startDate : start date of project
     */
    public void createProject(String facultyName, String projectName, String summary, String startDate){
        try{
            sql = "SELECT personId FROM person WHERE name = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1,facultyName);
            rs = stmt.executeQuery();

            if(rs.next()){
                facultyId = rs.getString(1);
            }
            sql = "INSERT INTO `project_db`.`Project` (`idProject`, `Faculty_Person_personId`, `title`, `summary`, `start`, `end`, `ongoing`) VALUES (?, ?, ?, ?, ?, NULL, 1);";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1,Integer.toString(countProject()));
            stmt.setString(2,facultyId);
            stmt.setString(3, projectName);
            stmt.setString(4,summary);
            stmt.setString(5,startDate);
            stmt.executeUpdate();
            System.out.println(projectName+" created!");
        }catch(SQLException sqle){
            System.out.println("Error");
        }
    }

    /**
     * Remove a project from the database
     * @param _name project name to be removed
     */
    public void removeProject(String _name){
        try {
            s = conn.createStatement();
            sql = "DELETE FROM project WHERE title ='"+_name+"'";
            s.executeUpdate(sql);
            System.out.println(_name+" deleted");
        }
        catch (SQLException sqle){
            System.out.println("Error removing "+_name+" from database");
        }
    }

    /**
     * Adds a student to the database using prepared statements
     * @param _name : student name
     * @param _email : student email address
     * @param _major : student major
     * @param _year : student year status
     */
    public void addStudent(String _name,String _email, String _major ,String _year){
        try{
            sql ="INSERT INTO `project_db`.`Person` (`personType`, `Name`, `email`) VALUES (?,?,?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1,"Student");
            stmt.setString(2,_name);
            stmt.setString(3,_email);
            stmt.executeUpdate();
            System.out.println("Added "+_name+" to database!");
            sql = "INSERT INTO `project_db`.`Student` (`year`, `Person_personId`, `MajorList_majorTitle`) VALUES (?,?,?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1,_year);
            stmt.setString(2,Integer.toString(countPerson()));
            stmt.setString(3,_major);
            stmt.executeUpdate();
        }
        catch (SQLException sqle){
            System.out.println("Error adding new student to database");
        }
    }

    /**
     * Add a faculty member to the database using prepared statements
     * @param _name : faculty name
     * @param _email : faculty email address
     * @param _dept : faculty department
     */
    public void addFaculty(String _name, String _email, String _dept){
        try{
            sql ="INSERT INTO `project_db`.`Person` (`personType`, `Name`, `email`) VALUES (?,?,?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1,"Faculty");
            stmt.setString(2,_name);
            stmt.setString(3,_email);
            stmt.executeUpdate();
            System.out.println("Added "+_name+" to database!");

            sql = "INSERT INTO `project_db`.`Faculty` (`Person_personId`, `DepartmentList_deptName`) VALUES (?,?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1,Integer.toString(countPerson()));
            stmt.setString(2,_dept);
            stmt.executeUpdate();
        }
        catch (SQLException sqle){
            System.out.println("Error adding new faculty");
        }
    }

    /**
     * Update the status of a project
     * @param _status : status code
     * @param _date : date of most recent progress
     * @param _title : project name to be updated
     */
    public void updateProject(int _status,String _date,String _title){
        try{
            sql = "UPDATE project SET ongoing = ?, end = ? WHERE title = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1,Integer.toString(_status));
            stmt.setString(2,_date);
            stmt.setString(3,_title);
            stmt.executeUpdate();
            System.out.println("Project updated!");
        }
        catch(SQLException sqle){
            System.out.println("Error updating project status");
        }
    }

    /**
     * Get the keywords of a person by their name
     * @param name name of person to search for
     * @return keywordList : a list of keywords from the database
     */
    public ArrayList<String> getKeywords(String name){
        ArrayList<String> keywordList = new ArrayList<>();
        try{
            sql = "SELECT keyword_title FROM person_has_keyword,person WHERE person.name ='"+name+"' AND person_has_keyword.person_personid = person.personid";
            s = conn.createStatement();
            rs = s.executeQuery(sql);
            while(rs.next()){
                keywordList.add(rs.getString(1));
            }
        }catch (SQLException sqle){
            System.out.println("Error pulling keywords");
        }
        return keywordList;
    }



    /**
     * Get the interests of a person by their name
     * @param name name of person to search for
     * @return interestList : a list of interests from the database
     */
    public ArrayList<String> getInterest(String name){
        ArrayList<String> interestList = new ArrayList<>();
        try{
            sql = "SELECT interest_title FROM person_has_interest,person WHERE person.name ='"+name+"' AND person_has_interest.person_personid = person.personid";
            s = conn.createStatement();
            rs = s.executeQuery(sql);
            while(rs.next()){
                interestList.add(rs.getString(1));
            }
        }catch (SQLException sqle){
            System.out.println("Error pulling keywords");
        }
        return interestList;
    }

    /**
     * Counts the number of people in the Person table
     * @return rows : the number of entries +1
     */
    public int countPerson(){
        int rows = 0;
        try{
            s = conn.createStatement();
            sql = "SELECT count(*) FROM person";
            rs = stmt.executeQuery(sql);
            if(rs.next()){
                rows = Integer.parseInt(rs.getString(1));
            }
        }catch(SQLException sqle){
            System.out.println("Error counting person");
        }
        return rows;
    }

    /**
     * Count the number of projects in the database
     * @return rows : number of projects in database
     */
    public int countProject(){
        int rows = 0;
        try{
            s = conn.createStatement();
            sql = "SELECT count(*) FROM project";
            rs = stmt.executeQuery(sql);
            if(rs.next()){
                rows = Integer.parseInt(rs.getString(1));
            }
        }catch(SQLException sqle){
            System.out.println("Error counting projects");
        }
        return rows;
    }

    /**
     * login using email address
     * @param email : email used as password
     * @return personType : type of person
     */
    public String login(String email){
        String personType ="";
        try {
            sql = "SELECT personType from person where email='"+email+"'";
            s = conn.createStatement();
            rs = s.executeQuery(sql);
            if(rs.next()){
                personType = rs.getString(1);
                System.out.println("Welcome "+personType);
            }
            else{
                System.out.println("No match for user");
                System.out.println("Signed in as a guest");
                personType = "Public";
            }
        } catch(SQLException sqle){
            System.out.println("Error logging in");
        }
        return personType;
    }
}
