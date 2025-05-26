 # Implemented Mutations Report for Student#5's Requirements

Mutations were performed on the branch `task147`.

## 1. Removed past validation for Moment attribute on MaintenanceRecord

**File:** `/Acme-ANS-D04/src/main/java/acme/entities/maintenanceRecords/MaintenanceRecord.java`  
**Status:** Detected - Multiple tests are failing, especially MaintenanceRecord create/publish/update tests.

**Code Change:**
```java
// Before
@Mandatory
@ValidMoment(past = true)
@Temporal(TemporalType.TIMESTAMP)
private Date					moment;

// After
@Mandatory
@ValidMoment
@Temporal(TemporalType.TIMESTAMP)
private Date					moment;
```

## 2. Increased length for field Description on Task

**File:** `/Acme-ANS-D04/src/main/java/acme/entities/tasks/Task.java`  
**Status:** Detected - Multiple tests are failing, especially Task create/publish/update tests.

**Code Change:**
```java
// Before
@Mandatory
@ValidString(min = 1, max = 255)
@Automapped
private String				description;

// After
@Mandatory
@ValidString(min = 1, max = 256)
@Automapped
private String				description;
```

## 3. Modified load logic in TechnicianTaskListService

**File:** `/Acme-ANS-D04/src/main/java/acme/features/technician/task/TechnicianTaskListService.java`  
**Status:** Detected - Tests are failing, since it is only displaying list of published tasks.


**Code Change:**
```java
// Before
@Override
public void load() {
  Collection<Task> tasks;
  boolean published;
  int technicianId;

  published = super.getRequest().getData("published", boolean.class);
  technicianId = super.getRequest().getPrincipal().getActiveRealm().getId();

  tasks = published ? this.repository.findTasksPublished() : this.repository.findTasksByTechnicianId(technicianId);

  super.getBuffer().addData(tasks);
}

// After
@Override
public void load() {
  Collection<Task> tasks;
  boolean published;
  int technicianId;

  published = super.getRequest().getData("published", boolean.class);
  technicianId = super.getRequest().getPrincipal().getActiveRealm().getId();

  published = true;

  tasks = published ? this.repository.findTasksPublished() : this.repository.findTasksByTechnicianId(technicianId);

  super.getBuffer().addData(tasks);
}
```

## 4. Change validation of nextInspectionDate on MaintenanceRecordValidator

**File:** `/Acme-ANS-D04/src/main/java/acme/constraints/MaintenanceRecordValidator.java` 

**Status:** Detected - Multiple tests are failing, especially MaintenanceRecord create/publish/update tests.

**Code Change:**
```java
// Before
Boolean isNextInspectionAfterMoment = MomentHelper.isAfter(nextInspectionDate, moment);

super.state(context, isNextInspectionAfterMoment, "nextInspectionDate", "acme.validation.maintenanceRecord.nextInspectionDate.message");


// After
Boolean isNextInspectionAfterMoment = MomentHelper.isBefore(nextInspectionDate, moment);

super.state(context, isNextInspectionAfterMoment, "nextInspectionDate", "acme.validation.maintenanceRecord.nextInspectionDate.message");

```

## 5. Changed logic of authorise method in TechnicianTaskPublishService

**File:** `/Acme-ANS-D04/src/main/java/acme/features/technician/task/TechnicianTaskPublishService.java`  
**Status:** Detected - Multiple tests have failed, more precisely Task publish tests since it is allowing to publish tasks belonging to other technicians.

**Code Change:**
```java
// Before
@Override
public void authorise() {

  boolean status;
  int taskId;
  Task task;
  Technician technician;

  taskId = super.getRequest().getData("id", int.class);
  task = this.repository.findTaskById(taskId);
  technician = task == null ? null : task.getTechnician();

  if (task == null)
    status = false;
  else
    status = task.getDraftMode() && super.getRequest().getPrincipal().hasRealm(technician);

  super.getResponse().setAuthorised(status);
}

// After
@Override
public void authorise() {

  boolean status;
  int taskId;
  Task task;
  Technician technician;

  taskId = super.getRequest().getData("id", int.class);
  task = this.repository.findTaskById(taskId);
  technician = task == null ? null : task.getTechnician();

  if (task == null)
    status = false;
  else
    status = task.getDraftMode() || super.getRequest().getPrincipal().hasRealm(technician);

  super.getResponse().setAuthorised(status);
}
```
