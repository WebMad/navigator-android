package webmad.navigator.controllers;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.search.ErrorCode;
import com.here.android.mpa.search.GeocodeRequest;
import com.here.android.mpa.search.GeocodeResult;
import com.here.android.mpa.search.ResultListener;

import java.util.ArrayList;
import java.util.List;

public class SearchController {

    private ArrayList<String> places = new ArrayList<>();
    private ArrayAdapter<String> searchResultsAdapter;

    public SearchController(final Activity activity, ListView placesList, final FrameLayout listActivity, final EditText searchField) {

        searchResultsAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, this.places);
        placesList.setAdapter(searchResultsAdapter);

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
                        if (errorCode != ErrorCode.NONE) {
                            System.out.println("ERROR: Cannot get results");
                            //handle error
                        } else {
                            int i = 0;
                            System.out.println(geocodeResults);
                            places.clear();
                            for (GeocodeResult result : geocodeResults) {
                                places.add(i, result.getLocation().getAddress().getText());
                                System.out.println(result.getLocation().getAddress().getText());
                                i++;
                            }

                            activity.runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    searchResultsAdapter.notifyDataSetChanged();
                                }
                            });

                            System.out.println("Everything is ok");

                        }

                    }
                };

                GeocodeRequest request = new GeocodeRequest(String.valueOf(searchField.getText())).setSearchArea(new GeoCoordinate(46.347869, 48.033574), 5000);
                if (request.execute(listener) != ErrorCode.NONE) {
                    // Handle request error
                    System.out.println("ERROR: Cannot get results");
                }

                return false;
            }

        });
    }

    public ArrayList<String> getPlaces() {
        return places;
    }
}
