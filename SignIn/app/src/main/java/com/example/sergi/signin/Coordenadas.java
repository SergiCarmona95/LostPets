package com.example.sergi.signin;

import java.io.Serializable;

/**
 * Created by Sergi on 09/05/2017.
 */

public class Coordenadas  implements Serializable {
    double latitud;
    double longitud;
    String activity;

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public Coordenadas( String activity,double latitud, double longitud) {
        this.latitud = latitud;
        this.activity = activity;
        this.longitud = longitud;
    }
}
