package org.example.Data_Models;

import java.io.Serializable;

public final class SimpleTask extends Task implements Serializable {
    private int startHour;
    private int endHour;

    public SimpleTask(String taskName, int startHour, int endHour){
        super(taskName);
        this.startHour = startHour;
        this.endHour = endHour;
    }

    public int estimateDuration(){
        if(this.isCompleted())
            return endHour - startHour;
        return 0;
    }

    public boolean isCompleted() {
        if(this.getStatus().equals("Completed"))
            return true;
        return false;
    }

    public void completeTask(){
        this.setCompleted();
        if(this.getParentTask() instanceof ComplexTask complexTask)
            complexTask.completeTask();
    }

    @Override
    public String toString() {
        return this.getTaskName() + " (S)";
    }
}
