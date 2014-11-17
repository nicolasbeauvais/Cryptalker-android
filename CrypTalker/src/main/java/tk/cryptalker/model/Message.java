package tk.cryptalker.model;

public class Message
{
    private String message;
    private int room_id;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }
}
