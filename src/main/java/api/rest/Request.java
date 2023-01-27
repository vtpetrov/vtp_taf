package api.rest;

import lombok.Data;
import utils.JsonUtils;
import utils.Misc;

import java.util.HashMap;
import java.util.Map;

@Data
public class Request {
    /**
     * The request path not including the base url
     */
    private String path;
    private Map<String, Object> params;
    /**
     * Use the setter on this field only if you want to replace all headers!
     */
    private Map<String, String> headers;
    private Map<String, String> queryParams;
    private Map<String, String> formParams;
    /**
     * Sets path parameters map
     * <br/> Map with path params key=paramID, value=paramValue
     */
    private Map<String, String> pathParams;
    private String body;
    private String contentType;
    private String version;
    private String verb;

    public Request() {
        path = "";
        body = "";
        version = "";
        verb = "";
        params = new HashMap<>();
        headers = new HashMap<>();
        queryParams = new HashMap<>();
        formParams = new HashMap<>();
        pathParams = new HashMap<>();
    }

    public Request(String path, Map<String, Object> params, Map<String, String> headers, Map<String, String> queryParams, Map<String, String> formParams, Map<String, String> pathParams, String body, String contentType, String version, String verb) {
        this.path = path;
        this.params = params;
        this.headers = headers;
        this.queryParams = queryParams;
        this.formParams = formParams;
        this.pathParams = pathParams;
        this.body = body;
        this.contentType = contentType;
        this.version = version;
        this.verb = verb;
    }

    public Request(Request requestParam) {
        this.path = requestParam.path;
        this.params = new HashMap<>(requestParam.params);
        this.headers = new HashMap<>(requestParam.headers);
        this.queryParams = new HashMap<>(requestParam.queryParams);
        this.formParams = new HashMap<>(requestParam.formParams);
        this.pathParams = new HashMap<>(requestParam.pathParams);
        this.body = requestParam.body;
        this.contentType = requestParam.contentType;
        this.version = requestParam.version;
        this.verb = requestParam.verb;
    }

    public static Request generate(String body, String path) {
        Request request = new Request();
        request.getHeaders().put("X-Client-Id", "rms-ui");
        request.setContentType("application/json");
        request.setBody(body);
        request.setPath(path);
        return request;
    }

    /**
     * Sets the version of the api that will be used for the current request
     * <p>
     * This is needed because for some apis there are multiple versions for the same endpoint. Set the version to an
     * empty string if it is not the case for the api you are testing.
     *
     * @param version the version as string
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Adds the passed value to the Authorization header.
     * <p>
     * Don't use this if you are adding the Authorization header using getHeaders().put()
     *
     * @param authString the token as string
     */
    public void addAuthorization(String authString) {
        headers.put("Authorization", authString);
    }

    /**
     * Removes the Authorization header
     */
    public void deleteAuthorization() {
        headers.remove("Authorization");
    }

    /**
     * Clears the request
     * <p>
     * Clears the headers, query params, path params, path and body
     */
    public void clear() {
        path = "";
        params.clear();
        headers.clear();
        queryParams.clear();
        formParams.clear();
        body = "";
    }

    @Override
    public String toString() {
        return String.format("---- Request ----\n" +
                        "Method(verb): %s\n" +
                        "Path:         %s\n" +
                        "Content-Type: %s\n" +
                        "Headers:      %s\n" +
                        "Body:\n%s\n" +
                        "Params:       %s\n" +
                        "Path parameters:%s\n" +
                        "QueryParams:  %s\n" +
                        "Version:      %s\n" +
                        "--------------------------------------------------------------",
                verb,
                path,
                contentType,
                Misc.prettyPrintMap(headers),
                (contentType != null ? contentType : "null")
                        .contains("application/json") ? JsonUtils.prettyPrintJson(body) : body,
                Misc.prettyPrintMap(params),
                Misc.prettyPrintMap(pathParams),
                Misc.prettyPrintMap(queryParams),
                version);
    }
}
