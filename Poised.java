import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;
/**
 * Represents a project management application that interacts with a MySQL database using JDBC.
 * Allows users to perform various tasks related to project management.
 */

public class Poised {


    /**
     * Default constructor for the Poised class.
     * This constructor is automatically invoked when an object of the class is created.
     * It initializes the Poised object.
     */
    public Poised() {
        // Any initialization logic for the Poised object can be added here
    }

    /**
     * The main entry point of the program.
     * This method establishes a connection to the MySQL database, creates necessary tables,
     * and displays the main menu for user interaction.
     *
     * @param args The command-line arguments (not used in this program).
     *
     */

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/PoisePMS";
        String username = "otheruser";
        String password = "swordfish";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("Connected to database");
            createTables(connection);
            displayMenu(connection);
        } catch (SQLException e) {
            System.out.println("Connection failed " + e.getMessage());
        }
    }
    /**
     * Shows the option to choose from.
     *
     * @param connection to connect to the database.
     */
    public static void displayMenu(Connection connection) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Select an option:");
            System.out.println("1. Enter architect information");
            System.out.println("2. Enter contractor information");
            System.out.println("3. Enter customer information");
            System.out.println("4. Enter project manager information");
            System.out.println("5. Enter new project details");
            System.out.println("6. Enter if you want to update a table");
            System.out.println("7. Show all past dues projects: ");
            System.out.println("8. Update Project Table: ");
            System.out.println("9. Delete the project: ");
            System.out.println("10. Find incomplete projects: ");
            System.out.println("0. Exit");

            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    captureArchitectInformationByUser(connection);
                    break;
                case 2:
                    captureContractorInformationByUser(connection);
                    break;
                case 3:
                    captureCustomerInformationByUser(connection);
                    break;
                case 4:
                    captureProjectManagerInformationByUser(connection);
                    break;
                case 5:
                    captureInformationByUser(connection);
                    break;

                case 6:
                    updateInformationByUser(connection);
                    break;

                case 7:
                    displayPastDueProjects(connection);
                    break;

                case 8:
                    updateProjectInformationValues(connection);
                    break;

                case 9:
                    deleteProject(connection);
                    break;

                case 10:
                    findIncompleteProjects(connection);
                    break;
                case 0:
                    System.out.println("Exiting program.");
                    System.exit(0);
                default:
                    System.out.println("Invalid option. Please enter a valid option.");
            }
        }
    }
    // Create the main table
    /**
     * Creates the necessary tables in the database for project management.
     * This includes the main projects table, as well as tables for architect, contractor,
     * customer, and project manager information.
     *
     * @param connection The database connection used for table creation.
     */
    public static void createTables(Connection connection) {
        try {

            createArchitectTable(connection);
            createContractorTable(connection);
            createCustomerTable(connection);
            createProjectManagerTable(connection);
            createTable(connection);



        } catch (SQLException e) {
            System.out.println("Table creation failed here " + e.getMessage());
        }
    }
    //Setting fields of the Main table
    /**
     * Creates the main projects table in the database.
     * The table includes fields such as project number, project name, building type, address,
     * ERF number, total fee, total paid, deadline, architect ID, contractor ID, customer ID,
     * project manager ID, status, and date of completion.
     *
     * @param connection The database connection used for table creation.
     * @throws SQLException If an SQL exception occurs during table creation.
     */
    public static void createTable(Connection connection) throws SQLException {
        try {
            String createTable = "CREATE TABLE IF NOT EXISTS projects (" +
                    "project_number INT PRIMARY KEY," +
                    "project_name VARCHAR(255)," +
                    "building_type VARCHAR(255)," +
                    "address VARCHAR(255)," +
                    "erf_number VARCHAR(255)," +
                    "total_fee DOUBLE," +
                    "total_paid DOUBLE," +
                    "deadline DATE," +
                    "architect_id INT NULL," +
                    "contractor_id INT NULL," +
                    "customer_id INT NULL," +
                    "manager_id INT NULL," +
                    "status VARCHAR(255)," +
                    "date_of_completion DATE," +
                    "CONSTRAINT fk_customer FOREIGN KEY (customer_id) REFERENCES customer(customer_id) ON DELETE CASCADE," +
                    "CONSTRAINT fk_architect FOREIGN KEY (architect_id) REFERENCES architect(architect_id) ON DELETE CASCADE," +
                    "CONSTRAINT fk_contractor FOREIGN KEY (contractor_id) REFERENCES contractor(contractor_id) ON DELETE CASCADE," +
                    "CONSTRAINT fk_project_manager FOREIGN KEY (manager_id) REFERENCES project_manager(manager_id) ON DELETE CASCADE)";

            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(createTable);
                System.out.println("Table created successfully");
            }
        } catch (SQLException e) {
            System.out.println("Table creation failed this " + e.getMessage());
        }
    }




    // Setting fields of Architeture table
    /**
     * Creates the 'architect' table in the MySQL database.
     *
     * @param connection The JDBC Connection to the MySQL database.
     * @throws SQLException If a database access error occurs.
     */
    public static void createArchitectTable(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String createArchitectTableQuery = "CREATE TABLE IF NOT EXISTS architect (" +
                    "architect_id INT PRIMARY KEY," +
                    "name VARCHAR(255)," +
                    "phone VARCHAR(20)," +
                    "email VARCHAR(255)," +
                    "address VARCHAR(255))";

            statement.executeUpdate(createArchitectTableQuery);
            System.out.println("Table architects created successfully");
        }
    }
        // Setting fields of Contractor Table
    /**
     * Creates the 'contractor' table in the MySQL database.
     *
     * @param connection The JDBC Connection to the MySQL database.
     * @throws SQLException If a database access error occurs.
     */
    public static void createContractorTable(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()){
            String createContractorTableQuery = "CREATE TABLE IF NOT EXISTS contractor (" +
                    "contractor_id INT PRIMARY KEY," +
                    "name VARCHAR(255)," +
                    "phone VARCHAR(20)," +
                    "email VARCHAR(255)," +
                    "address VARCHAR(255))";

            statement.executeUpdate(createContractorTableQuery);
            System.out.println("Table contractor created successfully");
        }
    }


    // SETTING FIELDS OF CUSTOMER TABLE
    /**
     * Creates the 'customer' table in the MySQL database.
     *
     * @param connection The JDBC Connection to the MySQL database.
     * @throws SQLException If a database access error occurs.
     */
    public static void createCustomerTable(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()){
            String createCustomerTableQuery = "CREATE TABLE IF NOT EXISTS customer (" +
                    "customer_id INT PRIMARY KEY," +
                    "name VARCHAR(255)," +
                    "phone VARCHAR(20)," +
                    "email VARCHAR(255)," +
                    "address VARCHAR(255))";

            statement.executeUpdate(createCustomerTableQuery);
            System.out.println("Table customer created successfully");
        }
    }

        // SETTING FIELDS OF MANAGER TABLE

    /**
     * Creates the 'project_manager' table in the MySQL database.
     *
     * @param connection The JDBC Connection to the MySQL database.
     * @throws SQLException If a database access error occurs.
     */
    public static void createProjectManagerTable(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String createProjectManagerTableQuery = "CREATE TABLE IF NOT EXISTS project_manager (" +
                    "manager_id INT PRIMARY KEY," +
                    "name VARCHAR(255)," +
                    "phone VARCHAR(20)," +
                    "email VARCHAR(255)," +
                    "address VARCHAR(255))";

            statement.executeUpdate(createProjectManagerTableQuery);
            System.out.println("Table Project manager created successfully");
        }
    }

    // FOR PARSING DATE
    /**
     * Parses a date string into a Date object.
     *
     * @param dateString The date string to parse.
     * @return The parsed Date object.
     */
    public static Date parseDate(String dateString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return new Date(dateFormat.parse(dateString).getTime());
        } catch (ParseException e) {
            throw new RuntimeException("Error parsing date: " + e.getMessage());
        }
    }

    // FOR CAPTURING THE MAIN TABLE INFORMATION BY USER
    /**
     * Captures project information entered by the user and stores it in the database.
     *
     * @param connection The JDBC Connection to the MySQL database.
     */
    public static void captureInformationByUser(Connection connection) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter project details ");

        System.out.println("Enter project number: ");
        int projectNumber = sc.nextInt();
        sc.nextLine();

        System.out.println("Enter the project name (leave blank to generate from customer name and building type): ");
        String projectName = sc.nextLine();

        System.out.println("Enter Building type: ");
        String buildingType = sc.nextLine();

        System.out.println("Enter the address: ");
        String address = sc.nextLine();

        System.out.println("Enter the ERF number: ");
        String erfNumber = sc.nextLine();

        System.out.println("Enter the total fee: ");
        Double totalFee = sc.nextDouble();

        System.out.println("Enter the total paid: ");
        Double totalPaid = sc.nextDouble();

        System.out.println("Enter the date: ");
        String dateString = sc.next();
        Date deadline = parseDate(dateString);

        sc.nextLine();

        System.out.println("Is the project finalized? (yes/no): ");
        String finalizationInput = sc.nextLine().toLowerCase();

        System.out.println("Enter the architect_id");
        int architect_id = sc.nextInt();

        System.out.println("Enter the contractor_id");
        int contractor_id = sc.nextInt();

        System.out.println("Enter the customer_id");
        int customer_id = sc.nextInt();

        System.out.println("Enter the manager_id");
        int manager_id = sc.nextInt();

        boolean isFinalized = finalizationInput.equals("yes");

        // If the user didn't provide a project name, generate it using the building type and customer surname
        if (projectName.isEmpty()) {
            // Retrieve the customer surname from the 'customer' table
            String customerSurname = getCustomerSurname(connection, customer_id);

            // Generate the project name using building type and customer surname
            projectName = buildingType + " " + customerSurname;
        }

        captureInformation(connection, projectNumber, projectName, buildingType, address, erfNumber, totalFee, totalPaid, deadline, isFinalized, architect_id, contractor_id, customer_id, manager_id);
    }

    // Helper method to retrieve customer surname from the 'customer' table
    /**
     * Captures customer surname.
     *
     * @param connection The JDBC Connection to the MySQL database.
     * @param customer_id customer_id
     */
    public static String getCustomerSurname(Connection connection, int customer_id) {
        try {
            String query = "SELECT name FROM customer WHERE customer_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, customer_id);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String customerName = resultSet.getString("name");

                        String[] nameParts = customerName.split("\\s+");
                        return nameParts[nameParts.length - 1];
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving customer surname: " + e.getMessage());
        }
        return ""; // Default value if something goes wrong
    }


    // FOR SETTING THE CAPTURED VALUES IN DATABASE

    /**
     * Captures project information and inserts it into the database.
     *
     * @param connection       Database connection.
     * @param project_number   Unique identifier for the project.
     * @param project_name     Name of the project.
     * @param building_type    Type of building for the project.
     * @param address          Address of the project.
     * @param erf_number       ERF number of the project.
     * @param total_fee        Total fee for the project.
     * @param total_paid       Total amount paid for the project.
     * @param deadline         Deadline for the project.
     * @param isFinalized      Boolean indicating whether the project is finalized.
     * @param contractor_id    Int id for the contractor
     * @param architect_id     Int id for the architect
     * @param customer_id      Int id for the customer
     * @param manager_id       Int id for the manager
     */

    public static void captureInformation(Connection connection, int project_number, String project_name, String building_type,
                                          String address, String erf_number, double total_fee, double total_paid, Date deadline,
                                          boolean isFinalized, int architect_id, int contractor_id, int customer_id, int manager_id) {

        String insertQuery = "INSERT INTO projects (project_number, project_name, building_type, address, erf_number, " +
                "total_fee, total_paid, deadline, status, date_of_completion, architect_id, contractor_id, customer_id, manager_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            connection.setAutoCommit(false);

            preparedStatement.setInt(1, project_number);
            preparedStatement.setString(2, project_name);
            preparedStatement.setString(3, building_type);
            preparedStatement.setString(4, address);
            preparedStatement.setString(5, erf_number);
            preparedStatement.setDouble(6, total_fee);
            preparedStatement.setDouble(7, total_paid);
            preparedStatement.setDate(8, deadline);

            if (isFinalized) {
                preparedStatement.setString(9, "finalise");
                preparedStatement.setDate(10, new Date(System.currentTimeMillis()));
            } else {
                preparedStatement.setString(9, null);
                preparedStatement.setDate(10, null);
            }

            preparedStatement.setInt(11, architect_id);
            preparedStatement.setInt(12, contractor_id);
            preparedStatement.setInt(13, customer_id);
            preparedStatement.setInt(14, manager_id);

            preparedStatement.executeUpdate();
            System.out.println("New project captured successfully.");
            connection.commit();

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackException) {
                throw new RuntimeException("Error rolling back transaction: " + rollbackException.getMessage(), rollbackException);
            }
            throw new RuntimeException("Error capturing project information: " + e.getMessage(), e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException setAutoCommitException) {
                throw new RuntimeException("Error setting auto-commit to true: " + setAutoCommitException.getMessage(), setAutoCommitException);
            }
        }
    }



    // CAPTURING ARCHITECTURE VALUES
    /**
     * Captures architect information and associates it with a project.
     *
     * @param connection      Database connection.
     * @param architect_id    Unique identifier for the architect.
     * @param name            Name of the architect.
     * @param phone           Phone number of the architect.
     * @param email           Email address of the architect.
     * @param address         Address of the architect.
     *
     */
    public static void captureArchitectInformation(Connection connection, int architect_id, String name, String phone, String email, String address) {
        String insertQuery = "INSERT INTO architect (architect_id, name, phone, email, address) VALUES (?,?,?,?,?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setInt(1, architect_id);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, phone);
            preparedStatement.setString(4, email);
            preparedStatement.setString(5, address);


            preparedStatement.executeUpdate();


            System.out.println("New architect information captured successfully.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // CAPTURING CONTRACTOR VALUES
    /**
     * Captures contractor information and associates it with a project.
     *
     * @param connection      Database connection.
     * @param contractor_id   Unique identifier for the contractor.
     * @param name            Name of the contractor.
     * @param phone           Phone number of the contractor.
     * @param email           Email address of the contractor.
     * @param address         Address of the contractor.
     *
     */
    public static void captureContractorInformation(Connection connection, int contractor_id, String name, String phone, String email, String address) {
        String insertQuery = "INSERT INTO contractor (contractor_id, name, phone, email, address) VALUES (?,?,?,?,?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setInt(1, contractor_id);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, phone);
            preparedStatement.setString(4, email);
            preparedStatement.setString(5, address);


            preparedStatement.executeUpdate();

            System.out.println("New contractor information captured successfully.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // CAPTURING CUSTOMER VALUES
    /**
     * Captures customer information and associates it with a project.
     *
     * @param connection      Database connection.
     * @param customer_id     Unique identifier for the customer.
     * @param name            Name of the customer.
     * @param phone           Phone number of the customer.
     * @param email           Email address of the customer.
     * @param address         Address of the customer.
     *
     */
    public static void captureCustomerInformation(Connection connection, int customer_id, String name, String phone, String email, String address) {
        String insertQuery = "INSERT INTO customer (customer_id, name, phone, email, address) VALUES (?,?,?,?,?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setInt(1, customer_id);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, phone);
            preparedStatement.setString(4, email);
            preparedStatement.setString(5, address);


            connection.setAutoCommit(false);
            preparedStatement.executeUpdate();

            connection.commit();
            System.out.println("New customer information captured successfully.");
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackException) {
                throw new RuntimeException(rollbackException);
            }
            throw new RuntimeException(e);
        }
    }


    // CAPTURE MANAGER VALUE
    /**
     * Captures project manager information and associates it with a project.
     *
     * @param connection      Database connection.
     * @param manager_id      Unique identifier for the project manager.
     * @param name            Name of the project manager.
     * @param phone           Phone number of the project manager.
     * @param email           Email address of the project manager.
     * @param address         Address of the project manager.
     *
     */
    public static void captureProjectManagerInformation(Connection connection, int manager_id, String name, String phone, String email, String address) {
        String insertQuery = "INSERT INTO project_manager (manager_id, name, phone, email, address) VALUES (?,?,?,?,?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setInt(1, manager_id);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, phone);
            preparedStatement.setString(4, email);
            preparedStatement.setString(5, address);


            preparedStatement.executeUpdate();

            System.out.println("New project manager information captured successfully.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // SETTING ARCHITECT INFORMATION IN DATABASE
    /**
     * Captures architect information by taking user input.
     *
     * @param connection Database connection.
     */
    public static void captureArchitectInformationByUser(Connection connection) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter architect details ");

        System.out.println("Enter architect id: ");
        int architectId = sc.nextInt();
        sc.nextLine();

        System.out.println("Enter architect name: ");
        String name = sc.nextLine();

        System.out.println("Enter architect phone: ");
        String phone = sc.nextLine();

        System.out.println("Enter architect email: ");
        String email = sc.nextLine();

        System.out.println("Enter architect address: ");
        String address = sc.nextLine();



        captureArchitectInformation(connection, architectId, name, phone, email, address);
    }

    // CAPTURING CONTRACTOR INFORMATION
    /**
     * Captures contractor information by taking user input.
     *
     * @param connection Database connection.
     */
    public static void captureContractorInformationByUser(Connection connection) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter architect details ");

        System.out.println("Enter contractor id: ");
        int contractorId = sc.nextInt();
        sc.nextLine();

        System.out.println("Enter contractor name: ");
        String name = sc.nextLine();

        System.out.println("Enter contractor phone: ");
        String phone = sc.nextLine();

        System.out.println("Enter contractor email: ");
        String email = sc.nextLine();

        System.out.println("Enter contractor address: ");
        String address = sc.nextLine();



        captureContractorInformation(connection, contractorId, name, phone, email, address);
    }

    // CAPTURING CUSTOMER INFORMATION
    /**
     * Captures customer information by taking user input.
     *
     * @param connection Database connection.
     */
    public static void captureCustomerInformationByUser(Connection connection) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Customer details ");

        System.out.println("Enter Customer id: ");
        int customerId = sc.nextInt();
        sc.nextLine();

        System.out.println("Enter Customer name: ");
        String name = sc.nextLine();

        System.out.println("Enter Customer phone: ");
        String phone = sc.nextLine();

        System.out.println("Enter Customer email: ");
        String email = sc.nextLine();

        System.out.println("Enter Customer address: ");
        String address = sc.nextLine();



       captureCustomerInformation(connection, customerId, name, phone, email, address);
    }

    // CAPTURE PROJECT MANAGER INFORMATION
    /**
     * Captures manager information by taking user input.
     *
     * @param connection Database connection.
     */
    public static void captureProjectManagerInformationByUser(Connection connection) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Project manager details ");

        System.out.println("Enter Project manager id: ");
        int managerId = sc.nextInt();
        sc.nextLine();

        System.out.println("Enter project manager name: ");
        String name = sc.nextLine();

        System.out.println("Enter project manager phone: ");
        String phone = sc.nextLine();

        System.out.println("Enter project email: ");
        String email = sc.nextLine();

        System.out.println("Enter project address: ");
        String address = sc.nextLine();



        captureProjectManagerInformation(connection, managerId, name, phone, email, address);
    }

    // UPDATE THE VALUES OF REST TABLES
    /**
     * Updates information in the specified table based on user input.
     *
     * @param connection Database connection.
     */
    public static void updateInformationByUser(Connection connection) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Select a table to update:");
        System.out.println("1. Architect");
        System.out.println("2. Contractor");
        System.out.println("3. Customer");
        System.out.println("4. Project Manager");
        System.out.print("Enter the table number: ");

        int tableNumber = scanner.nextInt();
        scanner.nextLine();

        switch (tableNumber) {
            case 1:
                updateTableInformation(connection, "architect");
                break;
            case 2:
                updateTableInformation(connection, "contractor");
                break;
            case 3:
                updateTableInformation(connection, "customer");
                break;
            case 4:
                updateTableInformation(connection, "project_manager");
                break;

            default:
                System.out.println("Invalid table number. Please enter a valid number.");
        }
    }

    // SETTING THE VALUES TO UPDATE
    /**
     * Updates information in the specified table based on user input.
     *
     * @param connection Database connection.
     * @param tableName  Name of the table to update.
     */
    public static void updateTableInformation(Connection connection, String tableName) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the " + tableName + " id to update: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Enter " + tableName + " name: ");
        String name = scanner.nextLine();

        System.out.println("Enter " + tableName + " phone: ");
        String phone = scanner.nextLine();

        System.out.println("Enter " + tableName + " email: ");
        String email = scanner.nextLine();

        System.out.println("Enter " + tableName + " address: ");
        String address = scanner.nextLine();



        String updateQuery = "UPDATE " + tableName + " SET name=?, phone=?, email=?, address=? WHERE " + tableName + "_id=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, phone);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, address);

            preparedStatement.setInt(5, id);

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println(tableName + " information updated successfully.");
            } else {
                System.out.println("No " + tableName + " information found for the given id.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // UPDATE PROJECT VALUES
    /**
     * Updates project information based on user input.
     *
     * @param connection Database connection.
     */
    public static void updateProjectInformationValues(Connection connection) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter the project number you want to update: ");
        int projectNumber = sc.nextInt();
        sc.nextLine();

        System.out.println("Enter the updated project details: ");
        System.out.println("Enter the project name (leave blank to keep existing): ");
        String projectName = sc.nextLine();

        System.out.println("Enter Building type: ");
        String buildingType = sc.nextLine();

        System.out.println("Enter the address: ");
        String address = sc.nextLine();

        System.out.println("Enter the ERF number: ");
        String erfNumber = sc.nextLine();

        System.out.println("Enter the total fee: ");
        Double totalFee = sc.nextDouble();

        System.out.println("Enter the total paid: ");
        Double totalPaid = sc.nextDouble();

        System.out.println("Enter the date (YYYY-MM-DD): ");
        String dateString = sc.next();
        Date deadline = parseDate(dateString);

        sc.nextLine();

        System.out.println("Is the project finalized? (yes/no): ");
        String finalizationInput = sc.nextLine().toLowerCase();

        System.out.println("Enter the architect_id");
        int architect_id = sc.nextInt();


        System.out.println("Enter the contractor_id");
        int contractor_id = sc.nextInt();

        System.out.println("Enter the customer_id");
        int customer_id = sc.nextInt();

        System.out.println("Enter the manager_id");
        int manager_id = sc.nextInt();

        boolean isFinalized = finalizationInput.equals("yes");

        updateProjectInformation(connection, projectNumber, projectName, buildingType, address, erfNumber, totalFee, totalPaid, deadline, architect_id, contractor_id, customer_id, manager_id,isFinalized);
    }

    // STATUS FOR PROJECT
    /**
     * Updates the status of a project to 'finalized'.
     *
     * @param connection    Database connection.
     * @param projectNumber Project number to update.
     */


    // UPDATE MAIN TABLE
    /**
     * Updates project information in the database.
     *
     * @param connection      Database connection.
     * @param projectNumber   Project number to update.
     * @param projectName     Updated project name.
     * @param buildingType    Updated building type.
     * @param address         Updated project address.
     * @param erfNumber       Updated ERF number.
     * @param totalFee        Updated total fee.
     * @param totalPaid       Updated total amount paid.
     * @param deadline        Updated project deadline.
     * @param architectId     Updated architect ID.
     * @param contractorId    Updated contractor ID.
     * @param customerId      Updated customer ID.
     * @param managerId       Updated project manager ID.
     */
    public static void updateProjectInformation(Connection connection, int projectNumber, String projectName, String buildingType,
                                                String address, String erfNumber, Double totalFee, Double totalPaid, Date deadline,
                                                Integer architectId, Integer contractorId, Integer customerId, Integer managerId, boolean isFinalized) {
        String updateQuery;

        if (isFinalized) {
            updateQuery = "UPDATE projects SET project_name=?, building_type=?, address=?, erf_number=?, " +
                    "total_fee=?, total_paid=?, deadline=?, architect_id=?, contractor_id=?, customer_id=?, manager_id=?, " +
                    "status=?, date_of_completion=CURRENT_DATE WHERE project_number=?";
        } else {
            updateQuery = "UPDATE projects SET project_name=?, building_type=?, address=?, erf_number=?, " +
                    "total_fee=?, total_paid=?, deadline=?, architect_id=?, contractor_id=?, customer_id=?, manager_id=?, " +
                    "status=?, date_of_completion=NULL WHERE project_number=?";
        }



        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, projectName);
            preparedStatement.setString(2, buildingType);
            preparedStatement.setString(3, address);
            preparedStatement.setString(4, erfNumber);
            preparedStatement.setDouble(5, totalFee);
            preparedStatement.setDouble(6, totalPaid);
            preparedStatement.setDate(7, deadline);

            if (architectId != null) {
                preparedStatement.setInt(8, architectId);
            } else {
                preparedStatement.setNull(8, Types.INTEGER);
            }

            if (contractorId != null) {
                preparedStatement.setInt(9, contractorId);
            } else {
                preparedStatement.setNull(9, Types.INTEGER);
            }

            if (customerId != null) {
                preparedStatement.setInt(10, customerId);
            } else {
                preparedStatement.setNull(10, Types.INTEGER);
            }

            if (managerId != null) {
                preparedStatement.setInt(11, managerId);
            } else {
                preparedStatement.setNull(11, Types.INTEGER);
            }

            if (isFinalized) {
                preparedStatement.setString(12, "finalise");

            } else {
                preparedStatement.setNull(12, Types.VARCHAR);

            }

            preparedStatement.setInt(13, projectNumber);

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Project information updated successfully.");
            } else {
                System.out.println("No project found for the given project number.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    
    // SHOW PAST DUES PROJECTS
    /**
     * Displays past due projects from the database.
     *
     * @param connection Database connection.
     */
    public static void displayPastDueProjects(Connection connection) {
        String query = "SELECT * FROM projects WHERE (status IS NULL OR status != 'finalise') AND deadline < CURRENT_DATE";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            if (resultSet.next()) {
                System.out.println("Past Due Projects:");
                do {
                    int projectNumber = resultSet.getInt("project_number");
                    String projectName = resultSet.getString("project_name");
                    Date deadline = resultSet.getDate("deadline");

                    System.out.println("Project Number: " + projectNumber);
                    System.out.println("Project Name: " + projectName);
                    System.out.println("Deadline: " + new SimpleDateFormat("yyyy-MM-dd").format(deadline));
                    System.out.println("--------------");

                } while (resultSet.next());
            } else {
                System.out.println("No projects are past due.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // DELETE PROJECTS USING PROJECT NUMBER
    /**
     * Deletes a project and associated records from the database.
     *
     * @param connection Database connection.
     */
    public static void deleteProject(Connection connection) {
        System.out.println("Enter the project number ");
        Scanner sc = new Scanner(System.in);
        int projectNumber= sc.nextInt();
        try {
            String deleteQuery = "DELETE FROM projects WHERE project_number = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
                preparedStatement.setInt(1, projectNumber);
                int rowsDeleted = preparedStatement.executeUpdate();

                if (rowsDeleted > 0) {
                    System.out.println("Project and associated records deleted successfully.");
                } else {
                    System.out.println("No project found for the given project number.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // FIND INCOMPLETE PROJECTS
    /**
     * Finds and displays incomplete projects from the database.
     *
     * @param connection Database connection.
     */
    public static void findIncompleteProjects(Connection connection) {
        String query = "SELECT * FROM projects WHERE status IS NULL OR status != 'finalise'";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            if (resultSet.next()) {
                System.out.println("Incomplete Projects:");
                do {
                    int projectNumber = resultSet.getInt("project_number");
                    String projectName = resultSet.getString("project_name");

                    System.out.println("Project Number: " + projectNumber);
                    System.out.println("Project Name: " + projectName);
                    System.out.println("--------------");

                } while (resultSet.next());
            } else {
                System.out.println("All projects are completed.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }   
}

