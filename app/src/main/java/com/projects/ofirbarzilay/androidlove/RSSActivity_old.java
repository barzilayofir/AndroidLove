package com.projects.ofirbarzilay.androidlove;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.projects.ofirbarzilay.androidlove.parser.IotdHandler;
import com.projects.ofirbarzilay.androidlove.parser.RSSItem;

import java.util.ArrayList;
import java.util.Iterator;


public class RSSActivity_old extends Activity {

    private IotdHandler handler;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss);

        refreshFromFeed();
    }

    private void refreshFromFeed() {

        dialog = ProgressDialog.show(
                this,
                "Loading",
                "Loading the image of the Day");

        handler = new IotdHandler();
        new MyTask().execute();
    }

    public void onRefresh(View view){
        refreshFromFeed();
    }

    private void resetDisplay () {

        ArrayList<RSSItem> rssItemList = handler.getRssItemList();

        Iterator<RSSItem> iterator = rssItemList.iterator();
        while (iterator.hasNext()) {
            RSSItem rssItem = iterator.next();

            TextView titleView = (TextView) findViewById (R.id.imageTitle);
            titleView.setText(rssItem.getTitle());

            TextView dateView = (TextView) findViewById(R.id.imageDate);
            dateView.setText(rssItem.getDate());

            ImageView imageView = (ImageView) findViewById (R.id.imageDisplay);
            imageView.setImageBitmap(rssItem.getImage());

            TextView descriptionView = (TextView) findViewById (R.id.imageDescription);
            descriptionView.setText(rssItem.getDescription());

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rs, menu);
        return true;
    }

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

    public class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            handler.processFeed();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            resetDisplay();
            super.onPostExecute(result);
            dialog.dismiss();
        }
    }
}

