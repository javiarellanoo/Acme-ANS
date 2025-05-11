package acme.features.authenticated.customer;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.components.principals.UserAccount;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.realms.Customer;

@GuiService
public class AuthenticatedCustomerCreateService extends AbstractGuiService<Authenticated, Customer> {

    @Autowired
    private AuthenticatedCustomerRepository repository;

    @Override
    public void authorise() {
        boolean status;
        status = !super.getRequest().getPrincipal().hasRealmOfType(Customer.class);
        super.getResponse().setAuthorised(status);
    }

    @Override
    public void load() {
        Customer customer;
        int userAccountId;
        UserAccount userAccount;

        userAccountId = super.getRequest().getPrincipal().getAccountId();
        userAccount = this.repository.findUserAccountById(userAccountId);

        customer = new Customer();
        customer.setUserAccount(userAccount);

        super.getBuffer().addData(customer);
    }

    @Override
    public void bind(final Customer customer) {
        super.bindObject(customer, "identifier", "phoneNumber", "address", "city", "country", "earnedPoints");
    }

    @Override
    public void validate(final Customer customer) {
        // Add custom validation if needed
    }

    @Override
    public void perform(final Customer customer) {
        this.repository.save(customer);
    }

    @Override
    public void unbind(final Customer customer) {
        Dataset dataset;
        dataset = super.unbindObject(customer, "identifier", "phoneNumber", "address", "city", "country",
                "earnedPoints");
        super.getResponse().addData(dataset);
    }

    @Override
    public void onSuccess() {
        if (super.getRequest().getMethod().equals("POST"))
            PrincipalHelper.handleUpdate();
    }
}
