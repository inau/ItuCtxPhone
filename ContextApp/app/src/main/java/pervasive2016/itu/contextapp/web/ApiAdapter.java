package pervasive2016.itu.contextapp.web;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.Observer;

import javax.net.ssl.HttpsURLConnection;

import pervasive2016.itu.contextapp.data.entity.BeaconEntity;
import pervasive2016.itu.contextapp.data.entity.ContextEntity;

/**
 * Created by Ivan on 22-Mar-16.
 */
public class ApiAdapter<T> extends AsyncTask<URL, Long, T[]>{
    final static String remoteIP = "http://contextphone.appspot.com/";

    final Class<T> returnType;
    final WebMethod request_type;
    final Map<String, String> headers;
    final Observer requester;
    final String request_body;

    /**
     * Wraps API calls to my JsonService
     * @param methodType HTTP GET, POST, DELETE, UPDATE etc.
     * @param headers HTTP headers in Map of String:String (key, value) ie. "Content-Type" : "application/json"
     * @param requestBody The http body to send if needed
     * @param requester Observer reference for callback
     * @param returnType Reference of return type class for instantiation of elements should be same as parameterized class
     */
    public ApiAdapter(WebMethod methodType, Map<String, String> headers, String requestBody,  Observer requester, Class<T> returnType) {
        this.request_type = methodType;
        this.headers = headers;
        this.requester = requester;
        this.returnType = returnType;
        this.request_body = requestBody;
    }


    @Override
    protected T[] doInBackground(URL... params) {
        T[] responseData = null;

        try {
            if(params[0]  == null) return responseData;
            URL url = params[0];
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            if(headers != null)
            for (Map.Entry<String, String> p : headers.entrySet()) {
                conn.addRequestProperty(p.getKey(), p.getValue());
            }

            switch (request_type) {
                case GET:
                    break;
                case POST:
                    conn.setDoOutput(true);
                    OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream() );
                    out.write(request_body);
                    out.flush();
                    out.close();
                    break;
                case DELETE:
                    conn.setRequestMethod(request_type.getRequest());
                    break;
            }


            conn.connect();

            if (conn.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                // Do normal input or output stream reading
                InputStreamReader in = new InputStreamReader((InputStream) conn.getContent());

                Gson gson = new Gson();
                JsonParser parser = new JsonParser();
                JsonElement root = parser.parse(in);
                if( root.isJsonArray() ) {
                    JsonArray rootArr = root.getAsJsonArray();
                    responseData = getReturnInstanceArray(rootArr.size());
                    for (int i = 0; i < rootArr.size(); i++) {
                        JsonObject element = rootArr.get(i).getAsJsonObject();
                        responseData[i] = gson.fromJson(element, returnType);
                    }
                } else if ( root.isJsonObject() ) {
                    responseData = getReturnInstanceArray(1);
                    responseData[0] = gson.fromJson(root, returnType);
                }
                in.close();
            } else {
                //response = "FAILED"; // See documentation for more info on response handling
                return null;
            }
        } catch (IOException e) {
            //TODO Handle problems..
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return responseData;
    }

    @Override
    protected void onPostExecute(T[] ts) {
        super.onPostExecute(ts);
        if (requester != null)
            requester.update( null, ts );
    }

    /****************************************************************************
     Helper classes
     ****************************************************************************/
    //factories
    public static ApiAdapter<ContextEntity> getApihandlerCTX(Observer obs, String body, WebMethod webm) {
        return new ApiAdapter<ContextEntity>(webm,
                null, body,
                obs, ContextEntity.class);
    }
    public static ApiAdapter<BeaconEntity> getApihandlerBCS(Observer obs, String body, WebMethod webm) {
        return new ApiAdapter<BeaconEntity>(webm,
                null, body,
                obs, BeaconEntity.class);
    }

    public enum APIS {
        CONTEXTS(remoteIP+"r/contexts"),
        BEACONS(remoteIP+"r/beacons");

        final String url;

        APIS(String s) {
            url = s;
        }

        public String getUrl() {
            return url;
        }
    }

    /**
     * Defines the basic HTTP methods for API Call
     */
    public enum WebMethod {
        GET("GET"), POST("POST"), DELETE("DELETE");

        final private String request;

        WebMethod(String request) {
            this.request = request;
        }

        public String getRequest() {
            return request;
        }
    }

    /****************************************************************************
     Helper methods for instantiating generic array and element for return types and URLS
     ****************************************************************************/
    public static URL urlBuilderFilter(APIS api, long lat, long lng, Date d) throws MalformedURLException {
        String res;
        res = d == null ? "?lat="+lat+"&lng="+lng : "?lat="+lat+"&lng="+lng+"&after="+d.getTime();
        return new URL(api.getUrl() + res);
    }

    public static URL urlBuilder(APIS api, String resource) throws MalformedURLException {
        return new URL(api.getUrl() + resource);
    }
    protected T[] getReturnInstanceArray(int size) throws IllegalAccessException, InstantiationException {
        return (T[]) Array.newInstance(returnType, size);
    }

    protected T getReturnInstanceObject() throws IllegalAccessException, InstantiationException {
        return (T) returnType.newInstance();
    }
}
