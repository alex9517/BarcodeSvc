// Modified : 2021-Jun-04

//////////////////////////////////////////////
//
// THIS AUXILIARY CLASS IS USED BY SOME TESTS
//
//////////////////////////////////////////////

package proj2.BarcodeSvc;

import java.util.Base64;
import java.io.InputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
// import java.net.URL;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
// import javax.net.ssl.TrustManager;
// import javax.net.ssl.X509TrustManager;
import javax.net.ssl.HostnameVerifier;
// import javax.net.ssl.HttpsURLConnection;
import java.security.cert.X509Certificate;
import java.security.cert.CertificateException;
import java.security.NoSuchAlgorithmException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.KeyStore;

import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
// import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;

import org.json.JSONException;
import javax.net.ssl.SSLPeerUnverifiedException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.boot.test.web.client.TestRestTemplate;


public class TestTemplate {
/*
  NOTE! 'cert_policy' can be:
    "zero" - force client to work without cert validation
             and without hostname verification;

    "self-signed" - force client to trust a self-signed
                    cert, but skip hostname verification;

    "default" - don't bother about it, because we have
                a real cert signed by a trusted CA;
*/
    private static KeyStore getStore(
            final String storeFileName, final char[] password )
                    throws KeyStoreException, IOException,
                            CertificateException, NoSuchAlgorithmException {

        // NOTE! When cert_policy is set to "zero" or
        // "self-signed", this method is never called.

        final KeyStore store = KeyStore.getInstance( "pkcs12" );
            // Can also be "jks";

        //URL url = getClass().getClassLoader().getResource( storeFileName );
        //InputStream inputStream = url.openStream();

        ClassLoader classLoader =
                Thread.currentThread().getContextClassLoader();
        InputStream ins = classLoader.getResourceAsStream( storeFileName );

        try {
            store.load( ins, password );
        } finally {
            ins.close();
        }
        return store;
    }


    ///////////////////
    //
    // CREATE HEADERS
    //
    ////////////////////////////////////////

    // This is for "basic" auth;

    public static HttpHeaders createHeaders(
            final String username, final String password ) {
        String plainText = username + ":" + password;
        Base64.Encoder enc = Base64.getEncoder();
        String encodedText = enc.encodeToString(
                plainText.getBytes( StandardCharsets.UTF_8 ));
        String authHeader = "Basic " + encodedText;

        return new HttpHeaders() {{
                set( HttpHeaders.AUTHORIZATION, authHeader );
        }};
    }


    // This is for "Bearer" + JWT;

    public static HttpHeaders createHeadersWithJwt( final String token ) {
        //Base64.Encoder enc = Base64.getEncoder();
        //String encodedText = enc.encodeToString(
        //        token.getBytes( StandardCharsets.UTF_8 ));
        // String authHeader = "Bearer " + encodedText;
        String authHeader = "Bearer " + token;
        return new HttpHeaders() {{
                set( HttpHeaders.AUTHORIZATION, authHeader );
        }};
    }


    /////////////////////////////
    //
    // CREATE TEST REST TEMPLATE
    //
    //////////////////////////////////////////////

    public static TestRestTemplate createTemplate(
            final String cert_policy ) {

        try {

            if ( cert_policy.equals( "zero" )) {
                    // Skip certificate validation;

                TrustStrategy acceptingTrustStrategy =
                    ( X509Certificate[] chain, String authType ) -> true;

                SSLContext sslContext =
                    org.apache.http.ssl.SSLContexts.custom()
                        .loadTrustMaterial( null, acceptingTrustStrategy )
                        .build();

                // SSLConnectionSocketFactory csf =
                //         new SSLConnectionSocketFactory(
                //             sslContext, new DefaultHostnameVerifier());

                // Skip hostname verification;

                SSLConnectionSocketFactory csf =
                    new SSLConnectionSocketFactory( sslContext,
                        new HostnameVerifier() {
                            public boolean verify(
                                    String hostname, SSLSession session ) {
                                return true;
                            }
                        });

                CloseableHttpClient httpClient =
                        HttpClients.custom()
                            .setSSLSocketFactory( csf )
                            .build();

                TestRestTemplate restTemplate = new TestRestTemplate();

                HttpComponentsClientHttpRequestFactory requestFactory =
                    ( HttpComponentsClientHttpRequestFactory ) restTemplate
                        .getRestTemplate()
                        .getRequestFactory();

                requestFactory.setHttpClient( httpClient );
                return restTemplate;
            }

            if ( cert_policy.equals( "self-signed" )) {

                SSLContext sslContext =
                    org.apache.http.ssl.SSLContexts.custom()
                        .loadTrustMaterial( null,
                                new TrustSelfSignedStrategy())
                        .build();

                // Skip hostname verification;

                SSLConnectionSocketFactory csf =
                    new SSLConnectionSocketFactory( sslContext,
                        new HostnameVerifier() {
                            public boolean verify(
                                    String hostname, SSLSession session ) {
                                return true;
                            }
                        });

                CloseableHttpClient httpClient = HttpClients.custom()
                    .setSSLSocketFactory( csf )
                    .build();

                // HttpClient httpClient = HttpClients.custom()
                //     .setSSLSocketFactory( csf )
                //     .build();

                TestRestTemplate restTemplate = new TestRestTemplate();

                (( HttpComponentsClientHttpRequestFactory ) restTemplate
                        .getRestTemplate()
                        .getRequestFactory())
                        .setHttpClient( httpClient );

                return restTemplate;
            }
            // SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
            //     .loadTrustMaterial( getStore( "keystore.p12", pass ),
            //         new TrustSelfSignedStrategy())
            //     .build();

            TestRestTemplate restTemplate = new TestRestTemplate();
            return restTemplate;
        }
        catch( NoSuchAlgorithmException |
                KeyStoreException | KeyManagementException e ) {
            // Do nothing;
        }
        return null;
    }

}

// -END-
