package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.VideoWriter;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SaveController extends Component implements Initializable{

    @FXML
    private AnchorPane scene_save;

    @FXML
    private TextField txb_width,txb_height,txb_fps;

    public void backMain(javafx.event.ActionEvent event) throws IOException {
       /* Parent root =FXMLLoader.load(getClass().getResource("..\\fxml\\margeOptions.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene =new Scene(root);
        stage.setScene(scene);
        stage.show();*/

        AnchorPane scene1= FXMLLoader.load(getClass().getResource("..\\fxml\\main.fxml"));
        scene_save.getChildren().removeAll();
        scene_save.getChildren().setAll(scene1);

    }

    public void saveVideo(String newPath){
        VideoCapture cap = new VideoCapture();
        String input = Controller.selectedFile.getAbsolutePath();
        cap.open(input);
        int frames_per_second = Integer.parseInt(txb_fps.getText());
        double newWidth= Double.parseDouble(txb_width.getText());
        double newHeight= Double.parseDouble(txb_height.getText());
        Mat frame = new Mat();
        int fourcc = VideoWriter.fourcc('H', '2', '6', '4');
        VideoWriter writer = new VideoWriter(newPath +".mp4", fourcc, frames_per_second, new Size(newWidth, newHeight), true);
        Mat frame1 = new Mat();
        if (writer.isOpened()) {
            while(true) {
                if (!cap.read(frame)) {
                    cap.release();
                    writer.release();
                    break;
                }

                writer.write(frame);

                Imgproc.resize(frame, frame1, new Size(newWidth, newHeight));
                writer.write(frame1);
            }
        }

    }

    public void saveBrowse(){
        // parent component of the dialog
        JFrame parentFrame = new JFrame();

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify a file to save");

        int userSelection = fileChooser.showSaveDialog(parentFrame);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            saveVideo(fileToSave.getAbsolutePath());

            System.out.println("Save as file: " + fileToSave.getAbsolutePath());
        }
    }


    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
