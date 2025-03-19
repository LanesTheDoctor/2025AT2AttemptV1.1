package gcesports_gui;

// Class representing a team with associated details like name, contact person, phone, and email.
public class Team
{
    // Instance variables for the team's attributes
    private String teamName;
    private String contactName;
    private String contactPhone;
    private String contactEmail;
    
    // Constructor to initialize a Team object with specific values.
    public Team (String teamName, String contactName, String contactPhone, String contactEmail)
    {
        this.teamName = teamName;
        this.contactName = contactName;
        this.contactPhone = contactPhone;
        this.contactEmail = contactEmail;
    }
    
    /**
     * Default constructor that initializes a Team object with test values.
     * Used when a default team is needed (e.g., for testing).
     */
    public Team()
    {
        this.teamName = "Test team";        // Default team name for testing
        this.contactName = "Test contact name";  // Default contact name for testing
        this.contactPhone = "0404";         // Default phone number for testing
        this.contactEmail = "test@gmail.com"; // Default email for testing
    }
    
    // Getter method for the teamName attribute
    public String getTeamName()
    {
        return teamName;
    }
    
    // Getter method for the contactName attribute
    public String getContactName()
    {
        return contactName;
    }
    
    // Getter method for the contactPhone attribute
    public String getContactPhone()
    {
        return contactPhone;
    }
    
    // Getter method for the contactEmail attribute
    public String getContactEmail()
    {
        return contactEmail;
    }
    
    // Setter method for the teamName attribute
    public void setTeamName(String teamName)
    {
        this.teamName = teamName;
    }
    
    // Setter method for the contactName attribute
    public void setContactName(String contactName)
    {
        this.contactName = contactName;
    }
    
    // Setter method for the contactPhone attribute
    public void setContactPhone(String contactPhone)
    {
        this.contactPhone = contactPhone;
    }
    
    // Setter method for the contactEmail attribute
    public void setContactEmail(String contactEmail)
    {
        this.contactEmail = contactEmail;
    }
    
    /**
     * Converts the team object into a CSV formatted string.
     * This method is useful for saving the team data to a file or displaying it in a readable format.
     * 
     * @return A CSV formatted string representing the team.
     */
    @Override
    public String toString()
    {
        // Create a string representation of the team object in CSV format
        String csvStr = teamName + "," + contactName + "," + contactPhone + "," + contactEmail;
        return csvStr;  // Return the CSV formatted string
    }
}