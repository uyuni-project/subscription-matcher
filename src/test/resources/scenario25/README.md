Scenario 25 - HardBundle applied on full-matched system for 1:2 POLICY
=======================================================================

SUBSCRIPTIONS:
- 2 subscription[1] for SLES11-Pool (product id 814)
- 2 subscription[2] for SLES11-Extras (product id 689)

SYSTEMS:
- 1 physical system with only SLES11-Extras installed (system id 100)
- 1 physical system with only SLES11-Pool installed (system id 101)
- 1 physical system with both products installed (system id 102) and 3 cpus

PRODUCTS:
- id 814: SLES11-Pool
- id 689: SLES11-Extras

PINS:
- 1 pin for system[101] on subscription[1] for product SLES11-Pool[814]

HARD-BUNDLE:
- 1 with part number 874-006875 [ONE_TWO]


Result
------

The HardBundle can only be full-matched or none.
The HardBundle contains 4 subscriptions, 2 for SLES11-Pool product
and 2 for SLES11-Extras product, so the only matching system can be the systemId=102,
and it will be full-matched:

System[102]
 - Subscription[1] --> SLES11-Pool[814], quantity = 2, CONFIRMED
 - Subscription[2] --> SLES11-Extras[689], quantity = 2, CONFIRMED


The pin on the system[101] will not be satisfied because it breaks the HardBundle.
