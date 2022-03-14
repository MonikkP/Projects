package domain.validators;

public interface Validator<T> {
    /**
     *
     * @param entity that will be validate
     * @throws ValidationException if the entity is not valid
     */
    void validate(T entity) throws ValidationException;
}