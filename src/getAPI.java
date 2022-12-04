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
    private static Element eElement;

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, NullPointerException {
//        System.out.println(detailURL);
        parsing();
    }

    public static void parsing(){
        try{
            final String KEY = "c89c9b35a0354e8e9a6d47b43834b769";
            String genre_code = "AAAB"; //뮤지컬 장르 코드
            String URL = "https://www.kopis.or.kr/openApi/restful/pblprfr?service=d2ff79b5a9ef4f2a87d79a89e7e35de7&stdate=20220101&eddate=20221230&prfstate=02&cpage=1&rows=2000&shcate=AAAB";
            DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactoty.newDocumentBuilder();
            Document doc = dBuilder.parse(URL);

            // 제일 첫번째 태그
            doc.getDocumentElement().normalize();
            // 파싱할 tag
            NodeList nList = doc.getElementsByTagName("db");

            //nList.getLength();
            for(int i=0; i<nList.getLength(); i++){
                Node nNode = nList.item(i);
                eElement = (Element) nNode;
                String showID = getTagValue("mt20id", eElement);
                System.out.println("뮤지컬코드 : " + getTagValue("mt20id", eElement)); //뮤지컬 코드
                String detailURL = "http://www.kopis.or.kr/openApi/restful/pblprfr/" + showID + "?service=" + KEY;
                System.out.println(detailURL);

                DocumentBuilderFactory dbFactoty2 = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder2 = dbFactoty.newDocumentBuilder();
                Document doc2 = dBuilder.parse(detailURL);
                doc2.getDocumentElement().normalize();
                NodeList nList2 = doc.getElementsByTagName("db");
                Node nNode2 = nList.item(i);
                Element eElement2 = (Element) nNode;

                System.out.println(getTagValue("prfnm", eElement2)); //뮤지컬이름
                System.out.println("배우" + getTagValue("prfcast", eElement2)); //배우
                System.out.println("장소: " + getTagValue("prfruntime", eElement2)); //장소
                System.out.println("러닝타임: " + getTagValue("fcltynm", eElement2)); //러닝타임
                System.out.println("가격: " + getTagValue("pcseguidance", eElement2)); //가격
                System.out.println(getTagValue("poster", eElement2)); //포스터사진
                System.out.println("시작: " +getTagValue("prfpdfrom", eElement2)); //시작날짜
                System.out.println("끝: " +getTagValue("prfpdto", eElement2)); //끝날짜
                System.out.println("========================================================");

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // tag값의 정보를 가져오는 함수
    public static String getTagValue(String tag, Element eElement){
        if(null == eElement.getElementsByTagName(tag)) {
            return null;
        }

        if(null == eElement.getElementsByTagName(tag).item(0)) {
            return null;
        }
        //결과를 저장할 result 변수
        String result = "";
        NodeList nlList =  eElement.getElementsByTagName(tag).item(0).getChildNodes();
        Node nValue = (Node)nlList.item(0);
        if(nValue == null)
            return null;

        return nValue.getNodeValue();

    }

//    public static String getPosterURL(Element eElement){
//        String showID2 = getTagValue("poster", eElement);
//        return showID2;
//    }
}
