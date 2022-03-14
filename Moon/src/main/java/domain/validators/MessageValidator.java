package domain.validators;

import domain.Message;

import java.util.ArrayList;

public class MessageValidator implements Validator<Message>{
    @Override
    public void validate(Message entity) throws ValidationException {
        ArrayList<String> errors=new ArrayList<>();
        if(entity.getMessage()=="")
            errors.add("Null message!\n");

        if(errors.size()>0)
            for (String e:errors)
                throw new ValidationException(e);
    }
    }

