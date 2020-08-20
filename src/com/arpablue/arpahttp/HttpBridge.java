/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arpablue.arpahttp;
// https://www.mkyong.com/webservices/jax-rs/restfull-java-client-with-java-net-url/

import java.io.IOException;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author augusto
 */
public abstract class HttpBridge extends Msger{

    public static final String HTTP_GET = "GET";

    protected static String USER_AGENT = "Mozilla/5.0";
    public static String PROTOCOL_HTTP = "http";
    public static String PROTOCOL_HTTPS = "https";
    public static int PORT_HTTP = 80;
    public static int PORT_HTTPS = 445;

    protected String mCurrentRequestMethod;

    protected int mPort = 80;
    protected String mHost;
    protected String mProtocol;
    protected String mEndPoint;
    private ArrayList<HttpParameter> mParameters;
    private ArrayList<HttpParameter> mHeaders;

    protected int mResponseCode;
    protected String mResponseBody;
    protected String mData;

    protected boolean mbOpen;

    protected HttpURLConnection mCon;

    /**
     * Execute a HTTP GET method.
     *
     * @return return the response of the method.
     */
    public abstract String get();

    /**
     * Execute a HTTP GET method.
     *
     * @return return the response of the method.
     */
    public abstract String post();

    /**
     * Execute a HTTP PUT method.
     *
     * @return return the response of the method.
     */
    public abstract String put();

    /**
     * Execute a HTTP PATCH method.
     *
     * @return return the response of the method.
     */
    public abstract String patch();

    /**
     * Execute a HTTP DELETE method.
     *
     * @return return the response of the method.
     */
    public abstract String delete();

    /**
     * It specify the json data to be send. It method is used for all http
     * methods except the GET method.
     *
     * @param json It is he json data to be send.
     */
    public void setJsonData(String json) {
        mData = json;
    }

    /**
     * Return th current json send for the request.
     *
     * @return
     */
    public String getJsonData() {
        return mData;
    }

    /**
     * It remove all parameters.
     */
    public void clearParameter() {
        this.mParameters.clear();
    }

    /**
     * It remove all headers.
     */
    public void clearHeaders() {
        this.mHeaders.clear();
    }

    /**
     * It verify the current connection is opened.
     *
     * @return
     */
    public boolean isOpen() {
        return mbOpen;
    }

    /**
     * Change the current state to open.
     *
     * @param open It is true if the current connection is opened.
     */
    protected void setOpen(boolean open) {
        mbOpen = open;
    }

    /**
     * Return the current response code.
     *
     * @return It is the code of the response.
     */
    public int getResponseCode() {
        return mResponseCode;
    }

    /**
     * It return the body of the response.
     *
     * @return It is the text of the response.
     */
    public String getResponseBody() {
        return mResponseBody;
    }

    /**
     * It execute an HTTP GET request in the URL specified.
     *
     * @param url It is the URL where the HTTP GET method is called.
     * @return It is the text of the response.
     */
    public String get(String url) {
        this.setURL(url);
        return get();
    }

    /**
     * It execute an HTTP DELETE request in the URL specified.
     *
     * @param url It is the URL where the HTTP DELETE method is called.
     * @return It is the text of the response.
     */
    public String delete(String url) {
        this.setURL(url);
        return delete();
    }

    /**
     * It execute an HTTP POST request in the URL specified.
     *
     * @param url It is the URL where the HTTP POST method is called.
     * @return It is the text of the response.
     */
    public String post(String url) {
        this.setURL(url);
        return post();
    }

    /**
     * It execute an HTTP PUT request in the URL specified.
     *
     * @param url It is the URL where the HTTP PUT method is called.
     * @return It is the text of the response.
     */
    public String put(String url) {
        this.setURL(url);
        return put();
    }

    /**
     * It execute an HTTP PATCH request in the URL specified.
     *
     * @param url It is the URL where the HTTP PATCH method is called.
     * @return It is the text of the response.
     */
    public String patch(String url) {
        this.setURL(url);
        return post();
    }

    /**
     * It specify the port
     *
     * @param port It is the port, if the port is negative then the value will
     * be change to positive.
     */
    public void setPort(int port) {
        mPort = port;
        if (mPort < 0) {
            mPort = mPort * -1;
        }

    }

    /**
     * It specify the port number in a string.
     *
     * @param port It is the string that contain the port number.
     */
    public void setPort(String port) {
        setPort(80);
        if (port == null) {
            return;
        }
        try {
            setPort(Integer.parseInt(port));
        } catch (Exception e) {
        }

    }

    /**
     * It return the current port number.
     *
     * @return It is the port of the current port number.
     */
    public int getPort() {
        return mPort;
    }

    /**
     * It specify the current host.
     *
     * @param host It is the host direction, the http, https and other protocols
     * are removed and specified as a port.
     */
    public void setHost(String host) {
        mHost = host;
        if (mHost == null) {
            return;
        }
        String[] v = null;
        mHost = formatUrl(mHost);
        if (mHost.indexOf("://") > -1) {
            v = mHost.split("://");
            mHost = v[1];
            this.setProtocol(v[0]);
        }
        if (mHost.indexOf(":") == -1) {
            return;
        }
        v = mHost.split(":");
        mHost = v[0];
        this.setPort(v[1]);
    }

    /**
     * It specify the protocol in an string.
     *
     * @param protocol It is the protocol use for the current connection( http,
     * https, ftp, etc)
     */
    public void setProtocol(String protocol) {
        mProtocol = protocol;
        if (mProtocol == null) {
            mProtocol = HttpBridge.PROTOCOL_HTTP;
            return;
        }
        mProtocol = formatUrl(mProtocol);
        mProtocol = mProtocol.toLowerCase();
        putPort();
    }

    /**
     * It evaluate the protocol to be used in the call.
     */
    protected void putPort() {
        if (mProtocol.equalsIgnoreCase(HttpBridge.PROTOCOL_HTTPS)) {
            this.setPort(PORT_HTTPS);
            return;
        }
        this.setPort(PORT_HTTP);

    }

    /**
     * It specify the end point to be used in the call.
     *
     * @param endPoint It is the end point of the host, by default is empty.
     */
    public void setEndPoint(String endPoint) {
        mEndPoint = endPoint;
        if (mEndPoint == null) {
            mEndPoint = "/";
            return;
        }
        mEndPoint = formatUrl(mEndPoint);
        mEndPoint = "/" + mEndPoint;
    }

    /**
     * It set in format an URL.
     *
     * @param target It is the URL to be reviewed an set in correct format.
     * @return It is the URL with the correct format.
     */
    protected static String formatUrl(String target) {
        if (target == null) {
            return target;
        }
        target = target.replace('\\', '/');
        target = target.replace('/', ' ');
        target = target.trim();
        target = target.replace(' ', '/');
        return target;
    }

    /**
     * It add a parameter specifying its name and value.
     *
     * @param name It is the name of the parameter.
     * @param value It is the value of the parameter.
     */
    public void addParameter(String name, String value) {
        addParameter(new HttpParameter(name, value));
    }

    /**
     * It add a parameter object tot he request.
     *
     * @param parameter It is the parameter object.
     */
    public void addParameter(HttpParameter parameter) {
        if (mParameters == null) {
            mParameters = new ArrayList<HttpParameter>();
        }
        mParameters.add(parameter);
    }

    /**
     * It add a parameter specifying its name and value.
     *
     * @param name It is the name of the parameter.
     * @param value It is the value of the parameter.
     */
    public void addHeader(String name, String value) {
        addHeader(new HttpParameter(name, value));
    }

    /**
     * It add a parameter object tot he request.
     *
     * @param parameter It is the parameter object.
     */
    public void addHeader(HttpParameter parameter) {
        if (mHeaders == null) {
            mHeaders = new ArrayList<HttpParameter>();
        }
        mHeaders.add(parameter);
    }

    /**
     * It specify the URL to be used in the calls, aal the data of the host,
     * port, end point is extracted from this URL.
     *
     * @param url It is the url of the host.
     */
    public void setURL(String url) {
        this.setHost(null);
        this.setProtocol(HttpBridge.PROTOCOL_HTTP);
        this.setEndPoint(null);
        this.setPort(80);
        if (url == null) {
            return;
        }
        String aux = url;
        aux = formatUrl(aux);
        aux = aux.replace("//", "#*::*#");

        String[] v = null;
        if (aux.indexOf("/") > -1) {
            v = aux.split("/");
            aux = v[0].replace("#*::*#", "//");
            url = url.replace(aux, "");
            this.setEndPoint(url);
        }
        aux = aux.replace("#*::*#", "//");
        this.setHost(aux);
    }

    /**
     * It return the URL used for the current calls.
     *
     * @return It is the URL used for the current calls.
     */
    public String getUrl() {
        String res = this.getProtocol() + "://" + this.getHost();
        boolean addProto = true;
        if ((this.getPort() == HttpBridge.PORT_HTTP) && (this.getProtocol().equalsIgnoreCase(HttpBridge.PROTOCOL_HTTP))) {
            addProto = false;
        }
        if ((this.getPort() == HttpBridge.PORT_HTTPS) && (this.getProtocol().equalsIgnoreCase(HttpBridge.PROTOCOL_HTTPS))) {
            addProto = false;
        }
        if (addProto) {
            res = res + ":" + this.getPort();
        }
        if (this.getEndPoint() != null) {
            res = res + this.getEndPoint();
        }
        return res;
    }

    /**
     * It set all data of the current connection in a JSON format.
     *
     * @return It is the JSON with all data of the current data.
     */
    public String toJSON() {
        String res = "{";
        res = res + "\"protocol\": \"" + this.getProtocol() + "\",";
        res = res + "\"host\": \"" + this.getHost() + "\",";
        res = res + "\"port\": " + this.getPort() + ",";
        res = res + "\"endPoint\": \"" + this.getEndPoint() + "\",";
        res = res + "\"url\": \"" + this.getUrl() + "\"";
        res = res + "}";
        return res;
    }

    /**
     * It set all data of the connection in a JSON format, but the JSON
     * structure has the format to be set in a file or in a screen, this is more
     * comfortable to see.
     *
     * @return It is the data in a JSON format.
     */
    public String toJSONnicely() {
        String res = "{\n";
        res = res + "    \"protocol\": \"" + this.getProtocol() + "\",\n";
        res = res + "    \"host\": \"" + this.getHost() + "\",\n";
        res = res + "    \"port\": " + this.getPort() + ",\n";
        res = res + "    \"endPoint\": \"" + this.getEndPoint() + "\",\n";
        res = res + "    \"url\": \"" + this.getUrl() + "\"\n";
        res = res + "}";
        return res;
    }

    /**
     * It return the current protocol.
     *
     * @return It is the current protocol.
     */
    public String getProtocol() {
        return mProtocol;
    }

    /**
     * It return the current host used for the call.
     *
     * @return It is the host used.
     */
    public String getHost() {
        return mHost;
    }

    /**
     * It return the current end point used for the call.
     *
     * @return It is the current end point of the host, by default the end point
     * is null.
     */
    public String getEndPoint() {
        return mEndPoint;
    }

    /**
     * It return the current URL of the connection.
     *
     * @return It is the current URL used.
     */
    @Override
    public String toString() {
        return getUrl();
    }

    /**
     * It verify is the response code of the request is a success code, it code
     * should be between 200 and 299.
     *
     * @return It is true if the response code of the request is a success code.
     */
    public boolean isSuccess() {
        boolean  res = ((199 < this.getResponseCode()) && (this.getResponseCode() < 300));
        return res;
    }

    /**
     * It execute the current call to the host using the HTTP method specified.
     *
     * @return It is the body response of the request.
     */
    public String send(String method) {
        this.mResponseCode = 462;
        String url = this.getUrl();
        if (method == null) {
            error400("It is not possible send a request for unknow method or a NULL method.");
            return this.getResponseBody();
        }
        if (url == null) {
            error400("It is not possible process a NULL url.");
            return this.getResponseBody();
        }
        if (method.equalsIgnoreCase(HTTP_GET)) {
            sendGET();
        } else {
            sendNoGET(method);
        }
        return this.getResponseBody();
    }

    /**
     * It write a JSON file int he channel to send to the host.
     *
     * @param con It is the connection of the channel.
     */
    protected void writeJsonOnChannel() {
        try {
            if (this.mCon == null) {
                error417("( HttpBridge - writeJsonOnChannel ) : The channel is NULL.");
                return;
            }
            String jsonData = this.getJsonData();

            // Send post request
            this.mCon.setDoOutput(true);
            /// Sending the JSON data
            DataOutputStream wr = new DataOutputStream(this.mCon.getOutputStream());
            wr.writeBytes(jsonData);
            wr.flush();
            wr.close();
        } catch (IOException ex) {
            error417("( HttpBridge - writeJsonOnChannel ) " + ex.getMessage());
        }
    }

    protected void readingResponse() {
        BufferedReader in = null;
        try {
            // Reading the response
            in = new BufferedReader(
                    new InputStreamReader(this.mCon.getInputStream()));
            String output;
            StringBuffer response = new StringBuffer();
            while ((output = in.readLine()) != null) {
                response.append(output);
            }
            in.close();
            this.mResponseBody = response.toString();
        } catch (IOException ex) {
            error417("( HttpBridge - readingResponse ) " + ex.getMessage());
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                error417("( HttpBridge - readingResponse ) " + ex.getMessage());
            }
        }
    }

    /**
     * It open the channel for the connection with the host.
     *
     * @return It is true if the channel has been opened without problems.
     */
    protected boolean open() {
        try {
            if (!close()) {
                return false;
            }
            this.mCon = null;
            String url = this.getUrl();
            if (url == null) {
                error400("( HttpBridge - sendNoGET ): The URL cannot be NULL.");
                return false;
            }
            URL obj = new URL(url);
            this.mCon = (HttpURLConnection) obj.openConnection();
            this.setOpen(true);
            return true;
        } catch (MalformedURLException ex) {
            error417("( HttpBridge - sendNoGET ) " + ex.getMessage());
        } catch (IOException ex) {
            error417("( HttpBridge - sendNoGET ) " + ex.getMessage());
        }
        return false;
    }

    /**
     * It close the channel.
     *
     * @return It is true, the channel has been closed without problems.
     */
    protected boolean close() {
        this.setOpen(false);
        if (this.mCon == null) {
            return true;
        }
        return true;
    }

    /**
     * It execute a non GET HTTP request method.
     *
     * @param method It method is any HTTTP request different to GET.
     */
    protected void sendNoGET(String method) {
        try {
            if (method == null) {
                error400("( HttpBridge - sendNoGET ): The method cannot be NULL.");
                return;
            }
            if (!open()) {
                return;
            }
            // Setting basic post request
            this.mCon.setRequestMethod(method);
            sendHeaders();
            writeJsonOnChannel();
            this.mResponseCode = this.mCon.getResponseCode();
            readingResponse();
            close();
        } catch (Exception ex) {
            error417("( HttpBridge - sendNoGET ) " + ex.getMessage());
        }
    }

    protected void sendGET() {
        try {
            if (!open()) {
                return;
            }
            // By default it is GET request
            this.mCon.setRequestMethod(HttpBridge.HTTP_GET);
            sendHeaders();
            this.mResponseCode = this.mCon.getResponseCode();
            readingResponse();
            close();
        } catch (Exception e) {
            error417("( HttpBridge - sendGET ) ");
        }
    }

    protected void sendHeaders() {
        if (this.mCon == null) {
            return;
        }
        if (this.mHeaders == null) {
            return;
        }

        this.mHeaders.add(new HttpParameter("User-Agent", USER_AGENT));
        this.mHeaders.add(new HttpParameter("Accept-Language", "en-US,en;q=0.5"));
        this.mHeaders.add(new HttpParameter("Content-Type", "application/json"));

        for (HttpParameter param : this.mHeaders) {
            this.mCon.setRequestProperty("User-Agent", USER_AGENT);
        }
    }

    /**
     * It return an error in a JSON format.
     *
     * @param error It is the error code.
     * @param title It is the title of type of the error.
     * @param message
     */
    protected void errorHttp(int error, String title, String message) {
        this.mResponseCode = 400;
        this.mResponseBody = "{\"code\":" + error + ",";
        this.mResponseBody = this.mResponseBody + "\"title\":\"" + title + "\",";
        this.mResponseBody = this.mResponseBody + "\"body\":\"" + message + "\"";
        this.mResponseBody = this.mResponseBody + "}";
    }

    /**
     * It specify an error 400 in the body.
     *
     * @param message It is the message of the error.
     */
    protected void error400(String message) {
        errorHttp(400, "Bad Request", message);
    }

    /**
     * It specify an error 417 in the body.
     *
     * @param message It is the message of the error.
     */
    protected void error417(String message) {
        errorHttp(417, "Expectation Failed", message);
    }
}
