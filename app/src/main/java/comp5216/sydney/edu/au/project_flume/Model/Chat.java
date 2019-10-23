package comp5216.sydney.edu.au.project_flume.Model;


public class Chat {
    private String sender, receiver, message;
    private boolean isSeen;

    public Chat(String sender, String receiver, String message, boolean isSeen) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.isSeen = isSeen;

    }
    public Chat() { }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getIsSeen() { return isSeen; }

    public void setIsSeen(boolean seen) { isSeen = seen; }
}
