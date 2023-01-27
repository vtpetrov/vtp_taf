package endava.api.models;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.Comparator;

@Data
public class UserGet {

    Integer id;
    String email;
    @SerializedName("first_name")
    String firstName;
    @SerializedName("last_name")
    String lastName;
    String avatar;


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

    public static class ComparatorByFirstName implements Comparator<UserGet> {
        public int compare(UserGet user1, UserGet user2) {
            return user1.getFirstName().compareToIgnoreCase(user2.getFirstName());
        }
    }

}
