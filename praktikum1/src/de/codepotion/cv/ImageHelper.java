package de.codepotion.cv;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class ImageHelper {

	/**
	 * Fast conversion, based on
	 * http://answers.opencv.org/question/10344/opencv-
	 * java-load-image-to-gui/?answer=26499#post-id-26499
	 * 
	 * @param input
	 *            Mat to convert
	 * @return converted Mat as javafx-image
	 */
	public static Image mat2Image(Mat inputMat) {
		 int type = BufferedImage.TYPE_BYTE_GRAY;
	        if ( inputMat.channels() > 1 ) {
	            type = BufferedImage.TYPE_3BYTE_BGR;
	        }
	        
	        byte [] data = new byte[inputMat.channels()*inputMat.cols()*inputMat.rows()];
	        inputMat.get(0,0,data); // get all the pixels
	        BufferedImage image = new BufferedImage(inputMat.cols(),inputMat.rows(), type);
	        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
	        System.arraycopy(data, 0, targetPixels, 0, data.length);  
	        
		return SwingFXUtils.toFXImage(image, null);
	}

	public static Mat image2Mat(Image input) {
		BufferedImage original = SwingFXUtils.fromFXImage(input, null);
		
		//always convert input to 3B BGR so we can copy direct to Mat
		BufferedImage outputImage = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		outputImage.getGraphics().drawImage(original, 0, 0, null);
		
		byte[] data = ((DataBufferByte) outputImage.getRaster().getDataBuffer()).getData();
		Mat outputMat = new Mat(outputImage.getHeight(), outputImage.getWidth(), CvType.CV_8UC3);
		outputMat.put(0, 0, data);
	    return outputMat;
	}
}
