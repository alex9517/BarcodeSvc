//  Created : 2021-Jun-01
// Modified : 2021-Jun-02
//
//////////////////////////////
//
//   ZXING BARCODE GENERATOR
//
//////////////////////////////

package proj2.BarcodeSvc.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.oned.EAN13Writer;
import com.google.zxing.oned.UPCAWriter;
import com.google.zxing.pdf417.PDF417Writer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import java.awt.image.BufferedImage;


// NOTE!
// public static BufferedImage toBufferedImage( BitMatrix matrix )
// Renders a BitMatrix as an image, where "false" bits are rendered as white,
// and "true" bits are rendered as black. Uses default configuration.

public class ZxingBarcodeGenerator {

    public static final int BARCODE_WIDTH = 300;
    public static final int BARCODE_HEIGHT = 150;
    public static final int PDF_417_SIZE = 700;
    public static final int QR_CODE_SIZE = 200;


    public static BufferedImage generateUPCABarcodeImage( String data )
            throws Exception {
        UPCAWriter barcodeWriter = new UPCAWriter();
        BitMatrix bitMatrix = barcodeWriter.encode(
                data, BarcodeFormat.UPC_A, BARCODE_WIDTH, BARCODE_HEIGHT );
                // content, format, width, height; throws WriterException
                // if contents cannot be encoded legally in a format;
        return MatrixToImageWriter.toBufferedImage( bitMatrix );
    }


    public static BufferedImage generateEAN13BarcodeImage( String data )
            throws Exception {
        EAN13Writer barcodeWriter = new EAN13Writer();
        BitMatrix bitMatrix = barcodeWriter.encode(
                data, BarcodeFormat.EAN_13, BARCODE_WIDTH, BARCODE_HEIGHT );
        return MatrixToImageWriter.toBufferedImage( bitMatrix );
    }


    public static BufferedImage generateCode128BarcodeImage( String data )
            throws Exception {
        Code128Writer barcodeWriter = new Code128Writer();
        BitMatrix bitMatrix = barcodeWriter.encode(
                data, BarcodeFormat.CODE_128, BARCODE_WIDTH, BARCODE_HEIGHT );
        return MatrixToImageWriter.toBufferedImage( bitMatrix );
    }


    public static BufferedImage generatePDF417BarcodeImage( String data )
            throws Exception {
        PDF417Writer barcodeWriter = new PDF417Writer();
        BitMatrix bitMatrix = barcodeWriter.encode(
                data, BarcodeFormat.PDF_417, PDF_417_SIZE, PDF_417_SIZE );
        return MatrixToImageWriter.toBufferedImage( bitMatrix );
    }


    public static BufferedImage generateQRCodeImage( String data )
            throws Exception {
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = barcodeWriter.encode(
                data, BarcodeFormat.QR_CODE, QR_CODE_SIZE, QR_CODE_SIZE );
                // Throws WriterException
                // if contents cannot be encoded legally in a format;
        return MatrixToImageWriter.toBufferedImage( bitMatrix );
    }

}

// -END-
