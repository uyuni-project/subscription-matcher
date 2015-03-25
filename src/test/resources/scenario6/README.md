Scenario 6 - Stackable subscription different start/end dates
=============================================================

This scenario has 2 SLES subscriptions with different start and end dates.
Both subscriptions are for 2 cpus, stackable and system_limit 3.

We have 2 systems. One with 4 cpus and one with 8 cpus.

Result
------

The system with 4 cpus can be entitled, while the other system with 8 cpus
cannot.

It is not allowed to mix subscriptions with different start and end dates
for one product.

