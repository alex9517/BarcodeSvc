//  Created : 2021-Jun-01
// Modified : 2021-Jun-16
//
///////////////////////////
//
//   BARCODE4J GENERATOR
//
///////////////////////////

package proj2.BarcodeSvc.utils;

import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.impl.pdf417.PDF417Bean;
import org.krysalis.barcode4j.impl.upcean.EAN13Bean;
import org.krysalis.barcode4j.impl.upcean.EAN8Bean;
import org.krysalis.barcode4j.impl.upcean.UPCABean;
import org.krysalis.barcode4j.impl.upcean.UPCEBean;

import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;

import java.awt.image.BufferedImage;


public class Barcode4jBarcodeGenerator {

    private static final int DOTS_PER_INCH = 160;  // Resolution;


    public static BufferedImage generateUPCABarcodeImage( String barcode ) {

        UPCABean barcodeGenerator = new UPCABean();
        BitmapCanvasProvider canvas = new BitmapCanvasProvider(
                DOTS_PER_INCH, BufferedImage.TYPE_BYTE_BINARY, false, 0 );
        barcodeGenerator.generateBarcode( canvas, barcode );
        return canvas.getBufferedImage();
    }


    public static BufferedImage generateUPCEBarcodeImage( String barcode ) {

        UPCEBean barcodeGenerator = new UPCEBean();
        BitmapCanvasProvider canvas = new BitmapCanvasProvider(
                DOTS_PER_INCH, BufferedImage.TYPE_BYTE_BINARY, false, 0 );
        barcodeGenerator.generateBarcode( canvas, barcode );
        return canvas.getBufferedImage();
    }


    public static BufferedImage generateEAN13BarcodeImage( String barcode ) {

        EAN13Bean barcodeGenerator = new EAN13Bean();
        BitmapCanvasProvider canvas = new BitmapCanvasProvider(
                DOTS_PER_INCH, BufferedImage.TYPE_BYTE_BINARY, false, 0 );
        barcodeGenerator.generateBarcode( canvas, barcode );
        return canvas.getBufferedImage();
    }


    public static BufferedImage generateEAN8BarcodeImage( String barcode ) {

        EAN8Bean barcodeGenerator = new EAN8Bean();
        BitmapCanvasProvider canvas = new BitmapCanvasProvider(
                DOTS_PER_INCH, BufferedImage.TYPE_BYTE_BINARY, false, 0 );
        barcodeGenerator.generateBarcode( canvas, barcode );
        return canvas.getBufferedImage();
    }


    public static BufferedImage generateCode128BarcodeImage( String barcode ) {
        Code128Bean barcodeGenerator = new Code128Bean();
        BitmapCanvasProvider canvas = new BitmapCanvasProvider(
                DOTS_PER_INCH, BufferedImage.TYPE_BYTE_BINARY, false, 0 );
        barcodeGenerator.generateBarcode( canvas, barcode );
        return canvas.getBufferedImage();
    }


    public static BufferedImage generatePDF417BarcodeImage( String barcode ) {

        PDF417Bean barcodeGenerator = new PDF417Bean();
        BitmapCanvasProvider canvas = new BitmapCanvasProvider(
                DOTS_PER_INCH, BufferedImage.TYPE_BYTE_BINARY, false, 0 );
        barcodeGenerator.setColumns( 10 );
        barcodeGenerator.generateBarcode( canvas, barcode );
        return canvas.getBufferedImage();
    }

}

// -END-
