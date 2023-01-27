package endava.api.models;

import lombok.Data;
import utils.JsonUtils;

@Data
public class UserCreate {


    String name;
    String job;
    String id;
    String createdAt;

    /**
     * POST request format:
     *     <pre>
     *     {
     *         "name": "morpheus",
     *          "job": "leader"
     *     }
     *     </pre>
     * @param name the value to put inside 'name' json property
     * @param job the value to put inside 'job' json property
     * @return the generated json
     */
    public static String generateUserCreateBody(String name, String job) {
        UserCreate user = new UserCreate();
        user.setName(name);
        user.setJob(job);
        String jsonStringFromObject = JsonUtils.getJsonStringFromObject(user);

        return jsonStringFromObject;
    }

    @Override
    public String toString() {
        return "UserCreate{" +
                "name='" + name + '\'' +
                ", job='" + job + '\'' +
                ", id='" + id + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }

}
