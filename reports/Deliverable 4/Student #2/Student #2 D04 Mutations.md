# Implemented Mutations Report for Student#2's Requirements

Mutations were performed on the branch `task153`.

---

## 1. Changed pattern for locatorCode in Booking

**File:** `/Acme-ANS-D04/src/main/java/acme/entities/bookings/Booking.java`  
**Status:** Detected - Tests for Booking create, update, and publish are failing.

**Code Change:**
```java
// Before
@Mandatory
@ValidString(pattern = "^[A-Z0-9]{6,8}$")
@Column(unique = true)
private String locatorCode;

// After
@Mandatory
@ValidString(pattern = "^[A-Z0-9]{6,9}$")
@Column(unique = true)
private String locatorCode;
```
This change allows locator codes of up to 9 characters instead of 8.

---

## 2. Changed min length for fullName in Passenger

**File:** `/Acme-ANS-D04/src/main/java/acme/entities/passenger/Passenger.java`  
**Status:** Detected - Tests for Passenger create, update, and publish are failing.

**Code Change:**
```java
// Before
@Mandatory
@ValidString(max = 255, min = 1)
@Automapped
private String fullName;

// After
@Mandatory
@ValidString(max = 255, min = 0)
@Automapped
private String fullName;
```
Now, a passenger can have an empty full name, which breaks validation in several flows.

---

## 3. Changed validation logic in CustomerBookingCreateService

**File:** `/Acme-ANS-D04/src/main/java/acme/features/customer/booking/CustomerBookingCreateService.java`  
**Status:** Detected - Booking create service now allows bookings only for flights in the past, not current or future.

**Code Change:**
```java
// Before
status = flight != null && !flight.getDraftMode() && MomentHelper.isAfterOrEqual(flight.getScheduledDeparture(), MomentHelper.getCurrentMoment());

// After
status = flight != null && !flight.getDraftMode() && MomentHelper.isBefore(flight.getScheduledDeparture(), MomentHelper.getCurrentMoment());
```
This mutation inverts the logic, so only flights with a scheduled departure in the past are allowed for booking.

---

## 4. Changed logic for adding passengers in CustomerBookingRecordCreateService

**File:** `/Acme-ANS-D04/src/main/java/acme/features/customer/bookingRecord/CustomerBookingRecordCreateService.java`  
**Status:** Detected - Now only allows adding the same passenger multiple times to a booking; cannot add new passengers without using devtools.

**Code Change:**
```java
// Before
boolean validPassenger = passenger != null && passengers.contains(passenger);

// After
boolean validPassenger = passenger != null && !passengers.contains(passenger);
```
This change means only passengers not already in the booking can be added, which breaks the intended logic.

---

## 5. Changed authorisation logic in CustomerPassengerShowService

**File:** `/Acme-ANS-D04/src/main/java/acme/features/customer/passenger/CustomerPassengerShowService.java`  
**Status:** Detected - Now allows viewing a passenger even if it is null, which causes failures.

**Code Change:**
```java
// Before
status = passenger != null && super.getRequest().getPrincipal().hasRealm(customer);

// After
status = passenger != null || super.getRequest().getPrincipal().hasRealm(customer);
```
This mutation allows access if either the passenger exists or the user has the realm, instead of requiring both.
