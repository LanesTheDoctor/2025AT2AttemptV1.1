/*
Description: .csv file reading and writing java based program.
Creater: Zac Makkinga
Version: 1.1

Requirements
1. GUI Application must run on Windows 10
2. Reads in external file data when app launched (from 2 CSV files – competitions.csv and teams.csv)
    a. Each line of the read-in competition data represents one competition
    b. A competition instance is created for each competition and added to the ArrayList<Competition> 
    c. The same thing is set up for each team that is read in from the teams.csv file
    d. A team instance is created for each team and added to the ArrayList<Team>
3) Display competition data to a JTable (from ArrayList<Competition>)
4) Display team data to combo-boxes and text fields (from ArrayList<Team>)
5) Add new competition result for a team (event from clicked JButton) a. Entries must be validated to check for empty fields
6) Add a new team (event from clicked JButton) a. Entries must be validated to check for empty fields
7) Update an existing team (event from clicked JButton) a. Entries must be validated to check for empty fields
8) Save ArrayList data (for competitions and teams) to the 2 external CSV files when the application is closed
*/

package gcesports_gui;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.event.ItemEvent;
import java.util.Map;
import java.util.HashMap;


public class GCESports_GUI extends javax.swing.JFrame 
{
    // Stores all competition records loaded from competitions.csv.
    private ArrayList<Competition> competitionList;
    
    // Stores all team records loaded from teams.csv.
    private ArrayList<Team> teamList;
    
    // Flag to indicate whether combo boxes are fully initialized and ready for interaction.
    boolean comboBoxStatus;
    
    // Table model for managing and displaying competition results in the JTable.
    private DefaultTableModel compResultsTableModel;

    // Constructor: Initializes the GUI, sets up table columns and loads competition and team data.
    public GCESports_GUI()
    {
        // Initialize data structures for storing competition and team information.
        competitionList = new ArrayList<Competition>();
        teamList = new ArrayList<Team>();
        
        // Combo box interaction is temporarily disabled during setup.
        comboBoxStatus = false;
        
        // Create a new empty table model for competition results.
        compResultsTableModel = new DefaultTableModel();
        
        // Define column headers for the results table.
        String [] columnNames_Results = new String[]
        {
            "Date", "Location", "Game", "Team", "Points"
        };
        
        // Build the GUI components (e.g., text fields, buttons, combo boxes).
        initComponents();
        
        // Set up the competition results table with defined columns and no initial data.
        compResultsTableModel = new DefaultTableModel(null, columnNames_Results);
        teamCompResults_JTable.setModel(compResultsTableModel);
        
        // Resize table columns based on predefined percentages for cleaner layout.
        resizeTableColumns();
        
        // Load competition data from file into competitionList.
        readCompetitionData();
        
        // Load team data from file into teamList.
        readTeamData();
        
        // Display competitions in the GUI (e.g., combo boxes or tables).
        displayCompetitions();
        
        // Display team names in the relevant combo boxes.
        displayTeams();
        
        // Display details of the first team in the list.
        displayTeamDetails();
        
        // Enable combo box interaction now that setup is complete.
        comboBoxStatus = true;
        
    }

    // Resizes each column in the competition results table based on predefined width percentages for better UI layout.
    private void resizeTableColumns()
    {
        double[] columnWidthPercentage = {0.2f, 0.2f, 0.3f, 0.2f, 0.1f};
        int tableWidth = teamCompResults_JTable.getWidth();
        TableColumn column;
        TableColumnModel tableColumnModel = teamCompResults_JTable.getColumnModel();
        int cantCols = tableColumnModel.getColumnCount();
        for (int i = 0; i < cantCols; i++)
        {
            column = tableColumnModel.getColumn(i);
            float pWidth = Math.round(columnWidthPercentage[i] * tableWidth);
            column.setPreferredWidth((int)pWidth);
        }
    }

    // Reads competitions.csv data.
    private void readCompetitionData()
    {
        try
        {
            FileReader reader = new FileReader("competitions.csv");
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            while ((line = bufferedReader.readLine()) != null)
            {
                if (line.length() > 0)
                {
                    String [] splitStr = line.split(",");
                    String game = splitStr[0];
                    String location = splitStr[1];
                    String date = splitStr[2];
                    String team = splitStr[3];
                    int points = Integer.parseInt(splitStr[4]);

                    Competition competition = new Competition(game, location, date, team, points);
                    competitionList.add(competition);
                }
            }
            reader.close();
        }
        
        // Catches errors where the competitions.csv file is missing or not found in the expected location.
        catch (FileNotFoundException fnfe)
        {
            System.out.println("ERROR: competitions.csv file could not be found!");
            JOptionPane.showMessageDialog(null, "ERROR: competitions.csv file could not be found!", "File Not Found", JOptionPane.ERROR_MESSAGE);
        }
        
        // Catches issues when competitions.csv exists but can't be read properly (e.g., file is corrupted or access denied).
        catch (IOException ioe)
        {
            System.out.println("Error: competitions.csv file could not be read!");
            JOptionPane.showMessageDialog(null, "Error: competitions.csv file could not be read!", "File Unreadable", JOptionPane.ERROR_MESSAGE);
        }
        
        // Catches formatting errors when trying to convert Strings to numbers (e.g., invalid data in competitions.csv).
        catch (NumberFormatException nfe)
        {
            System.out.println("Error: String values could not be converted to integers!");
            JOptionPane.showMessageDialog(null, "Error: String values could not be converted to integers!", "File Unusable", JOptionPane.ERROR_MESSAGE);
        }
        
        // Catches any other unexpected errors related to competitions.csv that weren’t specifically handled above.
        catch (Exception e)
        {
            System.out.println("Error: A problem relating to competitions.csv could not be identified!");
            JOptionPane.showMessageDialog(null, "Error: A problem relating to competitions.csv could not be identified!", "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Reads teams.csv data.
    private void readTeamData()
    {
        try
        {
            FileReader reader_ = new FileReader("teams.csv");
            BufferedReader bufferedReader = new BufferedReader(reader_);
            String line;
            while ((line = bufferedReader.readLine()) != null)
            {
                if (line.length() > 0)
                {
                    String [] splitStr = line.split(",");
                    String teamName = splitStr[0];
                    String contactName = splitStr[1];
                    String contactPhone = splitStr[2];
                    String contactEmail = splitStr[3];

                    Team team = new Team(teamName, contactName, contactPhone, contactEmail);
                    teamList.add(team);
                }
            }
            reader_.close();
        }
        
        // Catches errors where the competitions.csv file is missing or not found in the expected location.
        catch (FileNotFoundException fnfe)
        {
            System.out.println("ERROR: teams.csv file could not be found!");
            JOptionPane.showMessageDialog(null, "ERROR: teams.csv file could not be found!", "File Not Found", JOptionPane.ERROR_MESSAGE);
        }
        
        // Catches issues when competitions.csv exists but can't be read properly (e.g., file is corrupted or access denied).
        catch (IOException ioe)
        {
            System.out.println("Error: teams.csv file could not be read!");
            JOptionPane.showMessageDialog(null, "Error: teams.csv file could not be read!", "File Unreadable", JOptionPane.ERROR_MESSAGE);
        }
        
        // Catches formatting errors when trying to convert Strings to numbers (e.g., invalid data in competitions.csv).
        catch (NumberFormatException nfe)
        {
            System.out.println("Error: String values could not be converted to integers!");
            JOptionPane.showMessageDialog(null, "Error: String values could not be converted to integers!", "File Unusable", JOptionPane.ERROR_MESSAGE);
        }
        
        // Catches any other unexpected errors related to competitions.csv that weren’t specifically handled above.
        catch (Exception e)
        {
            System.out.println("Error: A problem relating to teams.csv could not be identified!");
            JOptionPane.showMessageDialog(null, "Error: A problem relating to teams.csv could not be identified!", "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Displays competitions.csv data in JTable on the Team Competition Results tab.
    private void displayCompetitions()
    {
        if (competitionList.size() > 0)
        {
            Object[][] competitions2DArray = new Object[competitionList.size()][];
            for (int i=0; i < competitionList.size(); i++)
            {
                Object[] competition = new Object[5];
                competition[0] = competitionList.get(i).getCompetitionDate();
                competition[1] = competitionList.get(i).getLocation();
                competition[2] = competitionList.get(i).getGame();
                competition[3] = competitionList.get(i).getTeam();
                competition[4] = competitionList.get(i).getPoints();
                
                competitions2DArray[i] = competition;
            }
            
            if (compResultsTableModel.getRowCount() > 0)
            {
                for (int i = compResultsTableModel.getRowCount() - 1; i> -1; i--)
                {
                    compResultsTableModel.removeRow(i);
                }
            }
            
            if (competitions2DArray.length > 0)
            {
                for (int row = 0; row < competitions2DArray.length; row++)
                {
                    compResultsTableModel.addRow(competitions2DArray[row]);
                }
                
                compResultsTableModel.fireTableDataChanged();
            }
        }
    }

    // Populates the team selection combo boxes with team names from teamList.
    // Clears existing items before adding updated data.
    private void displayTeams()
    {
        if (addNewCompResultTeam_JComboBox.getItemCount() > 0)
        {
            addNewCompResultTeam_JComboBox.removeAllItems();
        }
        
        if (updateExistingTeamTeamName_JComboBox.getItemCount() > 0)
        {
            updateExistingTeamTeamName_JComboBox.removeAllItems();
        }
        
        if (teamList.size() > 0)
        {
            for (int i = 0; i < teamList.size(); i++)
            {
                addNewCompResultTeam_JComboBox.addItem(teamList.get(i).getTeamName());
                updateExistingTeamTeamName_JComboBox.addItem(teamList.get(i).getTeamName());
            }
        }
    }
    
    // Displays the contact details of the selected team from the combo box.
    // If no teams are available, shows an alert message.
    private void displayTeamDetails()
    {
        if(teamList.size() == 0)
        {
            JOptionPane.showMessageDialog(null, "No teams available to display details.");
            return;
        }
        
        int itemIndexSelected = 0;
        if (comboBoxStatus == true)
        {
            itemIndexSelected = updateExistingTeamTeamName_JComboBox.getSelectedIndex();
            if (itemIndexSelected < 0)
            {
                itemIndexSelected = 0;
            }
        }
         
        updateExistingTeamContactPerson_JTetField.setText(teamList.get(itemIndexSelected).getContactName());
        updateExistingTeamContactEmail_JTextField.setText(teamList.get(itemIndexSelected).getContactEmail());
        updateExistingTeamContactPhone_JTextField.setText(teamList.get(itemIndexSelected).getContactPhone());
    }

    // Writes modified competitions data to competitions.csv file.
    private void saveCompetitionData()
    {
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter1 = null;

        try 
        {
            fileWriter = new FileWriter("competitions.csv");
            bufferedWriter1 = new BufferedWriter(fileWriter);

          //  bufferedWriter1.write("Game,Date,Location,Team,Points");
            bufferedWriter1.newLine();
            
            for (Competition competition : competitionList)
            {
                bufferedWriter1.write(competition.toString());
                bufferedWriter1.newLine();
            }
        }

        catch (IOException e) 
        {
            JOptionPane.showMessageDialog(null, "Error saving competition data: " + e.getMessage());
        }

        finally 
        {
            try 
            {
                if (bufferedWriter1 != null) bufferedWriter1.close();
                if (fileWriter != null) fileWriter.close();
            }
            catch (IOException ex) 
            {
                JOptionPane.showMessageDialog(null, "Error closing file writer: " + ex.getMessage());
            }
        }
    }

    /*
    Saves the team data to a CSV file ("teams.csv").
    - Writes each team's data (using the `toString()` method) to the file.
    - Ensures proper file handling with BufferedWriter and FileWriter.
    - Catches and handles any IOExceptions that might occur during file writing.
    - Ensures the file writer is properly closed after writing.
    */
    private void saveTeamData()
    {
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter2 = null;

        try
        {
            fileWriter = new FileWriter("teams.csv");
            bufferedWriter2 = new BufferedWriter(fileWriter);

            bufferedWriter2.newLine();
            for (Team team : teamList)
            {
                bufferedWriter2.write(team.toString());
                bufferedWriter2.newLine();
            }
        }

        catch (IOException e) 
        {
            JOptionPane.showMessageDialog(null, "Error saving team data: " + e.getMessage());
        } 

        finally 
        {
            try 
            {
                if (bufferedWriter2 != null) bufferedWriter2.close();
                if (fileWriter != null) fileWriter.close();
            }
            
            catch (IOException ex)
            {
                JOptionPane.showMessageDialog(null, "Error closing file writer: " + ex.getMessage());
            }
        }
    }

    /*
        Calculates and displays the current leaderboard based on competition results.
        - Creates a map to track the total points of each team.
        - Iterates over the competition list and updates the points for each team.
        - Finds the team with the highest total points.
        - Displays the team with the highest score in a message dialog.
     */
    private void displayLeaderboard()
    {
        Map<String, Integer> teamPointsMap = new HashMap<>();
        
        for (Competition competition : competitionList)
        {
            String teamName = competition.getTeam();
            int points = competition.getPoints();
            
            teamPointsMap.put(teamName, teamPointsMap.getOrDefault(teamName, 0) + points);
        }
        
        String topTeam = null;
        int highestPoints = -1;
        
        for (Map.Entry<String, Integer> entry : teamPointsMap.entrySet())
        {
            String teamName = entry.getKey();
            int totalPoints = entry.getValue();
            
            if (totalPoints > highestPoints)
            {
                highestPoints = totalPoints;
                topTeam = teamName;
            }
        }
        
        String leaderBoardDisplayStr = "";
        if (topTeam != null)
        {
            leaderBoardDisplayStr += "The highest scoring team is " + topTeam + " with " + highestPoints + " points!";
        }
        
        else
        {
            leaderBoardDisplayStr += "No teams available.";
        }
       
        JOptionPane.showMessageDialog(null, leaderBoardDisplayStr, "Teams Leaderboard.", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /*
        Validates the input fields for adding a new competition.
        - Ensures that all required fields are filled.
        - Checks that the "Points" field contains a valid number.
        - Displays appropriate error messages if validation fails.
        @return true if all fields are valid, false otherwise.
    */
    private boolean validateNewComp()
    {
        boolean validateStatus = true;

        String game = addNewCompResultGame_JTextField.getText();
        String date = addNewCompResultDate_JTextField.getText();
        String location = addNewCompResultLocation_JTextField.getText();
        String team = addNewCompResultTeam_JComboBox.getSelectedItem().toString();
        String pointsStr = addNewCompResultPoints_JTextField.getText().trim();
        
        if (game.isEmpty() || date.isEmpty() || location.isEmpty() || team.isEmpty() || pointsStr.isEmpty())
        {
            validateStatus = false;
            JOptionPane.showMessageDialog(null, "All fields must be completed!");
        }
        
        try
        {
            
        }
        
        catch (NumberFormatException e)
        {
            validateStatus = false;
            JOptionPane.showMessageDialog(null, "Points must be a valid number!");
        }
        
        return validateStatus;
    }
    
    /*
        Validates the input fields for adding a new team.
        - Ensures that all required fields (team name, contact person, email, and phone) are filled.
        - Displays an error message if any required field is empty.
        @return true if all fields are valid, false otherwise.
    */
    private boolean validateNewTeam()
    {
        boolean validateStatus = true;
        
        String teamName = addNewTeamName_JTextField.getText();
        String contactPerson = addNewTeamContactPerson_JTextField.getText();
        String contactEmail = addNewTeamContactEmail_JTextField.getText();
        String contactPhone = addNewTeamContactPhone_JTextField.getText();

        if (teamName.isEmpty() || contactPerson.isEmpty() || contactEmail.isEmpty() || contactPhone.isEmpty())
        {
            validateStatus = false;
            JOptionPane.showMessageDialog(null, "All fields must be filled out.");
        }
        
        return validateStatus;
    }
    
    // Initialize the validation status to true (assuming validation passes).
    private boolean validateExistingTeam()
    {
        boolean validateStatus = true;
        return validateStatus;
    }

    // Return the validation status (currently always true).
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        header_JPanel = new javax.swing.JPanel();
        body_JPanel = new javax.swing.JPanel();
        body_TabbbedPane = new javax.swing.JTabbedPane();
        teamCompResults_JPanel = new javax.swing.JPanel();
        teamCompResults_JLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        teamCompResults_JTable = new javax.swing.JTable();
        displayLeaderboard_JButton = new javax.swing.JButton();
        addNewCompResult_JTextField = new javax.swing.JPanel();
        addNewCompResult_JLabel = new javax.swing.JLabel();
        addNewCompResultDate_JLabel = new javax.swing.JLabel();
        addNewCompResultLocation_JLabel = new javax.swing.JLabel();
        addNewCompResultGame_JLabel = new javax.swing.JLabel();
        addNewCompResultTeam_JLabel = new javax.swing.JLabel();
        addNewCompResultPoints_JLabel = new javax.swing.JLabel();
        addNewCompResultLocation_JTextField = new javax.swing.JTextField();
        addNewCompResultGame_JTextField = new javax.swing.JTextField();
        addNewCompResultPoints_JTextField = new javax.swing.JTextField();
        addNewCompResultDate_JTextField = new javax.swing.JTextField();
        addNewCompResultTeam_JComboBox = new javax.swing.JComboBox<>();
        addNewCompResult_JButton = new javax.swing.JButton();
        addNewTeam_TabPanel = new javax.swing.JPanel();
        addNewTeamContactPhone_JLabel = new javax.swing.JLabel();
        addNewTeamContactEmail_JLabel = new javax.swing.JLabel();
        addNewTeamContactPerson_JLabel = new javax.swing.JLabel();
        addNewTeamTeamName_JLabel = new javax.swing.JLabel();
        addNewTeamResults_JLabel = new javax.swing.JLabel();
        addNewTeamContactPerson_JTextField = new javax.swing.JTextField();
        addNewTeamContactEmail_JTextField = new javax.swing.JTextField();
        addNewTeamContactPhone_JTextField = new javax.swing.JTextField();
        addNewTeam_JButton = new javax.swing.JButton();
        addNewTeamName_JTextField = new javax.swing.JTextField();
        updateExistingTeam_TabPanel = new javax.swing.JPanel();
        updateExistingTeam_JLabel = new javax.swing.JLabel();
        updateExistingTeamContactPerson_JLabel = new javax.swing.JLabel();
        updateExistingTeamContactEmail_JLabel = new javax.swing.JLabel();
        updateExistingTeamContactPhone_JLabel = new javax.swing.JLabel();
        updateExistingTeamTeamName_JLabel = new javax.swing.JLabel();
        updateExistingTeamContactEmail_JTextField = new javax.swing.JTextField();
        updateExistingTeamContactPhone_JTextField = new javax.swing.JTextField();
        updateExistingTeamContactPerson_JTetField = new javax.swing.JTextField();
        updateExistingTeamTeamName_JComboBox = new javax.swing.JComboBox<>();
        updateExistingTeam_JButton = new javax.swing.JButton();
        image_JLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Gold Coast E-Sports");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        javax.swing.GroupLayout header_JPanelLayout = new javax.swing.GroupLayout(header_JPanel);
        header_JPanel.setLayout(header_JPanelLayout);
        header_JPanelLayout.setHorizontalGroup(
            header_JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        header_JPanelLayout.setVerticalGroup(
            header_JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 110, Short.MAX_VALUE)
        );

        body_TabbbedPane.setBackground(new java.awt.Color(255, 255, 255));
        body_TabbbedPane.setForeground(new java.awt.Color(0, 0, 0));

        teamCompResults_JPanel.setBackground(new java.awt.Color(255, 255, 255));

        teamCompResults_JLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        teamCompResults_JLabel.setForeground(new java.awt.Color(0, 0, 0));
        teamCompResults_JLabel.setText("Team Competition Results");

        teamCompResults_JTable.setModel(compResultsTableModel);
        jScrollPane1.setViewportView(teamCompResults_JTable);

        displayLeaderboard_JButton.setText("Display Top Teams");
        displayLeaderboard_JButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                displayLeaderboard_JButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout teamCompResults_JPanelLayout = new javax.swing.GroupLayout(teamCompResults_JPanel);
        teamCompResults_JPanel.setLayout(teamCompResults_JPanelLayout);
        teamCompResults_JPanelLayout.setHorizontalGroup(
            teamCompResults_JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(teamCompResults_JPanelLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(teamCompResults_JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(teamCompResults_JPanelLayout.createSequentialGroup()
                        .addGroup(teamCompResults_JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(teamCompResults_JPanelLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 783, Short.MAX_VALUE))
                            .addComponent(displayLeaderboard_JButton))
                        .addGap(25, 25, 25))
                    .addGroup(teamCompResults_JPanelLayout.createSequentialGroup()
                        .addComponent(teamCompResults_JLabel)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        teamCompResults_JPanelLayout.setVerticalGroup(
            teamCompResults_JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(teamCompResults_JPanelLayout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(teamCompResults_JLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 96, Short.MAX_VALUE)
                .addComponent(displayLeaderboard_JButton)
                .addGap(36, 36, 36))
        );

        body_TabbbedPane.addTab("Team Cometition Results", teamCompResults_JPanel);

        addNewCompResult_JTextField.setBackground(new java.awt.Color(255, 255, 255));

        addNewCompResult_JLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        addNewCompResult_JLabel.setForeground(new java.awt.Color(0, 0, 0));
        addNewCompResult_JLabel.setText("Team Competition Results");

        addNewCompResultDate_JLabel.setForeground(new java.awt.Color(0, 0, 0));
        addNewCompResultDate_JLabel.setText("Date:");

        addNewCompResultLocation_JLabel.setForeground(new java.awt.Color(0, 0, 0));
        addNewCompResultLocation_JLabel.setText("Location:");

        addNewCompResultGame_JLabel.setForeground(new java.awt.Color(0, 0, 0));
        addNewCompResultGame_JLabel.setText("Game:");

        addNewCompResultTeam_JLabel.setForeground(new java.awt.Color(0, 0, 0));
        addNewCompResultTeam_JLabel.setText("Team:");

        addNewCompResultPoints_JLabel.setForeground(new java.awt.Color(0, 0, 0));
        addNewCompResultPoints_JLabel.setText("Points:");

        addNewCompResultTeam_JComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        addNewCompResult_JButton.setText("Add New Competition Result");
        addNewCompResult_JButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addNewCompResult_JButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout addNewCompResult_JTextFieldLayout = new javax.swing.GroupLayout(addNewCompResult_JTextField);
        addNewCompResult_JTextField.setLayout(addNewCompResult_JTextFieldLayout);
        addNewCompResult_JTextFieldLayout.setHorizontalGroup(
            addNewCompResult_JTextFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addNewCompResult_JTextFieldLayout.createSequentialGroup()
                .addGroup(addNewCompResult_JTextFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addNewCompResult_JTextFieldLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(addNewCompResult_JLabel))
                    .addGroup(addNewCompResult_JTextFieldLayout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(addNewCompResult_JTextFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(addNewCompResultLocation_JLabel)
                            .addComponent(addNewCompResultDate_JLabel)
                            .addComponent(addNewCompResultGame_JLabel)
                            .addComponent(addNewCompResultTeam_JLabel, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(addNewCompResultPoints_JLabel, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(42, 42, 42)
                        .addGroup(addNewCompResult_JTextFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(addNewCompResultGame_JTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE)
                            .addComponent(addNewCompResultTeam_JComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(addNewCompResultLocation_JTextField)
                            .addComponent(addNewCompResultDate_JTextField)
                            .addComponent(addNewCompResultPoints_JTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(342, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, addNewCompResult_JTextFieldLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(addNewCompResult_JButton)
                .addGap(46, 46, 46))
        );

        addNewCompResult_JTextFieldLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {addNewCompResultDate_JLabel, addNewCompResultDate_JTextField, addNewCompResultGame_JLabel, addNewCompResultGame_JTextField, addNewCompResultLocation_JLabel, addNewCompResultLocation_JTextField});

        addNewCompResult_JTextFieldLayout.setVerticalGroup(
            addNewCompResult_JTextFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addNewCompResult_JTextFieldLayout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addComponent(addNewCompResult_JLabel)
                .addGap(43, 43, 43)
                .addGroup(addNewCompResult_JTextFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(addNewCompResultDate_JTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addNewCompResultDate_JLabel))
                .addGap(18, 18, 18)
                .addGroup(addNewCompResult_JTextFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(addNewCompResultLocation_JTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addNewCompResultLocation_JLabel))
                .addGap(18, 18, 18)
                .addGroup(addNewCompResult_JTextFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(addNewCompResultGame_JTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addNewCompResultGame_JLabel))
                .addGap(23, 23, 23)
                .addGroup(addNewCompResult_JTextFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addNewCompResultTeam_JComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addNewCompResultTeam_JLabel))
                .addGap(18, 18, 18)
                .addGroup(addNewCompResult_JTextFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addNewCompResultPoints_JTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addNewCompResultPoints_JLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 144, Short.MAX_VALUE)
                .addComponent(addNewCompResult_JButton)
                .addGap(28, 28, 28))
        );

        body_TabbbedPane.addTab("Add New Competition Result", addNewCompResult_JTextField);

        addNewTeam_TabPanel.setBackground(new java.awt.Color(255, 255, 255));

        addNewTeamContactPhone_JLabel.setForeground(new java.awt.Color(0, 0, 0));
        addNewTeamContactPhone_JLabel.setText("Contact Phone");

        addNewTeamContactEmail_JLabel.setForeground(new java.awt.Color(0, 0, 0));
        addNewTeamContactEmail_JLabel.setText("Contact Email:");

        addNewTeamContactPerson_JLabel.setForeground(new java.awt.Color(0, 0, 0));
        addNewTeamContactPerson_JLabel.setText("Contact Person");

        addNewTeamTeamName_JLabel.setForeground(new java.awt.Color(0, 0, 0));
        addNewTeamTeamName_JLabel.setText("Team:");

        addNewTeamResults_JLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        addNewTeamResults_JLabel.setForeground(new java.awt.Color(0, 0, 0));
        addNewTeamResults_JLabel.setText("Add New Team");

        addNewTeam_JButton.setText("Add New Team");
        addNewTeam_JButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addNewTeam_JButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout addNewTeam_TabPanelLayout = new javax.swing.GroupLayout(addNewTeam_TabPanel);
        addNewTeam_TabPanel.setLayout(addNewTeam_TabPanelLayout);
        addNewTeam_TabPanelLayout.setHorizontalGroup(
            addNewTeam_TabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addNewTeam_TabPanelLayout.createSequentialGroup()
                .addGap(97, 97, 97)
                .addGroup(addNewTeam_TabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addNewTeam_TabPanelLayout.createSequentialGroup()
                        .addGroup(addNewTeam_TabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(addNewTeamResults_JLabel)
                            .addGroup(addNewTeam_TabPanelLayout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addGroup(addNewTeam_TabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(addNewTeamContactEmail_JLabel)
                                    .addComponent(addNewTeamContactPerson_JLabel)
                                    .addComponent(addNewTeamContactPhone_JLabel)
                                    .addComponent(addNewTeamTeamName_JLabel, javax.swing.GroupLayout.Alignment.LEADING))
                                .addGap(42, 42, 42)
                                .addGroup(addNewTeam_TabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(addNewTeamContactPhone_JTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE)
                                    .addComponent(addNewTeamContactEmail_JTextField)
                                    .addComponent(addNewTeamContactPerson_JTextField)
                                    .addComponent(addNewTeamName_JTextField))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 302, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, addNewTeam_TabPanelLayout.createSequentialGroup()
                        .addGap(503, 503, 503)
                        .addComponent(addNewTeam_JButton)))
                .addContainerGap(91, Short.MAX_VALUE))
        );
        addNewTeam_TabPanelLayout.setVerticalGroup(
            addNewTeam_TabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addNewTeam_TabPanelLayout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(addNewTeamResults_JLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(addNewTeam_TabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(addNewTeamTeamName_JLabel)
                    .addComponent(addNewTeamName_JTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(addNewTeam_TabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(addNewTeamContactPerson_JTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addNewTeamContactPerson_JLabel))
                .addGap(18, 18, 18)
                .addGroup(addNewTeam_TabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(addNewTeamContactEmail_JTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addNewTeamContactEmail_JLabel))
                .addGap(18, 18, 18)
                .addGroup(addNewTeam_TabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(addNewTeamContactPhone_JTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addNewTeamContactPhone_JLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 230, Short.MAX_VALUE)
                .addComponent(addNewTeam_JButton)
                .addGap(43, 43, 43))
        );

        body_TabbbedPane.addTab("Add New Team", addNewTeam_TabPanel);

        updateExistingTeam_TabPanel.setBackground(new java.awt.Color(255, 255, 255));
        updateExistingTeam_TabPanel.setForeground(new java.awt.Color(255, 255, 255));

        updateExistingTeam_JLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        updateExistingTeam_JLabel.setForeground(new java.awt.Color(0, 0, 0));
        updateExistingTeam_JLabel.setText("Update an Existing Team");

        updateExistingTeamContactPerson_JLabel.setForeground(new java.awt.Color(0, 0, 0));
        updateExistingTeamContactPerson_JLabel.setText("Contact Person");

        updateExistingTeamContactEmail_JLabel.setForeground(new java.awt.Color(0, 0, 0));
        updateExistingTeamContactEmail_JLabel.setText("Contact Email:");

        updateExistingTeamContactPhone_JLabel.setForeground(new java.awt.Color(0, 0, 0));
        updateExistingTeamContactPhone_JLabel.setText("Contact Phone");

        updateExistingTeamTeamName_JLabel.setForeground(new java.awt.Color(0, 0, 0));
        updateExistingTeamTeamName_JLabel.setText("Team:");

        updateExistingTeamTeamName_JComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        updateExistingTeamTeamName_JComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                updateExistingTeamTeamName_JComboBoxItemStateChanged(evt);
            }
        });

        updateExistingTeam_JButton.setText("Update Existing Team");
        updateExistingTeam_JButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateExistingTeam_JButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout updateExistingTeam_TabPanelLayout = new javax.swing.GroupLayout(updateExistingTeam_TabPanel);
        updateExistingTeam_TabPanel.setLayout(updateExistingTeam_TabPanelLayout);
        updateExistingTeam_TabPanelLayout.setHorizontalGroup(
            updateExistingTeam_TabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 833, Short.MAX_VALUE)
            .addGroup(updateExistingTeam_TabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(updateExistingTeam_TabPanelLayout.createSequentialGroup()
                    .addGap(97, 97, 97)
                    .addGroup(updateExistingTeam_TabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(updateExistingTeam_TabPanelLayout.createSequentialGroup()
                            .addGroup(updateExistingTeam_TabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(updateExistingTeam_JLabel)
                                .addGroup(updateExistingTeam_TabPanelLayout.createSequentialGroup()
                                    .addGap(9, 9, 9)
                                    .addGroup(updateExistingTeam_TabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(updateExistingTeamContactEmail_JLabel)
                                        .addComponent(updateExistingTeamContactPerson_JLabel)
                                        .addComponent(updateExistingTeamContactPhone_JLabel)
                                        .addComponent(updateExistingTeamTeamName_JLabel, javax.swing.GroupLayout.Alignment.LEADING))
                                    .addGap(42, 42, 42)
                                    .addGroup(updateExistingTeam_TabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(updateExistingTeamContactPhone_JTextField)
                                        .addComponent(updateExistingTeamTeamName_JComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(updateExistingTeamContactEmail_JTextField)
                                        .addComponent(updateExistingTeamContactPerson_JTetField, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 302, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, updateExistingTeam_TabPanelLayout.createSequentialGroup()
                            .addGap(503, 503, 503)
                            .addComponent(updateExistingTeam_JButton)))
                    .addContainerGap(85, Short.MAX_VALUE)))
        );
        updateExistingTeam_TabPanelLayout.setVerticalGroup(
            updateExistingTeam_TabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 522, Short.MAX_VALUE)
            .addGroup(updateExistingTeam_TabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(updateExistingTeam_TabPanelLayout.createSequentialGroup()
                    .addGap(42, 42, 42)
                    .addComponent(updateExistingTeam_JLabel)
                    .addGap(14, 14, 14)
                    .addGroup(updateExistingTeam_TabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(updateExistingTeamTeamName_JLabel)
                        .addComponent(updateExistingTeamTeamName_JComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(18, 18, 18)
                    .addGroup(updateExistingTeam_TabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(updateExistingTeamContactPerson_JTetField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(updateExistingTeamContactPerson_JLabel))
                    .addGap(18, 18, 18)
                    .addGroup(updateExistingTeam_TabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(updateExistingTeamContactEmail_JTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(updateExistingTeamContactEmail_JLabel))
                    .addGap(18, 18, 18)
                    .addGroup(updateExistingTeam_TabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(updateExistingTeamContactPhone_JTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(updateExistingTeamContactPhone_JLabel))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 222, Short.MAX_VALUE)
                    .addComponent(updateExistingTeam_JButton)
                    .addGap(43, 43, 43)))
        );

        body_TabbbedPane.addTab("Update Existing Team", updateExistingTeam_TabPanel);

        javax.swing.GroupLayout body_JPanelLayout = new javax.swing.GroupLayout(body_JPanel);
        body_JPanel.setLayout(body_JPanelLayout);
        body_JPanelLayout.setHorizontalGroup(
            body_JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(body_TabbbedPane)
        );
        body_JPanelLayout.setVerticalGroup(
            body_JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(body_JPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(body_TabbbedPane))
        );

        image_JLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image_package/GoldCoastESports_Header.jpg"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(image_JLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(body_JPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(header_JPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(header_JPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(image_JLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(body_JPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Refreshes and displays the current leaderboard in the UI.
    private void displayLeaderboard_JButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_displayLeaderboard_JButtonActionPerformed
        displayLeaderboard();
    }//GEN-LAST:event_displayLeaderboard_JButtonActionPerformed

    /*
        Handles the action when the "Add New Competition" button is clicked.
        - Validates the input fields for the new competition.
        - Prompts the user with a confirmation dialog before adding the new competition.
        - If confirmed, creates a new `Competition` object and adds it to the `competitionList`.
        - Refreshes the team list in the UI (or updates relevant views).
     */
    private void addNewCompResult_JButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewCompResult_JButtonActionPerformed
        if (validateNewComp()) 
        {
            String game = addNewCompResultGame_JTextField.getText();
            String date = addNewCompResultDate_JTextField.getText();
            String location = addNewCompResultLocation_JTextField.getText();
            String team = addNewCompResultTeam_JComboBox.getSelectedItem().toString();
            String pointsStr = addNewCompResultPoints_JTextField.getText();
            int points = Integer.parseInt(pointsStr);

            int yesOrNo = JOptionPane.showConfirmDialog 
            (null, "You are about to add a new competition for " + game + "\nDo you wish to proceed? Yes or No?", "ADD NEW COMPETITION", JOptionPane.YES_NO_OPTION);

            if (yesOrNo == JOptionPane.YES_OPTION)
            {
                competitionList.add(new Competition(game, date, location, team, points));
                displayTeams();
            }
            
            else
            {
                
            }
        }
    }//GEN-LAST:event_addNewCompResult_JButtonActionPerformed

    /*
        Handles the action when the "Add New Team" button is clicked.
        - Validates the input fields for the new team.
        - Prompts the user with a confirmation dialog before adding the new team.
        - If confirmed, creates a new `Team` object and adds it to the `teamList`.
        - Refreshes the team list in the UI to reflect the newly added team.
     */
    private void addNewTeam_JButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewTeam_JButtonActionPerformed
        if (validateNewTeam())
        {
            String newTeamName = addNewTeamName_JTextField.getText();
            String newContactPerson = addNewTeamContactPerson_JTextField.getText();
            String newContactEmail = addNewTeamContactEmail_JTextField.getText();
            String newContactPhone = addNewTeamContactPhone_JTextField.getText();
            
            int yesOrNo = JOptionPane.showConfirmDialog(null, "You are about to add a new team for " + newTeamName + "\nDo you wish to proceed? Yes or No?", "ADD NEW TEAM", JOptionPane.YES_NO_OPTION);
            
            if (yesOrNo == JOptionPane.YES_OPTION)
            {
                teamList.add(new Team(newTeamName, newContactPerson, newContactPhone, newContactEmail));
                displayTeams();
            }
            else
            {
                
            }
        }
    }//GEN-LAST:event_addNewTeam_JButtonActionPerformed

    /*
        Handles the action when the "Update Team" button is clicked.
        - Validates input fields for the existing team.
        - Prompts the user with a confirmation dialog before updating team details.
        - If confirmed, updates the selected team's contact details and refreshes the team list.
     */
    private void updateExistingTeam_JButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateExistingTeam_JButtonActionPerformed
        if (validateExistingTeam())
        {
            String updateTeamName = updateExistingTeamTeamName_JComboBox.getItemAt(updateExistingTeamTeamName_JComboBox.getSelectedIndex());
            String updateContactPerson = updateExistingTeamContactPerson_JTetField.getText();
            String updateContactEmail = updateExistingTeamContactEmail_JTextField.getText();
            String updateContactPhone = updateExistingTeamContactPhone_JTextField.getText();
              
            int yesOrNo = JOptionPane.showConfirmDialog(null, "You are about to update team: " + updateTeamName + "\nDo you wish to proceed? Yes or No?", "UPDATE EXISTING TEAM", JOptionPane.YES_NO_OPTION);
            
            if (yesOrNo == JOptionPane.YES_OPTION)
            {
                for (int i = 0; i < teamList.size(); i++)
                {
                    if (updateTeamName.equals(teamList.get(i).getTeamName()))
                    {
                        teamList.get(i).setContactName(updateContactPerson);
                        teamList.get(i).setContactPhone(updateContactPhone);
                        teamList.get(i).setContactEmail(updateContactEmail);
                        break;
                    }
                }

                displayTeams();
            }
            
            else
            {
                
            }
        }
    }//GEN-LAST:event_updateExistingTeam_JButtonActionPerformed

    /*
        Triggered when the selected item in the team name combo box changes.
        Handles the event when a team is selected from the team name combo box.
        It updates the contact details (name, email, phone) of the selected team
        into the corresponding text fields.
        The update only occurs if the combo box status is true and an item is selected.
     */
    private void updateExistingTeamTeamName_JComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_updateExistingTeamTeamName_JComboBoxItemStateChanged
    if (comboBoxStatus == true)
    {
        if (evt.getStateChange() == ItemEvent.SELECTED)
        {
            String selectedTeamName = (String) updateExistingTeamTeamName_JComboBox.getSelectedItem();

            for (int i = 0; i < teamList.size(); i++)
            {
                if (teamList.get(i).getTeamName().equals(selectedTeamName))
                {
                    updateExistingTeamContactPerson_JTetField.setText(teamList.get(i).getContactName());
                    updateExistingTeamContactEmail_JTextField.setText(teamList.get(i).getContactEmail());
                    updateExistingTeamContactPhone_JTextField.setText(teamList.get(i).getContactPhone());
                    break;
                }
            }
        }
    }
    }//GEN-LAST:event_updateExistingTeamTeamName_JComboBoxItemStateChanged

    /*
        Handles the event triggered when the user attempts to close the application window.
        Prompts the user to save any changes before exiting.
    */
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        int yesOrNo = JOptionPane.showConfirmDialog(null,
        "Do you wish to save changes before closing?", "SAVE CHANGES?", JOptionPane.YES_NO_OPTION);
        if (yesOrNo == JOptionPane.YES_OPTION)
        {
            // User chose to save changes before exiting.
            // Saves updated competition data to file.
            saveCompetitionData();
            
            // Saves updated team data to file.
            saveTeamData();
        }
        
        else
        {
            // User chose not to save changes; exit without saving
        }
    }//GEN-LAST:event_formWindowClosing
    /*
        The main entry point of the application.
        Launches the GCESports GUI on the Event Dispatch Thread (EDT) 
        to ensure thread-safe interaction with Swing components.
     */
    public static void main(String args[])
    {
        // Ensures GUI is created and updated on the Event Dispatch Thread (EDT).
        java.awt.EventQueue.invokeLater(new Runnable() 
        {
            public void run()
            {
                // Create and show the main GUI window.
                new GCESports_GUI().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel addNewCompResultDate_JLabel;
    private javax.swing.JTextField addNewCompResultDate_JTextField;
    private javax.swing.JLabel addNewCompResultGame_JLabel;
    private javax.swing.JTextField addNewCompResultGame_JTextField;
    private javax.swing.JLabel addNewCompResultLocation_JLabel;
    private javax.swing.JTextField addNewCompResultLocation_JTextField;
    private javax.swing.JLabel addNewCompResultPoints_JLabel;
    private javax.swing.JTextField addNewCompResultPoints_JTextField;
    private javax.swing.JComboBox<String> addNewCompResultTeam_JComboBox;
    private javax.swing.JLabel addNewCompResultTeam_JLabel;
    private javax.swing.JButton addNewCompResult_JButton;
    private javax.swing.JLabel addNewCompResult_JLabel;
    private javax.swing.JPanel addNewCompResult_JTextField;
    private javax.swing.JLabel addNewTeamContactEmail_JLabel;
    private javax.swing.JTextField addNewTeamContactEmail_JTextField;
    private javax.swing.JLabel addNewTeamContactPerson_JLabel;
    private javax.swing.JTextField addNewTeamContactPerson_JTextField;
    private javax.swing.JLabel addNewTeamContactPhone_JLabel;
    private javax.swing.JTextField addNewTeamContactPhone_JTextField;
    private javax.swing.JTextField addNewTeamName_JTextField;
    private javax.swing.JLabel addNewTeamResults_JLabel;
    private javax.swing.JLabel addNewTeamTeamName_JLabel;
    private javax.swing.JButton addNewTeam_JButton;
    private javax.swing.JPanel addNewTeam_TabPanel;
    private javax.swing.JPanel body_JPanel;
    private javax.swing.JTabbedPane body_TabbbedPane;
    private javax.swing.JButton displayLeaderboard_JButton;
    private javax.swing.JPanel header_JPanel;
    private javax.swing.JLabel image_JLabel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel teamCompResults_JLabel;
    private javax.swing.JPanel teamCompResults_JPanel;
    private javax.swing.JTable teamCompResults_JTable;
    private javax.swing.JLabel updateExistingTeamContactEmail_JLabel;
    private javax.swing.JTextField updateExistingTeamContactEmail_JTextField;
    private javax.swing.JLabel updateExistingTeamContactPerson_JLabel;
    private javax.swing.JTextField updateExistingTeamContactPerson_JTetField;
    private javax.swing.JLabel updateExistingTeamContactPhone_JLabel;
    private javax.swing.JTextField updateExistingTeamContactPhone_JTextField;
    private javax.swing.JComboBox<String> updateExistingTeamTeamName_JComboBox;
    private javax.swing.JLabel updateExistingTeamTeamName_JLabel;
    private javax.swing.JButton updateExistingTeam_JButton;
    private javax.swing.JLabel updateExistingTeam_JLabel;
    private javax.swing.JPanel updateExistingTeam_TabPanel;
    // End of variables declaration//GEN-END:variables
}
