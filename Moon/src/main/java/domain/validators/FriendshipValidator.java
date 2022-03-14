package domain.validators;

import domain.Friendship;

public class FriendshipValidator implements Validator<Friendship> {
    @Override
    public void validate(Friendship entity) throws ValidationException {

        if(entity.getId().getId1()<0 || entity.getId().getId2()<0)

            throw new ValidationException("Id invalid!\n");

    }
}
