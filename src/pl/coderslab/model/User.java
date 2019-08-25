package pl.coderslab.model;

import pl.coderslab.utils.PasswordUtil;

public class User {
    private int id;
    private String userName;
    private String email;
    private String password;
    private int groupId;

    public User(String userName, String email, String password, int groupId) {
        this.userName = userName;
        this.email = email;
        this.password = PasswordUtil.createHash(password);
        this.groupId = groupId;
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
}