package vendas.validation.constraintvalidation;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import vendas.validation.NotEmptyList;

public class NotEmptyListValidator implements ConstraintValidator<NotEmptyList, List> {

	@Override
	public boolean isValid(List list, ConstraintValidatorContext constraintValidatorContext) {
		// TODO Auto-generated method stub
		return list != null && !list.isEmpty();
	}

	@Override
	public void initialize(NotEmptyList constraintAnnotation) {
		// TODO Auto-generated method stub
	}

}
