package tk.cryptalker.event;

import tk.cryptalker.model.Message;

public class MessageEvent
{
    private Message message;

    public MessageEvent(Message message)
    {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
