import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.extensions.appengine.datastore.AppEngineDataStoreFactory;
import com.google.api.client.extensions.appengine.http.UrlFetchTransport;
//import com.google.api.client.extensions.auth.helpers.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.extensions.appengine.auth.oauth2.AppIdentityCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.ParentReference;
import com.google.appengine.api.appidentity.AppIdentityService;
import com.google.appengine.api.appidentity.AppIdentityServiceFactory;
import com.google.common.io.CharStreams;


import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

//import java.io.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import static java.lang.System.out;

/**
 * Created by Mec on 20/03/2017.
 */
public class Driver extends  HttpServlet {

    private static HttpTransport httpTransport;
    private static JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

   /* private static String APPLICATION_NAME = "Test-Project-20-03-17";
    private static String USER_EMAIL = "X@gmail.com";
    private static String SERVICE_ACCOUNT_EMAIL = "X@appspot.gserviceaccount.com";
    private static String CLIENT_ID = "XXXXXXXXX";
    private static String KEY_PASSWORD = "XXXXXXXXXXX";
    private static String CLIENT_SECRET_P12_LOC = "../client_id_test_project.json"; */

    private static String size;
    String six = "nope";
    Drive service;
    //int size = 20;
    static String seven = "nope";
    static String eight = "nope";
    static String nine = "nope";
    static String ten = "nope";
    String output = "";

    

    String testTitle = "TestFile.jpg";
    String testDesc = "Test Description";
    String testMime = "image/jpeg";

    JSONArray w = null;
    static String q = "nah";
    List<File> files;

    String inputFileUrl = null;
    String listCheck = null;

    String client_secret = "XXXXX";
    String client_id = "X.apps.googleusercontent.com";
    HttpTransport httpTransport2 = new NetHttpTransport();
    JsonFactory jsonFactory = new JacksonFactory();

    private static final AppEngineDataStoreFactory DATA_STORE_FACTORY =
            AppEngineDataStoreFactory.getDefaultInstance();

    GoogleCredential credential;
    GoogleCredential servCredential;

    String scope1 = "https://www.googleapis.com/auth/drive";
    Collection<String> scopes = Collections.singletonList(scope1);

    String privateKey = "X.p12";



    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {

        String Access_Token = (String) request.getAttribute("Response");
        //String fileId = (String) request.getParameter("getFile");
        // String savedToken = (String) request.getParameter("Response2");
        Enumeration paramCount = request.getParameterNames();
        String Code = request.getParameter("Code");

        //Service account auth = works
        try {
            servCredential = new GoogleCredential.Builder()
                    .setTransport(httpTransport2)
                    .setJsonFactory(jsonFactory)
                    .setServiceAccountId("X@appspot.gserviceaccount.com")
                    .setServiceAccountScopes(scopes)
                    .setServiceAccountPrivateKeyFromP12File(new java.io.File(privateKey))
                    .build();
        }catch(Exception sce){System.out.println("ServCred ex: "+sce);}


        if (request.getParameterMap().containsKey("path")) {
            inputFileUrl = request.getParameter("path");
            System.out.println("PATH: " + inputFileUrl);
        }

        if (request.getParameterMap().containsKey("list"))
        {
            listCheck = request.getParameter("list");
            System.out.println("LIST: " + listCheck);
        }


        // credential.getAccessToken();

        try {
            if (Access_Token != null) {
                System.out.println("There was no file uploaded");
                credential = new GoogleCredential.Builder()
                        .setTransport(httpTransport2)
                        .setJsonFactory(jsonFactory)
                        .setClientSecrets(client_id, client_secret).build();
                credential.setAccessToken(Access_Token);
                /*        .setClientSecrets(client_id, client_secret).build();
                Access_Token = credential.getRefreshToken();
                credential.setAccessToken(Access_Token); */


                service = service(credential);
                //files = retrieveAllFiles(service);
            }/* else if (listCheck != null) {
                System.out.println("else list part");
                credential = new GoogleCredential.Builder()
                        .setTransport(httpTransport2)
                        .setJsonFactory(jsonFactory)
                        .setClientSecrets(client_id, client_secret).build();
                credential.setAccessToken(credential.getRefreshToken());
            } */
             else {
                System.out.println("else part");
                credential = new GoogleCredential.Builder()
                        .setTransport(httpTransport2)
                        .setJsonFactory(jsonFactory)
                        .setClientSecrets(client_id, client_secret).build();
                credential.setAccessToken(credential.getRefreshToken());
                //service = service(credential);
                System.out.println("File: " + inputFileUrl + " attempting to upload");

                uploadFile(service, inputFileUrl);
            }
            //  w = retrieveAllFiles(service);
            System.out.println("W: " + w);
            // q = jsonReader(w);
            //   size = files.toString();
            nine = "yes";
        } catch (Exception e) {
            System.out.println("Ex: " + e);
        }
        six = "yes";

if (listCheck != null) {

    try{
        files = retrieveAllFiles(service);
    }catch(Exception e) {
        System.out.println("list ex: " + e);
    }
    q = jsonReader(files);
    request.setAttribute("listlength", q);
    System.out.println("Q: " + q);
}

        request.setAttribute("six",six);
        request.setAttribute("seven",seven);
        request.setAttribute("eight",eight);
        request.setAttribute("nine",nine);
        request.setAttribute("ten",ten);
    RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request,response);
}


  /*  @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        Drive service = getDriveService(req, resp);
        ClientFile clientFile = new ClientFile(req.getReader());
        File file = clientFile.toFile();

        if (!clientFile.content.equals("")) {
            file = service.files().insert(file,
                    ByteArrayContent.fromString(clientFile.mimeType, clientFile.content))
                    .execute();
        } else {
            file = service.files().insert(file).execute();
        }

        resp.setContentType(JSON_MIMETYPE);
        resp.getWriter().print(new Gson().toJson(file.getId()).toString());
    }

 */

    static Drive service(Credential credential) {
        //  AppIdentityCredential credential =
        //         new AppIdentityCredential(Arrays.asList(DriveScopes.DRIVE));
        seven = "yes";
        return new Drive.Builder(new UrlFetchTransport(), JacksonFactory.getDefaultInstance(), credential)
                .setApplicationName("Test-Project-20-03-17")
                .build();

    }
    //List<File>
    private static List<File> retrieveAllFiles(Drive service) throws IOException {
        List<File> result = new ArrayList<File>();
        Drive.Files.List request = service.files().list();

        do {
            try {
                FileList files = request.execute();
                eight = "yes";
                result.addAll(files.getItems());
                request.setPageToken(files.getNextPageToken());
            } catch (IOException e) {
                System.out.println("An error occurred: " + e);
                request.setPageToken(null);
                nine = "yes";
            }
        } while (request.getPageToken() != null &&
                request.getPageToken().length() > 0);
        ten = "yes";
        // int x = result.size();
        //size = x;

      /*  if (result != null)
        {

        } */


        return result;
    }


    public String jsonReader(List<File> input) {
        Gson gson = new Gson();
        String json = gson.toJson(input);

        JSONArray jsonArray = new JSONArray(json);
        System.out.println(jsonArray);
        output = "";
        if (jsonArray != null && jsonArray.length() > 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
                String dlUrl = "";
                String removeMe = "gd=true";
                int start = 0;
                String name = jsonArray.getJSONObject(i).getString("title");
                //  if (jsonArray.getJSONObject(i).getString("downloadUrl") != null)
                try {
                    dlUrl = jsonArray.getJSONObject(i).getString("downloadUrl");
                    if (dlUrl.endsWith(removeMe))
                    {dlUrl = dlUrl.substring(0, dlUrl.length()-8);}
                    //start = dlUrl.indexOf(removeMe);
                   // dlUrl = dlUrl.substring(dlUrl.indexOf("&dg=true"), dlUrl.length());
                } catch (Exception e) {
                    JSONObject one = jsonArray.optJSONObject(i).getJSONObject("exportLinks");
                    String two = one.getString("application/pdf");
                    // JSONObject three = (JSONObject) two.getJSONObject(i).get("application/pdf");

                    dlUrl = two;
                }

                output += (i+1) +" "+ name +" "+ "<a href='"+dlUrl+"'>Download</a><br/><br/>";
                System.out.println((i + 1) + " name: " + name + " url: " + dlUrl);



               /* JSONArray childJsonArray = jsonArray.optJSONArray(i);
                if (childJsonArray != null && childJsonArray.length() > 0) {
                    for (int j = 0; j < childJsonArray.length(); j++) {
                        System.out.println("B" + childJsonArray.optString(j));
                    }
                } */
                }
        }
        return output;
    }


    /**
     * Insert new file.
     *
     * @param service Drive API service instance.
     * @param title Title of the file to insert, including the extension.
     * @param description Description of the file to insert.
     * @param parentId Optional parent folder's ID.
     * @param mimeType MIME type of the file to insert.
     * @param filename Filename of the file to insert.
     * @return Inserted file metadata if successful, {@code null} otherwise.
     */
    private static File insertFile(Drive service, String title, String description,
                                   String parentId, String mimeType, String filename) {
        // File's metadata.
        File body = new File();
        body.setTitle(title);
        body.setDescription(description);
        body.setMimeType(mimeType);

        // Set the parent folder.
        if (parentId != null && parentId.length() > 0) {
            body.setParents(
                    Arrays.asList(new ParentReference().setId(parentId)));
        }

        // File's content.
        java.io.File fileContent = new java.io.File(filename);
        FileContent mediaContent = new FileContent(mimeType, fileContent);
        try {
            File file = service.files().insert(body, mediaContent).execute();

            // Uncomment the following line to print the File ID.
             System.out.println("File ID: " + file.getId());

            return file;
        } catch (IOException e) {
            System.out.println("An error occurred: " + e);
            return null;
        }
    }

    private static void uploadFile(Drive service, String inputFileUrl) throws IOException
    {
        File fileMetadata = new File();
        fileMetadata.setTitle(inputFileUrl);
        java.io.File filePath = new java.io.File(inputFileUrl);
        System.out.println("Path: "+filePath);
        System.out.println(new java.io.File(".").getAbsolutePath());

       FileContent mediaContent = new FileContent("image/jpeg", filePath);
        File file = service.files().insert(fileMetadata, mediaContent)
                .setFields("id")
                .execute();
        q = "";
        System.out.println("File ID: " + file.getId());
    }

}

