Scenario 26 - It seems to be an HardBundle but it's not --> quantity is different between subscriptions
=======================================================================================================

SUBSCRIPTIONS:
- **2** subscription[1] for SLES11-Pool (product id 814)
- **3** subscription[2] for SLES11-Extras (product id 689)

SYSTEMS:
- 1 physical system with only SLES11-Extras installed (system id 100)
- 1 physical system with only SLES11-Pool installed (system id 101)
- 1 physical system with both products installed (system id 102)

PRODUCTS:
- id 814: SLES11-Pool
- id 689: SLES11-Extras

PINS:
- 1 pin for system[101] on subscription[1] for product SLES11-Pool[814]

HARD-BUNDLE:
- 1 with part number 874-006875 [ONE_TWO]


Result
------
The HardBundle is a set of subscriptions sharing certain attributes and owning
products from the same product class.

The HardBundle should contain subscriptions with the same quantity for each subscription
but it contains 2 for SLES11-Pool and 3 for SLES11-Extras, so it's not an HardBundle.
Then subscriptions can be applied on different systems, just based on their policy rules.

System[100]
 - Subscription[2] --> SLES11-Extras[689], quantity = 1, CONFIRMED

System[101]
 - Subscription[1] --> SLES11-Pool[814], quantity = 1, CONFIRMED

System[102]
 - Subscription[2] --> SLES11-Extras[689], quantity 2, CONFIRMED
 - Subscription[1] --> SLES11-Pool[814], quantity 2, NOT CONFIRMED because of the pin on 101 consumes 1 of the 2 subscriptions, then 1 only is left but this system require 2 subscriptions since of its 3 cpus.

The pin on the system[101] is satisfied.
