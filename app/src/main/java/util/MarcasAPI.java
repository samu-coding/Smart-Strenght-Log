package util;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

public class MarcasAPI extends Application {

    private static MarcasAPI instance; //Instance for the singleton
    private List<Integer> marcas =new ArrayList<>();


    public List<Integer> getMarcas() {
        return marcas;
    }

    public void setMarcas(List<Integer> marcas) {
        this.marcas = marcas;
    }

    public static MarcasAPI getInstance(){
        if (instance == null)
            instance = new MarcasAPI();
        return instance;
    }






}
