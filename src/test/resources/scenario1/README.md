Scenario 1 - Virtual Machines and unassignable system
=====================================================

The first scenario defines 2 virtual hosts with either
two virtual systems on top of it.

One virtual host is a *SLES* (id 100) and the second one
is a *VMware* server (id 200).

For virtual hosts we need to map all products of all virtual
systems running on these hosts to the host to make use of
the **unlimited virtualization** option of many subscriptions.

The subscription we have in this scenario, part number
874-005030, is for product id 814 (SLES11 SP3 x86_64),
it has *unlimited virtualization* and up to 32 cpus.

Additionally we have one system in this scenario where we do
not have a subscription for (1122, RES 5).

Result
------

The result should be 100 and 200 consume each 1 subscription.
1 subscription is free and the system 300 can not be entitled.

