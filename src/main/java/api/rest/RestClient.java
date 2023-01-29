package api.rest;

import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.response.ExtractableResponse;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Misc;

import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;

public class RestClient {
    private static final Logger logger = LoggerFactory.getLogger(RestClient.class.getSimpleName());
    private static boolean automaticRedirects = false;
    private static Response lastResponse;
    private static Request lastRequest;

    public RestClient() {
    }

    /**
     * Sends the current request using PUT
     *
     * @param request the current request
     * @return the response received
     */
    public static Response put(Request request) {
        request.setVerb("PUT");
        logger.info("Sending PUT request {}", request);
        ExtractableResponse<io.restassured.response.Response> response = given()
                .redirects().follow(automaticRedirects)
                .contentType(request.getContentType())
                .headers(request.getHeaders())
                .queryParams(request.getQueryParams())
                .pathParams(request.getPathParams())
                .body(request.getBody())
                .log().all()
                .put(request.getVersion() + request.getPath())
                .then()
                .log().all()
                .extract();
        lastResponse = new Response(response.statusCode(), response.body().asString(), response.headers());
        lastRequest = new Request(request);

        logger.info("Received response {}", lastResponse);
        return lastResponse;
    }

    /**
     * Sends the current request using GET
     *
     * @param request the current request
     * @return the response received
     */
    public static Response get(Request request) {
        request.setVerb("GET");
        logger.info("Sending GET request {}", request);
        ExtractableResponse<io.restassured.response.Response> response = given()
                .redirects().follow(automaticRedirects)
                .contentType(request.getContentType())
                .headers(request.getHeaders())
                .queryParams(request.getQueryParams())
                .pathParams(request.getPathParams())
                .log().all()
                .get(request.getVersion() + request.getPath())
                .then()
//                .log().all()
                .extract();
        lastResponse = new Response(response.statusCode(), response.body().asString(), response.headers());
        lastRequest = new Request(request);
        logger.info("Received response {}", lastResponse);
        return lastResponse;
    }

    /**
     * Sends the current request using POST
     * @param request the current request
     * @return the response received
     */
    public static Response post(Request request) {
        request.setVerb("POST");
        logger.info("Sending POST request {}", request);
        RequestSpecification rs = given()
                .redirects().follow(automaticRedirects)
                .contentType(request.getContentType())
                .headers(request.getHeaders());

        if (!request.getFormParams().isEmpty()) {
            //use form params and not body;
            rs.formParams(request.getFormParams());
        } else if (!request.getParams().isEmpty()) {
            //encode the params map and send it in the request body
            rs.body(Misc.mapToUrlEncodedString(request.getParams()));
        } else {
            // send just the body, usually used when the content-type is application/json
            rs.body(request.getBody());
        }

        rs
                .queryParams(request.getQueryParams())
                .pathParams(request.getPathParams())
                .log().all();


        ExtractableResponse<io.restassured.response.Response> response = rs
                .post(request.getVersion() + request.getPath())
                .then()
                .log().all()
                .extract();

        lastResponse = new Response(response.statusCode(), response.body().asString(), response.headers());
        lastRequest = new Request(request);
        logger.info("Received response {}", lastResponse);
        return lastResponse;
    }

    /**
     * Sends the current request using DELETE
     * @param request the current request
     * @return the response received
     */
    public static Response delete(Request request) {
        request.setVerb("DELETE");
        logger.info("Sending DELETE request {}", request);
        ExtractableResponse<io.restassured.response.Response> response = given()
                .redirects().follow(automaticRedirects)
                .contentType(request.getContentType())
                .headers(request.getHeaders())
                .log().all()
                .delete(request.getVersion() + request.getPath())
                .then()
                .log().all()
                .extract();
        lastResponse = new Response(response.statusCode(), response.body().asString(), response.headers());
        lastRequest = new Request(request);
        logger.info("Received response {}", lastResponse);
        return lastResponse;
    }

    /**
     * Sends the current request using HEAD
     * @param request the current request
     * @return the response received
     */
    public static Response head(Request request) {
        request.setVerb("HEAD");
        logger.info("Sending HEAD request {}", request);
        ExtractableResponse<io.restassured.response.Response> response = given()
                .redirects().follow(automaticRedirects)
                .contentType(request.getContentType())
                .headers(request.getHeaders())
                .log().all()
                .head(request.getVersion() + request.getPath())
                .then()
                .log().all()
                .extract();
        lastResponse = new Response(response.statusCode(), response.body().asString(), response.headers());
        lastRequest = new Request(request);
        logger.info("Received response {}", lastResponse);
        return lastResponse;
    }

    /**
     * Sets the default encoding used in all the requests
     *
     * @param charset the encoding
     */
    public static void setDefaultEncoding(String charset) {
        logger.info("Setting default encoding to {}", charset);
        config = config().encoderConfig(EncoderConfig.encoderConfig().defaultContentCharset(charset).
                appendDefaultContentCharsetToContentTypeIfUndefined(false));
    }

    /**
     * Sets the base URI that will be used by all the http calls.
     * This should be the base URI of the api
     *
     * @param baseURI the base url
     */
    public static void setBaseURI(String baseURI) {
        logger.info("Setting baseURI to {}", baseURI);
        RestAssured.baseURI = baseURI;
    }

    /**
     * Whether RestClient should automatically append the content charset to the content-type header if not defined explicitly
     *
     * @param value value to be set
     */
    public static void appendDefaultContentCharsetToContentTypeIfUndefined(boolean value) {
        config = config().encoderConfig(EncoderConfig.encoderConfig().
                appendDefaultContentCharsetToContentTypeIfUndefined(value));
    }

    /**
     * Set to true if you want the http calls to follow redirects (not recommended for testing)
     *
     * @param value value to be set
     */
    public static void setAutomaticRedirects(boolean value) {
        automaticRedirects = value;
    }

    public static Response getLastResponse() {
        return lastResponse;
    }

    public static Request getLastRequest() {
        return lastRequest;
    }
}