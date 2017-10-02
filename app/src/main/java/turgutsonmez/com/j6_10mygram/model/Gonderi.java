package turgutsonmez.com.j6_10mygram.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Mesut on 18.09.2017.
 */

public class Gonderi {
    private String id, gonderenId, yol;
    private int goruntulenme;
    private HashMap<String, Object> eklenmeZamani;
    private ArrayList<String> likes;
    private ArrayList<String> bakanlar;

    public Gonderi() {
        this.id = UUID.randomUUID().toString();
        this.goruntulenme = 0;
    }

    public ArrayList<String> getBakanlar() {
        return bakanlar;
    }

    public void setBakanlar(ArrayList<String> bakanlar) {
        this.bakanlar = bakanlar;
    }

    public ArrayList<String> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<String> likes) {
        this.likes = likes;
    }

    public HashMap<String, Object> getEklenmeZamani() {
        return eklenmeZamani;
    }

    public void setEklenmeZamani(HashMap<String, Object> eklenmeZamani) {
        this.eklenmeZamani = eklenmeZamani;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGonderenId() {
        return gonderenId;
    }

    public void setGonderenId(String gonderenId) {
        this.gonderenId = gonderenId;
    }

    public String getYol() {
        return yol;
    }

    public void setYol(String yol) {
        this.yol = yol;
    }

    public int getGoruntulenme() {
        return goruntulenme;
    }

    public void setGoruntulenme(int goruntulenme) {
        this.goruntulenme = goruntulenme;
    }
}
