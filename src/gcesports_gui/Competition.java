package gcesports_gui;

// Class representing the competition event and associated details.
public class Competition
{
    // Instance variables for the competition's attributes
    private String game;
    private String competitionDate;
    private String location;
    private String team;
    private int points;
    
    public Competition (String game, String competitionDate, String location, String team, int points)
    {
        // Constructor to initialize a Competition object with specific values.
        this.game = game;
        this.competitionDate = competitionDate;
        this.location = location;
        this.team = team;
        this.points = points;
    }
   
    // Getter method for the game attribute
    public String getGame()
    {
        return game;
    }
    
    // Getter method for the competition date attribute
    public String getCompetitionDate()
    {
        return competitionDate;
    }
    
    // Getter method for the location attribute
    public String getLocation()
    {
        return location;
    }
    
    // Getter method for the team attribute
    public String getTeam()
    {
        return team;
    }
    
    // Getter method for the points attribute
    public int getPoints()
    {
        return points;
    }
    
    // Setter method for the game attribute
    public void setGame(String game)
    {
        this.game = game;
    }
    
    // Setter method for the competition date attribute
    public void setCompetitionDate(String competitionDate)
    {
        this.competitionDate = competitionDate;
    }
    
    // Setter method for the location attribute
    public void setLocation(String location)
    {
        this.location = location;
    }
    
    // Setter method for the team attribute
    public void setTeam(String team)
    {
        this.team = team;
    }
    
    // Setter method for the points attribute
    public void setPoints(int points)
    {
        this.points = points;
    }
    
    /**
     * Converts the competition object into a CSV formatted string.
     * This method can be useful for saving the competition data to a file or displaying it in a readable format.
     * 
     * @return A CSV formatted string representing the competition.
     */
    @Override
    public String toString()
    {
        // Create a string representation of the competition object in CSV format
        String csvStr = game + "," + competitionDate + "," + location + "," + team + "," + points;
        return csvStr;  // Return the CSV formatted string
    }
}

