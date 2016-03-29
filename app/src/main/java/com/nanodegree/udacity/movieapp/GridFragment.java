package com.nanodegree.udacity.movieapp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class GridFragment extends Fragment {

    ArrayList<Information> finalarraylist=new ArrayList<>();
    GridView gridView=null;
    public GridFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_grid, container, false);
      gridView= (GridView)root.findViewById(R.id.gridview);
        finalarraylist=new ArrayList<Information>();
        //checking for network connectivity
        return root;
    }
    @Override
    public void onStart() {
        super.onStart();
        finalarraylist.clear();
        if(isConnected())
        {
            //if netconnection is established then call Async Task methods
            GetDetails getDetails=new GetDetails();
            //Log.v("On start","called");
            getDetails.execute(getParameter());

        }
        else {

            Toast.makeText(getActivity(),"Please check internet Connection",Toast.LENGTH_SHORT).show();
        }

    }

    String getParameter()
    {
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getActivity());
        return sharedPreferences.getString(getString(R.string.movie_sort_key),getString(R.string.popularmovies));
    }
    boolean isConnected()
    {
        ConnectivityManager connectivityManager= (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null) {
            if (networkInfo.isConnectedOrConnecting())
                return true;
        }
        return false;
    }

    class GetDetails extends AsyncTask<String,Void,Void> {
        final String API_KEY=getString(R.string.api_key);
        final String BASE_URL="https://api.themoviedb.org/3/movie/";
        BufferedReader reader;
        StringBuffer buffer;
        HttpURLConnection httpURLConnection;
        @Override
        protected Void doInBackground(String... params) {
            try{
                URL url=new URL(BASE_URL+params[0]+"?api_key="+API_KEY);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();
                buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }

            }
            catch(Exception e)
            {
                Log.v("ERROR MESSAGE", e.getMessage());
            }
            finally {

            }
            //parse the json String to convert into appropriate JSON Objects
            parse_json(buffer.toString());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            gridView.setAdapter(new ImageAdapter(getActivity()));
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i=new Intent(getActivity(),Details.class);
                    Bundle b=new Bundle();
                    b.putSerializable("list",(Serializable)finalarraylist);
                    i.putExtra("bundle",b);
                    i.putExtra("position",position);
                    startActivity(i);
                }
            });
        }
    }

    void parse_json(String jsonstring)
    {

        try {
            JSONObject json = new JSONObject(jsonstring);
            JSONArray jarray=json.getJSONArray("results");
            for(int iterate=0;iterate<jarray.length();iterate++)
            {
                Information i=new Information();
                JSONObject result=jarray.getJSONObject(iterate);
                String title=result.getString("original_title");
                String description=result.getString("overview");
                double votes=result.getDouble("vote_average");
                String releasedate=result.getString("release_date");
                String posterpath=result.getString("poster_path");
                i.setTitle(title);
                i.setDescription(description);
                i.setVotes(votes+"");
                i.setReleasedate(releasedate);
                i.setPosterpath(posterpath);
                finalarraylist.add(i);
            }
        }
        catch(Exception e)
        {
            Log.v("JOSN EXCEPTION",e.getMessage());
        }

    }

    //Custom Adapter to display images
    class ImageAdapter extends BaseAdapter {
        Context c;
        ImageAdapter(Context c) {
            this.c = c;
        }
        @Override
        public int getCount() {
            //Log.e("final Array",finalarraylist.size()+"");
            return finalarraylist.size();
        }

        @Override
        public Object getItem(int position) {
            return finalarraylist.get(position).getPosterpath();
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

           //Log.e("position",""+position);
            ImageView view = (ImageView) convertView;
            if (view == null) {
                view = new ImageView(c);
                view.setLayoutParams(new GridView.LayoutParams(300,400));
                view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }

            String url =finalarraylist.get(position).getPosterpath();
           // Log.e("position",""+url);
            Picasso.with(c).load(url).into(view);
            return view;
        }

    }


}
