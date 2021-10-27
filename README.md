# BarcodeSvc
is a RESTful web-service allowing to read and create barcodes.

Technically, this is **Java + Spring Boot** application using
[Zxing](https://github.com/zxing/zxing) and
[Barcode4j](http://barcode4j.sourceforge.net) libraries to perform the main
job.

The current app version was built with OpenJDK Java 11 and Spring Boot 2.5.0.

Since it depends on **Zxing**, it is supposed to decode all types of barcodes
recognized by this library. The **Zxing** is also used to create QR Codes,
while **Barcode4j** is used to create EAN-13, EAN-8, UPC-A, UPC-E, Code 128,
PDF417. Some other types can be created too, if they are supported by those
libraries, and if you're ready to create some additional code.

The quality of decoding is exactly what Zxing provides with
`DecodeHintType.TRY_HARDER` set to `Boolean.True`, i.e. optimized for
quality, not for speed.


### Client app
Actually, this is not even the part of the project, this is just a simplest
**demo client** that can be used to try/test *BarcodeSvc*, and also
demonstrates how to interract with the service.

Technically, it's just a bunch of HTML files using CSS, JavaScript, jQuery.

**Important!** You cannot just open this client's `index.html` in your browser
and send requests to web-service, because if your browser is not very old, and
if it is configured right, then the **CORS** security mechanism is going to
block any HTTP/HTTPS requests.

To make it work, you should move all client's files [preserving dir tree
structure] to some web-server, e.g. locally running Apache. And probably you
should check `src/main/resources/application.yml` settings related to CORS.

To read/decode a barcode, you have to provide a PNG or JPEG image file of that
barcode. For simplicity, this client uploads image files from the local
filesystem. Also, when you create a barcode, you get HTML page with a picture
that is supposed to be saved using the standard browser capabilities.

There are some other inconveniencies:
- You have to edit `index.html` and set `BASE_URL`. It must be something like
`https://*your_hostname*:8443`. Note that switching to HTTP would require
some code editing.
- Web-service is configured to use HTTPS with a self-signed certificate. It
means that at some step you must create that cert (see below) and force your
browser to accept it.

I know, all these problems with CORS and HTTPS are boring and irritating,
but this is a modern trend. We all should care about security nowdays.


### Certificate
HTTPS won't do without a certificate that proves server's identity.
Unless you have a **real certificate** (this case is not considered here),
you must create your own self-signed certificate bound to your server. The
`src/main/resources/keystore.p12` that comes with this project is of no use
because it was created for a specific host. Consider it a placeholder.

The self-signed certificate creation procedure is a separate topic, but,
to save your time, here is a short memo.

You can use `keytool` (which is part of JDK), or you can use  `openssl` (which
is part of a typical Linux distro).

**Keystore** is keystore because it contains **certificate** and related
**private key**. Keystore type can be **JKS** (Java Key Store), or, "more
standard and universal" **PKCS12**.

First, prepare a good password for your keystore. Password is required because
private key is always a *secret thing*.

Second, decide how are you going to set `CN` (Common name). It must be FQDN or
just a hostname, but it must be exactly what you are going to specify in HTTPS
requests. **Note** that if `keytool` prompts you for `First and Last Name`, it
probably means `CN`.

Other fields are less important unless you want to get a "good looking"
certificate.

Assuming that JDK is installed, run
```
keytool -genkey -alias cert01 -storetype PKCS12 -keyalg RSA -keysize 2048
        -keystore keystore.p12 -validity 1827```
and carefully answer those questions.

Actually, `-alias` should be more informative. The `-validity 1827` is
approx 5 years. It may be good for development, but unsafe for production.
RSA `-keysize 2048` is a minimal safe length; once again, it's good for
development, but for production you should probably select 3072 or 4096.

If you think that self-signed certificates are only appropriate for
development, I think you're wrong. But this is a separate discussion...



### About application.yml
The config file `src/main/resources/application.yml` has two usual profiles:
`dev` and `prod`. There is no big difference between them [in this project],
except the development profile can generate large log files.

The following properties are installation specific:

- `app.cors-allowed-origins` - depends on how and where you install your
client app. If browser signals CORS-related errors, pay attention to this
property.
- `server.port` - if you decide to change it, do it before you force browser
to accept your self-signed cert, because browser registers both FQDN and port.
- `server.ssl.key-store-*` - these properties depend on you choices of keystore
format, location, etc.
- `spring.security.oauth2.resourceserver.jwt.issuer-uri` - this is probably not
required at all.
- `spring.security.oauth2.resourceserver.jwt.jwk-set-uri` - this is only
required if you want to access **Actuator**'s endpoints (see explanation below).



### Authorization
is **not required** to read or create the barcodes, and you can just ignore
the rest of this chapter.

To be more specific, everybody can access `/barcodes/**` or `/actuator/health`
endpoints without any credentials.

Authorization is only required if you want to access `/actuator/metrics` and
some other Actuator-related stuff.

For this case, BarcodeSvc is configured as a **Resource server**, and it relies
on **Authorization** server which is supposed to authenticate users and decide
if they are allowed to access protected endpoints.

I use a stand-alone **Keycloak** server, but the only thing that is related to
*keycloak* is `*.jwt.jwk-set-uri` property. Probably, some other Authorization
server will be good too, if it supports OIDC (Open ID Connect) and issues JWTs
(Java Web Token) which can be verified with a public key available through
`jwk-set-uri`.



### Additional bash scripts
They are not directly related to the project, and they are only good for Linux
systems. Windows users can create something similar, or use those auto created
`mvnw` (I never used, TL;DR).

My scripts are quite trivial wrappers automating routine procedures:
- `runBarcodeSvc.sh` sets miscellaneous Java/Spring options, some environment
variables and then starts application. It saves you from necessity to type long
command and allows to change some properties without editing `application.yml`.
I usually copy app's executable TAR and `runBarcodeSvc.sh` to `$HOME/bin`, but
you can do whatever you like.
- `runBuild.sh` sets environment variable providing keystore password, then it
executes `mvn package` to build app. The password is required to pass tests.
The idea is to put this password in this script in the morning and remove it
at the end of the workday. Of course, you can put it in `application.yml`, or
you can use your own favorite approach (whatever it is).
