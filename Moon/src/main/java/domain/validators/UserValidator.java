package domain.validators;


import domain.User;

import java.util.ArrayList;

public class UserValidator implements Validator<User>{
    @Override
    public void validate(User entity) throws ValidationException {
        ArrayList<String> errors=new ArrayList<>();
       /* if(entity.getId()<0 || entity.getId()==null)
            errors.add("Id ivalid!\n");*/
        if(entity.getFirstName()=="" || entity.getLastName()=="")
            errors.add("Nume invalid!\n");
        if(entity.getCountry()=="")
            errors.add("Tara invalida!\n");

        if(errors.size()>0)
            for (String e:errors)
                throw new ValidationException(e);
    }
}
