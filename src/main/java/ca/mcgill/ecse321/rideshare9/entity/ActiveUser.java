package ca.mcgill.ecse321.rideshare9.entity;

public class ActiveUser {
    private long id;
    private String username;


    public ActiveUser(long id, String username) {
        this.id = id;
        this.username = username;

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
