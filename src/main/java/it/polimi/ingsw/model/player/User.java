package it.polimi.ingsw.model.player;

public class User {
    private final String username;

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof User))
            return false;
        User u = (User) obj;
        return username.equals(u.username);
    }


}
