package com.example.sergi.signin;

import com.google.firebase.database.Exclude;

/**
 * Created by Sergi on 21/03/2017.
 */

public class Perro {
    String nombre;
    boolean perdido;
    boolean encontrado;
    String raza;
    int recompensa;
    String fecha;
    String chip;
    String coger;
    String color;
    String idUser;
    String nombreUser;
    String id;
    String imageUri;
    User u;
    String descripcion;

    public Perro(){}

    public Perro(String nombre, String imageUri, String id,
                 String color, String coger, String chip, int recompensa, String raza,
                 boolean encontrado, boolean perdido,User u, String fecha) {
        this.fecha= fecha;
        this.nombre = nombre;
        this.imageUri = imageUri;
        this.id = id;
        this.color = color;
        this.coger = coger;
        this.chip = chip;
        this.recompensa = recompensa;
        this.raza = raza;
        this.perdido=perdido;
        this.encontrado=encontrado;
        this.u=u;
    }

    public Perro(boolean perdido, boolean encontrado, String raza, String fecha, String color, User u, String descripcion, String imageUri, String id) {
        this.perdido = perdido;
        this.encontrado = encontrado;
        this.raza = raza;
        this.fecha = fecha;
        this.color = color;
        this.u = u;
        this.descripcion = descripcion;
        this.imageUri = imageUri;
        this.id = id;
    }

    @Exclude
    public String getDescripcion(){return descripcion;}
    @Exclude
    public void setDescripcion(String descripcion){this.descripcion=descripcion;}
    @Exclude
    public String getFecha(){return fecha;}
    @Exclude
    public void setFecha(String fecha){this.fecha=fecha;}
    @Exclude
    public User getUser(){return u;}
    @Exclude
    public void setUser(User u){this.u=u;}
    @Exclude
    public boolean isEncontrado() {return encontrado;}
    @Exclude
    public void setEncontrado(boolean encontrado) {this.encontrado = encontrado;}
    @Exclude
    public boolean isPerdido() {return perdido;}
    @Exclude
    public void setPerdido(boolean perdido) {this.perdido = perdido;}
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

    @Override
    public String toString() {
        return "Perro{" +
                "nombre='" + nombre + '\'' +
                ", raza='" + raza + '\'' +
                ", recompensa=" + recompensa +
                ", fecha='" + fecha + '\'' +
                ", u=" + u +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
