package google.firestore.models;

import java.util.List;

public record TranslationInformation(List<String> details) {
    public List<String> getDetails() {
        return details;
    }

}
