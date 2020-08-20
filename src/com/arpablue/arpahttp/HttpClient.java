/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arpablue.arpahttp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.Certificate;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;

/**
 *
 * @author augusto
 */
public class HttpClient extends HttpBridge {

    public static final String HTTP_GET_REQUEST = "GET";
    public static final String HTTP_POST_REQUEST = "POST";
    public static final String HTTP_PUT_REQUEST = "PUT";
    public static final String HTTP_PATCH_REQUEST = "PATCH";
    public static final String HTTP_DELETE_REQUEST = "DELETE";

    private URL mURL;
    private HttpURLConnection mChannel;
    private BufferedReader mReciver;
    private ArrayList<Certificate> mCertificates;
    private ArrayList<HttpCouple> mHeaders;
    private ArrayList<HttpCouple> mParameters;
    private String mTextBody = null;
    private boolean mbSendFile = false;

    public HttpClient() {
        mHeaders = new ArrayList<HttpCouple>();
        addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
        addHeader("Content-Type", "application/json");

    }
    /**
     * Writhe a message of a exception and raise the StackTrace.
     * @param method It is the name of the method where the exception happens.
     * @param e It is the exception raised.
     */
    protected void exc(String method ,Exception e){
        System.out.println("( "+this.getClass().getSimpleName()+" - "+method+" ): "+e);
        e.printStackTrace();
    }
    /**
     * It specify is a file has will be send with the request.
     *
     * @param flag It is true if a file will be send.
     */
    protected void setsendAFile(boolean flag) {
        this.mbSendFile = flag;
    }

    /**
     * It return true if a file is send.
     *
     * @return
     */
    protected boolean isSendindAFile() {
        return this.mbSendFile;
    }

    /**
     * It specify a JSON to be send in the request. When the this method is
     * used, the Content-type: application/json header is added to the request.
     *
     * @param json It is a string that content a JSON structure.
     */
    public void setJSon(String json) {
        this.addHeader("Content-Type", "application/json");
        this.setTextBody(json);
    }

    /**
     * It return the current JSON body used for the request.
     */
    public String getJSon() {
        return this.getTextBody();
    }

    /**
     *
     * It specify a text to be send as a file in the request.
     *
     * @param text
     */
    public void setTextBody(String text) {
        mTextBody = text;

    }

    public String getTextBody() {
        return mTextBody;
    }

    /**
     * It add a header to the request.
     *
     * @param name It is the name of the header field.
     * @param value It is the value of the header to be send.
     */
    public void addHeader(String name, String value) {
        if (name == null) {
            return;
        }
        if (value == null) {
            return;
        }
        name = name.trim();
        value = value.trim();
        for (HttpCouple e : mHeaders) {
            if (e.isThis(name)) {
                e.setValue(value);
                return;
            }
        }
        mHeaders.add(new HttpCouple(name, value));
    }

    protected void sendHeaders() {
        if (mChannel == null) {
            return;
        }
        if (mHeaders == null) {
            return;
        }
        write("- Headers:");
        for (HttpCouple e : mHeaders) {
            write("        " + e.getName() + ": " + e.getValue());
            mChannel.setRequestProperty(e.getName(), e.getValue());
        }
        mChannel.setUseCaches(false);
    }

    /**
     * It open the connection to create the request to the URL specified.
     *
     * @return It is true if the connection cam be stablish.
     */
    protected boolean open() {
        try {
            mCertificates = new ArrayList<Certificate>();
            close();
            mURL = new URL(this.getUrl());
            mChannel = (HttpURLConnection) mURL.openConnection();
            if (this.getProtocol().equalsIgnoreCase(HttpBridge.PROTOCOL_HTTPS)) {
                //getHttpsCert(); //this certificate have problems to download the content.
            }
            this.setOpen(true);
            return true;
        } catch (MalformedURLException e) {
            error400("Malformed URL: " + e.getMessage());
        } catch (Exception e) {
            error400("Connection error: " + e.getMessage());
        }
        return false;
    }

    /**
     * It close the connection.
     *
     * @return It is close if the connection can be closed without problems, if
     * the connection is closed then return true.
     */
    protected boolean close() {
        try {
            if (this.isOpen()) {
                mReciver.close();
            }
            this.setOpen(false);
            return true;
        } catch (Exception e) {
            error400("Close connection error: " + e.getMessage());

        }
        return false;
    }

    /**
     * I specify data for simulate a web browser, it is necessary because some
     * serves reject connection that are not from a web browser.
     */
    protected void sendRequestMethod() {
        try {
            mChannel.setRequestMethod(mCurrentRequestMethod);
            sendHeaders();
            sendParameters();
        } catch (Exception e) {
           exc("sendRequestMethod" ,e);
        }
    }

    /**
     * It add the parameters for the request.
     */
    protected void sendParameters() {
        if (mCurrentRequestMethod.equalsIgnoreCase(HTTP_GET_REQUEST)) {
            sendParametersGET();
        } else {
            sendParametersNoGET();
        }
    }

    protected void sendParametersGET() {

    }

    /**
     * It is called to send parameter when the request is different to a HTTP
     * GET method.
     */
    protected void sendParametersNoGET() {
        try {

            // For POST only - START
            mChannel.setDoOutput(true);
            OutputStream os = mChannel.getOutputStream();
            // Send the parameters
            if( getParamsString() != null ){
                
                os.write(getParamsString().getBytes());
            }
            // Sent the json file
            if (this.isSendindAFile()) {
                os.write(this.getJsonData().getBytes("utf-8"));
            }

            os.flush();
            os.close();
            // For POST only - END
        } catch (Exception e) {
            exc("sendParametersNoGET", e );
        }

    }

    protected String getParamsString() {
        StringBuilder params = new StringBuilder();
        boolean first = true;
        if( mParameters == null ){
            return null;
        }
        for (HttpCouple param : mParameters) {
            if (first) {
                first = false;
            } else {
                params.append("&");
            }
            params.append(param.getName());
            params.append(param.getValue());
        }
        return params.toString();
    }

    /**
     * This method is called for https connections, it download all certifies of
     * the host.
     */
    protected void getHttpsCert() {
        HttpsURLConnection con = (HttpsURLConnection) mChannel;
        if (con != null) {

            try {

//                System.out.println("Response Code : " + con.getResponseCode());
//                System.out.println("Cipher Suite : " + con.getCipherSuite());
//                System.out.println("\n");
                Certificate[] certs = con.getServerCertificates();
                for (Certificate cert : certs) {
                    this.mCertificates.add(cert);
//                    System.out.println("Cert Type : " + cert.getType());
//                    System.out.println("Cert Hash Code : " + cert.hashCode());
//                    System.out.println("Cert Public Key Algorithm : "
//                            + cert.getPublicKey().getAlgorithm());
//                    System.out.println("Cert Public Key Format : "
//                            + cert.getPublicKey().getFormat());
//                    System.out.println("\n");
                }

            } catch (SSLPeerUnverifiedException e) {
                exc("getHttpsCert", e );
            } catch (IOException e) {
                exc("getHttpsCert )" , e );
            }

        }

    }

    /**
     * It read the response of the server.
     */
    protected void readRequest() {
        try {
            this.mResponseCode = mChannel.getResponseCode();
            mReciver = new BufferedReader(new InputStreamReader(mChannel.getInputStream()));
            StringBuilder res = new StringBuilder();
            String inputLine = "";
            while (inputLine != null) {
                inputLine = mReciver.readLine();
                if (inputLine != null) {
                    res.append(inputLine + "\n");
                }
            }
            this.mResponseBody = res.toString();
        } catch (IOException e) {
            error400("IO error: " + e.getMessage());
        } catch (Exception e) {
            error400("Transaction error: " + e.getMessage());
        }
        close();
    }

    /**
     * It method realize a put request.
     *
     * @return It is the response body of the the request.
     */
    @Override
    public String get() {
        mCurrentRequestMethod = HTTP_GET_REQUEST;
        this.mResponseBody = null;
        if (open()) {
            error400("It is not possible open the url: " + this.toString());
        }
        sendRequestMethod();
        readRequest();
        return this.getResponseBody();
    }

    @Override
    public String post() {
        mCurrentRequestMethod = HTTP_POST_REQUEST;
        this.mResponseBody = null;
        if (open()) {
            error400("It is not possible open the url: " + this.toString());
        }
        sendRequestMethod();
        readRequest();
        return this.getResponseBody();
    }

    @Override
    public String put() {
        mCurrentRequestMethod = HTTP_PUT_REQUEST;
        this.mResponseBody = null;
        if (open()) {
            error400("It is not possible open the url: " + this.toString());
        }
        sendRequestMethod();
        readRequest();
        return this.getResponseBody();
    }

    @Override
    public String patch() {
        mCurrentRequestMethod = HTTP_PATCH_REQUEST;
        return null;
    }

    @Override
    public String delete() {
        mCurrentRequestMethod = HTTP_DELETE_REQUEST;
        return null;
    }

}
