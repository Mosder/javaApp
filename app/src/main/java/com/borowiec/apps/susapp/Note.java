package com.borowiec.apps.susapp;

public class Note {
    private String title;
    private String content;
    private int color;
    private int id;
    private String filePath;

    public Note(String title, String content, int color, int id, String filePath) {
        this.title = title;
        this.content = content;
        this.color = color;
        this.id = id;
        this.filePath = filePath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
