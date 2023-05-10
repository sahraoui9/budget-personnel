package bgpersonnel.budget.exeception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessageResponse {
    private String message = "Error";
    private LocalDateTime dateTimeError = LocalDateTime.now();
    private String description;

    public ErrorMessageResponse(String description) {
        this.description = description;
    }

}
