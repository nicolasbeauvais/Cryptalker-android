package tk.cryptalker.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserInfo
{
    private ArrayList<Room> friendRequestReceived = new ArrayList<Room>();
    private ArrayList<Room> rooms = new ArrayList<Room>();

    public ArrayList<Room> getFriendRequestReceived()
    {
        return friendRequestReceived;
    }

    public void setFriendRequestReceived(JSONArray friendRequestReceived)
    {

        for (int i = 0; i < friendRequestReceived.length(); i++) {

            try {
                JSONObject obj = friendRequestReceived.getJSONObject(i);
                Room room = new Room();

                room.setName(obj.getString("pseudo"));
                room.setInvite(true);
                room.setInviteId(obj.getInt("id"));

                this.friendRequestReceived.add(room);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<Room> getRooms()
    {
        return rooms;
    }

    public void setRooms(JSONArray rooms)
    {

        for (int i = 0; i < rooms.length(); i++) {

            try {
                JSONObject obj = rooms.getJSONObject(i);
                Room room = new Room();

                room.setId(obj.getInt("room_id"));
                room.setName(obj.getString("name"));

                JSONArray arrayMessages = obj.getJSONArray("messages");
                ArrayList<Message> messages = new ArrayList<Message>();

                for (int j = 0; j < arrayMessages.length(); j++) {
                    Message message = new Message();

                    JSONObject objMessage = (JSONObject)arrayMessages.get(j);

                    message.setMessage(objMessage.getString("message"));
                    message.setFrom(objMessage.getString("from"));
                    message.setDatetime(objMessage.getString("datetime"));

                    messages.add(message);
                }

                room.setMessages(messages);

                room.setInvite(false);

                this.rooms.add(room);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<Message> getMessagesByRoomId(int id)
    {
        for (int i = 0; i < rooms.size(); i++) {
            Room room = rooms.get(i);

            if (room.getId() == id) {
                return room.getMessages();
            }
        }

        return null;
    }

    public void addMessageToRoom(int roomId, Message message)
    {
        for (int i = 0; i < rooms.size(); i++) {
            Room room = rooms.get(i);

            if (room.getId() == roomId) {
                ArrayList<Message> roomMessages = room.getMessages();
                roomMessages.add(message);

                room.setMessages(roomMessages);

                rooms.set(i, room);
            }
        }
    }
}
