package com.main.climbingdiary.service;

import org.junit.Test;

public class WebScrapperTest {

    @Test
    public void WebScrapperResturnsRouteListOK(){
        WebScrapper webScrapper = new WebScrapper("loren-mucha");
        webScrapper.getRouteList();
    }
}
