package bgpersonnel.budget.exeception;

import lombok.Getter;

@Getter
public class ApiResponseErrorValidation<T> {

    private T data;

    public ApiResponseErrorValidation(T data) {
        this.data = data;
    }

}