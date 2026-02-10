package gr.aueb.cf.schoolapp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record TeacherEditDTO(

        @NotNull
        UUID uuid,
//        @NotNull(message = "Το όνομα δεν μπορεί να είναι κενό")
//        @Size(min = 2, message = "Το όνομα πρέπει να περιέχει τουλάχιστον δύο χαρακτήρες.")
        @NotNull
        @Size(min = 2)
        String firstname,

//        @NotNull(message = "Το επώνυμο δεν μπορεί να είναι κενό")
//        @Size(min = 2, message = "Το επώνυμο πρέπει να περιέχει τουλάχιστον δύο χαρακτήρες.")
        @NotNull
        @Size(min = 2)
        String lastname,

//        @Pattern(regexp = "\\d{9,}", message = "Το ΑΦΜ δεν μπορεί να είναι μικρότερο από εννέα ψηφία.")
        @Pattern(regexp = "\\d{9,}")
        String vat,

//        @NotNull(message = "Η περιοχή δεν μπορεί να είναι κενή.")
        @NotNull
        Long regionId
) {
    public static TeacherEditDTO empty() {
        return new TeacherEditDTO(null, "", "", "", 0L);
    }
}
