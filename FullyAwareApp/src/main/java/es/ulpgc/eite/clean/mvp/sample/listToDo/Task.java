package es.ulpgc.eite.clean.mvp.sample.listToDo;

import java.util.UUID;



public class Task {


    private String taskId;
    private int tagId;
    private String date;
    private String title;
    private String description;

    public Task(int tagId,  String title, String description, String date) {
        this.taskId= UUID.randomUUID().toString();
        this.tagId = tagId;
        this.date = date;
        this.title = title;
        this.description = description;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getTaskId() {
        return taskId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Task){
            Task task = (Task) obj;
            if(task.getTaskId() == getTaskId()){
                return true;
            }
        }
        return false;
    }
}
