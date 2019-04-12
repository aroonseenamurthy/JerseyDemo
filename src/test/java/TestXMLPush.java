import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TestXMLPush {
	public static void main(String[] args) {        
        try {
            String url = "http://localhost:8080/JerseyDemos/service/publishxml";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type","application/xml;charset=utf-8");
            String urlParameters = ""
            		+ "<Employee>"
            		+ "<firstName>John</firstName>"
            		+ "<lastName>Smith</lastName>"
            		+ "<dateOfBirth>12/11/2001</dateOfBirth>"
            		+ "</Employee>";
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
            String responseStatus = con.getResponseMessage();

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println("response:" + response.toString());
            System.out.println("Response Status:"+responseStatus);

            
        } catch (IOException e) {
            System.out.println("error" + e.getMessage());
        }
	}
}
    
	

