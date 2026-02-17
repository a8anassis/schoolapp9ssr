package gr.aueb.cf.schoolapp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserInsertDTO(

        @NotNull(message = "To username δεν μπορεί να είναι null.")
        @Size(min = 2, max = 20, message = "Το username πρέπει να είναι μεταξύ 2-20 χαρακτήρες.")
        String username,

        @NotNull(message = "To password δεν μπορεί να είναι null.")
        @Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])^.{8,}$",
                message = "Το password πρέπει να περιέχει τουλάχιστον 1 πεζό, 1 κεφαλαίο, 1 ψηφίο, και 1 ειδικό χαρακτήρα χωρίς κενά")
        String password,

        @NotNull(message = "Ο ρόλος δεν μπορεί να είναι κενός.")
        Long roleId
) {
    public static UserInsertDTO empty() {
        return new UserInsertDTO("", "", 0L);
    }
}
