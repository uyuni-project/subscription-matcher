Scenario 1 - Virtual Machines and unassignable system
=====================================================

The first scenario defines 2 virtual hosts with either
two virtual systems on top of it.

One virtual host is a *SLES* and the second one in a *VMware*
server.

For virtual hosts we need to map all products of all virtual
systems running on these hosts to the host to make use of
the **unlimited virtualization** option of many subscriptions.

The subscription we have in this scenario is for product id **1**.
It has *unlimited virtualization* and upto 32 cpus.

Additionally we have one system in this scenario where we do
not have a subscription for.

Result
------

The result should be *lesch* and *vmware* consume each 1 subscription.
1 subscription is free and the system *berthold* can not be entitled.

