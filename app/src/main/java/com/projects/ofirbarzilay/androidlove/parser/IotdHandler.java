package com.projects.ofirbarzilay.androidlove.parser;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by Ofir.Barzilay on 14/12/2014.
 */
public class IotdHandler extends DefaultHandler {
    private String url = "http://www.nasa.gov/rss/dyn/image_of_the_day.rss";
    private boolean inUrl = false;
    private boolean inTitle = false;
    private boolean inDescription = false;
    private boolean inItem = false;
    private boolean inDate = false;
    private boolean inLink = false;
    private Bitmap image = null;
    private RSSItem currentRSSItem;
    private ArrayList<RSSItem> rssItemList = new ArrayList<RSSItem>();


    public void processFeed() {
        try {
            SAXParserFactory factory =
                    SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLReader reader = parser.getXMLReader();
            reader.setContentHandler(this);
            InputStream inputStream = new URL(url).openStream();
            reader.parse(new InputSource(inputStream));

        } catch (Exception e) {
            e.printStackTrace();


        }
    }

    private Bitmap getBitmap(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bilde = BitmapFactory.decodeStream(input);
            input.close();
            return bilde;
        } catch (IOException ioe) {
            return null;
        }
    }

    @Override
    public void startElement(String url, String localName, String qName, Attributes attributes) throws SAXException {
        if (rssItemList.size() > 1 && rssItemList.get(1).isReady()) {
            return;
        }
        if (localName.equals("item")) {
            inItem = true;
            currentRSSItem = new RSSItem();
            rssItemList.add(currentRSSItem);
        }
        if (inItem) {
            if (localName.equals("title")) {
                inTitle = true;
            }
            if (localName.equals("link")) {
                inLink = true;
            }
            if (localName.equals("description")) {
                inDescription = true;
            }
            if (localName.equals("pubDate")) {
                inDate = true;
            }
            if (localName.equals("enclosure")) {
                String attributesValue = attributes.getValue("url");
                Bitmap bitmap = getBitmap(attributesValue);
                if (bitmap != null) {
                    currentRSSItem.setImage(bitmap);
                }
            }
            //
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (localName.equals("item")) {
            inItem = false;
        }
        if (localName.equals("title")) {
            inTitle = false;
        }
        if (localName.equals("description")) {
            inDescription = false;
        }
        if (localName.equals("pubDate")) {
            inDate = false;
        }
        if (localName.equals("link")) {
            inLink = false;
        }
    }


    @Override
    public void characters(char ch[], int start, int length) {
        String chars = new String(ch).substring(start, start + length);

        if (inTitle && currentRSSItem.getTitle() == null) {
            currentRSSItem.setTitle(chars);
        }
        if (inDescription && currentRSSItem.getDescription() == null) {
            currentRSSItem.setDescription(chars);
        }
        if (inDate && currentRSSItem.getDate() == null) {
            currentRSSItem.setDate(chars);
        }
        if (inLink && currentRSSItem.getLink() == null) {
            currentRSSItem.setLink(chars);
        }
    }

    public ArrayList<RSSItem> getRssItemList() {
        return rssItemList;
    }
}

