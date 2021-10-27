//  Created : 2021-Jun-01
// Modified : 2021-Jun-22
//
////////////////////////////
//
//      BARCODE READER
//
////////////////////////////

package proj2.BarcodeSvc.utils;

import java.util.Map;
import java.util.HashMap;
import java.io.InputStream;
import java.io.ByteArrayInputStream;

import javax.imageio.ImageIO;

import com.google.zxing.Result;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import proj2.BarcodeSvc.domain.Barcode;

// MultiFormatReader:
//   public Result decode(BinaryBitmap image) throws NotFoundException
//
// This version of decode honors the intent of Reader.decode(BinaryBitmap)
// in that it passes null as a hint to the decoders. However, that makes it
// inefficient to call repeatedly. Use setHints() followed by
// decodeWithState() for continuous scan applications.
//
//   public Result decodeWithState(BinaryBitmap image) throws NotFoundException
//
// Decode an image using the state set up by calling setHints() previously.
// Continuous scan clients will get a large speed increase by using this
// instead of decode().
//
//   public void setHints(Map<DecodeHintType,?> hints)
//
// This method adds state to the MultiFormatReader. By setting the hints once,
// subsequent calls to decodeWithState(image) can reuse the same set of readers
// without reallocating memory. This is important for performance in continuous
// scan clients.
//
//   void reset()
//
// Resets any internal state the implementation has after a decode,
// to prepare it for reuse.


public class BarcodeReader {

    public static Barcode read( byte[] image ) {
            // throws IOException, NotFoundException {

        try {
            HashMap<DecodeHintType, Object> hints = new HashMap<>();
            hints.put( DecodeHintType.TRY_HARDER, Boolean.TRUE );
            hints.put( DecodeHintType.CHARACTER_SET, "UTF-8" );

            InputStream in = new ByteArrayInputStream( image );

            BinaryBitmap binaryBitmap = new BinaryBitmap( new HybridBinarizer(
                    new BufferedImageLuminanceSource( ImageIO.read( in ))));

            Result result = new MultiFormatReader().decode( binaryBitmap, hints );

            // Result result =
            //         new MultiFormatReader().decodeWithState( binaryBitmap );
            // Throws NotFoundException for any errors which occurred;

            return new Barcode(
                    1,    // Meaningless / reserverd for future;
                    getShortInfo( result.getBarcodeFormat()),
                          // Short info about the barcode type;
                          // It can also be "error" if decode failed;
                    result.getText(),
                          // Barcode info if decode was success;
                          // Otherwise it's supposed to be an error message;
                    null
                          // This is supposed to a barcode image. However,
                          // since this method has received it as an argument,
                          // there is no sense to include it in the response;
            );
        } catch( Exception e ) {
            return new Barcode(
                    0, "error", e.getMessage().substring( 0, 100 ), null );
        }
    }


    public static String getShortInfo( BarcodeFormat fmt ) {
        switch( fmt.name()) {
            case "AZTEC" : return "Aztec (2D)";
            case "CODABAR" : return "CODABAR (1D)";
            case "CODE_128" : return "Code 128 (1D)";
            case "CODE_39" : return "Code 39 (1D)";
            case "CODE_93" : return "Code 93 (1D)";
            case "DATA_MATRIX" : return "Data Matrix (2D)";
            case "EAN_13" : return "EAN-13 (1D)";
            case "EAN_8" : return "EAN-8 (1D)";
            case "ITF" : return "ITF (Interleaved Two of Five) (1D)";
            case "MAXICODE" : return "MaxiCode (2D)";
            case "PDF_417" : return "PDF417 (2D)";
            case "QR_CODE" : return "QR Code (2D)";
            case "RSS_14" : return "RSS 14";
            case "RSS_EXPANDED" : return "RSS Expanded";
            case "UPC_A" : return "UPC-A (1D)";
            case "UPC_E" : return "UPC-E (1D)";
            case "UPC_EAN_EXTENSION" : return "UPC/EAN extension";
            default : return "Unknown";
        }
    }

}

// -END-

/*

-----
public static void readQrCode(InputStream inputStream) throws IOException {
    BufferedImage image = ImageIO.read(inputStream);
    LuminanceSource source = new BufferedImageLuminanceSource(image);
    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
    QRCodeReader reader = new QRCodeReader();
    Result result = null;

    try {
        result = reader.decode(bitmap);
    } catch (ReaderException e) {
        e.printStackTrace();
    }
    System.out.println(result.getText());
}

-----

public static String decode(InputStream in) throws Exception {
    BufferedImage image = ImageIO.read(in);
    if (image == null) {
        return null;
    }
    BufferedImageLuminanceSource source =
            new BufferedImageLuminanceSource(image);
    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
    Result result;
    Hashtable<DecodeHintType, Object> hints =
            new Hashtable<DecodeHintType, Object>();
    hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
    result = new MultiFormatReader().decode(bitmap, hints);
    String resultStr = result.getText();
    in.close();
    return resultStr;
}

-----

    HashMap<DecodeHintType, Object> hints = new HashMap<>();
    hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
    hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");

-----
@Test
public void givenUsingPlainJava_whenConvertingByteArrayToInputStream_thenCorrect() 
  throws IOException {
    byte[] initialArray = { 0, 1, 2 };
    InputStream targetStream = new ByteArrayInputStream(initialArray);
}


package crunchify.com.tutorials;
 
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
 
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
 
 
public class CrunchifyQRCodeGenerator {
 
    public static void main(String[] args) {
        String myCodeText = "https://crunchify.com";
        String filePath = "//cdn.crunchify.com/Users/app/Document/Crunchify.com-QRCode.png";
        int size = 512;
        String crunchifyFileType = "png";
        File crunchifyFile = new File(filePath);
        try {
 
            Map<EncodeHintType, Object> crunchifyHintType = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            crunchifyHintType.put(EncodeHintType.CHARACTER_SET, "UTF-8");
 
            // Now with version 3.4.1 you could change margin (white border size)
            crunchifyHintType.put(EncodeHintType.MARGIN, 1); // default = 4
            Object put = crunchifyHintType.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
 
            QRCodeWriter mYQRCodeWriter = new QRCodeWriter(); // throws com.google.zxing.WriterException
            BitMatrix crunchifyBitMatrix = mYQRCodeWriter.encode(myCodeText, BarcodeFormat.QR_CODE, size,
                    size, crunchifyHintType);
            int CrunchifyWidth = crunchifyBitMatrix.getWidth();
 
            // The BufferedImage subclass describes an Image with an accessible buffer of crunchifyImage data.
            BufferedImage crunchifyImage = new BufferedImage(CrunchifyWidth, CrunchifyWidth,
                    BufferedImage.TYPE_INT_RGB);
 
            // Creates a Graphics2D, which can be used to draw into this BufferedImage.
            crunchifyImage.createGraphics();
 
            // This Graphics2D class extends the Graphics class to provide more sophisticated control over geometry, coordinate transformations, color management, and text layout.
            // This is the fundamental class for rendering 2-dimensional shapes, text and images on the Java(tm) platform.
            Graphics2D crunchifyGraphics = (Graphics2D) crunchifyImage.getGraphics();
 
            // setColor() sets this graphics context's current color to the specified color.
            // All subsequent graphics operations using this graphics context use this specified color.
            crunchifyGraphics.setColor(Color.white);
 
            // fillRect() fills the specified rectangle. The left and right edges of the rectangle are at x and x + width - 1.
            crunchifyGraphics.fillRect(0, 0, CrunchifyWidth, CrunchifyWidth);
 
            // TODO: Please change this color as per your need
            crunchifyGraphics.setColor(Color.BLUE);
 
            for (int i = 0; i < CrunchifyWidth; i++) {
                for (int j = 0; j < CrunchifyWidth; j++) {
                    if (crunchifyBitMatrix.get(i, j)) {
                        crunchifyGraphics.fillRect(i, j, 1, 1);
                    }
                }
            }
 
            // A class containing static convenience methods for locating
            // ImageReaders and ImageWriters, and performing simple encoding and decoding.
            ImageIO.write(crunchifyImage, crunchifyFileType, crunchifyFile);
 
            System.out.println("\nCongratulation.. You have successfully created QR Code.. \n" +
                    "Check your code here: " + filePath);
        } catch (WriterException e) {
            System.out.println("\nSorry.. Something went wrong...\n");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
 
    }
}

*/
