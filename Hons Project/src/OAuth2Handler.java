import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Mec on 20/03/2017.
 */
public class OAuth2Handler extends HttpServlet {

    String Response = "placeholder";
    String Response2 = "response2";
    String RefreshResponse = "placeholder";
    String Code = "";
    String RefCode = "";
    String Token = "default";
    String path = "";
    private final String USER_AGENT = "Mozilla/5.0";
    //String API_Key = "XXXXXXXXXXX";
   // String Serv_Client_ID = "XXXXXXXXXX";
    //String API_Client_ID = "X.apps.googleusercontent.com";
    String Client_Secret = "XXXXXXXX";
    String Client_ID = "X.apps.googleusercontent.com";

    String Client_ID2 = "X.apps.googleusercontent.com";
    String Client_Secret2 = "XXXXXXX";

    String Client_ID3 = "X.apps.googleusercontent.com";
    String Client_Secret3 = "XXXXXXX";

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        System.out.println("WE IN OAUTH2Handler");
            System.out.println("sendPost block");

//HttpSession session = request.getSession(true);
                Code = request.getParameter("code");
                RefCode = (String) request.getAttribute("refcode");
                String uptime = (String) request.getAttribute("uptime");
                System.out.println("Oauth2 ref code: "+RefCode);

                System.out.println("Code?: " + Code);
                // request.setAttribute("API", API_Key);
                // request.setAttribute("SID", Serv_Client_ID);
        request.setAttribute("uptime", uptime);
                request.setAttribute("ID", Client_ID3);
                request.setAttribute("Token", Token);


                //Code = Code.substring(0,Code.length()-1);
        if (Code != null) {
            try {
                System.out.println("sending post (code)");
                sendPost(Code);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
        {
            try {
                System.out.println("Ref code tried to be passed: " + RefCode);
                sendPost(RefCode);
            }catch(Exception e){};
        }

                request.setAttribute("Response", Response);
                request.setAttribute("Refresh", RefreshResponse);
                RequestDispatcher rd = request.getRequestDispatcher("/Drive2Servlet");
                rd.forward(request, response);
            }


    // HTTP POST request for token
    private void sendPost(String Client_Code) throws Exception {

        String url = "https://www.googleapis.com/oauth2/v4/token";
        // String codeStr = Client_Code; //auth code goes here
        String clientStr = Client_ID3;
        String secretStr = Client_Secret3;
       //String uriStr = "X.appspot.com/OAuth2Servlet";
        //String uriStr = "X.appspot.com/OAuth2Servlet";
        //String uriStr = "X.appspot.com/OAuth2Servlet";
      String uriStr = "http://localhost:8080/OAuth2Servlet"; //Change this for local/web deployments
        String authStr = "authorization_code";
        String urlParameters = null;
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Host", "www.googleapis.com");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        if (Client_Code.contains("4/")){
            System.out.println("CALLED ACCESS TOKEN");
            //String urlParameters = "code="+codeStr+"&client_id="+clientStr+"&client_secret="+secretStr+"&redirect_uri="+uriStr+"&grant_type="+authStr;
            urlParameters = "code=" + Client_Code + "&client_id=" + clientStr + "&client_secret=" + secretStr + "&redirect_uri=" + uriStr + "&grant_type=" + authStr;
        }
        else{
            System.out.println("CALLED REFRESH");
            urlParameters = "client_id=" + clientStr + "&client_secret=" + secretStr + "&refresh_token=" + Client_Code + "&grant_type=refresh_token";
        }

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);


        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println("PR: "+response.toString());

        Response = response.toString();
        RefreshResponse = response.toString();
        Response2 = response.toString();
        String accessStr = "access_token";
        String endStr = "token_type";
        String refreshStr = "refresh_token";
        String endRefStr = "}";


        int start = Response.indexOf(accessStr);
        int finish = Response.lastIndexOf(endStr);
        int startRefresh = Response.indexOf(refreshStr);
        int finishRefresh = Response.lastIndexOf(endRefStr);
        System.out.println("Access: "+start+"Finish: "+finish);
        System.out.println("Refresh: "+startRefresh+"RefFinish: "+finishRefresh);
        Response = Response.substring(start+16, finish-4);

        RefreshResponse = RefreshResponse.substring(startRefresh+17, finishRefresh-1);
        System.out.println("Resp: "+Response+" Ref: "+RefreshResponse);
    }
}
