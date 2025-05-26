package acme.features.authenticated.administrator.populator;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.OneToMany;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import acme.client.components.basis.AbstractDatatype;
import acme.client.components.basis.AbstractEntity;
import acme.client.components.models.Errors;
import acme.client.helpers.PropertyHelper;
import acme.client.helpers.StringHelper;
import acme.internals.components.database.DatabaseManager;
import acme.internals.components.database.EntityStore;
import acme.internals.components.database.EntityWeb;
import acme.internals.components.exceptions.PassThroughException;
import acme.internals.helpers.ErrorsHelper;
import acme.internals.helpers.ReflectionHelper;
import acme.internals.helpers.ThrowableHelper;

@Component
public class CustomPopulator {

	// Constructors -----------------------------------------------------------

	protected CustomPopulator() {
	}

	// Internal state ---------------------------------------------------------


	@Autowired
	private DatabaseManager	manager;

	@Autowired
	private Validator		validator;

	@Autowired
	private ResourceLoader	loader;

	// Business methods -------------------------------------------------------


	public void populate(final boolean createSchema, final String descriptor) {
		assert !StringHelper.isBlank(descriptor) && StringHelper.anyOf(descriptor, "none|initial|sample|recommendation");

		String key, entry;
		String[] folders;

		if (descriptor.equals("none"))
			folders = null;
		else {
			key = String.format("acme.population.%s", descriptor);
			entry = PropertyHelper.getRequiredProperty(key, String.class);
			folders = entry.trim().split("\\s*,\\s*");
		}
		this.populate(createSchema, folders);
	}

	// Ancillary methods ------------------------------------------------------

	protected void populate(final boolean createSchema, final String... folders) {
		assert folders == null || !StringHelper.someBlank(folders);

		EntityWeb web;
		Locale currentLocale, englishLocale;

		currentLocale = null;
		try {
			currentLocale = LocaleContextHolder.getLocale();
			englishLocale = Locale.ENGLISH;
			LocaleContextHolder.setLocale(englishLocale);
			{
				// HINT: data must be persisted without any validation.
				this.manager.startTransaction();
				this.manager.setReadUncommittedIsolationLevel();
				if (folders == null)
					web = new EntityWeb();
				else
					web = this.readEntities(folders);
				this.handleSchema(createSchema);
				this.saveEntities(web);
				this.manager.commitTransaction();
			}
			{
				// HINT: validation happens after all data have been persisted.
				// HINT+ Otherwise, cyclic dependencies can't be addressed.
				this.manager.startTransaction();
				this.validate(web);
				this.manager.commitTransaction();
			}
		} catch (final Throwable oops) {
			try {
				if (this.manager.isTransactionActive())
					this.manager.rollbackTransaction();
			} catch (Throwable auch) {
				;
			}
			throw new PassThroughException(oops);
		} finally {
			if (currentLocale != null)
				LocaleContextHolder.setLocale(currentLocale);
		}
	}

	protected EntityWeb readEntities(final String[] folders) {
		EntityWeb result;
		String path;

		result = new EntityWeb();
		for (final String folder : folders) {
			path = String.format("classpath:/WEB-INF/data/%s", folder);
			this.readEntities(result, path);
		}

		return result;
	}

	protected void readEntities(final EntityWeb web, final String path) {
		assert web != null;
		assert !StringHelper.isBlank(path);

		File file;
		Resource resource;
		EntityStore store;

		try {
			resource = this.loader.getResource(path);
			assert resource.exists() : String.format("Could not find resource '%s'.", path);
			file = resource.getFile();
			assert file.canRead() : String.format("Could not read from resource '%s'.", path);
			assert file.isDirectory() : String.format("Resource '%s' is not a folder.", path);
			store = EntityStore.from(file);
			web.add(store);
		} catch (final Throwable oops) {
			throw new PassThroughException(oops);
		}
	}

	protected void handleSchema(final boolean createSchema) {
		if (createSchema)
			this.manager.createSchema();
		else
			this.manager.cleanSchema();
	}

	private void saveEntities(final EntityWeb web) {
		assert web != null;

		this.sort(web);
		this.persist(web);
	}

	protected Errors validate(final AbstractEntity entity) {
		assert entity != null;

		Errors result;
		Set<ConstraintViolation<AbstractEntity>> violations;

		violations = this.validator.validate(entity);
		result = new Errors();
		ErrorsHelper.transferErrors(violations, result);
		this.checkAttributeTypes(result, entity);

		return result;
	}

	protected boolean validate(final EntityWeb web) {
		assert web != null;

		boolean result;
		Errors errors;
		boolean valid;
		String name, fullName;
		AbstractEntity entity;
		String message;

		result = true;
		for (final Entry<String, AbstractEntity> entry : web) {
			name = entry.getKey();
			entity = entry.getValue();

			errors = this.validate(entity);
			valid = !errors.hasErrors();
			result = result && valid;

			if (valid)
				return result;
			else {
				fullName = String.format("%s@%s", entity.getClass().getName(), name);
				message = ThrowableHelper.toString(fullName, errors);
				// throw new ValidationException(message);
			}
		}

		return result;
	}

	protected void sort(final EntityWeb web) {
		assert web != null;

		List<Class<AbstractEntity>> order;
		Set<List<Class<AbstractEntity>>> cycles;

		web.close();
		order = web.getClazzOrder();
		cycles = web.getCycles();

		if (!cycles.isEmpty())
			for (final List<Class<AbstractEntity>> cycle : cycles) {
				StringBuilder message;
				String separator;

				message = new StringBuilder();
				separator = "";
				for (final Class<AbstractEntity> clazz : cycle) {
					message.append(separator);
					message.append(clazz.getName());
					separator = " -> ";
				}
			}

		assert cycles.isEmpty() : "Cannot persist your entities due cyclic clazz dependencies.";
	}

	protected void persist(final EntityWeb web) {
		assert web != null;

		List<Class<AbstractEntity>> order;
		Collection<AbstractEntity> entities;

		order = web.getClazzOrder();

		for (final Class<AbstractEntity> clazz : order) {
			entities = web.getEntities(clazz);
			this.manager.persist(entities);
			this.manager.flushTransaction();
		}
	}

	protected void checkAttributeTypes(final Errors errors, final AbstractEntity entity) {
		assert errors != null;
		assert entity != null;

		Map<String, TypeDescriptor> descriptors;

		descriptors = ReflectionHelper.getProperties(entity);
		for (final Entry<String, TypeDescriptor> entry : descriptors.entrySet()) {
			String propertyName;
			TypeDescriptor propertyType;

			propertyName = entry.getKey();
			propertyType = entry.getValue();
			if (!this.isTypeAllowed(propertyType))
				errors.add(propertyName, "Non-permitted type");
			if (!this.areAnnotationsAllowed(propertyType))
				errors.add(propertyName, "Non-permitted annotation");
		}
	}

	protected boolean isTypeAllowed(final TypeDescriptor descriptor) {
		assert descriptor != null;

		boolean result;
		Class<?> rootClazz, componentClazz;
		OneToMany oneToMany;
		String mappedBy;

		rootClazz = descriptor.getType();
		result = ReflectionHelper.isPrimitive(descriptor) || //
			ReflectionHelper.isEnum(descriptor) || //
			ReflectionHelper.isAssignable(AbstractEntity.class, rootClazz) || //
			ReflectionHelper.isAssignable(AbstractDatatype.class, rootClazz);
		// ReflectionHelper.isAssignable(GrantedAuthority.class, rootClazz)

		if (!result && ReflectionHelper.isCollection(rootClazz)) {
			componentClazz = descriptor.getElementTypeDescriptor().getObjectType(); // NOSONAR
			oneToMany = descriptor.getAnnotation(javax.persistence.OneToMany.class);
			mappedBy = oneToMany != null ? oneToMany.mappedBy() : null;

			result = ReflectionHelper.isAssignable(AbstractEntity.class, componentClazz) && (oneToMany == null || mappedBy != null);
		}

		return result;
	}

	protected boolean areAnnotationsAllowed(final TypeDescriptor descriptor) {
		assert descriptor != null;

		return true;
	}

}
