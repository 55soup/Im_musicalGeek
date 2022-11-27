import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class getAPI {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        final String KEY = "c89c9b35a0354e8e9a6d47b43834b769";
        String genre_code = "AAAB"; //뮤지컬 장르 코드
        String URL = "https://www.kopis.or.kr/openApi/restful/pblprfr?service=d2ff79b5a9ef4f2a87d79a89e7e35de7&stdate=20220101&eddate=20221230&prfstate=02&cpage=1&rows=2000&shcate=AAAB";
        String showID = "PF195242";
        int page=1;
        String detailURL = "http://www.kopis.or.kr/openApi/restful/pblprfr/" + showID + "?service=" + KEY;
//        System.out.println(detailURL);

        try{
            DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactoty.newDocumentBuilder();
            Document doc = dBuilder.parse(URL);

            // 제일 첫번째 태그
            doc.getDocumentElement().normalize();

            // 파싱할 tag
            NodeList nList = doc.getElementsByTagName("db");

            for(int i=0; i<nList.getLength(); i++){
                Node nNode = nList.item(i);
                Element eElement = (Element) nNode;
                System.out.println("뮤지컬코드 : " + getTagValue("mt20id", eElement));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    // tag값의 정보를 가져오는 함수
    public static String getTagValue(String tag, Element eElement) {

        //결과를 저장할 result 변수 선언
        String result = "";
        NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
        result = nlList.item(0).getTextContent();

        return result;
    }
}
