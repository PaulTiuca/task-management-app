package org.example.Data_Access;

import org.example.Business.TasksManagement;

import java.io.*;

public class SerializationHandler {
    private static final String FILE_NAME = "AppData.bin";

    public static void saveData(TasksManagement tasksManagement){
        try(ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            objectOutputStream.writeObject(tasksManagement);
            System.out.println("Data saved successfully!");
        }
        catch (IOException e){
            System.err.println("ERROR! Data could not be saved!");
        }
    }

    public static TasksManagement loadData(){
        try(ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            System.out.println("Data loaded successfully!");
            return (TasksManagement) objectInputStream.readObject();
        }
        catch(IOException | ClassNotFoundException e) {
            System.out.println("Data could not be found. Initializing...");
            return null;
        }
    }
}
