package util;

import android.app.Application;

import com.example.smartstrengthlog.WorkoutSessionLog;

import java.util.HashMap;
import java.util.Map;

public class WorkoutSessionAPI extends Application {


    //We will make it a singleton
    private static WorkoutSessionAPI instance; //Instance for the singleton


    private Map<String, Object> set1_E1 = new HashMap<>();
    private Map<String, Object> set2_E1 = new HashMap<>();
    private Map<String, Object> set3_E1 = new HashMap<>();

    private Map<String, Object> set1_E2 = new HashMap<>();
    private Map<String, Object> set2_E2 = new HashMap<>();
    private Map<String, Object> set3_E2 = new HashMap<>();

    private Map<String, Object> set1_E3 = new HashMap<>();
    private Map<String, Object> set2_E3 = new HashMap<>();
    private Map<String, Object> set3_E3 = new HashMap<>();

    private int exerciseNumber;


    public static WorkoutSessionAPI getInstance(){
        if (instance == null)
            instance = new WorkoutSessionAPI();
        return instance;
    }

    //Getter and setter

    public Map<String, Object> getSet1_E1() {
        return set1_E1;
    }

    public void setSet1_E1(Map<String, Object> set1_E1) {
        this.set1_E1 = set1_E1;
    }

    public Map<String, Object> getSet2_E1() {
        return set2_E1;
    }

    public void setSet2_E1(Map<String, Object> set2_E1) {
        this.set2_E1 = set2_E1;
    }

    public Map<String, Object> getSet3_E1() {
        return set3_E1;
    }

    public void setSet3_E1(Map<String, Object> set3_E1) {
        this.set3_E1 = set3_E1;
    }

    public Map<String, Object> getSet1_E2() {
        return set1_E2;
    }

    public void setSet1_E2(Map<String, Object> set1_E2) {
        this.set1_E2 = set1_E2;
    }

    public Map<String, Object> getSet2_E2() {
        return set2_E2;
    }

    public void setSet2_E2(Map<String, Object> set2_E2) {
        this.set2_E2 = set2_E2;
    }

    public Map<String, Object> getSet3_E2() {
        return set3_E2;
    }

    public void setSet3_E2(Map<String, Object> set3_E2) {
        this.set3_E2 = set3_E2;
    }

    public Map<String, Object> getSet1_E3() {
        return set1_E3;
    }

    public void setSet1_E3(Map<String, Object> set1_E3) {
        this.set1_E3 = set1_E3;
    }

    public Map<String, Object> getSet2_E3() {
        return set2_E3;
    }

    public void setSet2_E3(Map<String, Object> set2_E3) {
        this.set2_E3 = set2_E3;
    }

    public Map<String, Object> getSet3_E3() {
        return set3_E3;
    }

    public void setSet3_E3(Map<String, Object> set3_E3) {
        this.set3_E3 = set3_E3;
    }

    public int getExerciseNumber() {
        return exerciseNumber;
    }

    public void setExerciseNumber(int exerciseNumber) {
        this.exerciseNumber = exerciseNumber;
    }
}
