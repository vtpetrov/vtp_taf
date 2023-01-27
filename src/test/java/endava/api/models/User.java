package endava.api.models;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import utils.JsonUtils;

import java.util.Comparator;

@Data
public class User {

    Integer id;
    String email;
    @SerializedName("first_name")
    String firstName;
    @SerializedName("last_name")
    String lastName;
    String avatar;
//    ------------ fields used for POST:

    String name;
    String job;


//    String sampleResponse =
//            """
//                        {
//                        "id": 1,
//                        "email": "george.bluth@reqres.in",
//                        "first_name": "George",
//                        "last_name": "Bluth",
//                        "avatar": "https://reqres.in/img/faces/1-image.jpg"
//                    }
//                        """;



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
        User user = new User();
        user.setName(name);
        user.setJob(job);
        String jsonStringFromObject = JsonUtils.getJsonStringFromObject(user);

        return jsonStringFromObject;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }

    public static class ComparatorByFirstName implements Comparator<User> {
        public int compare(User user1, User user2) {
            return user1.getFirstName().compareToIgnoreCase(user2.getFirstName());
        }
    }

}
