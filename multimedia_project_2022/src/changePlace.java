import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.VideoWriter;
import org.opencv.videoio.Videoio;


public class changePlace {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    public static void main(String[] args) {

        VideoCapture cap = new VideoCapture();
        String input = "C:\\Users\\ASUS\\Desktop\\lol.mp4";
        cap.open(input);
        int frames_per_second = (int) cap.get(Videoio.CAP_PROP_FPS);

        double start = 10, end = 12;
        double start_frame = start * (double) frames_per_second;
        System.out.println(start_frame);
        double end_frame = end * (double) frames_per_second;
        System.out.println(end_frame);

        Mat frame = new Mat();
        Mat frame1 = new Mat();
        Mat frame2 = new Mat();

        int fourcc = VideoWriter.fourcc('H', '2', '6', '4');

        VideoWriter writer = new VideoWriter("C:\\Users\\ASUS\\Desktop\\newcopy.mp4", fourcc, 30, new Size((int) cap.get(Videoio.CAP_PROP_FRAME_WIDTH), (int) cap.get(Videoio.CAP_PROP_FRAME_HEIGHT)), true);
        VideoWriter copyDelete = new VideoWriter("C:\\Users\\ASUS\\Desktop\\deletecopy.mp4", fourcc, 30, new Size((int) cap.get(Videoio.CAP_PROP_FRAME_WIDTH), (int) cap.get(Videoio.CAP_PROP_FRAME_HEIGHT)), true);
        VideoWriter writer2 = new VideoWriter("C:\\Users\\ASUS\\Desktop\\editvideo2.mp4", fourcc, 30, new Size((int) cap.get(Videoio.CAP_PROP_FRAME_WIDTH), (int) cap.get(Videoio.CAP_PROP_FRAME_HEIGHT)), true);

        int number = 0;
        if (writer.isOpened() && copyDelete.isOpened()) {
            while (cap.read(frame)) //the last frame of the movie will be invalid. check for it !
            {
                if (!(end_frame > number && start_frame < number)) {
                    writer.write(frame);
                } else
                    copyDelete.write(frame);

                number++;
            }


        }
        writer.release();
        copyDelete.release();
        cap.release();
        double new_start_forMerge = 0;
        double new_start_frame = new_start_forMerge * (double) frames_per_second;
        VideoCapture cap1 = new VideoCapture();
        String input1 = "C:\\Users\\ASUS\\Desktop\\newcopy.mp4";
        cap1.open(input1);

        VideoCapture cap2 = new VideoCapture();
        String input2 = "C:\\Users\\ASUS\\Desktop\\deletecopy.mp4";
        cap2.open(input2);

        int count = 0;
        if (writer2.isOpened()) {
            //   System.out.println("12222222222222222222hhhhhhhhhhhhhhhhhhhhhh");
            while (cap1.read(frame1)) {
                //    System.out.println("hhhhhhhhhhhhhhhhhhhhhh");
                if (new_start_frame == count) {
                    while (cap2.read(frame2)) {
                        writer2.write(frame2);

                    }


                } else {
                    writer2.write(frame1);
                    //   System.out.println("hhhhhhhhhhhhhhhhhhhhhh2222222222222222222222222222222");

                }

                count++;
            }
        }

        cap1.release();
        cap2.release();

        writer2.release();
    }


}


