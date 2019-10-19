package comp5216.sydney.edu.au.project_flume;

import java.util.Date;

public class Chat_message {
    private String mText, mUser;
    private long mTime;

    public Chat_message(String text, String mUser) {
        mText = text;
        this.mUser = mUser;
        mTime = new Date().getTime();
    }

    public Chat_message() { }

    public String getMessageText() { return mText; }
    public void setMessageText( String text ){ mText = text; }

    public String getUser() { return mUser;}
    public void setUser( String user ) { this.mUser = user; }

    public long getTime() { return mTime; }
    public void setTime( long time ) { mTime = time; }
}
