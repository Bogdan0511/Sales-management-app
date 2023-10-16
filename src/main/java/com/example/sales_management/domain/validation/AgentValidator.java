package com.example.sales_management.domain.validation;

import com.example.sales_management.domain.Agent;
import com.example.sales_management.exceptions.ValidationException;

public class AgentValidator implements Validator<Agent> {
    @Override
    public void validate(Agent agent) throws ValidationException {
        String message = "";
        if (agent.getUsername() == null || agent.getUsername().trim().length() == 0) {
            message += "Username can not be empty!\n";
        }
        if (agent.getPassword() == null || agent.getPassword().trim().length() == 0) {
            message += "Password can not be empty!\n";
        }
        if (message.length() > 0) {
            throw new ValidationException(message);
        }
    }
}
