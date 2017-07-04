import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Mec on 24/03/2017.
 */
public class OAuth2Handler0 extends HttpServlet {

    String Response = "placeholder";
    String Response2 = "response2";
    String RefreshResponse = "placeholder";
    String Code = "";
    String Token = "default";
    String path = "";
    String deployURI = "X.appspot.com/OAuth2Servlet";
    String deployURI2 = "X.appspot.com/OAuth2Servlet";
    String deployURI3 = "X.appspot.com/OAuth2Servlet";
    String localURI = "http://localhost:8080/OAuth2Servlet";
    private final String USER_AGENT = "Mozilla/5.0";
    //String API_Key = "X";
    // String Serv_Client_ID = "X.iam.gserviceaccount.com";
    //String API_Client_ID = "X.apps.googleusercontent.com";
    String Client_Secret = "XXXX";
    String Client_ID = "X.apps.googleusercontent.com";

    String Client_ID2 = "X.apps.googleusercontent.com";
    String Client_Secret2 = "XXXX";

    String Client_ID3 = "X.apps.googleusercontent.com";
    String Client_Secret3 = "XXXX";



    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        System.out.println("WE IN OAUTH20Handler");
        // redirect to google for authorization
        StringBuilder oauthUrl = new StringBuilder().append("https://accounts.google.com/o/oauth2/auth")
                .append("?client_id=").append(Client_ID3) // the client id from the api console registration
                .append("&response_type=code")
                .append("&scope=https://www.googleapis.com/auth/drive") // scope is the api permissions we are requesting
                .append("&redirect_uri=").append(localURI) // the servlet that google redirects to after authorization
                .append("&state=1234")
                .append("&access_type=offline") // here we are asking to access to user's data while they are not signed in
                .append("&approval_prompt=force"); // this requires them to verify which account to use, if they are already signed in
        System.out.println(oauthUrl.toString());
        response.sendRedirect(oauthUrl.toString());
    }

}