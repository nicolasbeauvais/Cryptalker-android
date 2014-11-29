package tk.cryptalker.model;

import android.util.Base64;
import tk.cryptalker.application.CrypTalkerApplication;
import tk.cryptalker.util.AESUtil;

public class Message
{
    private String message;
    private String from;
    private String datetime;
    private int room_id;
    private boolean pending;
    private boolean fail;

    public Message()
    {
        this.pending = false;
        this.fail = false;
    }

    public String getMessage()
    {
        try {

            String key = CrypTalkerApplication.getUserInfo().getRoomById(room_id).getKey();
            byte[] cipherData = AESUtil.decrypt(Base64.decode(message, Base64.DEFAULT), key);

            return new String(cipherData, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }

    public boolean isFail() {
        return fail;
    }

    public void setFail(boolean fail) {
        this.fail = fail;
    }

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                ", from='" + from + '\'' +
                ", datetime='" + datetime + '\'' +
                ", room_id=" + room_id +
                ", pending=" + pending +
                ", fail=" + fail +
                '}';
    }
}
