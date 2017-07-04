import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.appengine.datastore.AppEngineDataStoreFactory;
import com.google.api.client.extensions.appengine.http.UrlFetchTransport;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.batch.BatchRequest;
import com.google.api.client.googleapis.batch.json.JsonBatchCallback;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.ParentReference;
import com.google.api.services.drive.model.Permission;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreInputStream;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
//import com.google.appengine.api.files.AppEngineFile;
//import com.google.appengine.api.files.FileService;
//import com.google.appengine.api.files.FileServiceFactory;
//import com.google.appengine.api.files.FileServicePb;
import com.google.appengine.api.images.*;
import com.google.appengine.api.images.Image;
import com.google.appengine.repackaged.com.google.protobuf.ByteString;
import com.google.appengine.repackaged.org.joda.time.DateTime;
import com.google.appengine.tools.cloudstorage.*;
import com.google.cloud.Page;
import com.google.gson.Gson;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.joda.time.Seconds;
import org.json.JSONArray;
import org.json.JSONObject;
import org.mortbay.jetty.HttpParser;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.util.*;
import java.util.List;
//import com.google.api.client.extensions.auth.helpers.Credential;
//import java.io.*;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.BucketInfo;

import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsInputChannel;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;

/**
 * Created by Mec on 20/03/2017.
 */
public class Driver2 extends  HttpServlet {

    private static HttpTransport httpTransport;
    private static JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

   /* private static String APPLICATION_NAME = "Test-Project-20-03-17";
    private static String USER_EMAIL = "XXXXXXXXXXXX@gmail.com";
    private static String SERVICE_ACCOUNT_EMAIL = "XXXXXXXXXXXX@appspot.gserviceaccount.com";
    private static String CLIENT_ID = "XXXXXXXXXXXXXXX";
    private static String KEY_PASSWORD = "XXXXXXXXXXXXXXXX";
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
    String state = null;




    String testTitle = "TestFile.jpg";
    String testDesc = "Test Description";
    String testMime = "image/jpeg";

    JSONArray w = null;
    static String q = "nah";
    List<File> files;

    String inputFileUrl = null;
    static String listCheck = null;
    String Access_Token = null;
    String Refresh_Token = null;
    String SavedRefToken = null;
    String deleteID = null;
    String shareID = null;
    String unshareID = null;
    static String permID = null;

    String fileName = null;
    String mime = null;
    InputStream is;

    //String client_secret = "XXXXXXXXXX";
    //String client_id = "X.apps.googleusercontent.com";

    String client_id = "X.apps.googleusercontent.com";
    String client_secret = "XXXXXXXXXX";



    HttpTransport httpTransport2 = new NetHttpTransport();
    JsonFactory jsonFactory = new JacksonFactory();

    private static final AppEngineDataStoreFactory DATA_STORE_FACTORY =
            AppEngineDataStoreFactory.getDefaultInstance();

    GoogleCredential credential;
    GoogleCredential servCredential;

    String scope1 = "https://www.googleapis.com/auth/drive";
    Collection<String> scopes = Collections.singletonList(scope1);

    org.joda.time.DateTime creationTime = null;
    org.joda.time.DateTime refreshTime = null;

    String privateKey = "X.p12";

   /* private final GcsService gcsService = GcsServiceFactory.createGcsService(new RetryParams.Builder()
            .initialRetryDelayMillis(10)
            .retryMaxAttempts(10)
            .totalRetryPeriodMillis(15000)
            .build());
    private static final int BUFFER_SIZE = 2 * 1024 * 1024;
    public static final boolean SERVE_USING_BLOBSTORE_API = false; */


    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        System.out.println("WE IN DRIVER2");
        //     if (request.getParameterMap().containsKey("Response")) {
        Access_Token = (String) request.getAttribute("Response");
        System.out.println("Resp Token: " + Access_Token);
        Refresh_Token = (String) request.getAttribute("Refresh");
        System.out.println("Ref Code: " + Refresh_Token);
        if (Refresh_Token != null && Refresh_Token.contains("1/"))
        {
            SavedRefToken = Refresh_Token;

            System.out.println("Saved Ref Code was set");
        }

        System.out.println("Saved Ref Code: " + SavedRefToken);
     //   }
        //String fileId = (String) request.getParameter("getFile");
        // String savedToken = (String) request.getParameter("Response2");
        Enumeration paramCount = request.getParameterNames();
        String Code = request.getParameter("Code");

        //Service account auth = works
      /*  try {
            servCredential = new GoogleCredential.Builder()
                    .setTransport(httpTransport2)
                    .setJsonFactory(jsonFactory)
                    .setServiceAccountId("X@appspot.gserviceaccount.com")
                    .setServiceAccountScopes(scopes)
                    .setServiceAccountPrivateKeyFromP12File(new java.io.File(privateKey))
                    .build();
        }catch(Exception sce){System.out.println("ServCred ex: "+sce);} */


        if (request.getParameterMap().containsKey("path")) {
            inputFileUrl = request.getParameter("path");
            System.out.println("PATH: " + inputFileUrl);
        }

        if (request.getParameterMap().containsKey("list"))
        {
            listCheck = request.getParameter("list");
            System.out.println("LIST: " + listCheck);
        }

        if (request.getParameterMap().containsKey("state"))
        {
            state = request.getParameter("state");
            System.out.println("STATE: "+state);
        }

        if (request.getParameterMap().containsKey("delete"))
        {
            deleteID = request.getParameter("delete");
            System.out.println("DELETE: "+deleteID);
        }

        if (request.getParameterMap().containsKey("share"))
        {
            shareID = request.getParameter("share");
            System.out.println("SHARE: "+shareID);
        }

        if (request.getParameterMap().containsKey("unshare"))
        {
            unshareID = request.getParameter("unshare");
            System.out.println("UNSHARE: "+unshareID);
        }

        // credential.getAccessToken();

        try {
            if (Access_Token == null && credential == null)
            {
                System.out.println("No Token/No Creds");
               //response.sendRedirect("http://X.appspot.com/OAuth20Servlet");
                //response.sendRedirect("https:X.appspot.com/OAuth20Servlet");
                //response.sendRedirect("https:X.appspot.com/OAuth20Servlet");
               response.sendRedirect("http://localhost:8080/OAuth20Servlet");
               // RequestDispatcher rd = request.getRequestDispatcher("OAuth20Servlet");
              //  rd.forward(request,response);
            }
            else if (Access_Token != null) {
                //System.out.println("There was no file uploaded");
                System.out.println("No Cred: Access Token: "+ Access_Token); //TRY WITH LIMITED LENGTH OF TIME CREDS
                credential = new GoogleCredential.Builder()
                        .setTransport(httpTransport2)
                        .setJsonFactory(jsonFactory)
                        .setClientSecrets(client_id, client_secret).build();
                credential.setAccessToken(Access_Token);
                System.out.println("Ori Exp: "+credential.getExpiresInSeconds());
                credential.setExpiresInSeconds((long) 20);
                System.out.println("New Exp: "+credential.getExpiresInSeconds());
                /*        .setClientSecrets(client_id, client_secret).build();
                Access_Token = credential.getRefreshToken();
                credential.setAccessToken(Access_Token); */

                creationTime = org.joda.time.DateTime.now();

                System.out.println("Creation time" + creationTime);

                service = service(credential);
                Access_Token = null;
                System.out.println("Deleting token after service creation. Token value: "+Access_Token);
                //files = retrieveAllFiles(service);
                //Access_Token == null &&
            } else if ((SavedRefToken != null)&& (credential != null)) {
                refreshTime = org.joda.time.DateTime.now();
                System.out.println("Current time" + refreshTime);

                Seconds t = Seconds.secondsBetween(creationTime, refreshTime);
                String tString = t.toString();
                tString = tString.substring(tString.indexOf("PT")+2, tString.indexOf("S"));
                System.out.println("tstring: "+tString);
                int timeInt = Integer.parseInt(tString);
                System.out.println("Seconds between creation/current time: "+ timeInt + " seconds.");
                System.out.println("No Token: Access Token: "+ Access_Token);
                System.out.println("Refresh token sender attempt: "+ SavedRefToken);
                //HttpSession session = request.getSession(true);
                request.setAttribute("refcode", SavedRefToken);
                request.setAttribute("uptime", timeInt);

               if (timeInt >3500) {
                   creationTime = org.joda.time.DateTime.now();
                   refreshTime = null;
                   //response.sendRedirect("X.appspot.com/OAuth20Servlet");
                  //response.sendRedirect("X.appspot.com/OAuth20Servlet");
                  //response.sendRedirect("X.appspot.com/OAuth20Servlet");
                   response.sendRedirect("http://localhost:8080/OAuth20Servlet");

                 //  RequestDispatcher rd = request.getRequestDispatcher("/OAuth2Servlet");
                 //  rd.forward(request, response);
                   return;
               }

               /* credential = new GoogleCredential.Builder()
                        .setTransport(httpTransport2)
                        .setJsonFactory(jsonFactory)
                        .setClientSecrets(client_id, client_secret).build();
                credential.setAccessToken(credential.getRefreshToken()); */
            }

            else
            {System.out.println("Completely screwed authentication");}
            //  w = retrieveAllFiles(service);
            // q = jsonReader(w);
            //   size = files.toString();
            nine = "yes";
        } catch (Exception e) {
            System.out.println("Auth Check Ex: " + e);
        }
        six = "yes";

if (listCheck != null && (Access_Token != null || credential != null)){
    shareID = null;
    unshareID = null;
    deleteID = null;
    System.out.println("List check passed");

    try{
        files = retrieveAllFiles(service);
    }catch(Exception e) {
        System.out.println("list ex: " + e);
    }
    q = jsonReader(files);
    request.setAttribute("listlength", q);
    System.out.println("Q: " + q);
}


/* if (inputFileUrl != null && (Access_Token != null || credential != null)) {

    try {
        uploadFile(service, inputFileUrl);
        System.out.println("File: " + inputFileUrl + " was uploaded to Drive.");
    } catch (Exception upex)
    {   request.setAttribute("Error", "Error: File not found");
        System.out.println("Upload Exception: "+upex);}
} */

if (deleteID != null && (Access_Token != null || credential != null))
{
    try {
        listCheck = null;
        deleteFile(service, deleteID);
        request.setAttribute("listlength", "<h2 style='padding-left: 150px;'>File ID: "+deleteID+"<br/> Has been succesfully deleted from Drive<br/><br/><form action = \"Drive2Servlet\">\n" +
                "    <input type=\"hidden\" name=\"list\" value=\"true\">\n" +
                "    <input  class='btn btn-default' type = \"submit\" value=\"Back to List\"></form></h2>");
        System.out.println("File: "+deleteID+" was deleted from Drive.");
    }
    catch (Exception delex)
        {
            System.out.println("Deletion Exception: "+delex);
    }
}

if (shareID != null && (Access_Token != null || credential != null))
{
    try {
        deleteID = null;
        shareFile(service, shareID);
        request.setAttribute("listlength", "<h2 style='padding-left: 150px;'>File ID: "+shareID+"<br/> can now be shared by QRCode<br/><br/><form action = \"Drive2Servlet\">\n" +
                "    <input type=\"hidden\" name=\"list\" value=\"true\">\n" +
                "    <input  class='btn btn-default' type = \"submit\" value=\"Back to List\"></form></h2>");
        System.out.println("File: "+shareID+" can now be shared.");

    }
    catch (Exception delex)
    {
        System.out.println("Deletion Exception: "+delex);
    }
}
        if (unshareID != null && (Access_Token != null || credential != null))
        {
            try {
                deleteID = null;
                removePermission(service, unshareID);
                request.setAttribute("listlength", "<h2 style='padding-left: 150px;'>File ID: "+unshareID+"<br/> is now restricted from sharing<br/><br/><form action = \"Drive2Servlet\">\n" +
                        "    <input type=\"hidden\" name=\"list\" value=\"true\">\n" +
                        "    <input  class='btn btn-default' type = \"submit\" value=\"Back to List\"></form></h2>");
                System.out.println("File: "+unshareID+" now cannot be shared.");

            }
            catch (Exception delex)
            {
                System.out.println("Deletion Exception: "+delex);
            }
        }

   /*     GcsFilename fileName = getFileName(request);
        if (SERVE_USING_BLOBSTORE_API) {
            BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
            BlobKey blobKey = blobstoreService.createGsBlobKey(
                    "/gs/" + fileName.getBucketName() + "/" + fileName.getObjectName());
            blobstoreService.serve(blobKey, response);
        } else {
            GcsInputChannel readChannel = gcsService.openPrefetchingReadChannel(fileName, 0, BUFFER_SIZE);
            copy(Channels.newInputStream(readChannel), response.getOutputStream());
        }
*/
        request.setAttribute("PermID", permID);
        request.setAttribute("showID", shareID);
        request.setAttribute("six",six);
        request.setAttribute("seven",seven);
        request.setAttribute("eight",eight);
        request.setAttribute("nine",nine);
        request.setAttribute("ten",ten);
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request,response);
}

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            if (Access_Token == null && credential == null)
            {
                System.out.println("No Token/No Creds");
               // response.sendRedirect("X.appspot.com//OAuth20Servlet");
                //response.sendRedirect("X.appspot.com/OAuth20Servlet");
                //response.sendRedirect("X.appspot.com/OAuth20Servlet");
                response.sendRedirect("http://localhost:8080/OAuth20Servlet");
                // RequestDispatcher rd = request.getRequestDispatcher("OAuth20Servlet");
                //  rd.forward(request,response);
            }
            else if (Access_Token != null && credential == null) {
                System.out.println("There was no file uploaded");
                System.out.println("No Cred: Access Token: "+ Access_Token);
                credential = new GoogleCredential.Builder()
                        .setTransport(httpTransport2)
                        .setJsonFactory(jsonFactory)
                        .setClientSecrets(client_id, client_secret).build();
                credential.setAccessToken(Access_Token);
                /*        .setClientSecrets(client_id, client_secret).build();
                Access_Token = credential.getRefreshToken();
                credential.setAccessToken(Access_Token); */


                service = service(credential);
                Access_Token = null;
                System.out.println("[POST]Deleting token after service creation. Token value: "+Access_Token);
                //files = retrieveAllFiles(service);
            } else if (Access_Token == null && credential != null) {
                System.out.println("No Token: Access Token: "+ Access_Token);
                credential = new GoogleCredential.Builder()
                        .setTransport(httpTransport2)
                        .setJsonFactory(jsonFactory)
                        .setClientSecrets(client_id, client_secret).build();
                credential.setAccessToken(credential.getRefreshToken());
            }

            else
            {System.out.println("Completely screwed authentication");}
        } catch (Exception e) {
            System.out.println("Auth Check Ex: " + e);
        }
        System.out.println("IN DRIVER2 POST");


/*
        GcsFileOptions instance = GcsFileOptions.getDefaultInstance();
        GcsFilename fileName = getFileName(request);
        GcsOutputChannel outputChannel;
        outputChannel = gcsService.createOrReplace(fileName, instance);
        copy(request.getInputStream(), Channels.newOutputStream(outputChannel));
*/




       // Storage storage = StorageOptions.getDefaultInstance().getService();
      //  String bucketName = "X.appspot.com"; // Change this to something unique
      //  Bucket bucket = storage.get(bucketName);
      //  Blob blob = bucket.create(
        //"my_blob_name", "a simple blob".getBytes(UTF_8), "text/plain");

       /* ServletFileUpload upload = new ServletFileUpload();
        try {
            FileItemIterator iter = upload.getItemIterator(request);
            List list = null;
            while (iter.hasNext()) {
                FileItemStream item = iter.next();
                fileName = item.getName();
                mime = item.getContentType();
                is = item.openStream();

                if(item.isFormField()) {
                    // Process regular form field (input type="text|radio|checkbox|etc", select, etc).
                } else {




           /*  //   BlobId blobId = BlobId.of(bucketName, "blob_"+fileName);
                OutputStream os = new OutputStream() {
                    @Override
                    public void write(int b) throws IOException {

                    }
                };
                byte[] by = new byte[BUFFER_SIZE];
                int readBytes = is.read(by, 0, BUFFER_SIZE);
                System.out.println("Byte1: "+readBytes);
                while (readBytes != -1) {
                    os.write(by, 0, readBytes);
                    readBytes = is.read(by, 0, readBytes);

                    }
                    try {

                        System.out.println("Name: "+fileName + " Mime: "+mime);
                        insertFileA(service, credential, "uploaded_" + fileName, null, mime, fileName, is);
                    } catch (Exception insertEx) {
                        System.out.println("Insert Ex: " + insertEx);
                    }

                System.out.println("Byte2: "+readBytes);

                os.close();
                is.close();


            }




        } catch (FileUploadException e) {
            e.printStackTrace();
        } */

        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request,response);
    }

    protected void doRefresh(String refcode)
    {

    }

   /* private GcsFilename getFileName(HttpServletRequest req) {
        String[] splits = req.getRequestURI().split("/", 4);
        if (!splits[0].equals("") || !splits[1].equals("gcs")) {
            throw new IllegalArgumentException("The URL is not formed as expected. " +
                    "Expecting /gcs/<bucket>/<object>");
        }
        return new GcsFilename(splits[2], splits[3]);
    } */

    /**
     * Transfer the data from the inputStream to the outputStream. Then close both streams.
     */
  /*  private void copy(InputStream input, OutputStream output) throws IOException {
        try {
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = input.read(buffer);
            while (bytesRead != -1) {
                output.write(buffer, 0, bytesRead);
                bytesRead = input.read(buffer);
            }
        } finally {
            input.close();
            output.close();
        }
    } */


    //parentId  ID folder drive
    public static File insertFileA(Drive service, GoogleCredential credential,String title, String parentId, String mimeType, String filename, InputStream stream) {
        System.out.println("INSERTFILEA");
        try {
            // File's metadata.
            File body = new File();
            body.setTitle(title);
            body.setMimeType(mimeType);

            // Set the parent folder.
            if (parentId != null && parentId.length() > 0) {
                body.setParents(
                        Arrays.asList(new ParentReference().setId(parentId)));
            }

            // File's content.
            InputStreamContent mediaContent = new InputStreamContent(mimeType, new BufferedInputStream(stream));
            try {
                File file = service.files().insert(body, mediaContent).execute();

                return file;
            } catch (IOException e) {
                System.out.println("whoops in file: " +e);
                return null;
            }

        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return null;
        }

    }

    static Drive service(Credential credential) {
        //  AppIdentityCredential credential =
        //         new AppIdentityCredential(Arrays.asList(DriveScopes.DRIVE));
        seven = "yes";
        return new Drive.Builder(new UrlFetchTransport(), JacksonFactory.getDefaultInstance(), credential)
                .setApplicationName("Honours-Project-2017")
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

        listCheck = null;
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
                String id = jsonArray.getJSONObject(i).getString("id");
                String icon = (String) jsonArray.getJSONObject(i).get("iconLink");
                boolean shared = (boolean) jsonArray.getJSONObject(i).get("shared");
                //  if (jsonArray.getJSONObject(i).getString("downloadUrl") != null)
                try {
                    dlUrl = jsonArray.getJSONObject(i).getString("downloadUrl");
                    if (dlUrl.endsWith(removeMe))
                    {dlUrl = dlUrl.substring(0, dlUrl.length()-8);}
                    //start = dlUrl.indexOf(removeMe);
                   // dlUrl = dlUrl.substring(dlUrl.indexOf("&dg=true"), dlUrl.length());
                    if (jsonArray.optJSONObject(i).getJSONObject("exportLinks") != null) {
                        JSONObject one = jsonArray.optJSONObject(i).getJSONObject("exportLinks");
                        String two = one.getString("application/pdf");

                        // JSONObject three = (JSONObject) two.getJSONObject(i).get("application/pdf");

                        dlUrl = two;}
                    else
                    {dlUrl = "no link";}
                } catch (Exception e) {

                }
                //"<form method='get action='"+dlUrl+"'><input type = 'submit' value='Download'></form>
               // "<a href='"+dlUrl+"'>Download</a><form action='Drive2Servlet'><input type='hidden' name='delete' value='"+id+"'>\n"
                if (shared == true) {
                    output += (i + 1) + " <img height='25px' src='" + icon + "'/>   " + name + "  <br/><img height ='150px' 'id='qrTag_" + id + "'src='https://chart.googleapis.com/chart?cht=qr&chs=100x100&chl=https://drive.google.com/file/d/" + id + "/view?usp'><table><tr><td><a target = '_blank' href=" + dlUrl + "'><button type='button' class='btn btn-primary'>File Preview</button></a></td><td><a target='_blank' href='https://drive.google.com/file/d/" + id + "/view?usp'><button type='button' class='btn btn-primary'>Shared Link</button></a></td></tr><tr><td><br/><form action='Drive2Servlet'><input type='hidden' name='unshare' value='" + id + "'><input type = 'submit' class = 'btn btn-warning' value='Toggle Share' ></form><form action='Drive2Servlet'><input type='hidden' name='delete' value='" + id + "'></td>" +
                            "<td><input type = 'submit'class = 'btn btn-danger' value='Delete'></form></td></tr></table><br/><br/>";
                }
                else
                {
                    output += (i + 1) + " <img height='25px' src='" + icon + "'/>   " + name + "  <br/><table><tr><td><a target = '_blank' href=" + dlUrl + "'><button type='button' class='btn btn-primary'>File Preview</button></a></td><td></td></tr><tr><td><br/><form action='Drive2Servlet'><input type='hidden' name='share' value='" + id + "'><input type = 'submit' class = 'btn btn-warning' value='Toggle Share' ></form><form action='Drive2Servlet'><input type='hidden' name='delete' value='" + id + "'></td>" +
                            "<td><input type = 'submit'class = 'btn btn-danger' value='Delete'></form></td></tr></table><br/><br/>";
                }
                System.out.println((i + 1) + " name: " + name + " url: " + dlUrl + " id: " +id);



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
     * Permanently delete a file, skipping the trash.
     *
     * @param service Drive API service instance.
     * @param fileId ID of the file to delete.
     */
    private static void deleteFile(Drive service, String fileId) {
        try {
            service.files().delete(fileId).execute();
        } catch (IOException e) {
            System.out.println("An error occurred: " + e);
        }
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
                                   String parentId, String mimeType, String filename, InputStream stream) {
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
       // java.io.File fileContent = new java.io.File(filename);

        InputStreamContent mediaContent = new InputStreamContent(mimeType, new BufferedInputStream(stream));
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


private static void shareFile(Drive service, String fileId) {
    JsonBatchCallback<Permission> callback = new JsonBatchCallback<Permission>() {
        @Override
        public void onFailure(GoogleJsonError e,
                              HttpHeaders responseHeaders)
                throws IOException {
            // Handle error
            System.err.println(e.getMessage());
        }

        @Override
        public void onSuccess(Permission permission,
                              HttpHeaders responseHeaders)
                throws IOException {
            System.out.println("Permission ID: " + permission.getId());
            permID = permission.getId();
        }
    };

    BatchRequest batch = service.batch();
    Permission userPermission = new Permission()
            .setType("anyone")
            .setRole("reader")
            .setWithLink(true)
            .setValue("user@example.com");
    try {

        System.out.println("user: "+userPermission);
        service.permissions().insert(fileId, userPermission)
                .setFields("id")
                .queue(batch, callback);
    } catch (IOException e) {
        e.printStackTrace();
        System.out.println("User permission exception: "+e);
    }

    /*try { //disabled 9/4/17

        Storage storage = StorageOptions.getDefaultInstance().getService();

        String bucketName = "X.appspot.com"; // Change this to something unique
        Bucket bucket = storage.get(bucketName);
        // Upload a blob to the newly created bucket
        BlobId blobId = BlobId.of(bucketName, "my_blob_name");
        // Blob blob = bucket.create(
        // "my_blob_name", "a simple blob".getBytes(UTF_8), "text/plain");
        //qrGen();
    }catch(Exception qrEx){System.out.println("QR Ex: "+qrEx);}
 */
 /*   Permission domainPermission = new Permission()
            .setType("domain")
            .setRole("reader")
            .setValue("example.com");
    try {

        System.out.println("dom: "+domainPermission);
        service.permissions().insert(fileId, domainPermission)
                .setFields("id")
                .queue(batch, callback);
    } catch (IOException e) {
        e.printStackTrace();
        System.out.println("Domain permission exception: "+e);
    }
 */

    try {
        batch.execute();
    } catch (IOException e) {
        e.printStackTrace();
    }

}

    private static void unShareFile(Drive service, String fileId) {
        JsonBatchCallback<Permission> callback = new JsonBatchCallback<Permission>() {
            @Override
            public void onFailure(GoogleJsonError e,
                                  HttpHeaders responseHeaders)
                    throws IOException {
                // Handle error
                System.err.println(e.getMessage());
            }

            @Override
            public void onSuccess(Permission permission,
                                  HttpHeaders responseHeaders)
                    throws IOException {
                System.out.println("Permission ID: " + permission.getId());
            }
        };

        BatchRequest batch = service.batch();
        Permission userPermission = new Permission()
                .setType("anyone")
                .setRole("reader")
                .setWithLink(true)
                .setValue("user@example.com");
        try {

            System.out.println("user: "+userPermission);
            service.permissions().insert(fileId, userPermission)
                    .setFields("id")
                    .queue(batch, callback);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("User permission exception: "+e);
        }

        try {
            batch.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void removePermission(Drive service, String fileId) {
        String permissionId = "anyoneWithLink";

        try {
            service.permissions().delete(fileId, permissionId).execute();
        } catch (IOException e) {
            System.out.println("An error occurred: " + e);
        }
    }

public static void qrGen()
{


    Storage storage = StorageOptions.getDefaultInstance().getService();

    String bucketName = "X.appspot.com"; // Change this to something unique
    Bucket bucket = storage.get(bucketName);
    Page<Blob> blobs = bucket.list();
    Iterator<Blob> blobIterator = blobs.iterateAll();
    while (blobIterator.hasNext()) {
        Blob blob = blobIterator.next();
        System.out.println("blob" + blob.getName().toString());
    }


  /*  // Create a bucket
    String bucketName = "X.appspot.com"; // Change this to something unique
    Bucket bucket = storage.get(bucketName);
*/



        System.out.println("IN QRGEN");
       // String text = "hello world";
        //java.io.File qrcode = QRCode.from("http://localhost:8080").to(ImageType.PNG).file();
        //System.out.println(qrcode.getAbsoluteFile());

  /*  ByteArrayOutputStream bout =
   // java.io.File qrtag =
            QRCode.from("http://memorynotfound.com")
                    .withSize(250, 250)
                    .to(ImageType.PNG)
                    .stream();
                    //.file(); */
    //byte[] imgTest = QRCode.from("www.google.com").to(ImageType.PNG).stream().toByteArray();
   // Blob blob = bucket.create("QRCode", imgTest,"image/png");
    //return ImagesServiceFactory.makeImage(imgTest);

    //try {
   //     try (BlobOutputStream out =
    //        bout.writeTo(out);
    //        out.flush();
   //         out.close();
   //     }
// Upload a blob to the newly created bucket
      /*  BlobId blobId = BlobId.of(bucketName, "my_blob_name");
       // Blob blob = bucket.create(
               // "my_blob_name", "a simple blob".getBytes(UTF_8), "text/plain");
        Blob blob = bucket.create("testQr", isf.setImageData(imgTest), "image/png");
*/

    //} catch (FileNotFoundException e){
   //     e.printStackTrace();
   // } catch (IOException e) {
  //     e.printStackTrace();
  //  }

}

}

