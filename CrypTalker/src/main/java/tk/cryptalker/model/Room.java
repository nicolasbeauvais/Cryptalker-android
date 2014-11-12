package tk.cryptalker.model;

public class Room
{
    private int id;
    private String name;
    private boolean isInvite;
    private String lastMessage;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isInvite() {
        return isInvite;
    }

    public void setInvite(boolean isInvite) {
        this.isInvite = isInvite;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
