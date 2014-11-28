package tk.cryptalker.model;

import java.util.ArrayList;

public class Room
{
    private int id;
    private String name;
    private boolean isInvite;
    private int invite_id;
    private String lastMessage;
    private String key;
    private ArrayList<Message> messages;

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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isInvite=" + isInvite +
                ", invite_id=" + invite_id +
                ", lastMessage='" + lastMessage + '\'' +
                ", key='" + key + '\'' +
                ", messages=" + messages +
                '}';
    }
}
