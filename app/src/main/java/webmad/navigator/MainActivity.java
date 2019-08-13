package webmad.navigator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
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
import com.here.android.mpa.search.ErrorCode;
import com.here.android.mpa.search.GeocodeRequest;
import com.here.android.mpa.search.GeocodeResult;
import com.here.android.mpa.search.ResultListener;

import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends FragmentActivity {

    Map map = null;
    Button btn;
    EditText searchField;
    ListView placesList;
    ArrayList<String> places;
    ArrayAdapter<String> searchResultsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        placesList = findViewById(R.id.places_list);

        places = new ArrayList<>();
        searchResultsAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, places);
        placesList.setAdapter(searchResultsAdapter);

        btn = findViewById(R.id.setAss);
        searchField = findViewById(R.id.search_field);
        final FrameLayout listActivity = findViewById(R.id.list_activity);

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

        searchField.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (searchField.getText().toString().equals("")) {
                    listActivity.setVisibility(View.GONE);
                    return false;
                }

                listActivity.setVisibility(View.VISIBLE);

                ResultListener<List<GeocodeResult>> listener = new ResultListener<List<GeocodeResult>>() {
                    @Override
                    public void onCompleted(List<GeocodeResult> geocodeResults, ErrorCode errorCode) {
                        if(errorCode != ErrorCode.NONE) {
                            System.out.println("ERROR: Cannot get results");
                            //handle error
                        }
                        else {
                            int i = 0;
                            System.out.println(geocodeResults);
                            places.clear();
                            for(GeocodeResult result : geocodeResults) {
                                places.add(i, result.getLocation().getAddress().getText());
                                System.out.println(result.getLocation().getAddress().getText());
                                i++;
                            }

                            System.out.println("Everything is ok");
                        }
                    }
                };
                GeocodeRequest request = new GeocodeRequest(String.valueOf(searchField.getText())).setSearchArea(new GeoCoordinate(46.347869, 48.033574), 5000);
                if (request.execute(listener) != ErrorCode.NONE) {
                    // Handle request error
                    System.out.println("ERROR: Cannot get results");
                }

                searchResultsAdapter.notifyDataSetChanged();

                return false;
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

    public class GeocodeListener implements ResultListener<List<GeocodeResult>> {

        @Override
        public void onCompleted(List<GeocodeResult> geocodeResults, ErrorCode errorCode) {
            if(errorCode != ErrorCode.NONE) {
                System.out.println("ERROR: это пиздец");
                //handle error
            }
            else {
                int i = 0;
                System.out.println(geocodeResults);

                for(GeocodeResult result : geocodeResults) {
                    places.add(i, result.getLocation().getAddress().getText());
                    System.out.println(result.getLocation().getAddress().getText());
                    i++;
                }

                searchResultsAdapter.notifyDataSetChanged();

                System.out.println("Everything is ok");
            }
        }
    }
}
