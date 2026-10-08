package com.example.myapplication.model;

public class Message {
    private String text;
    private String time;
    private boolean isMe;
    private String profileImage;
    private long timestamp;

    public Message(String text, String time, boolean isMe) {
        this.text = text;
        this.time = time;
        this.isMe = isMe;
    }

    public boolean isSent() {
        return isMe;
    }


    public String getText() { return text; }
    public String getTime() { return time; }
    public boolean isMe() { return isMe; }

    public String getProfileImage() { return profileImage; }
    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long ts) { timestamp = ts; }
}
