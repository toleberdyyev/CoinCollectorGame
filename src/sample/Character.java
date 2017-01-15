package sample;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;
import sun.lwawt.macosx.CImage;

import static sample.Main.*;

public class Character extends Pane{


    Label scoreLabel ;
    Label nameLabel;
    Image image = new Image(getClass().getResourceAsStream("1.png"));
    ImageView imageView = new ImageView(image);
    String name = "";
    int count = 3;
    int columns = 3;
    int offsetX = 0;
    int offsetY = 0;
    int width = 32;
    int height = 32;
    int score = 0;
    int danger = 0;
    double speed = 1;
    public void setName(String l){
        this.name=l;
    }
    ImageView removeCoin = null;
    ImageView removeMush = null;
    ImageView removeHeart = null;
    SpriteAnimation animation;


    public Character(){
//        this.imageView = imageView;
        this.nameLabel = new Label(name);
        this.scoreLabel = new Label("0");
        scoreLabel.getStyleClass().add("outline");
//        scoreLabel.setStyle("");
//        scoreLabel.setFont(new Font("Arial",20));
        scoreLabel.setMinHeight(50);
        scoreLabel.setMinWidth(32);
        scoreLabel.setAlignment(Pos.BOTTOM_CENTER);
        scoreLabel.setPadding(new Insets(0,0,0,0));
        this.imageView.setViewport(new Rectangle2D(offsetX,offsetY,width,height));
        animation = new SpriteAnimation(imageView,Duration.millis(300),count,columns,offsetX,offsetY,width,height);
        getChildren().addAll(imageView);

    }
    public void setDefault(){
        int xxx = (int) Math.floor(Math.random() * 370);
        int yyy = (int) Math.floor(Math.random() * 370);
        this.count = 3;
        this.columns = 3;
        this.offsetX = 0;
        this.offsetY = 0;
        this.width = 32;
        this.height = 32;
        this.score = 0;
        this.danger= 0;
        this.speed =1;
        this.scoreLabel.setText("0");
        this.setTranslateY(xxx);
        this.setTranslateX(yyy);
        this.imageView.setViewport(new Rectangle2D(offsetX,offsetY,width,height));
    }
    public void moveX(int x){
        boolean right = x>0?true:false;
        for(int i = 0; i < Math.abs(x); i++) {
            if(right){
                if((this.getTranslateX()+10)>=370){
                    this.setTranslateX(this.getTranslateX());
                }
                else{
                    this.setTranslateX(this.getTranslateX() + speed);
                }
//                System.out.println(this.getTranslateY()+" that is TRUE");
            }
            else{
                if((this.getTranslateX()-10)<=0){
                    this.setTranslateX(this.getTranslateX());
                }
                else{
                    this.setTranslateX(this.getTranslateX() - speed);
                }
//                System.out.println(this.getTranslateX());
            }
            isBonuseEat();
            isDisBonusEat();
            isHealth();
//            Main.indicator.setTranslateX(this.getTranslateX());
        }
    }
    public void moveY(int y) {
        boolean down = y > 0 ? true : false;
        for (int i = 0; i < Math.abs(y); i++) {
            if(down){
                if((this.getTranslateY()+10)>=370){
                    this.setTranslateY(this.getTranslateY());
                }
                else{
                    this.setTranslateY(this.getTranslateY() + speed);
                }
//                System.out.println(this.getTranslateY()+" that is TRUE");
            }
            else{
                if((this.getTranslateY()-10)<=0){
                    this.setTranslateY(this.getTranslateY());
                }
                else{
                    this.setTranslateY(this.getTranslateY() - speed);
                }
//                System.out.println(this.getTranslateY());
            }
            isBonuseEat();
            isDisBonusEat();
            isHealth();
//            Main.indicator.relocate();

        }
    }
    public void isBonuseEat(){
        bonuses.forEach((coin) -> {
            if (isTouch(this,coin)) {
            removeCoin = coin;
            score++;
            Main.deadtime  -= Main.deadtime * 10 ;
            speed += (double) score/5000;
            Main.indicator.setText(Main.login+":"+score);
            System.out.println(score);
            }
        });
        bonuses.remove(removeCoin);
        root.getChildren().remove(removeCoin);
}
    public void isDisBonusEat(){
        disbonuses.forEach((mush)->{
            if(isTouch(this,mush)){
                removeMush = mush;
                danger++;
                System.out.println(danger+" : this is DANGER");
                if(danger==1){
                    Main.indicator.setTextFill(Color.web("#FC3B17"));
                    Main.deadtime  += Main.deadtime  ;
                    for(ImageView i : disbonuses){
                        Main.root.getChildren().remove(i);
                    }
                    disbonuses.clear();

                }
                if(danger==2){
                    Main.indicator.setTextFill(Color.web("#D02A11"));
                    Main.deadtime  += Main.deadtime * 2 ;
                    for(ImageView i : disbonuses){
                        Main.root.getChildren().remove(i);
                    }disbonuses.clear();
                }
                if(danger==3){
                    Main.indicator.setTextFill(Color.web("#5D0E09"));
                    Main.deadtime  += Main.deadtime * 4 ;
                    for(ImageView i : disbonuses){
                        Main.root.getChildren().remove(i);
                    }disbonuses.clear();
                }
                if(danger==4){
                    Main.deadtime +=10;
                }
                speed=0.5;
                Main.indicator.setText(Main.login+":"+score);
//                System.out.println(score);
                root.getChildren().remove(removeMush);
            }
        });
        disbonuses.remove(removeMush);

    }
    public void isHealth(){
        hearts.forEach((heart)->{
           if(isTouch(this,heart)){
                removeHeart = heart;
                if(danger>=1){
                    Main.indicator.setTextFill(Color.web("white"));
                    danger=0;
                }
                Main.indicator.setText(Main.login+":"+score);
//                System.out.println(score);
            }
        });
        hearts.remove(removeHeart);
        root.getChildren().remove(removeHeart);
    }

    public boolean isTouch(Character player , ImageView obj){
        double w = obj.getFitWidth();
        double h = obj.getFitHeight();
        if (this.getBoundsInParent().intersects(obj.getBoundsInParent())) {
            if (Math.abs(obj.getX() - player.getTranslateX()) <= ((w/2)+(w/4)) && (Math.abs(obj.getX() - player.getTranslateX()) >= (w - w))) {
                if (Math.abs(obj.getY() - player.getTranslateY()) <= ((h/2)+(h/4)) && (Math.abs(obj.getY() - player.getTranslateY()) >= (h - h))) {
                    return true;
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }else{
            return false;
        }

    }
}
