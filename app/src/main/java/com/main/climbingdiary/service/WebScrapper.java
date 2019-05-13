package com.main.climbingdiary.service;

import android.util.Log;

import com.main.climbingdiary.models.Route;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

public class WebScrapper {
    private String username;
    private String url = "https://www.8a.nu/scorecard/";

    public WebScrapper(String _username){
        this.username = _username;
    }
    public void getRouteList(){
        //create the user url
        StringBuilder sb = new StringBuilder();
        sb.append(this.url)
                .append(this.username)
                .append("/routes/?AscentClass=0&AscentListTimeInterval=0&AscentListViewType=1&GID=dc1dcb3276b8b01b569e128b202fab15");

        try{
            Connection connection = Jsoup.connect(sb.toString());
            //set user agent to Google Chrome
            connection.userAgent("Mozilla/5.0");
            //set timeout to 50 seconds
            connection.timeout(50000);
            Document doc = connection.get();
            Element element = doc.getElementById("main");
            Elements td = element.select("tr");
            for(Element el : td){
                System.out.println(el.outerHtml());
            }

        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
