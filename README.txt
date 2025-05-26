# README.txt
#
# Copyright (C) 2012-2025 Rafael Corchuelo.
#
# In keeping with the traditional purpose of furthering education and research, it is
# the policy of the copyright owner to permit non-commercial use and redistribution of
# this software. It has been tested carefully, but it is not guaranteed for any particular
# purposes.  The copyright owner does not offer any warranties or representations, nor do
# they accept any liabilities with respect to them.

This is a starter project.  It is intended to be a core learning asset for the students
who have enrolled the Design and Testing subject of the Software Engineering curriculum of the 
University of Seville.  This project helps them start working on their new information system 
projects.

NOTE: THE REQUIREMENT DOCUMENTS TO BE TAKEN INTO ACCOUNT FOR THE FINAL EVALUATION ARE THE ONES PLACED UNDER THE DELIVERY 04 SUBFOLDER, THE PREVIOUS ONES CONTAIN PREVIOUS VERSIONS OF THE DOCUMENTS THAT HAVE EVOLVED DURING THE PROJECT


NOTES REGARDING THE IMPLEMENTATION OF CERTAIN REQUIREMENTS, USEFUL FOR THEIR EVALUATION:

Regarding the showing of services in all pages: Whenever services have a picture, their picture will be shown. In case that they do not have one, the name of the service will appear instead, in a similar way to how it is done on Acme Jobs. This was validated by the lecturer during D02.

Regarding publishing Maintenance Records: For the PUBLISH submit to appear in a Maintenance Record, it must meet the necessary conditions to be published. Otherwise, if an illegal action is attempted (i.e publish a Maintenance Record that cannot be published) an authorisation exception will be risen.

Regarding visualisation of Activity Logs and Flight Assignments: Only the FlightCrewMember that created them will be able to visualize them, undistinctively of their draft mode. However, edition features and deletions are reserved for logs and assignments with draftMode = true, as stated in the requirements.

Regarding deletion on Booking Records: When it is attempted to delete a booking record via DevTools, and the passenger id is edited, it does not affect the deletion, since it only deletes the association, not the passenger.

