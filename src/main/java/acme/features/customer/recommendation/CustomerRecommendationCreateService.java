package acme.features.customer.recommendation;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.recommendations.Recommendation;
import acme.realms.Customer;

@GuiService
public class CustomerRecommendationCreateService extends AbstractGuiService<Customer, Recommendation> {

    @Autowired
    private CustomerRecommendationRepository repository;

    @Override
    public void authorise() {
        super.getResponse().setAuthorised(true);
    }

    @Override
    public void load() {
        Recommendation recommendation = new Recommendation();
        super.getBuffer().addData(recommendation);
    }

    @Override
    public void bind(final Recommendation recommendation) {
        super.bindObject(recommendation, "title", "description", "category", "location", "city");
    }

    @Override
    public void validate(final Recommendation recommendation) {
        // No custom validation
    }

    @Override
    public void perform(final Recommendation recommendation) {
        this.repository.save(recommendation);
    }

    @Override
    public void unbind(final Recommendation recommendation) {
        Dataset dataset = super.unbindObject(recommendation, "title", "description", "category", "location", "city");
        super.getResponse().addData(dataset);
    }
}
