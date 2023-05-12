package bgpersonnel.budget.exeception;


import lombok.Getter;

import java.time.LocalDateTime;


@Getter
public class ErrorMessageResponse {
    private String message = "Error";
    private LocalDateTime dateTimeError = LocalDateTime.now();
    private String description;

    public ErrorMessageResponse(String description) {
        this.description = description;
    }

}
