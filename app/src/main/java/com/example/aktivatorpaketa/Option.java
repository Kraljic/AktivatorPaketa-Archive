package com.example.aktivatorpaketa;

import java.io.Serializable;
import java.util.List;

public class Option implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String title;
    private String description;

    private String number;
    private List<String> messageList;

    public Option(Long id, String title, String description, String number, List<String> messageList) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.number = number;
        this.messageList = messageList;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getNumber() {
        return number;
    }

    public List<String> getMessageList() {
        return messageList;
    }
}
