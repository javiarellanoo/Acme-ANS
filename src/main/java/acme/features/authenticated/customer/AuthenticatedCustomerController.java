package acme.features.authenticated.customer;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Authenticated;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.realms.Customer;

@GuiController
public class AuthenticatedCustomerController extends AbstractGuiController<Authenticated, Customer> {

    @Autowired
    private AuthenticatedCustomerCreateService createService;

    @Autowired
    private AuthenticatedCustomerUpdateService updateService;

    @PostConstruct
    protected void initialise() {
        // Task 118
        super.addBasicCommand("create", this.createService);
        
        // Task 119
        super.addBasicCommand("update", this.updateService);
    }
}
