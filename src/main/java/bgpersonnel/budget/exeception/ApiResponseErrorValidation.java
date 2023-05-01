package bgpersonnel.budget.exeception;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class ApiResponseErrorValidation<T> {

    private T data;

    public ApiResponseErrorValidation(T data) {
        this.data = data;
    }

}