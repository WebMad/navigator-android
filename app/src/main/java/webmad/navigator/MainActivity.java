package webmad.navigator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapRoute;
import com.here.android.mpa.mapping.SupportMapFragment;
import com.here.android.mpa.routing.Route;
import com.here.android.mpa.routing.RouteManager;
import com.here.android.mpa.routing.RouteOptions;
import com.here.android.mpa.routing.RoutePlan;
import com.here.android.mpa.routing.RouteResult;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends FragmentActivity {

    Map map = null;
    Button btn;
    EditText searchField;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        btn = findViewById(R.id.setAss);
        searchField = findViewById(R.id.search_field);

        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapfragment);
        // initialize the Map Fragment and
        // retrieve the map that is associated to the fragment

        assert mapFragment != null;
        mapFragment.init(new OnEngineInitListener() {

            @Override
            public void onEngineInitializationCompleted(Error error) {
                if (error == Error.NONE) {
                    // now the map is ready to be used
                    map = mapFragment.getMap();
                    // ...
                } else {
                    System.out.println("ERROR: Cannot initialize SupportMapFragment");
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                setAssTrahan();
            }
        });
    }

    protected void setAssTrahan() {
        RouteManager rm = new RouteManager();

        RoutePlan routePlan = new RoutePlan();
        routePlan.addWaypoint(new GeoCoordinate(46.347869, 48.033574));
        routePlan.addWaypoint(new GeoCoordinate(52.52437, 13.41053));

        RouteOptions routeOptions = new RouteOptions();
        routeOptions.setTransportMode(RouteOptions.TransportMode.CAR);
        routeOptions.setRouteType(RouteOptions.Type.FASTEST);

        routePlan.setRouteOptions(routeOptions);

        rm.calculateRoute(routePlan, new RouteListener());

        map.setCenter(new GeoCoordinate(46.347869, 48.033574), Map.Animation.LINEAR);
    }

    public class RouteListener implements RouteManager.Listener {

        @Override
        public void onProgress(int i) {

        }

        @Override
        public void onCalculateRouteFinished(RouteManager.Error error, List<RouteResult> routeResult) {
            // If the route was calculated successfully
            if (error == RouteManager.Error.NONE) {
                // Render the route on the map
                MapRoute mapRoute = new MapRoute(routeResult.get(0).getRoute());
                map.addMapObject(mapRoute);
            }
            else {
                System.out.println("ERROR: Cannot calculate route");
                // Display a message indicating route calculation failure
            }

        }
    }
}
