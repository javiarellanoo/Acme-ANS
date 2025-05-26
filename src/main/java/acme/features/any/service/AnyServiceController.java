
package acme.features.any.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Any;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.services.Service;

@GuiController
public class AnyServiceController extends AbstractGuiController<Any, Service> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyServiceListService	listService;

	@Autowired
	private AnyServiceShowService	showService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
	}
}
