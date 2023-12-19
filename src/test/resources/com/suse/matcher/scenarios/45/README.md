Scenario 45 - SLE Micro vCores and Hypervisor
=============================================

This scenario defines 3 systems running SLE Micro (id 2605)

The first system (id 100) has 24 cores and is a pure bare metal system.

The second (id 101) is a virtual system without host with 16 cores.

The last system (id 102) is a physical system running as hypervisor
with 2 sockets.


The SLE Micro subscription (id 1) can only be used for pure physical systems
or virtual systems and is counting cores, while the hypervisor requires
a SLES subcription (id 2).

Result
------

The result should be 100 consume all 3 subscription.
Subscription 200 is also fully used for system id 102.

