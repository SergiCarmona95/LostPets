package com.example.sergi.signin;

import com.google.firebase.database.Exclude;

/**
 * Created by Sergi on 21/03/2017.
 */

public class Perro {
    String nombre;
    String raza;
    int recompensa;
    String chip;
    String coger;
    String color;
    String idUser;
    String nombreUser;
    String id;
    String imageUri;

    public Perro() {
    }

    public Perro(String nombre, String imageUri, String id, String nombreUser, String idUser, String color, String coger, String chip, int recompensa, String raza) {
        this.nombre = nombre;
        this.imageUri = imageUri;
        this.id = id;
        this.nombreUser = nombreUser;
        this.idUser = idUser;
        this.color = color;
        this.coger = coger;
        this.chip = chip;
        this.recompensa = recompensa;
        this.raza = raza;
    }
    @Exclude
    public String getIdUser() {
        return idUser;
    }
    @Exclude
    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }
    @Exclude
    public String getImageUri() {
        return imageUri;
    }
    @Exclude
    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
    @Exclude
    public String getId() {
        return id;
    }
    @Exclude
    public void setId(String id) {
        this.id = id;
    }
    @Exclude
    public String getNombreUser() {
        return nombreUser;
    }
    @Exclude
    public void setNombreUser(String nombreUser) {
        this.nombreUser = nombreUser;
    }

    @Exclude
    public String getColor() {
        return color;
    }
    @Exclude
    public void setColor(String color) {
        this.color = color;
    }
    @Exclude
    public String getCoger() {
        return coger;
    }
    @Exclude
    public void setCoger(String coger) {
        this.coger = coger;
    }
    @Exclude
    public String getChip() {
        return chip;
    }
    @Exclude
    public void setChip(String chip) {
        this.chip = chip;
    }
    @Exclude
    public int getRecompensa() {
        return recompensa;
    }
    @Exclude
    public void setRecompensa(int recompensa) {
        this.recompensa = recompensa;
    }
    @Exclude
    public String getNombre() {
        return nombre;
    }
    @Exclude
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    @Exclude
    public String getRaza() {
        return raza;
    }
    @Exclude
    public void setRaza(String raza) {
        this.raza = raza;
    }
    @Exclude

    @Override
    public String toString() {
        return "Perro{" +
                "nombre='" + nombre + '\'' +
                ", raza='" + raza + '\'' +
                ", recompensa=" + recompensa +
                ", chip='" + chip + '\'' +
                ", coger='" + coger + '\'' +
                ", color='" + color + '\'' +
                ", idUser='" + idUser + '\'' +
                ", nombreUser='" + nombreUser + '\'' +
                ", id='" + id + '\'' +
                ", imageUri='" + imageUri + '\'' +
                '}';
    }
}
