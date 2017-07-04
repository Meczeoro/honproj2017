/**
 * Created by Mec on 20/03/2017.
 */
/*
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.appengine.http.UrlFetchTransport;
//import com.google.api.client.extensions.auth.helpers.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.extensions.appengine.auth.oauth2.AppIdentityCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.appengine.api.appidentity.AppIdentityService;
import com.google.appengine.api.appidentity.AppIdentityServiceFactory;
import com.google.common.io.CharStreams;


import org.json.JSONObject;
import org.json.JSONTokener;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.List;


@SuppressWarnings("serial")
public class UrlShortener extends HttpServlet {

    String blah = "unchanged";
    String one = "nope";
    String two = "nope";
    String three = "nope";
    String four = "nope";
    String five = "nope";
    String resp = "nope";
    String x = "https://www.google.co.uk/";

    /** Application name. */
    private static final String APPLICATION_NAME =
            "Test Project 20-03-17";


    /** Directory to store user credentials for this application. */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
           "../GDriveAuthTester/");
            // System.getProperty("user.dir"), ".credentials/gdrive");

    /** Global instance of the {@link FileDataStoreFactory}. */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY =
            JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;

    /** Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/drive-java-quickstart
     */
    private static final List<String> SCOPES =
            Arrays.asList(DriveScopes.DRIVE_METADATA_READONLY);

    static {

        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }


    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in =
                UrlShortener.class.getResourceAsStream("web/WEB-INF/client_id_test_project.json");
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                        .setDataStoreFactory(DATA_STORE_FACTORY)
                        .setAccessType("offline")
                        .build();
        Credential credential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver()).authorize("user");
        System.out.println(
                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    /**
     * Build and return an authorized Drive client service.
     * @return an authorized Drive client service
     * @throws IOException
     */

    public static Drive getDriveService() throws IOException {
        Credential credential = authorize();
        return new Drive.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    private static InputStream downloadFile(Drive service, File file)
    {
        if (file.getDownloadUrl() != null && file.getDownloadUrl().length() > 0) {
            try {
                HttpResponse resp =
                        service.getRequestFactory().buildGetRequest(new GenericUrl(file.getDownloadUrl()))
                                .execute();
                return resp.getContent();
            } catch (IOException e) {
                // An error occurred.
                e.printStackTrace();
                return null;
            }
        } else {
            // The file doesn't have any content stored on Drive.
            return null;
        }
    }



    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {



        x = request.getParameter("input");
        System.out.println(x);
        try {
          //  Drive service = getDriveService();

            resp = createShortUrl(x);

        } catch (Exception e) {
            e.printStackTrace();
        }

        AppIdentityService appBro = AppIdentityServiceFactory.getAppIdentityService();
        blah = appBro.getServiceAccountName();
        System.out.println(blah);

        request.setAttribute("Response", resp);

        request.setAttribute("Additional", blah);

        request.setAttribute("one", one);
        request.setAttribute("two", two);
        request.setAttribute("three", three);
        request.setAttribute("four", four);
        request.setAttribute("five", five);
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);




    }



        // [START asserting_identity_to_Google_APIs]
    /**
     * Returns a shortened URL by calling the Google URL Shortener API.
     *
     * <p>Note: Error handling elided for simplicity.
     */
    public String createShortUrl(String longUrl) throws Exception {
        ArrayList<String> scopes = new ArrayList<String>();
        scopes.add("https://www.googleapis.com/auth/urlshortener");
       /* scopes.add("https://www.googleapis.com/auth/drive");
        scopes.add("https://www.googleapis.com/auth/drive.appdata");
        scopes.add("https://www.googleapis.com/auth/drive.file");
        scopes.add("https://www.googleapis.com/auth/drive.metadata");
        scopes.add("https://www.googleapis.com/auth/drive.metadata.readonly");
        scopes.add("https://www.googleapis.com/auth/drive.photos.readonly");
        scopes.add("https://www.googleapis.com/auth/drive.readonly");
        scopes.add("https://www.googleapis.com/auth/drive.scrips"); */

        final AppIdentityService appIdentity = AppIdentityServiceFactory.getAppIdentityService();
        final AppIdentityService.GetAccessTokenResult accessToken = appIdentity.getAccessToken(scopes);

        // The token asserts the identity reported by appIdentity.getServiceAccountName()
        JSONObject request = new JSONObject();
        request.put("longUrl", longUrl);
        System.out.println("1: "+longUrl);
        one = "yes";
        URL url = new URL("https://www.googleapis.com/urlshortener/v1/url?pp=1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.addRequestProperty("Content-Type", "application/json");
        connection.addRequestProperty("Authorization", "Bearer " + accessToken.getAccessToken());

        System.out.println("Authorization: " + accessToken.getAccessToken());

        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
        request.write(writer);
        writer.close();
        System.out.println("2: "+longUrl);
        two = "yes";
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            // Note: Should check the content-encoding.
            //       Any JSON parser can be used; this one is used for illustrative purposes.
            System.out.println("3: "+longUrl);
            three = "yes";
            JSONTokener responseTokens = new JSONTokener(connection.getInputStream());
            JSONObject response = new JSONObject(responseTokens);
            four = "yes";
            return (String) response.get("id");
        } else { five = "yes";
            try (InputStream s = connection.getErrorStream();
                 InputStreamReader r = new InputStreamReader(s, StandardCharsets.UTF_8)) {
                throw new RuntimeException(String.format(
                        "got error (%d) response %s from %s",
                        connection.getResponseCode(),
                        CharStreams.toString(r),
                        connection.toString()));
            }
       // else{return "1";}
        }
    }



}
    // [END asserting_identity_to_Google_APIs]
