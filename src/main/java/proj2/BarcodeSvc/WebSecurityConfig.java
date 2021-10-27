//
//  Created : 2021-Jun-01
// Modified : 2021-Jun-10
//
//////////////////////////////
//
//    WEB SECURITY CONFIG
//
//////////////////////////////

package proj2.BarcodeSvc;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsConfiguration;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.http.HttpMethod;

// import static org.springframework.security.config.Customizer.withDefaults;

// DO NOT REMOVE LOGGER! You may need it later!

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

import proj2.BarcodeSvc.AppProperties;


@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    // DO NOT REMOVE LOGGER! You may need it later!

    // private static final Logger log =
    //         LoggerFactory.getLogger( WebSecurityConfig.class );

    private AppProperties props;

    @Autowired
    public WebSecurityConfig( AppProperties props ) {
        this.props = props;
    }


    @Override
    protected void configure( HttpSecurity http ) throws Exception {

        http.cors()  // CORS must be set before authorize!
            .and()
            .csrf().disable()
            .sessionManagement()
            .sessionCreationPolicy( SessionCreationPolicy.STATELESS )
            .and()
            .authorizeRequests( authz -> authz
                .mvcMatchers( "/barcodes/**" ).permitAll()
                .mvcMatchers( "/actuator/health" ).permitAll()
                .mvcMatchers( "/actuator/**" ).authenticated())
            .oauth2ResourceServer( OAuth2ResourceServerConfigurer::jwt );
    }


    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri( props.getJwkSetUri()).build();
    }


    // The following code works with Spring Boot 2.4.0 / 2.5.0;

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins( props.getCorsAllowedOrigins());
        config.setAllowedMethods( props.getCorsAllowedMethods());

        config.setAllowedHeaders( Arrays.asList(
                "Origin",
                "Authorization",
                "Content-Type" ));
        config.setExposedHeaders( Arrays.asList(
                "Access-Control-Allow-Origin",
                "Access-Control-Allow-Methods",
                "Access-Control-Allow-Headers" ));

        // Origin, X-Requested-With, Content-Type, Accept

        config.setMaxAge( 600L );
            // Configure how long (sec) the response from
            // a pre-flight request can be cached by clients.
            // By default this is not set.

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration( "/**", config );

        return source;
    }

}

// -END-
