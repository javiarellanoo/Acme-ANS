
package acme.helpers;

public class UniquenessHelper {

	// Constructors -----------------------------------------------------------

	protected UniquenessHelper() {
	}

	// Methods -------------------------------------------------------

	public static boolean checkUniqueness(final Object existingEntity, final Object entity) {
		return existingEntity == null || existingEntity.equals(entity);
	}
}
