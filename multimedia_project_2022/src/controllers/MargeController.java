package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.VideoWriter;
import org.opencv.videoio.Videoio;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MargeController extends Component implements Initializable{

    @FXML
    private AnchorPane scene_marge;

    int numVideosToMarge=0;

    @FXML
    private Label lbl_video;

    @FXML
    private TextField txb_path,txb_num_marge;

    @FXML
    private Button btn_browseVideo1;

    @FXML
    private Button btn_next;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void numToMarge(){
        if(txb_num_marge.getText()!="") {
            numVideosToMarge = Integer.parseInt(txb_num_marge.getText());

            lbl_video.setDisable(false);
            txb_path.setDisable(false);
            btn_browseVideo1.setDisable(false);

            if (numVideosToMarge >= 1) {
                if (numVideosToMarge == 1)
                    btn_next.setText("Done");
                btn_next.setDisable(false);
            }
        }
    }

    public void browseVideo1(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            txb_path.setText(file.getAbsolutePath());
        }
    }

    public void nextVid(){
        if(txb_path.getText()!="") {

            MargeVideos(txb_path.getText());

            numVideosToMarge -= 1;
            txb_num_marge.setText(String.valueOf(numVideosToMarge));
            if (numVideosToMarge > 1) {
                txb_path.setText("");
            } else if (numVideosToMarge == 1) {
                txb_path.setText("");
            } else if (numVideosToMarge == 0) {
                lbl_video.setDisable(true);
                txb_path.setDisable(true);
                btn_browseVideo1.setDisable(true);
                btn_next.setDisable(true);
            }

        }
    }

    public void MargeVideos(String secondPath){
        VideoCapture cap = new VideoCapture();
        VideoCapture cap1=new VideoCapture();

        String input = Controller.selectedFile.getAbsolutePath();
        String input1=txb_path.getText();
        cap.open(input);
        cap1.open(input1);

        int frames_per_second = (int) cap.get(Videoio.CAP_PROP_FPS);

        //int frame_number = (int) cap.get(Videoio.CAP_PROP_FRAME_COUNT);
        //float duration = (float) frame_number / (float) frames_per_second;

        Mat frame = new Mat();

        int fourcc = VideoWriter.fourcc('H', '2', '6', '4');
        String savePath="C:\\Users\\ASUS\\Desktop\\version\\"+Controller.step+".mp4";
        VideoWriter writer = new VideoWriter(savePath, fourcc, frames_per_second, new Size((int) cap.get(Videoio.CAP_PROP_FRAME_WIDTH), (int) cap.get(Videoio.CAP_PROP_FRAME_HEIGHT)), true);


        if (writer.isOpened()) {
            while (cap.read(frame)) //the last frame of the movie will be invalid. check for it !
            {
                writer.write(frame);

            }
            cap.release();

            while (cap1.read(frame)) //the last frame of the movie will be invalid. check for it !
            {
                writer.write(frame);

            }
            cap1.release();
            writer.release();

        }
        Controller.selectedFile=new File(savePath);
        Controller.step++;
    }

    public void backMain(javafx.event.ActionEvent event) throws IOException {
       /* Parent root =FXMLLoader.load(getClass().getResource("..\\fxml\\margeOptions.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene =new Scene(root);
        stage.setScene(scene);
        stage.show();*/

        AnchorPane scene1= FXMLLoader.load(getClass().getResource("..\\fxml\\main.fxml"));
        scene_marge.getChildren().removeAll();
        scene_marge.getChildren().setAll(scene1);

    }

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
}
