import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TestXMLMultiPush {
	public static void main(String[] args) {        
        try {
            String url = "http://localhost:8080/JerseyDemos/service/publishmultixml";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type","application/xml;charset=utf-8");
            String urlParameters = ""
            		+"<Employees>"
            		+ "<Employee>"
            		+ "<firstName>John1</firstName>"
            		+ "<lastName>Smith</lastName>"
            		+ "<dateOfBirth>12/11/20011</dateOfBirth>"
            		+ "</Employee>"
               		+ "<Employee>"
            		+ "<firstName>Adam1</firstName>"
            		+ "<lastName>Smeeth</lastName>"
            		+ "<dateOfBirth>13/11/20011</dateOfBirth>"
            		+ "</Employee>"
            	    + "</Employees>";
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
            
        } catch (IOException e) {
            System.out.println("error" + e.getMessage());
        }
	}
}
    
	

