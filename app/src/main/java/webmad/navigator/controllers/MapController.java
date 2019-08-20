package webmad.navigator.controllers;

import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.SupportMapFragment;

public class MapController {

    Map map;
    GeoCoordinate center = new GeoCoordinate(46.347869, 48.033574); //по умолчанию Астрахань
    double zoom = 10; //zoom карты 10 - это medium
    float orientation = 0; //направление карты, по умолчанию она направлена на север
    float tilt = 0; //Наклон карты
    SupportMapFragment mapFragment;

    public MapController(SupportMapFragment mapFragment) {
        this.mapFragment = mapFragment;
    }

    public MapController(GeoCoordinate center, double zoom, float orientation, float tilt) {
        this.center = center;
        this.zoom = zoom;
        this.orientation = orientation;
        this.tilt = tilt;
    }

    /**
     * Инициализация карты
     * @return void
     */
    public void initMap() {
        mapFragment.init(new OnEngineInitListener() {
            @Override
            public void onEngineInitializationCompleted(Error error) {
                if (error == Error.NONE) {
                    // now the map is ready to be used
                    map = mapFragment.getMap();
                    ApplySettings();

                } else {
                    System.out.println("ERROR: Cannot initialize SupportMapFragment");
                }
            }
        });
    }

    private void ApplySettings() {
        map.setCenter(center, Map.Animation.NONE);
        map.setZoomLevel(zoom);
        //map.setOrientation(orientation); почему-то не находит этот метод
        //map.setTilt(tilt); почему-то не находит этот метод
    }

    public Map getMap() {
        return map;
    }

    public GeoCoordinate getCenter() {
        return center;
    }

    public void setCenter(GeoCoordinate center) {
        map.setCenter(center, Map.Animation.NONE);
        this.center = center;
    }

    public double getZoom() {
        return zoom;
    }

    public void setZoom(double zoom) {
        map.setZoomLevel(zoom);
        this.zoom = zoom;
    }
}
