package model;

import model.Pizza;

public interface ValidationHandler {
    void setNextHandler(ValidationHandler nextHandler);

    boolean validate(Pizza pizza);
}
