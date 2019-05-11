package com.main.climbingdiary.service;

import android.util.Log;

import com.main.climbingdiary.models.Route;

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
                .append("/routes/2009/?AscentClass=0&AscentListTimeInterval=0&AscentListViewType=1&GID=dc1dcb3276b8b01b569e128b202fab15");

        try{
            //todo, solution: https://www.htmlgoodies.com/html5/other/web-page-scraping-with-jsoup.html
            Document doc = Jsoup.connect(sb.toString()).userAgent("Opera").get();
            String content = doc.getElementById("div#main").outerHtml();
            System.out.println(content);
            /*for(Iterator<Element> ite = table.select("td").iterator(); ite.hasNext();){
               System.out.println(ite.next().text());
            }*/


        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
