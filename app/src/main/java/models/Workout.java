package models;

import java.util.ArrayList;
import java.util.List;

public class Workout {

    public String id; //Primary Key
    public String user; //Foreign key
    public String title;
    public String description;

    List<String> exercises = new ArrayList<String>();
    List<Integer> sets = new ArrayList<Integer>();
    List<String> notes = new ArrayList<String>();

    public Workout() { //Necesario paraa Firestore
    }

    public List<String> getNotes() { return notes; }

    public void setNotes(List<String> notes) { this.notes = notes; }

    public List<String> getNotes() { return notes; }

    public void setNotes(List<String> notes) { this.notes = notes; }

    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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

    public List<String> getExercises() {
        return exercises;
    }

    public void setExercises(List<String> exercises) {
        this.exercises = exercises;
    }

    public List<Integer> getSets() {
        return sets;
    }

    public void setSets(List<Integer> sets) {
        this.sets = sets;

    }

    public Workout(String id, String user, String title, String description, List<String> exercises, List<Integer> sets, List <String> notes) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.description = description;
        this.exercises = exercises;
        this.sets = sets;
        this.notes = notes;
    }
}
