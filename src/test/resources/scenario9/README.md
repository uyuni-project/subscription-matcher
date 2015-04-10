Scenario 9 - Stackable subscription and Instance subscriptions
==============================================================

We have one system with SLES11 SP4 x86_64 (id 1300) running as hypervisor
and on top of it a SUSE Cloud Control node (id 1288).

We have 2 SLES subscriptions 2 cpus stackable with unlimited virtualization
(874-006876), and one instance for the SUSE Cloud addon (874-006385).

Result
------

The 2 subscription with 2 cpus should be assigned to the system with 4 cpus,
while the instance of SUSE Cloud gets directly assigned to the virtual machine.

