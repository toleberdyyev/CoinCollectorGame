package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import java.io.IOException;

public class Data
{
    @FXML
    private HBox hBox;
    @FXML
    private Label label1;
    @FXML
    private Label label2;

    public Data()
    {
        hBox.getChildren().addAll(label1,label2);
    }
    public void setInfo(String string)
    {
        label1.setText(string);
        label2.setText(string);
    }

    public HBox getBox()
    {
        return hBox;
    }
}