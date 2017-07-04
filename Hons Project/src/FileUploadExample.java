/**
 * Created by Mec on 24/03/2017.
 */
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.ParentReference;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

public class FileUploadExample extends HttpServlet {
    private static final int MAX_MEMORY_SIZE = 10240;
    private static final String DATA_DIRECTORY = null;
    private static final int MAX_REQUEST_SIZE = 10240;


    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

// Check that we have a file upload request
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);


        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload();

// Parse the request
        FileItemIterator iter = null;
        try {
            iter = upload.getItemIterator(request);
        } catch (FileUploadException e) {
            e.printStackTrace();
        }
        try {
            while (iter.hasNext()) {
                FileItemStream item = iter.next();
                String name = item.getFieldName();
                InputStream stream = item.openStream();
                if (item.isFormField()) {
                    System.out.println("Form field " + name + " with value "
                            + Streams.asString(stream) + " detected.");
                } else {
                    System.out.println("File field " + name + " with file name "
                            + item.getName() + " detected.");
                    // Process the input stream
                    request.setAttribute("name", name);
                }
            }
        } catch (FileUploadException e) {
            e.printStackTrace();
        }


        //RequestDispatcher rd = request.getRequestDispatcher("/Drive2Servlet");
        //rd.forward(request, response);


      //  insertFile(service, "name", "fake desc", null, "image/jpeg", "namename.jpeg", stream);


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
    private static com.google.api.services.drive.model.File insertFile(Drive service, String title, String description,
                                                                       String parentId, String mimeType, String filename, InputStream stream) {
        // File's metadata.
        com.google.api.services.drive.model.File body = new com.google.api.services.drive.model.File();
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
            com.google.api.services.drive.model.File file = service.files().insert(body, mediaContent).execute();

            // Uncomment the following line to print the File ID.
            System.out.println("File ID: " + file.getId());

            return file;
        } catch (IOException e) {
            System.out.println("An error occurred: " + e);
            return null;
        }
    }

}







        /*
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);

        if (isMultipart) {
            // Create a factory for disk-based file items
            FileItemFactory factory = new DiskFileItemFactory();

            // Create a new file upload handler
            ServletFileUpload upload = new ServletFileUpload(factory);

            try {
                // Parse the request
                List /* FileItem */ /* items = upload.parseRequest(request);
                Iterator iterator = items.iterator();
                while (iterator.hasNext()) {
                    FileItem item = (FileItem) iterator.next();
                    if (!item.isFormField()) {
                        String fileName = item.getName();
                        String root = getServletContext().getRealPath("/");
                        File path = new File(root + "/uploads");
                        if (!path.exists()) {
                            boolean status = path.mkdirs();
                        }

                        File uploadedFile = new File(path + "/" + fileName);
                        System.out.println(uploadedFile.getAbsolutePath());
                        item.write(uploadedFile);
                    }
                }
            } catch (FileUploadException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } */

