//
//  Created : 2021-Jun-01
// Modified : 2021-Jun-10
//
// Description :
//   Properties loaded from application*.properties or application.yml;

// When using @ConstructorBinding (supported since SB 2.2), we need to
// provide the constructor with all the parameters we'd like to bind.
//
// Note that all the fields of AppProperties can be final, and in this case
// there should be no setter methods.
//
// It's important to emphasize that to use the constructor binding,
// we need to explicitly enable our configuration class either with
// @EnableConfigurationProperties or with @ConfigurationPropertiesScan.
// In this case @EnableConfigurationProperties is specified in
// 'XxxApplication.java' file.

package proj2.BarcodeSvc;

import java.util.List;
import java.util.Arrays;

import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;


@Validated
@ConstructorBinding
@ConfigurationProperties("app")
public class AppProperties {

    private static final List<String> ALLOWED_ORIGINS = Arrays.asList(
            "http://localhost",
            "http://localhost:3000" );

    private static final List<String> ALLOWED_METHODS = Arrays.asList(
            "GET", "PUT", "POST", "DELETE", "OPTIONS", "HEAD" );

    @Autowired
    private Environment env;

    private final List<String> corsAllowedOrigins;
    private final List<String> corsAllowedMethods;


    public AppProperties(
            List<String> corsAllowedOrigins,
            List<String> corsAllowedMethods ) {

        // I failed to find how to specify @DefaultValue
        // for list: it wants String, I need List<String>.
        // So, this is my dirty way (looks bad but works).

        if ( corsAllowedOrigins == null || corsAllowedOrigins.isEmpty()) {
            this.corsAllowedOrigins = ALLOWED_ORIGINS;
        } else {
            this.corsAllowedOrigins = corsAllowedOrigins;
        }

        if ( corsAllowedMethods == null || corsAllowedMethods.isEmpty()) {
            this.corsAllowedMethods = ALLOWED_METHODS;
        } else {
            this.corsAllowedMethods = corsAllowedMethods;
        }
    }


    public List<String> getCorsAllowedOrigins() {
        return corsAllowedOrigins;
    }


    public List<String> getCorsAllowedMethods() {
        return corsAllowedMethods;
    }


    public String getJwkSetUri() {
        return env.getProperty(
                "spring.security.oauth2.resourceserver.jwt.jwk-set-uri", "" );
    }

}

// -END-
