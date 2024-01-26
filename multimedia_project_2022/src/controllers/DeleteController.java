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
import org.opencv.videoio.Videoio;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class DeleteController extends Component implements Initializable{

    @FXML
    private AnchorPane scene_delete;

    @FXML
    private TextField txb_start,txb_end;


    public void backMain(javafx.event.ActionEvent event) throws IOException {
       /* Parent root =FXMLLoader.load(getClass().getResource("..\\fxml\\margeOptions.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene =new Scene(root);
        stage.setScene(scene);
        stage.show();*/

        AnchorPane scene1= FXMLLoader.load(getClass().getResource("..\\fxml\\main.fxml"));
        scene_delete.getChildren().removeAll();
        scene_delete.getChildren().setAll(scene1);

    }



    public void deleteFrames(){

        VideoCapture cap = new VideoCapture();

        String input = Controller.selectedFile.getAbsolutePath();
        cap.open(input);

        int frames_per_second = (int) cap.get(Videoio.CAP_PROP_FPS);

        int frame_number = (int) cap.get(Videoio.CAP_PROP_FRAME_COUNT);
        float duration = (float) frame_number / (float) frames_per_second;
        System.out.println(duration);

        double start = Double.parseDouble(txb_start.getText()), end = Double.parseDouble(txb_end.getText());
        double start_frame = start * (double) frames_per_second;
        System.out.println(start_frame);
        double end_frame = end * (double) frames_per_second;
        System.out.println(end_frame);

        Mat frame = new Mat();
        int fourcc = VideoWriter.fourcc('H', '2', '6', '4');

        VideoWriter writer = new VideoWriter("C:\\Users\\ASUS\\Desktop\\version\\Deleted"+Controller.step+".mp4", fourcc, frames_per_second, new Size((int) cap.get(Videoio.CAP_PROP_FRAME_WIDTH), (int) cap.get(Videoio.CAP_PROP_FRAME_HEIGHT)), true);
        VideoWriter copyDelete = new VideoWriter("C:\\Users\\ASUS\\Desktop\\version\\"+Controller.step+".mp4", fourcc, frames_per_second, new Size((int) cap.get(Videoio.CAP_PROP_FRAME_WIDTH), (int) cap.get(Videoio.CAP_PROP_FRAME_HEIGHT)), true);

        int number = 0;
        if (writer.isOpened()&&copyDelete.isOpened()) {
            while (cap.read(frame)) //the last frame of the movie will be invalid. check for it !
            {
                if (!(end_frame > number && start_frame < number) ) {
                    writer.write(frame);
                }
                else
                    copyDelete.write(frame);

                number++;
            }
            cap.release();
            writer.release();
            copyDelete.release();
        }

        Controller.selectedFile=new File("C:\\Users\\ASUS\\Desktop\\version\\"+Controller.step+".mp4");
        Controller.step++;
    }

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
