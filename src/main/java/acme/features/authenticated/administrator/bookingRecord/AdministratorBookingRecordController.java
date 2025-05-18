package acme.features.authenticated.administrator.bookingRecord;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Administrator;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.bookings.BookingRecord;

@GuiController
public class AdministratorBookingRecordController extends AbstractGuiController<Administrator, BookingRecord> {

    @Autowired
    private AdministratorBookingRecordShowService showService;

    @Autowired
    private AdministratorBookingRecordListService listService;

    @PostConstruct
    protected void initialise() {
        super.addBasicCommand("list", this.listService);
        super.addBasicCommand("show", this.showService);
    }
}
