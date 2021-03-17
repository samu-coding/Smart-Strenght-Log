package models;

import java.util.Date;

public class Set {

    private String id; //Primary Key
    private String exercise; //Foreign key: id del ejercicio
    private String workout; //Foreign key
    private String user; //Foreign key
    private int reps;
    private float weight;
    private int rir;
    private int rpe;
    private String notes;
    private Date date;

}
