package sample;

import com.sun.org.apache.bcel.internal.generic.BREAKPOINT;
import com.sun.org.apache.regexp.internal.RE;
import com.sun.org.apache.regexp.internal.StreamCharacterIterator;

import com.sun.tools.classfile.Dependencies;
import com.sun.tools.internal.xjc.*;
import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion;
import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Callback;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.*;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.rmi.MarshalException;
import java.util.*;
import java.sql.*;

public class Controller implements Initializable {

    Connection c =null;
    Statement stmt = null;
    Scanner in;
    PrintWriter out;

    private Socket s;

    @FXML
    private VBox mainRooter;
    @FXML
    private StackPane layers;
    @FXML
    private Menu LoginPage ;


    VBox LOGIN_pane = new VBox();
    VBox REG_pane = new VBox();
    static VBox MENU = new VBox(5);
    VBox RECORD_pane = new VBox(2);

    Label Brand = new Label("COIN COLLECTOR");
    Text text = new Text("\nCoin collector - it is a game\ncoded by ALisher from SDU. \n\nThis is game where you need to \ncollect small coins by the time,\nafter collecting your speed \ngrowth.Each earned coin less your \ntime limit.please Enjoy ! :)\n");
    TextField LOGIN_login = new TextField();
    PasswordField LOGIN_pass = new PasswordField();
    Button LOGIN_enter = new Button("Login");

    TextField REG_login = new TextField();
    PasswordField REG_pass1 = new PasswordField();
    PasswordField REG_pass2 = new PasswordField();
    Button REG_enter = new Button("Sign Up");

    Label MENU_label = new Label("");
    Button MENU_singl = new Button("Single Player");
    Button MENU_multi = new Button("Multi PLayer");
    Button MENU_records = new Button("Records List");
    Button MENU_exit = new Button("Quit");
    Label MENU_title = new Label("this project coded by - "+Main.developerName+"\nSuleyman Demirel University\n2016");

    JSONObject data = new JSONObject();
    JSONObject UsersData = new JSONObject();
    static JSONObject RecoredsData = new JSONObject();


    ListView<String> listView = new ListView<>();
    HashSet<String> list;
    ObservableList<UsersRecords> forsort = FXCollections.observableArrayList();
    ObservableList<String> observableList = FXCollections.observableArrayList();

    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        try{
            data.put("users",UsersData);
            data.put("records",RecoredsData);

            // for login PROMPT
            LOGIN_login.setPromptText("login");
            LOGIN_pass.setPromptText("password");

            // for reg PROMPT
            REG_login.setPromptText("login");
            REG_pass1.setPromptText("password");
            REG_pass2.setPromptText("password repeat");


            LOGIN_enter.setId("record-sales");
            REG_enter.setId("record-sales");
            MENU_singl.setId("record-sales");
            MENU_multi.setId("record-sales");
            MENU_multi.setDisable(true);
            MENU_records.setId("record-sales");
            MENU_exit.setId("record-sales");
            MENU_title.setAlignment(Pos.BOTTOM_CENTER);
            MENU.setAlignment(Pos.CENTER);

            REG_pane.getChildren().addAll(Brand,REG_login,REG_pass1,REG_pass2,REG_enter);
            LOGIN_pane.getChildren().addAll(Brand,text,LOGIN_login,LOGIN_pass,LOGIN_enter);
            MENU.getChildren().addAll(MENU_label,MENU_singl,MENU_records,MENU_exit,MENU_title);

            LOGIN_pane.setId("PANE");
            REG_pane.setId("PANE");

            LOGIN_pane.setMargin(LOGIN_pass, new Insets(5,0,5,0));
            REG_pane.setMargin(REG_pass1,new Insets(5,0,0,0));
            REG_pane.setMargin(REG_pass2,new Insets(5,0,5,0));


            LOGIN_pane.setPadding(new Insets(20,20,20,20));
            REG_pane.setPadding(new Insets(20,20,20,20));
            layers.setPadding(new Insets(10,10,10,10));

            layers.setStyle("-fx-background-color:#071E23;");
            layers.getChildren().addAll(Main.arenalayer);
            layers.getChildren().addAll(RECORD_pane);
            layers.getChildren().addAll(MENU);
            layers.getChildren().addAll(REG_pane);
            layers.getChildren().addAll(LOGIN_pane);
            layers.setId("layers");

            REG_enter.setOnAction((e)->{
                if(USERSAVE()){
                    REG_pane.setVisible(false);
                    LOGIN_pane.setVisible(true);
                    Main.arenalayer.setVisible(false);
                }
            });
            LOGIN_enter.setOnAction((e)->{
                if(USERCHECK(LOGIN_login.getText(),LOGIN_pass.getText())){
                    LOGIN_pane.setVisible(false);
                    REG_pane.setVisible(false);
                    Main.arenalayer.setVisible(false);
                    MENU.setVisible(true);
                    Main.login = LOGIN_login.getText();
                    MENU_label.setText("Hello , "+Main.login);
                    MENU_label.getStyleClass().addAll("outline");
                    System.out.println("Welcome , "+Main.login);
                    Main.data.setText("Hey, "+Main.login+"\nThis is your last score:"+GetRecord(Main.login)+"\nThis is your score :");
                    Main.indicator.setText(LOGIN_login.getText()+":"+0);
//                    initializeNetwork();

                }
            });
            MENU_singl.setOnAction((e)->{
                LOGIN_pane.setVisible(false);
                REG_pane.setVisible(false);
                MENU.setVisible(false);
                Main.arenalayer.setVisible(true);
            });
            MENU_records.setOnAction((event)->{
                RECORD_pane.getChildren().clear();
                listView.getItems().clear();
                observableList.clear();
                forsort.clear();
                MENU.setVisible(false);
                Button back = new Button("Back");
                back.setId("record-sales");
                Main.arenalayer.setVisible(false);
                RECORD_pane.setVisible(true);
                RECORD_pane.setAlignment(Pos.CENTER);
                back.setOnAction((eventiwe)->{
                    RECORD_pane.setVisible(false);
                    MENU.setVisible(true);
                });
                Connection c = null;
                Statement stmt = null;
                try {
                    Class.forName("org.sqlite.JDBC");
                    c = DriverManager.getConnection("jdbc:sqlite:test.db");
                    c.setAutoCommit(false);
                    System.out.println("Opened database successfully");
                    stmt = c.createStatement();
                    ResultSet rs = stmt.executeQuery( "SELECT * FROM USERS;" );
                    System.out.println();
                    int[][] data = new int[10000][10000];
                    while ( rs.next() ) {
                        String  name = rs.getString("name");
                        int record = rs.getInt("record");
                        UsersRecords u = new UsersRecords(name,record);
                        forsort.add(u);
                    }
                    rs.close();
                    stmt.close();
                    c.close();
                } catch ( Exception e) {
                    System.err.println( e.getClass().getName() + ": lol " + e.getMessage() );
                    System.exit(0);
                }
                Collections.sort(forsort, new Comparator<UsersRecords>() {
                    @Override
                    public int compare(UsersRecords o1, UsersRecords o2) {
                        return o2.record - o1.record;
                    }
                });
                for(UsersRecords i:forsort){
                    observableList.addAll(i.name+":"+String.valueOf(i.record));
                }
                listView.setItems(observableList);
                listView.setCellFactory(new Callback<ListView<String>, javafx.scene.control.ListCell<String>>() {
                    @Override
                    public ListCell<String> call(ListView<String> listView)
                    {
                        return new ListViewCell();
                    }
                });
                RECORD_pane.getChildren().addAll(listView,back);
            });
            MENU_exit.setOnAction((e)->{
                System.exit(0);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void login(){
        LOGIN_pane.setVisible(true);
        RECORD_pane.setVisible(false);
        REG_pane.setVisible(false);
        Main.arenalayer.setVisible(false);
    }
    public void reg(){
        LOGIN_pane.setVisible(false);
        REG_pane.setVisible(true);
        Main.arenalayer.setVisible(false);
    }
    public void rlist(){
        System.out.println("NOT DONE YET !");
    }

    public boolean USERSAVE(){
        boolean status = false;
        String data[] = new String[]{REG_login.getText(),REG_pass1.getText(),REG_pass2.getText()};
        if(data[1].equals(data[2]) && data[0]!="" && (data[1].length() == data[2].length())){
            try {
                String login =data[0];
                String password = data[1];
                int record =0;
                int pass = 0;
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:test.db");
                stmt = c.createStatement();
                c.setAutoCommit(false);
                ResultSet rs = stmt.executeQuery( "SELECT ID FROM USERS WHERE NAME='"+login+"';" );
                if(rs.next()){
                    System.out.println("REGISTRATION : this name is already taken");
                    status =false;
                }else{
                    System.out.println(login+" : this name is FREE to use");
                    String sql = "INSERT INTO USERS (NAME,PASSWORD,RECORD) VALUES('"+login+"','"+password+"',"+record+");";
                    stmt.executeUpdate(sql);
                    UsersData.put("login",data[0]);
                    UsersData.put("pass",data[1]);
                    status=true;
                }
                rs.close();
                stmt.close();
                c.commit();
                c.close();
            }catch (Exception e){
                System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                System.exit(0);
            }
            System.out.println(UsersData.toString());
            DATA();
            return status;
        }else {
            return false;
        }

    }
    public boolean USERCHECK(String login,String password){
        boolean status =false;
        Connection c =null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:test.db");
            stmt = c.createStatement();
            c.setAutoCommit(false);
            ResultSet rs = stmt.executeQuery( "SELECT * FROM USERS WHERE NAME='"+login+"';" );
            if(rs.next()){
                String  name = rs.getString("name");
                String  pass = rs.getString("password");
                if(name.equals(login)  && pass.equals(password)){
                    System.out.println("LOGIN : successfully login");
                    status=true;
                }else{
                    System.out.println("LOGIN : something wrong ");
                }
            }
            rs.close();
            stmt.close();
            c.commit();
            c.close();
        }catch (Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        return status;

    }
    public void DATA(){
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:test.db");
            c.setAutoCommit(false);
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM USERS;" );
            while (rs.next()){
                int id = rs.getInt("id");
                String  name = rs.getString("name");
                String  password = rs.getString("password");
                float record = rs.getFloat("record");
                System.out.println( "ID : " + id );
                System.out.println( "NAME : " + name );
                System.out.println( "PASSWORD : " + password );
                System.out.println( "RECORD : " + record );
                System.out.println();
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
    }

    static int GetID(String login){
        int result = 0;
        Connection c =null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:test.db");
            stmt = c.createStatement();
            c.setAutoCommit(false);
            ResultSet rs = stmt.executeQuery( "SELECT * FROM USERS WHERE NAME='"+login+"';" );
            if(rs.next()){
                result = rs.getInt("id");
            }
            rs.close();
            stmt.close();
            c.commit();
            c.close();
        }catch (Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        return result;
    }

    static String GetRecord(String login){
        String result = "";
        Connection c =null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:test.db");
            stmt = c.createStatement();
            c.setAutoCommit(false);
            ResultSet rs = stmt.executeQuery( "SELECT * FROM USERS WHERE NAME='"+login+"';" );
            if(rs.next()){
                result = rs.getString("record");
            }
            rs.close();
            stmt.close();
            c.commit();
            c.close();
        }catch (Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        return result;
    }

    static void SetRecord(int record,String login){
        int id = Controller.GetID(login);
        Connection c = null;
        Statement stmt = null;
        try {

            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:test.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");
            stmt = c.createStatement();
            String sql = "UPDATE USERS set RECORD ="+record+" where ID="+id+";";
            stmt.executeUpdate(sql);
            System.out.println("check");
            c.commit();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Operation done successfully");
    }

    class ListViewCell extends ListCell<String>
    {
        @Override
        public void updateItem(String string, boolean empty)
        {
            super.updateItem(string,empty);
            if(string != null)
            {
                HBox pane = new HBox();
                pane.setAlignment(Pos.CENTER);
                String data[] = string.split(":");
                Label name = new Label(data[0]+" : ");
                name.setAlignment(Pos.CENTER_LEFT);
                Label record = new Label(data[1]+" ");
                record.setAlignment(Pos.CENTER_RIGHT);
                name.setId("record-list");
                record.setId("record-list");
                record.setTextFill(Color.web("#34A853"));
                Image coinIMAGE = new Image(getClass().getResourceAsStream("coin.gif"));
                ImageView coinIMAGEVIEW = new ImageView(coinIMAGE);
                coinIMAGEVIEW.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 3, 0, 0, 2);");
                coinIMAGEVIEW.setFitWidth(22);
                coinIMAGEVIEW.setFitHeight(22);
                pane.getChildren().addAll(name,record,coinIMAGEVIEW);
                setGraphic(pane);
            }
        }
    }
    class UsersRecords{
        String name;
        int record;
        UsersRecords(String name , int record){
            this.name=name;
            this.record=record;
        }
    }
    public void BubbleSort(int[] list) {
        boolean needNextPass = true;
        for (int k = 1; k < list.length && needNextPass; k++) {
            needNextPass = false;
            for (int i = 0; i < list.length - k; i++) {
                if (list[i] > list[i + 1]) {
                    // Swap list[i] with list[i + 1]
                    int temp = list[i];
                    list[i] = list[i + 1];
                    list[i + 1] = temp;

                    needNextPass = true; // Next pass still needed
                }
            }
        }
    }
    public void initializeNetwork(){
        try {
            s = new Socket("localhost",3201);
            out = new PrintWriter(s.getOutputStream(),true);
            in = new Scanner(s.getInputStream());
            Thread th= new Thread(new ListeningThread());
            th.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    class ListeningThread implements Runnable{
        @Override
        public void run(){
            out.println("id:"+GetID(Main.login)+",Username:"+Main.login+",score : "+GetRecord(Main.login));
        }
    }
}


