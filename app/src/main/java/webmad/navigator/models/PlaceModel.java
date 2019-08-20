package webmad.navigator.models;

import java.lang.reflect.Array;

public class PlaceModel {
    private long id;
    private String adress;
    private Array coords;

    public PlaceModel(long id, String adress, Array coords) {
        this.id = id;
        this.adress = adress;
        this.coords = coords;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public Array getCoords() {
        return coords;
    }

    public void setCoords(Array coords) {
        this.coords = coords;
    }
}
