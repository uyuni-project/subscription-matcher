Scenario 27 - HardBundle applied for UNLIMITED_VIRTUALIZATION POLICY
=======================================================================

SUBSCRIPTIONS:
- 1 subscription[1] for SLES11-Pool (product id 814)
- 1 subscription[2] for SLES11-Extras (product id 689)

SYSTEMS:
- 1 host system with no product installed (system id 100)
- 1 guest system with both products installed (system id 101)
- 1 guest system with both products installed (system id 102)
- 1 guest system with both products installed (system id 103)

PRODUCTS:
- id 814: SLES11-Pool
- id 689: SLES11-Extras


HARD-BUNDLE:
- 1 with part number 113-002220-001 [UNLIMITED_VIRTUALIZATION]


Result
------

The HardBundle can only be full-matched or none.
The HardBundle contains 2 subscriptions, 1 for SLES11-Pool product
and 1 for SLES11-Extras product, so matching the host[100] system with the
unlimited_virtualization policy will match guest 101, 102 and 103 for both
products, but they must have installed both products on all guests.

System[100]
 - Subscription[1] --> SLES11-Pool[814], CONFIRMED
 - Subscription[2] --> SLES11-Extras[689], CONFIRMED
 
System[101]
 - Subscription[1] --> SLES11-Pool[814], CONFIRMED
 - Subscription[2] --> SLES11-Extras[689], CONFIRMED
 
System[102]
 - Subscription[1] --> SLES11-Pool[814], CONFIRMED
 - Subscription[2] --> SLES11-Extras[689], CONFIRMED

System[103]
 - Subscription[1] --> SLES11-Pool[814], CONFIRMED
 - Subscription[2] --> SLES11-Extras[689], CONFIRMED


