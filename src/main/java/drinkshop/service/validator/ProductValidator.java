package drinkshop.service.validator;

import drinkshop.domain.Product;

public class ProductValidator implements Validator<Product> {

    @Override
    public void validate(Product product) {

        String errors = "";

        if (product.getId() <= 0)
            errors += "ID invalid!\n";

        if (product.getNume() == null || product.getNume().isBlank())
            errors += "Numele nu poate fi gol!\n";

        if(product.getNume().length() < 3 ||  product.getNume().length() > 32)
            errors += "Numele trebuie sa fie de lungime in intervalul [3, 32]";

        if (product.getPret() <= 0 || product.getPret() > 20)
            errors += "Pret invalid!\n";

        if (!errors.isEmpty())
            throw new ValidationException(errors);
    }
}
