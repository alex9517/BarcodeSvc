//
// Modified : 2021-Jun-17
//
///////////////////////////////
//
// MAIN CONTROLLER REST TESTS
//
///////////////////////////////
//
// Tests : 16
//

package proj2.BarcodeSvc;

import java.util.Set;
import java.util.List;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Collections;
import java.io.IOException;
import java.time.*;
import java.time.format.*;
import java.net.URL;

import java.io.InputStream;
import java.io.ByteArrayOutputStream;

import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import com.fasterxml.jackson.databind.JsonNode;
import org.skyscreamer.jsonassert.JSONCompareMode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
// import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.junit.jupiter.api.AfterAll;
// import org.junit.jupiter.api.AfterEach;
// import org.junit.jupiter.api.BeforeAll;
// import org.junit.jupiter.api.BeforeEach;

// import org.springframework.test.context.ContextConfiguration;
// import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import proj2.BarcodeSvc.domain.Barcode;

import static proj2.BarcodeSvc.TestTemplate.*;

@ActiveProfiles("dev")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MainControllerRestTests {

    private static final int BUFFLEN = 16384;  // Or, set it to 32768;

    private static final String PICTURE_1 = "static/images/upca.jpg";
    private static final String PICTURE_2 = "static/images/ean13.jpg";
    private static final String PICTURE_3 = "static/images/qrcode_test_01.png";

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper om;

    private String cert_policy = "self-signed";


    @Test
    public void testRead_UPCA()
            throws IOException, JSONException, JsonProcessingException {

        byte[] picture = null;

        try {
            picture = readFileFromRes( PICTURE_1 );
        } catch( IOException | IllegalArgumentException e ) {
            ;
        }
        assertNotNull( picture );

        Barcode bc = new Barcode( 1, null, null, picture );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType( MediaType.APPLICATION_JSON );
        HttpEntity<String> entity =    // Serialize;
            new HttpEntity<>( om.writeValueAsString( bc ), headers );

        String url = "https://localhost:" + port + "/barcodes/read";

        TestRestTemplate restTemplate = createTemplate( cert_policy );
        assertNotNull( restTemplate );

        ResponseEntity<String> response =
                restTemplate.exchange(
                        url, HttpMethod.POST, entity, String.class );

        assertEquals( HttpStatus.OK, response.getStatusCode());
        JsonNode node = om.readTree( response.getBody());

        assertNotNull( node );

        int idFromResponse = node.get( "id" ).intValue();
        String dataFromResponse = node.get( "data" ).textValue();

        assertEquals( idFromResponse, 1 );
    }


    @Test
    public void testRead_QR_Code()
            throws IOException, JSONException, JsonProcessingException {

        byte[] picture = null;

        try {
            picture = readFileFromRes( PICTURE_3 );
        } catch( IOException | IllegalArgumentException e ) {
            ;
        }
        assertNotNull( picture );

        Barcode bc = new Barcode( 1, null, null, picture );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType( MediaType.APPLICATION_JSON );
        HttpEntity<String> entity =    // Serialize;
            new HttpEntity<>( om.writeValueAsString( bc ), headers );

        String url = "https://localhost:" + port + "/barcodes/read";

        TestRestTemplate restTemplate = createTemplate( cert_policy );
        assertNotNull( restTemplate );

        ResponseEntity<String> response =
                restTemplate.exchange(
                        url, HttpMethod.POST, entity, String.class );

        assertEquals( HttpStatus.OK, response.getStatusCode());
        JsonNode node = om.readTree( response.getBody());

        assertNotNull( node );

        int idFromResponse = node.get( "id" ).intValue();
        String dataFromResponse = node.get( "data" ).textValue();

        assertEquals( idFromResponse, 1 );
        assertTrue( dataFromResponse.startsWith( "This is my test sample" ));
    }


    @Test
    public void testCreateUpca_BadRequest()
            throws JSONException, JsonProcessingException {

        String url = "https://localhost:" + port
                + "/barcodes/create/upca/abcdletters";

        TestRestTemplate restTemplate = createTemplate( cert_policy );
        assertNotNull( restTemplate );

        ResponseEntity<String> response =
                restTemplate.getForEntity( url, String.class );

        assertEquals( HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


    @Test
    public void testCreateUpca()
            throws JSONException, JsonProcessingException {

        String url = "https://localhost:" + port
                + "/barcodes/create/upca/24567890395";

        TestRestTemplate restTemplate = createTemplate( cert_policy );
        assertNotNull( restTemplate );

        ResponseEntity<String> response =
                restTemplate.getForEntity( url, String.class );

        assertEquals( HttpStatus.OK, response.getStatusCode());
    }


    @Test
    public void testCreate_Ean13_BadRequest()
            throws JSONException, JsonProcessingException {

        String url = "https://localhost:" + port
                + "/barcodes/create/ean13/abcdletters2";

        TestRestTemplate restTemplate = createTemplate( cert_policy );
        assertNotNull( restTemplate );

        ResponseEntity<String> response =
                restTemplate.getForEntity( url, String.class );

        assertEquals( HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


    @Test
    public void testCreate_Ean13_BadRequest_too_long()
            throws JSONException, JsonProcessingException {

        String url = "https://localhost:" + port
                + "/barcodes/create/ean13/2456789039512467";

        TestRestTemplate restTemplate = createTemplate( cert_policy );
        assertNotNull( restTemplate );

        ResponseEntity<String> response =
                restTemplate.getForEntity( url, String.class );

        assertEquals( HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


    @Test
    public void testCreate_Ean13()
            throws JSONException, JsonProcessingException {

        String url = "https://localhost:" + port
                + "/barcodes/create/ean13/245678903951";

        TestRestTemplate restTemplate = createTemplate( cert_policy );
        assertNotNull( restTemplate );

        ResponseEntity<String> response =
                restTemplate.getForEntity( url, String.class );

        assertEquals( HttpStatus.OK, response.getStatusCode());
    }


    @Test
    public void testCreate_Ean8()
            throws JSONException, JsonProcessingException {

        String url = "https://localhost:" + port
                + "/barcodes/create/ean8/1234576";

        TestRestTemplate restTemplate = createTemplate( cert_policy );
        assertNotNull( restTemplate );

        ResponseEntity<String> response =
                restTemplate.getForEntity( url, String.class );

        assertEquals( HttpStatus.OK, response.getStatusCode());
    }


    @Test
    public void testCreate_UPCE()
            throws JSONException, JsonProcessingException {

        String url = "https://localhost:" + port
                + "/barcodes/create/upce/1234567";

        TestRestTemplate restTemplate = createTemplate( cert_policy );
        assertNotNull( restTemplate );

        ResponseEntity<String> response =
                restTemplate.getForEntity( url, String.class );

        assertEquals( HttpStatus.OK, response.getStatusCode());
    }


    @Test
    public void testCreate_UPCE_BadRequest()
            throws JSONException, JsonProcessingException {

        String url = "https://localhost:" + port
                + "/barcodes/create/upce/23451677";

        TestRestTemplate restTemplate = createTemplate( cert_policy );
        assertNotNull( restTemplate );

        ResponseEntity<String> response =
                restTemplate.getForEntity( url, String.class );

        assertEquals( HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


    @Test
    public void testCreate_Code128()
            throws JSONException, JsonProcessingException {

        String barcodeData = "Code_128_test_sample";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType( MediaType.APPLICATION_JSON );
        HttpEntity<String> entity =    // Serialize;
            new HttpEntity<>( om.writeValueAsString( barcodeData ), headers );

        String url = "https://localhost:" +
                port + "/barcodes/create/code128";

        TestRestTemplate restTemplate = createTemplate( cert_policy );
        assertNotNull( restTemplate );

        ResponseEntity<String> response =
                restTemplate.exchange(
                        url, HttpMethod.POST, entity, String.class );

        assertEquals( HttpStatus.OK, response.getStatusCode());
    }


    @Test
    public void testCreate_PDF417()
            throws JSONException, JsonProcessingException {

        String barcodeData = "This is my test sample.\n"
                + "Letters: QWERTY qwerty,\n"
                + "Numbers: 1234567890 // 2021-06-17";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType( MediaType.APPLICATION_JSON );
        HttpEntity<String> entity =    // Serialize;
            new HttpEntity<>( om.writeValueAsString( barcodeData ), headers );

        String url = "https://localhost:" +
                port + "/barcodes/create/pdf417";

        TestRestTemplate restTemplate = createTemplate( cert_policy );
        assertNotNull( restTemplate );

        ResponseEntity<String> response =
                restTemplate.exchange(
                        url, HttpMethod.POST, entity, String.class );

        assertEquals( HttpStatus.OK, response.getStatusCode());
    }


    @Test
    public void testCreate_QR_Code()
            throws JSONException, JsonProcessingException {

        String barcodeData = "This is my test sample.\n"
                + "Letters: QWERTY qwerty,\n"
                + "Numbers: 1234567890 // 2021-06-17";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType( MediaType.APPLICATION_JSON );
        HttpEntity<String> entity =    // Serialize;
            new HttpEntity<>( om.writeValueAsString( barcodeData ), headers );

        String url = "https://localhost:" +
                port + "/barcodes/create/qrcode";

        TestRestTemplate restTemplate = createTemplate( cert_policy );
        assertNotNull( restTemplate );

        ResponseEntity<String> response =
                restTemplate.exchange(
                        url, HttpMethod.POST, entity, String.class );

        assertEquals( HttpStatus.OK, response.getStatusCode());
    }




    //////////////////////////
    //
    // AUX METHODS, NOT TESTS
    //
    //////////////////////////


    ///////////////////////////
    //
    // IN STREAM TO BYTE ARRAY
    //
    ///////////////////////////////////////////////////
    // Moves bytes from an InputStream to a byte array;
    // InputStream.read(byte[], ..) throws IOException
    // and NullPointerException;

    public static byte[] inStreamToByteArray( InputStream in )
            throws IOException, NullPointerException {

        if ( in == null ) {
            throw new NullPointerException(
                "MainControllerRestTests.inStreamToByteArray() - arg is null" );
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buff = new byte[BUFFLEN];
        int len = 0;

        while (( len = in.read( buff, 0, buff.length )) != -1 ) {
            os.write( buff, 0, len );
        }
        os.flush();
        return os.toByteArray();
    }


    //////////////////////
    //
    // READ FILE FROM RES
    //
    ////////////////////////////////////////////////
    // Reads a file from resources and returns data
    // in a byte array (data can be text or binary);

    public static byte[] readFileFromRes( final String filename )
            throws IOException, IllegalArgumentException {
        if ( filename == null || filename.length() == 0 ) {
            throw new IllegalArgumentException(
                    "MainControllerRestTests.readFileFromRes() - bad arg!" );
        }
        ClassLoader classLoader =
                Thread.currentThread().getContextClassLoader();

        InputStream ins = classLoader.getResourceAsStream( filename );
        return inStreamToByteArray( ins );
    }

}

// -END-
