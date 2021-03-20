package models;

public class Workout {

    public String id; //Primary Key
    public String user; //Foreign key
    public String title;
    public String description;
    public String exercises;
    public int numberOfSets;

    public Workout() { //Necesario la Firestore
    }

    public int getNumberOfSets() {
        return numberOfSets;
    }

    public void setNumberOfSets(int numberOfSets) {
        this.numberOfSets = numberOfSets;
    }

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

    public String getExercises() {
        return exercises;
    }

    public void setExercises(String exercises) {
        this.exercises = exercises;
    }

    public Workout(String id, String user, String title, String description, String exercises, int numberOfSets) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.description = description;
        this.exercises = exercises;
        this.numberOfSets = numberOfSets;
    }
}
