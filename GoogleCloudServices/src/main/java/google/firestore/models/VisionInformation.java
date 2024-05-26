package google.firestore.models;

import java.util.List;

public record VisionInformation(List<String> details) {

    public List<String> getDetails() {
        return details;
    }

}
