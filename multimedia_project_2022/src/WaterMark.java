import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.VideoWriter;
import org.opencv.videoio.Videoio;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class WaterMark {

    // Using NIO e JCodec to convert multiple sequence png images to mp4 video file

    static void addImageWatermark(File watermarkImageFile, File sourceImageFile, File destImageFile) {
        try {
            BufferedImage sourceImage = ImageIO.read(sourceImageFile);
            BufferedImage watermarkImage = ImageIO.read(watermarkImageFile);

            // initializes necessary graphic properties
            Graphics2D g2d = (Graphics2D) sourceImage.getGraphics();
            AlphaComposite alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f);
            g2d.setComposite(alphaChannel);

            // calculates the coordinate where the image is painted
            int topLeftX = (sourceImage.getWidth() - watermarkImage.getWidth()) / 2;
            int topLeftY = (sourceImage.getHeight() - watermarkImage.getHeight()) / 2;

            // paints the image watermark
            g2d.drawImage(watermarkImage, topLeftX, topLeftY, null);

            ImageIO.write(sourceImage, "png", destImageFile);
            g2d.dispose();

            System.out.println("The image watermark is added to the image.");

        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
    public static void main(String[] args) {


        //////////////////////////////////////////////////
        String output = "C:\\Users\\HP\\Desktop\\waterMark";
        String output1 = "C:\\Users\\HP\\Desktop\\waterMark1";


        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        VideoCapture cap = new VideoCapture();
        VideoCapture cap1 = new VideoCapture();


        String input = "C:\\Users\\HP\\Videos\\lol.mp4";
        String input1 = "C:\\Users\\HP\\Desktop\\new\\water\\Video_hello.mp4";



        cap.open(input);
        cap.open(input1);

        Mat frame = new Mat();
        Mat frame1 = new Mat();

        int fourcc = VideoWriter.fourcc('H', '2', '6', '4');
        VideoWriter writer = new VideoWriter("C:\\Users\\HP\\Desktop", fourcc, 30, new Size((int) cap.get(Videoio.CAP_PROP_FRAME_WIDTH), (int) cap.get(Videoio.CAP_PROP_FRAME_HEIGHT)), true);

        int  frame_number=0;
        if (writer.isOpened()/*&&cap1.isOpened()*/)
        {
            while(cap.read(frame)&&cap1.read(frame1)) //the last frame of the movie will be invalid. check for it !
            {

                File i = new File(output+"/"+frame_number+".jpg");
                File j = new File(output1+"/"+frame_number+".jpg");
                File destImageFile = new File("C:\\Users\\HP\\Desktop\\watermark\\image"+frame_number+".jpg");
                System.out.println("yyyyy");
                addImageWatermark(j, i, destImageFile);
                Mat src = Imgcodecs.imread("C:\\Users\\HP\\Desktop\\watermark\\image"+frame_number+".jpg",32);

                writer.write(src);
                frame_number++;

            }
            cap.release();
            cap1.release();
            writer.release();
        }

    }
}
