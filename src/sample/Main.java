package sample;
import com.restfb.types.User;
import com.sun.corba.se.impl.presentation.rmi.ExceptionHandlerImpl;
import com.sun.corba.se.pept.transport.Connection;
import com.sun.corba.se.spi.orbutil.fsm.Guard;
import com.sun.javafx.font.freetype.HBGlyphLayout;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.event.*;
import javafx.event.EventHandler;
import javafx.event.*;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import sun.jvm.hotspot.debugger.win32.coff.COFFException;
import sun.jvm.hotspot.debugger.win32.coff.ExportDirectoryTable;
import sun.lwawt.macosx.CImage;
import sun.plugin.dom.css.Rect;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.*;
import java.net.URL;
import java.util.*;
import java.sql.*;
import com.restfb.*;
public class Main extends Application {

    static String login = "";
    static int record = 0;

    Button PlayBTN = new Button("Play");
    Button ExitBTN = new Button("Exit");
    Button test = new Button("test");

    private HashMap<KeyCode, Boolean> keys = new HashMap<>();
    public static ArrayList<ImageView> bonuses = new ArrayList<>();
    public static ArrayList<ImageView> disbonuses = new ArrayList<>();
    public static ArrayList<ImageView> hearts = new ArrayList<>();
    public    Character player = new Character();

    static Pane root = new Pane();
    static VBox arenalayer = new VBox();
    static Label indicator;
    static Label data;

    ProgressBar timeline = new ProgressBar(0.0);
    static double mainCoef = 0.00075;
    static double deadtime = (double) mainCoef;

    static int HARD_level = 80;
    static int keff = 250;
    static int HARD_START = 5;
    VBox finallyy = new VBox(5);

    // for SERVER SOCKET



    public void disBonus(){
        if(player.score>HARD_START){
            int random = (int) Math.floor(Math.random() * (keff));
            int randomDelete = (int) Math.floor(Math.random()*3);
            int x = (int) Math.floor(Math.random() * 370);
            int y = (int) Math.floor(Math.random() * 370);
            if(random==5){
                Image mushroom = new Image(getClass().getResourceAsStream("m.gif"));
                ImageView mushIMAGEVIEW = new ImageView(mushroom);
                mushIMAGEVIEW.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 3, 0, 0, 2);");
                mushIMAGEVIEW.setFitWidth(25);
                mushIMAGEVIEW.setFitHeight(25);
                mushIMAGEVIEW.setX(x);
                mushIMAGEVIEW.setY(y);
                if(disbonuses.size()>=3){
                    ImageView oldMush = disbonuses.get(randomDelete);
                    disbonuses.remove(randomDelete);
                    root.getChildren().removeAll(oldMush);
                }else{
                    disbonuses.add(mushIMAGEVIEW);
                    root.getChildren().addAll(mushIMAGEVIEW);
                }
            }
        }
    }

    public void HealthUp(){
        if(player.score>=30){
            int random = (int) Math.floor(Math.random() * (keff));
            int x = (int) Math.floor(Math.random() * 370);
            int y = (int) Math.floor(Math.random() * 370);
            if(random==5){
                Image health = new Image(getClass().getResourceAsStream("beat.gif"));
                ImageView healthIMAGEVIEW = new ImageView(health);
                healthIMAGEVIEW.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 3, 0, 0, 2);");
                healthIMAGEVIEW.setFitWidth(30);
                healthIMAGEVIEW.setFitHeight(30);
                healthIMAGEVIEW.setX(x);
                healthIMAGEVIEW.setY(y);
                if(hearts.size()<=0 && disbonuses.size()>0){
                    hearts.add(healthIMAGEVIEW);
                    root.getChildren().addAll(healthIMAGEVIEW);
                }
          }
        }}

    public void bonus() {
        int random = (int) Math.floor(Math.random() * HARD_level);
        int x = (int) Math.floor(Math.random() * 370);
        int y = (int) Math.floor(Math.random() * 370);
        if (random == 5 || bonuses.size()==1) {
            Image coinIMAGE = new Image(getClass().getResourceAsStream("coin.gif"));
            ImageView coinIMAGEVIEW = new ImageView(coinIMAGE);
            coinIMAGEVIEW.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 3, 0, 0, 2);");
            coinIMAGEVIEW.setFitWidth(20);
            coinIMAGEVIEW.setFitHeight(20);
            coinIMAGEVIEW.setX(x);
            coinIMAGEVIEW.setY(y);
            bonuses.add(coinIMAGEVIEW);
            root.getChildren().addAll(coinIMAGEVIEW);
        }
    }

    public void update() {
        if (isPressed(KeyCode.UP)) {
            player.animation.play();
            player.animation.setOffsetY(96);
            player.moveY(-2);
        } else if (isPressed(KeyCode.DOWN)) {
            player.animation.play();
            player.animation.setOffsetY(0);
            player.moveY(2);
        } else if (isPressed(KeyCode.RIGHT)) {
            player.animation.play();
            player.animation.setOffsetY(64);
            player.moveX(2);
        } else if (isPressed(KeyCode.LEFT)) {
            player.animation.play();
            player.animation.setOffsetY(32);
            player.moveX(-2);
        } else {
            player.animation.stop();
        }
    }

    public boolean isPressed(KeyCode key) {
        return keys.getOrDefault(key, false);
    }

    AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            indicator.relocate(player.getTranslateX()-10,player.getTranslateY()-20);
            update();
            bonus();
            disBonus();
            HealthUp();
//            System.out.println(deadtime+"\n"+mainCoef);
            timeOUT(deadtime);
            deadtime = timeUP(deadtime);
        }
    };


    static String developerName = "";
    @Override
    public void start(Stage primaryStage) throws Exception {



        /**
         *  special for FACEBOOK
         *
         * */

//        String accessToken = "EAACEdEose0cBALk1WRw1vEWtX7pM2fOFYCkW9TtClaQiGURm1af3hRtdofBI31EdLQspOVuDyZBzCPIfKZCtbI84Iws5eRSr1a5GH4j0h3lwciwXahbKITtdd9pMnrQwrxXAkPCyz36kfnuinayS1mOXnLTWUqpyZAfvuZA1lQZDZD";
//        FacebookClient fbClient = new DefaultFacebookClient(accessToken);
//        User me = fbClient.fetchObject("me",User.class);
//        developerName = me.getName();

        try {
            java.sql.Connection c =null;
            Statement stmt = null;
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:test.db");
            System.out.println("Opened database successfully");
            stmt = c.createStatement();
            DatabaseMetaData dbm = c.getMetaData();
            ResultSet tables = dbm.getTables(null, null, "USERS", null);
            if(tables.next()){
                System.out.println("Opened table ~ USERS ~ successfully");
            }else{
                String sql = "CREATE TABLE  USERS " +
                        "(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " NAME           TEXT    NOT NULL, " +
                        " PASSWORD       INT     NOT NULL,"+"" +
                        " RECORD         TEXT    NOT NULL)";
                stmt.executeUpdate(sql);
                System.out.println("Table created successfully");
            }
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        root.setDisable(false);
        indicator = new Label("as");
        indicator.getStyleClass().addAll("outline");
        indicator.setFont(new Font("Cambria", 7));
        player.setName(login);
        timeline.setId("progressBar");
        timeline.setMaxWidth(Double.MAX_VALUE);
        PlayBTN.setId("record-sales");
        ExitBTN.setId("record-sales");
        root.setPrefSize(400, 500);
        root.setId("pane");
        ExitBTN.setAlignment(Pos.CENTER);
        PlayBTN.setAlignment(Pos.CENTER);
        arenalayer.getChildren().addAll(timeline, root);
        arenalayer.setFillWidth(true);
        VBox mainRoot = FXMLLoader.load(new URL(Main.class.getResource("sample.fxml").toExternalForm()));
        Scene scene = new Scene(mainRoot);
        replay();
        /**
         *    SECTION /  PLAY  START ~ CONTINUE
         * */

        scene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
        scene.setOnKeyReleased(event -> {Boolean put = keys.put(event.getCode(), false);});
        primaryStage.setTitle("COIN COLLECTOR");
        scene.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

    }public void replay(){
        finallyy.getChildren().clear();
        if(!login.equals("")){
            int oldRecord = Integer.parseInt(Controller.GetRecord(login));
            int nowRecord = player.score;
            if(nowRecord>oldRecord){
                Controller.SetRecord(nowRecord,login);
                data = new Label("Hey, "+Main.login+"\nThis is your last score:"+Controller.GetRecord(Main.login)+"\nThis is your score :");
                Main.indicator.setText(login+":"+0);
            }
        }
        finallyy.setMouseTransparent(true);
        record = player.score;
        Label data1 = new Label(String.valueOf(player.score));
        data = new Label("Hey, "+Main.login+"\nThis is your last score:"+Controller.GetRecord(Main.login)+"\nThis is your score :");
        data1.setStyle("-fx-font-size:25px;-fx-font-weight: bold;");
        data.setTextFill(Color.web("white"));
        data1.setTextFill(Color.web("#34A853"));
        finallyy.getChildren().addAll(data,data1,PlayBTN,ExitBTN);
        finallyy.setId("finally");
        root.getChildren().clear();
        root.getChildren().addAll(finallyy);
        finallyy.relocate(85,100);
        finallyy.setAlignment(Pos.CENTER);
        finallyy.setPrefSize(250,200);
        finallyy.setPadding(new Insets(10,10,10,10));
        player.setDefault();
        PlayBTN.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("play");
                root.getChildren().clear();
                player.setDefault();
                timeline.setProgress(0.0);
                timer.start();
                player.setDefault();
                root.getChildren().addAll(player,indicator);indicator.setText(login+":0");
            }
        });
        ExitBTN.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("exit");
                System.out.println("exit");
                timer.stop();
                player.setDefault();
                Main.arenalayer.setVisible(false);
                Controller.MENU.setVisible(true);
            }
        });

    }
    public void clearArena(Pane root, ArrayList<ImageView> rect) {
        root.getChildren().clear();
        root.getChildren().addAll(player);
    }
    public void timeOUT(double deadtime) {
        if (timeline.getProgress() >= 1.0 ) {
            timer.stop();
            replay();
//            System.out.println(timeline.getProgress());
        } else {
            double res = timeline.getProgress() + deadtime;
            if(res<0){
                timeline.setProgress(0.0);
            }
            timeline.setProgress(timeline.getProgress() + deadtime);
        }

    }
    public static double timeUP(double deadtime){
        double res = deadtime;
        if(res>=mainCoef){
            res=mainCoef;
        }else{
            res+=0.0005;
        }
        return res;
    }

    public static void main(String[] args) {
        launch(args);
    }

}



