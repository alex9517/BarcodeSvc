//  Created : 2021-Jun-01
// Modified : 2021-Jun-04
//
// Description :
//  This is a RESTful web-service using Spring Boot framework.
//  It allows to create and read/decode the following types of barcodes:
//    EAN13, UPC-A, Code 128, PDF417, QR Code

package proj2.BarcodeSvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;

import java.awt.image.BufferedImage;


@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class BarcodeSvcApplication {

    public static void main( String[] args ) {
        SpringApplication.run( BarcodeSvcApplication.class, args );
    }


    @Bean
    public HttpMessageConverter<BufferedImage> createImageHttpMessageConverter() {
        return new BufferedImageHttpMessageConverter();
    }

}

// -END-
