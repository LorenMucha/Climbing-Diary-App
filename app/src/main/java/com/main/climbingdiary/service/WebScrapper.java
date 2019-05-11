package com.main.climbingdiary.service;

import com.main.climbingdiary.models.Route;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;

public class WebScrapper {
    private String username;
    private String url = "https://www.8a.nu/scorecard/";

    public WebScrapper(String _username){
        this.username = _username;
    }

}
