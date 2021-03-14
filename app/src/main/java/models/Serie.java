package models;

import java.util.Date;

public class Serie {

    private String id; //Primary Key
    private String ejercicio; //Foreign key: id del ejercicio
    private int reps;
    private String user; //Foreign key
    private float weight;
    private String workout; //Foreign key
    private int rir;
    private int rpe;
    private String notes;
    private Date fecha;

}
