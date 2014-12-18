package com.projects.ofirbarzilay.androidlove;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.projects.ofirbarzilay.androidlove.parser.IotdHandler;
import com.projects.ofirbarzilay.androidlove.parser.RSSItem;

import java.util.ArrayList;
import java.util.Iterator;

import static android.view.View.OnClickListener;


public class RSSActivity extends Fragment {

    private IotdHandler iotdHandler;
    private ProgressDialog dialog;
    private Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_rss, container, false);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        Button refreshBtn = (Button) getView().findViewById(R.id.refresh_button);
        refreshBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                onRefresh(v);
            }
        });

        Button setWallpaperBtn = (Button) getView().findViewById(R.id.set_wallpaper_button);
        setWallpaperBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                onSetWallpaper(v);
            }
        });
        refreshFromFeed();
    }

    private void refreshFromFeed() {

        dialog = ProgressDialog.show(
                getActivity(),
                "Loading",
                "Loading the image of the Day");
        Thread thread = new Thread() {
            @Override
            public void run() {
                if (iotdHandler == null) {
                    iotdHandler = new IotdHandler();
                }
                iotdHandler.processFeed();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        resetDisplay();
                        dialog.dismiss();
                    }
                });
            }
        };
        thread.start();


    }

    public void onRefresh(View view) {
        refreshFromFeed();
    }

    public void onSetWallpaper(View view) {
        Thread th = new Thread() {
            public void run() {
                WallpaperManager wallpaperManager =
                        WallpaperManager.getInstance(getActivity().getApplicationContext());
                try {
                    RSSItem rssItem = iotdHandler.getRssItemList().get(0);
                    wallpaperManager.setBitmap(rssItem.getImage());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Wallpaper set",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Wallpaper Failed to set",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        };
        th.start();
    }

    private void resetDisplay() {

        ArrayList<RSSItem> rssItemList = iotdHandler.getRssItemList();

        Iterator<RSSItem> iterator = rssItemList.iterator();
        while (iterator.hasNext()) {
            RSSItem rssItem = iterator.next();

            TextView titleView = (TextView) getActivity().findViewById(R.id.imageTitle);
            titleView.setText(rssItem.getTitle());

            TextView dateView = (TextView) getActivity().findViewById(R.id.imageDate);
            dateView.setText(rssItem.getDate());

            ImageView imageView = (ImageView) getActivity().findViewById(R.id.imageDisplay);
            imageView.setImageBitmap(rssItem.getImage());



            TextView descriptionView = (TextView) getActivity().findViewById(R.id.imageDescription);
            descriptionView.setText(rssItem.getDescription());

        }

    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_rs, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    public class MyTask extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            iotdHandler.processFeed();
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//            resetDisplay();
//            super.onPostExecute(result);
//            dialog.dismiss();
//        }
//    }
}

