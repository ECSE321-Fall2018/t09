package ca.mcgill.ecse321.rideshare9.entity;

public class ActiveUser {
    private long id;
    private String username;
    private String status;

    public ActiveUser(long id, String username, String status) {
        this.id = id;
        this.username = username;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
