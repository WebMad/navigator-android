package webmad.navigator.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapRoute;
import com.here.android.mpa.routing.RouteManager;
import com.here.android.mpa.routing.RouteOptions;
import com.here.android.mpa.routing.RoutePlan;
import com.here.android.mpa.routing.RouteResult;

import java.util.List;

import webmad.navigator.R;

public class RouteActivity extends AppCompatActivity {

    Map map = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.route);

    }

    //не обращайте внимания -_-
    protected void buildRoute() {
        RouteManager rm = new RouteManager();

        RoutePlan routePlan = new RoutePlan();
        routePlan.addWaypoint(new GeoCoordinate(46.347869, 48.033574));
        routePlan.addWaypoint(new GeoCoordinate(52.52437, 13.41053));

        RouteOptions routeOptions = new RouteOptions();
        routeOptions.setTransportMode(RouteOptions.TransportMode.CAR);
        routeOptions.setRouteType(RouteOptions.Type.FASTEST);

        routePlan.setRouteOptions(routeOptions);

        rm.calculateRoute(routePlan, new RouteActivity.RouteListener());

        //map.setCenter(new GeoCoordinate(46.347869, 48.033574), Map.Animation.LINEAR);
    }

    //listener для прокладки маршрута
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
            } else {
                System.out.println("ERROR: Cannot calculate route");
                // Display a message indicating route calculation failure
            }

        }
    }
}
