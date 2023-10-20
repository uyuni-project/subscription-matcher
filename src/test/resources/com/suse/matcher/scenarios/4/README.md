Scenario 4 - Stackable subscription
===================================

This scenario has 2 SLES11 SP4 i686 (id 1298) subscriptions. One for 4 cpus
and not stackable (part number 874-006260) and 4 times a second
subscription with 2 cpus but stackable (part number 874-006876).

We have 2 systems. One with 4 cpus and one with 8 cpus.

Result
------

The subscription with 4 cpus should be assigned to the system with 4 cpus,
while the system with 8 cpus gets the 4 2 cpu stackable subscriptions.

