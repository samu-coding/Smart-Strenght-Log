package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class History {

    public String id; //Primary Key
    public String user; //Foreign key
    public String workout; //Foreign Key -> id del Workout
    public Date date;
    List<String> exercises = new ArrayList<String>();
    List<Integer> sets = new ArrayList<Integer>();



}



