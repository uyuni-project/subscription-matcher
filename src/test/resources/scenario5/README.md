Scenario 5 - Stackable subscription different orgs
==================================================

This scenario has 2 SLES11 SP4 x86_64 subscriptions from different SCC 
organizations (multi mirrorcredentials feature, id 1118).
Both subscriptions are for 2 cpus, stackable and system_limit 3 (temporary
part number is 800-TEMPORARY-X86-2CPU-STACKABLE, will be updated with the
real one as soon as it is decided).
But one is from organization **UC5** while the other is from **UC7**.

We have 2 systems. One with 4 cpus and one with 8 cpus.

Result
------

The system with 4 cpus can be entitled, while the other system with 8 cpus
cannot.

It is not allowed to take one subscription from **UC5**
and assign them with 3 from **UC7** to 200.

