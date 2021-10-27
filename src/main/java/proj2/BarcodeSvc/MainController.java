//  Created : 2021-Jun-01
// Modified : 2021-Jun-18

//////////////////////////////
//
//      MAIN CONTROLLER
//
//////////////////////////////

// About barcodes
// --------------
// The UPC and EAN barcodes (since the 1970s) are used to encode
// Global Trade Item Numbers (GTIN), which uniquely identify products.
// UPC, UCC, EAN, JAN, GTIN-8, GTIN-12 and GTIN-13, ISBN and Bookland
// barcodes are all created from the same symbology type, commonly known
// as the UPC/EAN barcode. To be fully GTIN compliant, all UPC and EAN
// numbers should be stored in databases as 14-digit numbers and padded
// with zeros at the left side.

// The UPC-A barcode option is most commonly used to encode 12 digits
// of the GTIN. The 12th digit of UPCA is a MOD10 check digit that is
// recalculated to reduce scanning errors and may be omitted when encoding
// data. A 2 or 5 digit add-on may be created by appending the numbers to
// the end of the 11 or 12 digits that make up the primary symbol.

// The EAN-13 barcode specification is used to encode 13 digits of the
// GTIN barcode symbology. The 13th digit of the EAN-13 barcode is a MOD10
// check-digit that is recalculated to reduce scanning errors and may be
// omitted when encoding data. A 2 or 5 digit add-on may be created by
// appending the numbers to the end of the 12 or 13 digits that make up the
// primary symbol.

package proj2.BarcodeSvc;

import java.util.Collections;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;

import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.awt.image.BufferedImage;

// DO NOT REMOVE LOGGER! You may need it later!

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import proj2.BarcodeSvc.domain.Barcode;
import proj2.BarcodeSvc.utils.BarcodeReader;
import proj2.BarcodeSvc.utils.ZxingBarcodeGenerator;
import proj2.BarcodeSvc.utils.Barcode4jBarcodeGenerator;


@RestController
@RequestMapping("/barcodes")
public class MainController {

    // DO NOT REMOVE LOGGER! You may need it later!

    private static final Logger log =
            LoggerFactory.getLogger( MainController.class );

    private static final String thisClassName = "MainController";

    private static final String KEY_RESPONSE = "Response";

    private static final String MSG_BAD_ARGUMENT =
            "Input arg is NULL or has unacceptable length.";


    ///////////////////////
    //
    // READ/DECODE BARCODE
    //
    ///////////////////////

    // It is supposed to be able to read all
    // types of barcodes supported by Zxing library.

    @PostMapping( value = "/read",
                    produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<?> readBarcode( @RequestBody Barcode bc ) {

        if ( bc == null ) {
            return new ResponseEntity(
                Collections.singletonMap( KEY_RESPONSE, MSG_BAD_ARGUMENT ),
                HttpStatus.BAD_REQUEST );
        }
        byte[] picture = bc.getPicture();

        if ( picture == null || picture.length == 0 ) {
            return new ResponseEntity(
                Collections.singletonMap( KEY_RESPONSE, "No picture." ),
                HttpStatus.BAD_REQUEST );
        }
        int id = bc.getId();

        Barcode res = BarcodeReader.read( picture );

        if ( res.getType().equals( "error" )) {
            log.error( thisClassName
                    + "#readBarcode() failed .. " + res.getData());
            return new ResponseEntity( res, HttpStatus.INTERNAL_SERVER_ERROR );
        }
        log.debug( thisClassName + "#readBarcode(), data: " + res.getData());
        return new ResponseEntity( res, HttpStatus.OK );
    }


    ////////////////////////
    //
    // CREATE UPC-A BARCODE
    //
    ////////////////////////

    // NOTE!
    //   UPC-A is 12 digits, but this method wants exactly
    //   11 digits because the last digit must be a check digit,
    //   Barcode*Generator calculates it and adds to the provided number.

    @GetMapping( value = "/create/upca/{barcode}",
                    produces = MediaType.IMAGE_PNG_VALUE )
    public ResponseEntity<BufferedImage> createUPCA(
            @PathVariable( "barcode" ) String barcode ) {

        try {
            if ( barcode == null || barcode.length() != 11 )
                throw new IllegalArgumentException( MSG_BAD_ARGUMENT );

            Long.parseLong( barcode );  // It must be a number.

            BufferedImage image =
                    Barcode4jBarcodeGenerator
                            .generateUPCABarcodeImage( barcode );

            return new ResponseEntity( image, HttpStatus.OK );

        } catch( Exception e ) {
            log.debug( thisClassName + "#createUPCA .. " + e.getMessage());
        }
        return new ResponseEntity( null, HttpStatus.BAD_REQUEST );
    }


    ////////////////////////
    //
    // CREATE UPC-E BARCODE
    //
    ////////////////////////

    // NOTE!
    //   UPC-E is 8 digits, but this method wants exactly
    //   7 digits because the last digit must be a check digit,
    //   Barcode*Generator calculates it and adds to the provided number.

    @GetMapping( value = "/create/upce/{barcode}",
                    produces = MediaType.IMAGE_PNG_VALUE )
    public ResponseEntity<BufferedImage> createUPCE(
            @PathVariable( "barcode" ) String barcode ) {

        try {
            if ( barcode == null || barcode.length() != 7 )
                throw new IllegalArgumentException( MSG_BAD_ARGUMENT );

            Long.parseLong( barcode );  // It must be a number.

            BufferedImage image =
                    Barcode4jBarcodeGenerator
                            .generateUPCEBarcodeImage( barcode );

            return new ResponseEntity( image, HttpStatus.OK );

        } catch( Exception e ) {
            log.debug( thisClassName + "#createUPCE .. " + e.getMessage());
        }
        return new ResponseEntity( null, HttpStatus.BAD_REQUEST );
    }


    /////////////////////////
    //
    // CREATE EAN-13 BARCODE
    //
    /////////////////////////

    // NOTE that EAN-13 is numeric-only, 13 digits.
    //   But input data must be 12 digits because 13th
    //   digit is an auto-generated check digit.

    @GetMapping( value = "/create/ean13/{barcode}",
                    produces = MediaType.IMAGE_PNG_VALUE )
    public ResponseEntity<BufferedImage> createEAN13(
            @PathVariable( "barcode" ) String barcode ) {

        try {
            if ( barcode == null || barcode.length() != 12 )
                throw new IllegalArgumentException( MSG_BAD_ARGUMENT );

            Long.parseLong( barcode );  // It must be a number.

            BufferedImage image =
                    Barcode4jBarcodeGenerator
                            .generateEAN13BarcodeImage( barcode );

            return new ResponseEntity( image, HttpStatus.OK );

        } catch( Exception e ) {
            log.debug( thisClassName + "createEAN13 .. " + e.getMessage());
        }
        return new ResponseEntity( null, HttpStatus.BAD_REQUEST );
    }


    ////////////////////////
    //
    // CREATE EAN-8 BARCODE
    //
    ////////////////////////

    // NOTE that EAN-8 is numeric-only, 8 digits.
    //   But input data must be 7 digits because 8th
    //   digit is an auto-generated check digit.

    @GetMapping( value = "/create/ean8/{barcode}",
                    produces = MediaType.IMAGE_PNG_VALUE )
    public ResponseEntity<BufferedImage> createEAN8(
            @PathVariable( "barcode" ) String barcode ) {

        try {
            if ( barcode == null || barcode.length() != 7 )
                throw new IllegalArgumentException( MSG_BAD_ARGUMENT );

            Long.parseLong( barcode );  // It must be a number.

            BufferedImage image =
                    Barcode4jBarcodeGenerator
                            .generateEAN8BarcodeImage( barcode );

            return new ResponseEntity( image, HttpStatus.OK );

        } catch( Exception e ) {
            log.debug( thisClassName + "createEAN8 .. " + e.getMessage());
        }
        return new ResponseEntity( null, HttpStatus.BAD_REQUEST );
    }


    ///////////////////////////
    //
    // CREATE Code 128 BARCODE
    //
    ///////////////////////////

    // Valid input: uppercase and lowercase letters, numbers,
    //   punctuation, any letter or symbol appearing on the US keyboard
    //   and lower ASCII functions such as returns and tabs, up to about
    //   20-30 digits.

    @PostMapping( value = "/create/code128",
                    produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Barcode> createCode128(
            @RequestBody String barcode ) {

        try {
            if ( barcode == null || barcode.length() > 30 )
                throw new IllegalArgumentException( MSG_BAD_ARGUMENT );

            BufferedImage image =
                    Barcode4jBarcodeGenerator
                            .generateCode128BarcodeImage( barcode );

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write( image, "png", out );
            byte[] picture = out.toByteArray();

            return new ResponseEntity( new Barcode(
                    1, "Code 128 (1D)", null, picture ), HttpStatus.OK );

        } catch( Exception e ) {
            log.debug( thisClassName + "#createCode128 .. " + e.getMessage());
        }
        return new ResponseEntity( null, HttpStatus.BAD_REQUEST );
    }


    /////////////////////////
    //
    // CREATE PDF417 BARCODE
    //
    /////////////////////////

    // This is a 2D barcode.
    // Valid input: several lines of data of any type over 20 digits,
    //   numbers, letters, punctuation, ASCII 0 to 127 plus files and bytes.

    @PostMapping( value = "/create/pdf417",
                    produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Barcode> createPDF417(
            @RequestBody String barcode ) {

        try {
            // I have no idea how much data PDF417 can accept;
            if ( barcode == null || barcode.length() > 999 )
                throw new IllegalArgumentException( MSG_BAD_ARGUMENT );

            BufferedImage image =
                    Barcode4jBarcodeGenerator
                            .generatePDF417BarcodeImage( barcode );

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write( image, "png", out );
            byte[] picture = out.toByteArray();

            return new ResponseEntity( new Barcode(
                    1, "PDF417 (2D)", null, picture ), HttpStatus.OK );

        } catch( Exception e ) {
            log.debug( thisClassName + "#createPDF417 .. " + e.getMessage());
        }
        return new ResponseEntity( null, HttpStatus.BAD_REQUEST );
    }


    //////////////////
    //
    // CREATE QR CODE
    //
    //////////////////

    // Quick Response Codes are the most popular 2D barcodes.
    // Valid input: several lines of data of any type over 20 digits,
    //   numbers, letters, punctuation, ASCII 0 to 127 plus files and bytes.

    @PostMapping( value = "/create/qrcode",
                    produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Barcode> createQRCode(
            @RequestBody String barcode) throws Exception {

        try {
            if ( barcode == null || barcode.length() > 2956 )
                throw new IllegalArgumentException( MSG_BAD_ARGUMENT );

            BufferedImage image =
                    ZxingBarcodeGenerator
                            .generateQRCodeImage( barcode );

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write( image, "png", out );
            byte[] picture = out.toByteArray();

            return new ResponseEntity( new Barcode(
                    1, "QR Code (2D)", null, picture ), HttpStatus.OK );

        } catch( Exception e ) {
            log.debug( thisClassName + "#createQRCode .. " + e.getMessage());
        }
        return new ResponseEntity( null, HttpStatus.BAD_REQUEST );
    }

}

// -END-
