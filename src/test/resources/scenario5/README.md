Scenario 5 - Stackable subscription different orgs
==================================================

This scenario has 2 SLES subscriptions from different SCC organizations
(multi mirrorcredentials feature).
Both subscriptions are for 2 cpus, stackable and system_limit 3.
But one is from organization **UC5** while the other is from **UC7**.

We have 2 systems. One with 4 cpus and one with 8 cpus.

Result
------

The system with 4 cpus can be entitled, while the other system with 8 cpus
cannot.

It is not allowed to take one subscription from **UC5**
and assign them with 3 from **UC7** to **chupacabra**.

