package webmad.navigator.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.SupportMapFragment;
import com.here.android.mpa.search.ErrorCode;
import com.here.android.mpa.search.GeocodeRequest;
import com.here.android.mpa.search.GeocodeResult;
import com.here.android.mpa.search.ResultListener;

import java.util.ArrayList;
import java.util.List;

import webmad.navigator.R;
import webmad.navigator.controllers.MapController;
import webmad.navigator.controllers.SearchController;

public class MainActivity extends FragmentActivity {

    Map map = null;
    EditText searchField;
    ListView placesList;
    ArrayList<String> places = new ArrayList<>();
    ArrayAdapter<String> searchResultsAdapter;
    MapController mapController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        //Все, что связано с полем, установка слушателя на зменение поля
        final FrameLayout listActivity = findViewById(R.id.list_activity); //получение фрейма, где хранится ListView с результатами поиска.
        searchField = findViewById(R.id.search_field); //поле поиска
        placesList = findViewById(R.id.places_list); //listview

        SearchController searchController = new SearchController(this, placesList, listActivity, searchField);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapfragment);
        mapController = new MapController(mapFragment);
        mapController.initMap();
    }
}
