package org.example;

public sealed abstract class Task permits ComplexTask, SimpleTask {
    private String status;
    private String taskName;
    private Task parentTask;

    public Task(String taskName){
        this.status = "Uncompleted";
        this.taskName = taskName;
        this.parentTask = null;
    }

    public abstract int estimateDuration();

    public abstract boolean isCompleted();

    public abstract void completeTask();

    public void setCompleted(){
        this.status = "Completed";
    }

    public String getStatus(){
        return status;
    }

    public Task getParentTask() {
        return parentTask;
    }

    public String getTaskName(){
        return taskName;
    }

    public String getNameWithStatus(){
        return taskName + " [" + status + "]";
    }

    public void setParentTask(ComplexTask parentTask) {
        this.parentTask = parentTask;
    }
}
