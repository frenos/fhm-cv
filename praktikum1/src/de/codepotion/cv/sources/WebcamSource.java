package de.codepotion.cv.sources;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import org.opencv.core.Mat;
import org.opencv.highgui.VideoCapture;

public class WebcamSource {
	
	private VideoCapture myCamera;
	
	public WebcamSource()
	{
		myCamera = new VideoCapture(0);
	}
	
	public Image getFrame()
	{
		Mat newFrame = new Mat();
		myCamera.open(0);
		myCamera.read(newFrame);
		myCamera.release();
		return mat2Image(newFrame);
	}
	
	/**
	 *  Fast conversion, based on http://answers.opencv.org/question/10344/opencv-java-load-image-to-gui/?answer=26499#post-id-26499
	 * @param input Mat to convert
	 * @return converted Mat as javafx-image
	 */
	private Image mat2Image(Mat input)
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

}
