package de.codepotion.cv;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class ImageHelper {

	/**
	 *  Fast conversion, based on http://answers.opencv.org/question/10344/opencv-java-load-image-to-gui/?answer=26499#post-id-26499
	 * @param input Mat to convert
	 * @return converted Mat as javafx-image
	 */
	public static Image mat2Image(Mat input)
	{
		int type = BufferedImage.TYPE_BYTE_GRAY;
        if ( input.channels() > 1 ) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize = input.channels()*input.cols()*input.rows();
        byte [] b = new byte[bufferSize];
        input.get(0,0,b); // get all the pixels
        BufferedImage output = new BufferedImage(input.cols(),input.rows(), type);
        final byte[] targetPixels = ((DataBufferByte)output.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);  
        return SwingFXUtils.toFXImage(output, null);
	}
	
	public static Mat image2Mat(Image input)
	{
		BufferedImage inputBuffered = SwingFXUtils.fromFXImage(input, null);
		byte[] pixels = ((DataBufferByte)inputBuffered.getRaster().getDataBuffer()).getData();
		Mat mat = new Mat(inputBuffered.getHeight(),inputBuffered.getWidth(), CvType.CV_8UC3);
		mat.put(0, 0, pixels);
		return mat;
	}
}
