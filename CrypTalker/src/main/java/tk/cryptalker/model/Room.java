package tk.cryptalker.model;

public class Room
{
    private int id;
    private String name;
    private boolean isInvite;
    private int invite_id;
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

    public int getInviteId() {
        return invite_id;
    }

    public void setInviteId(int invite_id) {
        this.invite_id = invite_id;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
