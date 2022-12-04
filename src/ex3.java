import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

public class ex3 extends CalendarDataManager{ // CalendarDataManager의 GUI
    // 창 구성요소와 배치도
    JFrame mainFrame;
//    ImageIcon icon = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("icon.png")));

    // getAPI
    getAPI getapi = new getAPI();
    private static Element eElement;

    JPanel calOpPanel;
    JButton todayBut;
    JLabel todayLab;
    JButton lYearBut;
    JButton lMonBut;
    JLabel curMMYYYYLab;
    JButton nMonBut;
    JButton nYearBut;
    ListenForCalOpButtons lForCalOpButtons = new ListenForCalOpButtons();

    JPanel calPanel;
    JButton weekDaysName[];
    JButton dateButs[][] = new JButton[6][7];
//    listenForDateButs lForDateButs = new listenForDateButs();

    JPanel infoPanel;
    JLabel infoClock;

    JPanel memoPanel;
    JLabel selectedDate;
    JTextArea memoArea;
    JScrollPane memoAreaSP;
    JPanel memoSubPanel;
    JButton saveBut;
    JButton delBut;
    JButton clearBut;

    JPanel frameBottomPanel;
    JLabel bottomInfo = new JLabel("Im_musicalGeek");
    //상수, 메세지
    final String WEEK_DAY_NAME[] = { "월", "화", "수", "목", "금", "토", "일" };
    final String title = "나는 뮤덕";
    final String SaveButMsg1 = "를 MemoData폴더에 저장하였습니다.";
    final String SaveButMsg2 = "메모를 먼저 작성해 주세요.";
    final String SaveButMsg3 = "<html><font color=red>ERROR : 파일 쓰기 실패</html>";
    final String DelButMsg1 = "메모를 삭제하였습니다.";
    final String DelButMsg2 = "작성되지 않았거나 이미 삭제된 memo입니다.";
    final String DelButMsg3 = "<html><font color=red>ERROR : 파일 삭제 실패</html>";
    final String ClrButMsg1 = "입력된 메모를 비웠습니다.";

    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                new ex3();
            }
        });
    }
    public ex3(){ //구성요소 순으로 정렬되어 있음. 각 판넬 사이에 빈줄로 구별

        mainFrame = new JFrame(title);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(700,400);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setResizable(false);
//        mainFrame.setIconImage(icon.getImage());
        try{
            UIManager.setLookAndFeel ("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");//LookAndFeel Windows 스타일 적용
            SwingUtilities.updateComponentTreeUI(mainFrame) ;
        }catch(Exception e){
            bottomInfo.setText("ERROR : LookAndFeel setting failed");
        }

        calOpPanel = new JPanel();
        todayBut = new JButton("Today");
        todayBut.setToolTipText("Today");
        todayBut.addActionListener(lForCalOpButtons);
        todayLab = new JLabel(today.get(Calendar.MONTH)+1+"/"+today.get(Calendar.DAY_OF_MONTH)+"/"+today.get(Calendar.YEAR));
        lYearBut = new JButton("<<");
        lYearBut.setToolTipText("Previous Year");
        lYearBut.addActionListener(lForCalOpButtons);
        lMonBut = new JButton("<");
        lMonBut.setToolTipText("Previous Month");
        lMonBut.addActionListener(lForCalOpButtons);
        curMMYYYYLab = new JLabel("<html><table width=100><tr><th><font size=5>"+((calMonth+1)<10?"&nbsp;":"")+(calMonth+1)+" / "+calYear+"</th></tr></table></html>");
        nMonBut = new JButton(">");
        nMonBut.setToolTipText("Next Month");
        nMonBut.addActionListener(lForCalOpButtons);
        nYearBut = new JButton(">>");
        nYearBut.setToolTipText("Next Year");
        nYearBut.addActionListener(lForCalOpButtons);
        calOpPanel.setLayout(new GridBagLayout());
        GridBagConstraints calOpGC = new GridBagConstraints();
        calOpGC.gridx = 1;
        calOpGC.gridy = 1;
        calOpGC.gridwidth = 2;
        calOpGC.gridheight = 1;
        calOpGC.weightx = 1;
        calOpGC.weighty = 1;
        calOpGC.insets = new Insets(5,5,0,0);
        calOpGC.anchor = GridBagConstraints.WEST;
        calOpGC.fill = GridBagConstraints.NONE;
        calOpPanel.add(todayBut,calOpGC);
        calOpGC.gridwidth = 3;
        calOpGC.gridx = 2;
        calOpGC.gridy = 1;
        calOpPanel.add(todayLab,calOpGC);
        calOpGC.anchor = GridBagConstraints.CENTER;
        calOpGC.gridwidth = 1;
        calOpGC.gridx = 1;
        calOpGC.gridy = 2;
        calOpPanel.add(lYearBut,calOpGC);
        calOpGC.gridwidth = 1;
        calOpGC.gridx = 2;
        calOpGC.gridy = 2;
        calOpPanel.add(lMonBut,calOpGC);
        calOpGC.gridwidth = 2;
        calOpGC.gridx = 3;
        calOpGC.gridy = 2;
        calOpPanel.add(curMMYYYYLab,calOpGC);
        calOpGC.gridwidth = 1;
        calOpGC.gridx = 5;
        calOpGC.gridy = 2;
        calOpPanel.add(nMonBut,calOpGC);
        calOpGC.gridwidth = 1;
        calOpGC.gridx = 6;
        calOpGC.gridy = 2;
        calOpPanel.add(nYearBut,calOpGC);

        calPanel=new JPanel();
        weekDaysName = new JButton[7];
        for(int i=0 ; i<CAL_WIDTH ; i++){
            weekDaysName[i]=new JButton(WEEK_DAY_NAME[i]);
            weekDaysName[i].setBorderPainted(false);
            weekDaysName[i].setContentAreaFilled(false);
            weekDaysName[i].setForeground(Color.WHITE);
            if(i == 0) weekDaysName[i].setBackground(new Color(227, 120, 120));
            else if (i == 6) weekDaysName[i].setBackground(new Color(114, 226, 217));
            else weekDaysName[i].setBackground(new Color(150, 150, 150));
            weekDaysName[i].setOpaque(true);
            weekDaysName[i].setFocusPainted(false);
            calPanel.add(weekDaysName[i]);
        }
        for(int i=0 ; i<CAL_HEIGHT ; i++){
            for(int j=0 ; j<CAL_WIDTH ; j++){
                dateButs[i][j]=new JButton();
                dateButs[i][j].setBorderPainted(false);
                dateButs[i][j].setContentAreaFilled(false);
                dateButs[i][j].setBackground(Color.WHITE);
                dateButs[i][j].setOpaque(true);
//                dateButs[i][j].addActionListener(lForDateButs);
                calPanel.add(dateButs[i][j]);
            }
        }
        calPanel.setLayout(new GridLayout(0,7,2,2));
        calPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        showCal(); // 달력을 표시

        infoPanel = new JPanel();
        infoPanel.setLayout(new BorderLayout());
        infoClock = new JLabel("", SwingConstants.RIGHT);
        infoClock.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        infoPanel.add(infoClock, BorderLayout.NORTH);
        selectedDate = new JLabel("<Html><font size=3>"+(today.get(Calendar.MONTH)+1)+"/"+today.get(Calendar.DAY_OF_MONTH)+"/"+today.get(Calendar.YEAR)+"&nbsp;(Today)</html>", SwingConstants.LEFT);
        selectedDate.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

        memoPanel=new JPanel();
        memoPanel.setBorder(BorderFactory.createTitledBorder("뮤지컬 편성표"));
        memoArea = new JTextArea();
        memoArea.setLineWrap(true);
        memoArea.setWrapStyleWord(true);
        memoAreaSP = new JScrollPane(memoArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        memoArea.setPreferredSize(new Dimension(400, 10000)); // 메모장크기 조절
        String[][] getMusicalList = parsing();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < getMusicalList.length; i++) {
            for (int j = 0; j < 5; j++) {
                stringBuilder.append(getMusicalList[i][j]+ " ");
            }
        }

        String strArrayToString = stringBuilder.toString();
        memoArea.setText(strArrayToString);
//        readMemo();

        // 메모 저장
        memoSubPanel=new JPanel();
//        saveBut = new JButton("저장");
//        saveBut.addActionListener(new ActionListener(){
//            public void actionPerformed(ActionEvent arg0) {
//                try {
//                    File f= new File("MemoData");
//                    if(!f.isDirectory()) f.mkdir();
//
//                    String memo = memoArea.getText();
//                    if(memo.length()>0){
//                        BufferedWriter out = new BufferedWriter(new FileWriter("MemoData/"+calYear+((calMonth+1)<10?"0":"")+(calMonth+1)+(calDayOfMon<10?"0":"")+calDayOfMon+".txt"));
//                        String str = memoArea.getText();
//                        out.write(str);
//                        out.close();
//                        bottomInfo.setText(calYear+((calMonth+1)<10?"0":"")+(calMonth+1)+(calDayOfMon<10?"0":"")+calDayOfMon+".txt"+SaveButMsg1);
//                    }
//                    else
//                        bottomInfo.setText(SaveButMsg2);
//                } catch (IOException e) {
//                    bottomInfo.setText(SaveButMsg3);
//                }
//                showCal();
//            }
//        });
        // 메모 삭제
//        delBut = new JButton("삭제");
//        delBut.addActionListener(new ActionListener(){
//            public void actionPerformed(ActionEvent e) {
//                memoArea.setText("");
//                File f =new File("MemoData/"+calYear+((calMonth+1)<10?"0":"")+(calMonth+1)+(calDayOfMon<10?"0":"")+calDayOfMon+".txt");
//                if(f.exists()){
//                    f.delete();
//                    showCal();
//                    bottomInfo.setText(DelButMsg1);
//                }
//                else
//                    bottomInfo.setText(DelButMsg2);
//            }
//        });
//        // 메모 초기화
//        clearBut = new JButton("초기화");
//        clearBut.addActionListener(new ActionListener(){
//            public void actionPerformed(ActionEvent arg0) {
//                memoArea.setText(null);
//                bottomInfo.setText(ClrButMsg1);
//            }
//        });

        try {
            BufferedImage image;
            //URL을 사용하는 경우

            URL url = new URL("이미지 URL");
            image = ImageIO.read(url);

        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
//        memoSubPanel.add(saveBut);
//        memoSubPanel.add(delBut);
//        memoSubPanel.add(clearBut);
        memoPanel.setLayout(new BorderLayout());
        memoPanel.add(selectedDate, BorderLayout.NORTH);
        memoPanel.add(memoAreaSP,BorderLayout.CENTER);
        memoPanel.add(memoSubPanel,BorderLayout.SOUTH);
        //calOpPanel, calPanel을  frameSubPanelWest에 배치
        JPanel frameSubPanelWest = new JPanel();
        Dimension calOpPanelSize = calOpPanel.getPreferredSize();
        calOpPanelSize.height = 90;
        calOpPanel.setPreferredSize(calOpPanelSize);
        frameSubPanelWest.setLayout(new BorderLayout());
        frameSubPanelWest.add(calOpPanel,BorderLayout.NORTH);
        frameSubPanelWest.add(calPanel,BorderLayout.CENTER);

        //infoPanel, memoPanel을  frameSubPanelEast에 배치
        JPanel frameSubPanelEast = new JPanel();
        Dimension infoPanelSize=infoPanel.getPreferredSize();
        infoPanelSize.height = 65;
        infoPanel.setPreferredSize(infoPanelSize);
        frameSubPanelEast.setLayout(new BorderLayout());
        frameSubPanelEast.add(infoPanel,BorderLayout.NORTH);
        frameSubPanelEast.add(memoPanel,BorderLayout.CENTER);

        Dimension frameSubPanelWestSize = frameSubPanelWest.getPreferredSize();
        frameSubPanelWestSize.width = 410;
        frameSubPanelWest.setPreferredSize(frameSubPanelWestSize);

        //뒤늦게 추가된 bottom Panel
        frameBottomPanel = new JPanel();
        frameBottomPanel.add(bottomInfo);

        //frame에 전부 배치
        mainFrame.setLayout(new BorderLayout());
        mainFrame.add(frameSubPanelWest, BorderLayout.WEST);
        mainFrame.add(frameSubPanelEast, BorderLayout.CENTER);
        mainFrame.add(frameBottomPanel, BorderLayout.SOUTH);
        mainFrame.setVisible(true);

        focusToday(); //현재 날짜에 focus를 줌 (mainFrame.setVisible(true) 이후에 배치해야함)

        //Thread 작동(시계, bottomMsg 일정시간후 삭제)
        ThreadConrol threadCnl = new ThreadConrol();
        threadCnl.start();
    }
    // today버튼을 누를시 오늘로 이동.
    private void focusToday(){
        if(today.get(Calendar.DAY_OF_WEEK) == 1)
            dateButs[today.get(Calendar.WEEK_OF_MONTH)][today.get(Calendar.DAY_OF_WEEK)-1].requestFocusInWindow();
        else
            dateButs[today.get(Calendar.WEEK_OF_MONTH)-1][today.get(Calendar.DAY_OF_WEEK)-1].requestFocusInWindow();
    }
    //메모를 읽어옴.
//    private void readMemo(){
//        try{
//            File f = new File("MemoData/"+calYear+((calMonth+1)<10?"0":"")+(calMonth+1)+(calDayOfMon<10?"0":"")+calDayOfMon+".txt");
//            if(f.exists()){
//                BufferedReader in = new BufferedReader(new FileReader("MemoData/"+calYear+((calMonth+1)<10?"0":"")+(calMonth+1)+(calDayOfMon<10?"0":"")+calDayOfMon+".txt"));
//                String memoAreaText= new String();
//                while(true){
//                    String tempStr = in.readLine();
//                    if(tempStr == null) break;
//                    memoAreaText = memoAreaText + tempStr + System.getProperty("line.separator");
//                }
//                memoArea.setText(memoAreaText);
//                in.close();
//            }
//            else memoArea.setText("");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    private void showCal(){
        for(int i=0;i<CAL_HEIGHT;i++){
            for(int j=0;j<CAL_WIDTH;j++){
                String fontColor="black";
                if(j==0) fontColor="red";
                else if(j==6) fontColor="blue";

                File f =new File("MemoData/"+calYear+((calMonth+1)<10?"0":"")+(calMonth+1)+(calDates[i][j]<10?"0":"")+calDates[i][j]+".txt");
                if(f.exists()){
                    dateButs[i][j].setText("<html><b><font color="+fontColor+">"+calDates[i][j]+"</font></b></html>");
                }
                else dateButs[i][j].setText("<html><font color="+fontColor+">"+calDates[i][j]+"</font></html>");

                JLabel todayMark = new JLabel("<html><font color=green>*</html>");
                dateButs[i][j].removeAll();
                if(calMonth == today.get(Calendar.MONTH) &&
                        calYear == today.get(Calendar.YEAR) &&
                        calDates[i][j] == today.get(Calendar.DAY_OF_MONTH)){
                    dateButs[i][j].add(todayMark);
                    dateButs[i][j].setToolTipText("Today");
                }

                if(calDates[i][j] == 0) dateButs[i][j].setVisible(false);
                else dateButs[i][j].setVisible(true);
            }
        }
    }
    private class ListenForCalOpButtons implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == todayBut){
                setToday();
//                lForDateButs.actionPerformed(e);
                focusToday();
            }
            else if(e.getSource() == lYearBut) moveMonth(-12);
            else if(e.getSource() == lMonBut) moveMonth(-1);
            else if(e.getSource() == nMonBut) moveMonth(1);
            else if(e.getSource() == nYearBut) moveMonth(12);

            curMMYYYYLab.setText("<html><table width=100><tr><th><font size=5>"+((calMonth+1)<10?"&nbsp;":"")+(calMonth+1)+" / "+calYear+"</th></tr></table></html>");
            showCal();
        }
    }
//    private class listenForDateButs implements ActionListener{
//        public void actionPerformed(ActionEvent e) {
//            int k=0,l=0;
//            for(int i=0 ; i<CAL_HEIGHT ; i++){
//                for(int j=0 ; j<CAL_WIDTH ; j++){
//                    if(e.getSource() == dateButs[i][j]){
//                        k=i;
//                        l=j;
//                    }
//                }
//            }
//
//            if(!(k ==0 && l == 0)) calDayOfMon = calDates[k][l]; //today버튼을 눌렀을때도 이 actionPerformed함수가 실행되기 때문에 넣은 부분
//
//            cal = new GregorianCalendar(calYear,calMonth,calDayOfMon);
//
//            String dDayString = new String();
//            int dDay=((int)((cal.getTimeInMillis() - today.getTimeInMillis())/1000/60/60/24));
//            if(dDay == 0 && (cal.get(Calendar.YEAR) == today.get(Calendar.YEAR))
//                    && (cal.get(Calendar.MONTH) == today.get(Calendar.MONTH))
//                    && (cal.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH))) dDayString = "Today";
//            else if(dDay >=0) dDayString = "D-"+(dDay+1);
//            else if(dDay < 0) dDayString = "D+"+(dDay)*(-1);
//
//            selectedDate.setText("<Html><font size=3>"+(calMonth+1)+"/"+calDayOfMon+"/"+calYear+"&nbsp;("+dDayString+")</html>");
//
//            readMemo();
//        }
//    }
    private class ThreadConrol extends Thread{
        public void run(){
            boolean msgCntFlag = false;
            int num = 0;
            String curStr = new String();
            while(true){
                try{
                    today = Calendar.getInstance();
                    String amPm = (today.get(Calendar.AM_PM)==0?"AM":"PM");
                    String hour;
                    if(today.get(Calendar.HOUR) == 0) hour = "12";
                    else if(today.get(Calendar.HOUR) == 12) hour = " 0";
                    else hour = (today.get(Calendar.HOUR)<10?" ":"")+today.get(Calendar.HOUR);
                    String min = (today.get(Calendar.MINUTE)<10?"0":"")+today.get(Calendar.MINUTE);
                    String sec = (today.get(Calendar.SECOND)<10?"0":"")+today.get(Calendar.SECOND);
                    infoClock.setText(amPm+" "+hour+":"+min+":"+sec);

                    sleep(1000);
                    String infoStr = bottomInfo.getText();

                    if(infoStr != " " && (msgCntFlag == false || curStr != infoStr)){
                        num = 5;
                        msgCntFlag = true;
                        curStr = infoStr;
                    }
                    else if(infoStr != " " && msgCntFlag == true){
                        if(num > 0) num--;
                        else{
                            msgCntFlag = false;
                            bottomInfo.setText(" ");
                        }
                    }
                }
                catch(InterruptedException e){
                    System.out.println("Thread:Error");
                }
            }
        }
    }

    public static String[][] parsing(){
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
//            ArrayList<String>[] musicalList = new ArrayList[1000]; //뮤지컬내용을 담을 제네릭배열
            String[][] musicalList = new String[nList.getLength()][5];
            //nList.getLength();
            for(int i=0; i<nList.getLength(); i++) {
                Node nNode = nList.item(i);
                eElement = (Element) nNode;
                String showID = getTagValue("mt20id", eElement);
//                System.out.println("뮤지컬코드 : " + getTagValue("mt20id", eElement)); //뮤지컬 코드
                String detailURL = "http://www.kopis.or.kr/openApi/restful/pblprfr/" + showID + "?service=" + KEY;

                DocumentBuilderFactory dbFactoty2 = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder2 = dbFactoty.newDocumentBuilder();
                Document doc2 = dBuilder.parse(detailURL);
                doc2.getDocumentElement().normalize();
                NodeList nList2 = doc.getElementsByTagName("db");
                Node nNode2 = nList.item(i);
                Element eElement2 = (Element) nNode;

                String musicalName = getTagValue("prfnm", eElement2);
                String actor = getTagValue("prfcast", eElement2);
                String site = getTagValue("fcltynm", eElement2);
                String runningTime = getTagValue("fcltynm", eElement2);
                String price = getTagValue("pcseguidance", eElement2);
                String poster = getTagValue("poster", eElement2);
                String startDate = getTagValue("prfpdfrom", eElement2);
                String endDate = getTagValue("prfpdto", eElement2);

//                System.out.println(getTagValue("prfnm", eElement2)); //뮤지컬이름
//                System.out.println("배우" + getTagValue("prfcast", eElement2)); //배우
//                System.out.println("장소: " + getTagValue("fcltynm", eElement2)); //장소
//                System.out.println("러닝타임: " + getTagValue("fcltynm", eElement2)); //러닝타임
//                System.out.println("가격: " + getTagValue("pcseguidance", eElement2)); //가격
//                System.out.println(getTagValue("poster", eElement2)); //포스터사진
//                System.out.println("시작: " +getTagValue("prfpdfrom", eElement2)); //시작날짜
//                System.out.println("끝: " +getTagValue("prfpdto", eElement2)); //끝날짜
//                System.out.println("========================================================");
                musicalList[i][0] = musicalName; //뮤지컬 이름
                musicalList[i][1] = poster; //뮤지컬 포스터
                musicalList[i][2] = startDate; //시작 날짜
                musicalList[i][3] = endDate; //끝 날짜
                musicalList[i][4] = site; //장소
//                musicalList[i].add(site);
//                musicalList[i].add(poster);
//                musicalList[i].add(startDate);
//                musicalList[i].add(endDate);
//                return musicalName+actor+site+runningTime+"\n"+price+poster+startDate+endDate;
            }
            return musicalList;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
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

}