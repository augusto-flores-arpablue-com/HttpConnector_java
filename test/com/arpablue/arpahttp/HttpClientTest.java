/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arpablue.arpahttp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author augusto
 */
public class HttpClientTest {
    protected String mURL;
    protected String mURLget;
    protected String mURLpost;
    protected String mURLput;
    protected String mURLpatch;
    protected String mURLdelete;
    public HttpClientTest() {
        mURL = "https://reqres.in";
        mURLget = mURL + "/api/users";
        mURLpost = mURL + "/api/users/2";
        mURLput = mURL + "/api/users/2";
        mURLpatch = mURL + "/api/users/2";
        mURLdelete = "https://reqres.in/";
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of connect method, of class HttpClient.
     */
//    @Test
    public void test_HttpClient_url_0() {

        ArrayList<String[]> urls = new ArrayList<String[]>();
        String[] e = null;

        e = new String[2];
        e[0] = "www.google.com";
        e[1] = "http://www.google.com";
        urls.add(e);

        e = new String[2];
        e[0] = "http://www.google.com";;
        e[1] = "http://www.google.com";
        urls.add(e);

        e = new String[2];
        e[0] = "https://www.google.com";
        e[1] = "https://www.google.com";
        urls.add(e);

        e = new String[2];
        e[0] = "www.google.com/index.html";
        e[1] = "http://www.google.com/index.html";
        urls.add(e);

        e = new String[2];
        e[0] = "www.google.com:8080/index.html";
        e[1] = "http://www.google.com:8080/index.html";
        urls.add(e);

        e = new String[2];
        e[0] = "www.google.com:512/index.html";
        e[1] = "http://www.google.com:512/index.html";
        urls.add(e);

        e = new String[2];
        e[0] = "https://www.google.com:512/a/b/c/d/index.html";
        e[1] = "https://www.google.com:512/a/b/c/d/index.html";
        urls.add(e);

        e = new String[2];
        e[0] = "https://reqres.in/api/users";
        e[1] = "https://reqres.in/api/users";
        urls.add(e);

        HttpClient target = new HttpClient();
        boolean flag = true;
        for (String[] url : urls) {
            target.setURL(url[0]);
            if (!url[1].equalsIgnoreCase(target.getUrl())) {
                System.out.println("----------------------------------------");
                System.out.println("Are not equals:");
                System.out.println("    " + url[1]);
                System.out.println("    " + target.getUrl());
                System.out.println("");
                System.out.println("Data ot the target object:");
                System.out.println(target.toJSONnicely());
                System.out.println("");
                flag = false;
            }

        }
        if (!flag) {
            fail(" Some urls are not equals, when should be equals.");
        }

    }

    // @Test
    public void test_HttpClient_GETmethod_https() {
        HttpClient target = new HttpClient();
        String res = target.get( mURLget );
            System.out.println("------------------test_HttpClient_GETmethod_https--------------");
            System.out.println("Response code: " + target.getResponseCode());
            if (res == null) {
                System.out.println("Response: ");
                System.out.println(res);
            } else {
                System.out.println(res);
            }
            System.out.println("--------------------------------");
            if( !target.isSuccess() ) {
                fail("It is not possible complete the request, return a ERROR "+ target.getResponseCode());
            }
    }

//    @Test
    public void test_HttpClient_GETmethod_http() {
        HttpClient target = new HttpClient();
        String res = target.get( mURLget ) ;
            System.out.println("------------------test_HttpClient_GETmethod_http--------------");
            System.out.println("Response code: " + target.getResponseCode());
            if (res == null) {
                System.out.println("Response: ");
                System.out.println(res);
            } else {
                System.out.println(res);
            }
            System.out.println("--------------------------------");
            if( !target.isSuccess() ) {
                fail("It is not possible complete the request, return a ERROR "+ target.getResponseCode());
            }
    }

//    @Test
    public void test_HttpClient_GETmethod_savana() {
        HttpClient target = new HttpClient();
        String res = target.get(mURLget+"?page=2");
            System.out.println("------------------test_HttpClient_GETmethod_savana--------------");
            System.out.println("Response code: " + target.getResponseCode());
            if (res == null) {
                System.out.println("Response: ");
                System.out.println(res);
            } else {
                System.out.println(res);
            }
            System.out.println("--------------------------------");
            if( !target.isSuccess() ) {
                fail("It is not possible complete the request, return a ERROR "+ target.getResponseCode());
            }
    }

//    @Test
    public void test_HttpClient_POSTmethod_savana() {
        HttpClient target = new HttpClient();
        target.addParameter("name","Lucas");
        target.addParameter("position", "Duck");
        String res = target.post(mURLpost);
        System.out.println("------------------test_HttpClient_POSTmethod_savana--------------");
        System.out.println("Response code: " + target.getResponseCode());
        if (res == null) {
            System.out.println("Response: ");
            System.out.println(res);
        } else {
            System.out.println(res);
        }
        System.out.println("--------------------------------");
            if( !target.isSuccess() ) {
                fail("It is not possible complete the request, return a ERROR "+ target.getResponseCode());
            }
    }
    //@Test
    public void test_HttpClient_PUTmethod_savana() {
        HttpClient target = new HttpClient();
        target.addParameter("name", "Nuken");
        target.addParameter("job", "Zion President");
        String res = target.put(mURLput+"/2");
        System.out.println("------------------test_HttpClient_PUTmethod_savana--------------");
        System.out.println("Response code: " + target.getResponseCode());
        if (res == null) {
            System.out.println("Response: ");
            System.out.println(res);
        } else {
            System.out.println(res);
        }
        System.out.println("--------------------------------");
            if( !target.isSuccess() ) {
                fail("It is not possible complete the request, return a ERROR "+ target.getResponseCode());
            }
    }
    @Test
    public void test_HttpClient_PUTmethod_sendJSON(){
        System.out.println("------------------test_HttpClient_PUTmethod_sendJSON--------------");
        HttpClient target = new HttpClient();
        target.setJSon("{ \"name\": \"morpheus\", \"job\": \"zion resident\"}");
        String res = target.put(mURLput+"/2");
        System.out.println("Response code: " + target.getResponseCode());
        if (res == null) {
            System.out.println("Response: ");
            System.out.println(res);
        } else {
            System.out.println(res);
        }
        System.out.println("--------------------------------");
            if( !target.isSuccess() ) {
                fail("It is not possible complete the request, return a ERROR "+ target.getResponseCode());
            }
    }

}
