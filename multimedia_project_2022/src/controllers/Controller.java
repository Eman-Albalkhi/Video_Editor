package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.VideoWriter;
import org.opencv.videoio.Videoio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller extends Component implements Initializable {

    @FXML
    private AnchorPane scene1;

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private MediaView mediaView;

    @FXML
    private Button btn_play,btn_pause,btn_reset;

    private File file;
    private Media media;
    private MediaPlayer mediaPlayer;

    public static File selectedFile;
    public static int step=1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void Undo(){
        if(selectedFile!=null)
        {
            if (step>1)
                step--;
            String savePath="C:\\Users\\ASUS\\Desktop\\version\\"+Controller.step+".mp4";
            Controller.selectedFile=new File(savePath);
        }
    }

    public void playMedia(){
        if(selectedFile!=null)
        {
            media =new Media(selectedFile.toURI().toString());
            mediaPlayer=new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);
            mediaPlayer.play();
        }
    }

    public void pauseMedia(){
        if(mediaPlayer!=null)
        {
            mediaPlayer.pause();
        }
    }

    public void resetMedia(){
        if(mediaPlayer!=null)
        {
            mediaPlayer.seek(Duration.seconds(0.0));
        }
    }


    public void openVideo(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            /*File*/ selectedFile = fileChooser.getSelectedFile();
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());
            copy();
        }
    }

    public void copy(){
        VideoCapture cap = new VideoCapture();
        String input = Controller.selectedFile.getAbsolutePath();
        cap.open(input);
        int frames_per_second = (int) cap.get(Videoio.CAP_PROP_FPS);

        Mat frame = new Mat();
        int fourcc = VideoWriter.fourcc('H', '2', '6', '4');
        String savePath="C:\\Users\\ASUS\\Desktop\\version\\"+Controller.step+".mp4";
        VideoWriter writer = new VideoWriter(savePath, fourcc, frames_per_second, new Size((int) cap.get(Videoio.CAP_PROP_FRAME_WIDTH), (int) cap.get(Videoio.CAP_PROP_FRAME_HEIGHT)), true);

        if (writer.isOpened()) {
            while(true) {
                if (!cap.read(frame)) {
                    cap.release();
                    writer.release();
                    break;
                }

                writer.write(frame);

            }
        }
        step++;

    }

    @FXML
    private void saveVideo(javafx.event.ActionEvent event) throws IOException{
        /*root =FXMLLoader.load(getClass().getResource("margeOptions.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene =new Scene(root);
        stage.setScene(scene);
        stage.show();*/

        if(selectedFile!=null) {
            AnchorPane scene_save = FXMLLoader.load(getClass().getResource("..\\fxml\\saveOptions.fxml"));
            scene1.getChildren().removeAll();
            scene1.getChildren().setAll(scene_save);
        }
    }

    public void open_Delete(javafx.event.ActionEvent event) throws IOException {
       /* Parent root =FXMLLoader.load(getClass().getResource("..\\fxml\\margeOptions.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene =new Scene(root);
        stage.setScene(scene);
        stage.show();*/
        if(selectedFile!=null) {
            AnchorPane scene_marge= FXMLLoader.load(getClass().getResource("..\\fxml\\deleteOptions.fxml"));
            scene1.getChildren().removeAll();
            scene1.getChildren().setAll(scene_marge);
        }
    }


    public void open_MargeVideos(javafx.event.ActionEvent event) throws IOException {
       /* Parent root =FXMLLoader.load(getClass().getResource("..\\fxml\\margeOptions.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene =new Scene(root);
        stage.setScene(scene);
        stage.show();*/
        if(selectedFile!=null) {
            AnchorPane scene_delete= FXMLLoader.load(getClass().getResource("..\\fxml\\margeOptions.fxml"));
            scene1.getChildren().removeAll();
            scene1.getChildren().setAll(scene_delete);
        }
    }

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
}
