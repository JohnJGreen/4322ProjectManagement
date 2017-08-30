package cse_4322_13.enb.Database;

public class Event {

    private long ID;
    private String name;
    private String description;
    private String date;
    private String location;
    private String clubName;
    private String startTime;
    private String endTime;

    public Event() {
        this.ID = -1;
        this.name = null;
        this.description = null;
        this.date = null;
        this.location = null;
        this.clubName = null;
        this.startTime = null;
        this.endTime = null;
    }

    public Event(String name, String description, String date, String location, String clubName, String startTime, String endTime) {

        this.name = name;
        this.description = description;
        this.date = date;
        this.location = location;
        this.clubName = clubName;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return name + "\nStart: " + startTime + " End: " + endTime + "\n" + date + " " + location;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) { this.location = location; }

    public String getClubName() { return clubName; }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

}
