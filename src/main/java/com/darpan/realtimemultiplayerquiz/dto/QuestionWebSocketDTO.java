package com.darpan.realtimemultiplayerquiz.dto;


import java.util.List;

public class QuestionWebSocketDTO {

    private int id;
    private String questionTitle;
    private List<String> options;

    public QuestionWebSocketDTO(String questionTitle, List<String> options, int id) {
        this.id = id;
        this.questionTitle = questionTitle;
        this.options = options;
    }

    public QuestionWebSocketDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }
}
