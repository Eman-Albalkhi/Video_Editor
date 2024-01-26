import org.jcodec.api.awt.AWTSequenceEncoder;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.io.SeekableByteChannel;
import org.jcodec.common.model.Rational;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;


public class watermarkIMAGE {
    private static void sortByNumber(File[] files) {
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                int n1 = extractNumber(o1.getName());
                int n2 = extractNumber(o2.getName());
                return n1 - n2;
            }
            private int extractNumber(String name) {
                int i = 0;
                try {
                    int s = name.lastIndexOf('_')+1;
                    int e = name.lastIndexOf('.');
                    String number = name.substring(s, e);
                    i = Integer.parseInt(number);
                } catch(Exception e) {
                    i = 0; // if filename does not match the format then default to 0
                }
                return i;
            }
        });
        /*
        for(File f : files) {
            System.out.println(f.getName());
        }
        */
    }
    static void cut(){
        String output = "C:\\Users\\HP\\Desktop\\new";

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        VideoCapture cap = new VideoCapture();

        String input = "C:\\Users\\HP\\Videos\\lol.mp4";

        cap.open(input);

        Mat frame = new Mat();

        int  frame_number=0;
        if (cap.isOpened())
        {
            while(cap.read(frame)) //the last frame of the movie will be invalid. check for it !
            {
                Imgcodecs.imwrite(output + "/" + frame_number +".jpg", frame);
                frame_number++;
            }
            cap.release();
        }
        else
        {
            System.out.println("Fail");
        }
    }
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

            ImageIO.write(sourceImage, "jpg", destImageFile);
            g2d.dispose();

            System.out.println("The image watermark is added to the image.");

        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
    public static void main(String[] args) throws IOException {

        cut();

        Path directoryPath = Paths.get(new File("C:\\Users\\HP\\Desktop\\new").toURI());

        if (Files.isDirectory(directoryPath)) {
            DirectoryStream<Path> stream = Files.newDirectoryStream(directoryPath, "*." + "jpg");

            java.util.List<File> filesList = new ArrayList<File>();
            for (Path path : stream) {
                filesList.add(new File(String.valueOf(path.toFile())));
            }
            File[] files = new File[filesList.size()];
            filesList.toArray(files);

            sortByNumber(files);
            int i = 0;
            for (File img : files) {
                i++;
                addImageWatermark(new File("C:\\Users\\HP\\Desktop\\new\\ImageWater\\water_mark\\waterMark.jpg"), img, new File("C:\\Users\\HP\\Desktop\\new\\ImageWater\\Capture" + i + "+.jpg"));
            }
        }


        ////////////////////////
        SeekableByteChannel out = null;
        try {
            out = NIOUtils.writableFileChannel("C:\\Users\\HP\\Desktop\\new\\ImageWater\\water_image.mp4");

            // for Android use: AndroidSequenceEncoder
            AWTSequenceEncoder encoder = new AWTSequenceEncoder(out, Rational.R(25, 1));

            Path directoryPathh = Paths.get(new File("C:\\Users\\HP\\Desktop\\new\\ImageWater").toURI());

            if (Files.isDirectory(directoryPathh)) {
                DirectoryStream<Path> stream = Files.newDirectoryStream(directoryPathh, "*." + "jpg");

                List<File> filesList = new ArrayList<File>();
                for (Path path : stream) {
                    filesList.add(new File(String.valueOf(path.toFile())));
                }
                File[] files = new File[filesList.size()];
                filesList.toArray(files);

                sortByNumber(files);

                for (File img : files) {
                    System.err.println("Encoding image " + img.getName());
                    // Generate the image, for Android use Bitmap
                    BufferedImage image = ImageIO.read(img);
                    // Encode the image
                    encoder.encodeImage(image);
                }
            }
            // Finalize the encoding, i.e. clear the buffers, write the header, etc.
            encoder.finish();
        } finally {
            NIOUtils.closeQuietly(out);
        }
    }}
