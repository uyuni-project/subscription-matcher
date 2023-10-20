Scenario 42 - Virtual Machines with different SLE version
=========================================================

The scenario defines a virtual host with two virtual guests on top of it.

The host itself does not have any product (foreign system, like ESXi).

The guest 101 has SLE12 SP2 installed.
The guest 102 has SLE12 SP3 installed.

The subscription here is a single UNLIMITED VIRTUALIZATION subscription for
SLE.

Result
------
Even though the guests do not have identical product IDs, they should be
covered by a single UNLIMITED VIRTUALIZATION subscription assigned to the host.

