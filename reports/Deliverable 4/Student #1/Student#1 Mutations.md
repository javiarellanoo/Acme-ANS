 # Implemented Mutations Report for Student#1's Requirements

Mutations were performed on the branch `task140`.

## 1. Incremented length for field Tag on Flight

**File:** `/Acme-ANS-D04/src/main/java/acme/entities/flights/Flight.java`  
**Status:** Detected - Multiple tests are failing, especially Flight create/publish/update tests

**Code Change:**
```java
// Before
@Mandatory
@ValidString(min = 1, max = 50)
@Automapped
private String tag;

// After
@Mandatory
@ValidString(min = 1, max = 51)
@Automapped
private String tag;
```

## 2. Incremented length for field Description on Flight

**File:** `/Acme-ANS-D04/src/main/java/acme/entities/flights/Flight.java`  
**Status:** Detected - Multiple tests are failing, especially Flight create/publish/update tests

**Code Change:**
```java
// Before
@Optional
@ValidString(min = 0, max = 255)
@Automapped
private String description;

// After
@Optional
@ValidString(min = 0, max = 256)
@Automapped
private String description;
```

## 3. Modified validation logic in ManagerFlightPublishService

**File:** `/Acme-ANS-D04/src/main/java/acme/features/manager/ManagerFlightPublishService.java`  
**Status:** Detected - Tests are failing, allowing to publish Flights without legs


**Code Change:**
```java
// Before
	public void validate(final Flight flight) {
		Collection<Leg> legs = this.repository.findLegsByFlightId(flight.getId());
		boolean legsStatus = !legs.isEmpty() && legs.stream().allMatch(l -> l.getDraftMode() == false);
		super.state(legsStatus, "*", "acme.validation.flight.nonPublishedLegs.message");
	}

// After
	@Override
	public void validate(final Flight flight) {
		Collection<Leg> legs = this.repository.findLegsByFlightId(flight.getId());
		boolean legsStatus = legs.isEmpty() || legs.stream().allMatch(l -> l.getDraftMode() == false);
		super.state(legsStatus, "*", "acme.validation.flight.nonPublishedLegs.message");
	}
```

## 4. Enabling to create/update/publish Legs of Flights in the Past

**File:** `/Acme-ANS-D04/src/main/java/acme/features/manager/leg/ManagerLegCreateService.java` , 

**Status:** Detected - Multiple tests have failed, allowing for legs to be created/published/updated with a departure in the past.

**Code Change:**
```java
// Before
			validDate = MomentHelper.isAfterOrEqual(leg.getScheduledDeparture(), currentMoment);

// After
	validDate = MomentHelper.isBeforeOrEqual(leg.getScheduledDeparture(), currentMoment);
```

## 5. Changed logic of Leg publishing so that legs that overlap in a Flight are allowed. 

**File:** `/Acme-ANS-D04/src/main/java/acme/features/manager/leg/ManagerLegPublishService.java`  
**Status:** Detected - Multiple tests have failed, overlapping legs have been published when an error should have been raised.

**Code Change:**
```java
// Before
	for (Leg l : legsOfFlight)
				if (MomentHelper.isBeforeOrEqual(leg.getScheduledDeparture(), l.getScheduledArrival()) && MomentHelper.isAfterOrEqual(leg.getScheduledArrival(), l.getScheduledDeparture()))
					validLeg = false;

// After
			for (Leg l : legsOfFlight)
				if (MomentHelper.isBeforeOrEqual(leg.getScheduledDeparture(), l.getScheduledArrival()) && MomentHelper.isAfterOrEqual(leg.getScheduledArrival(), l.getScheduledDeparture()))
					validLeg = true;
```
