package models;

public class Event {

    public String user; //Foreign key
    public String dateEvent;
    public String nameEvent;

    public Event(String user, String dateEvent, String nameEvent) {
        this.user = user;
        this.dateEvent = dateEvent;
        this.nameEvent = nameEvent;
    }

    public Event() { //Necesario para Firestore
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDateEvent() {
        return dateEvent;
    }

    public void setDateEvent(String dateEvent) {
        this.dateEvent = dateEvent;
    }

    public String getNameEvent() {
        return nameEvent;
    }

    public void setNameEvent(String nameEvent) {
        this.nameEvent = nameEvent;
    }
}
