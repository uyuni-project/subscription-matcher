Scenario 28 - HardBundle applied for INHERITED_VIRTUALIZATION with UNLIMITED_VIRTUALIZATION POLICY
==================================================================================================

SUBSCRIPTIONS:
- 1 subscription[1] for SLES11 (product id 1298)
- 1 subscription[2] for an inherited virtualization product (product id 1299)
- 1 subscription[3] for an inherited virtualization product (product id 1300)

SYSTEMS:
- 1 host system with no product installed (system id 100)
- 1 guest system with all products installed (system id 101)
- 1 guest system with all products installed (system id 102)
- 1 physical system with SLES11[1298] installed (system id 103)

PRODUCTS:
- id 1298: SLES11
- id 1299: addon product
- id 1300: addon product

PINS:
- 1 pin for system[103] on subscription[1] for product SLES11[1298]

HARD-BUNDLE:
- 1 with part number 662644477479 [INHERITED_VIRTUALIZATION] for subscriptions [2] and [3]


Result
------

The HardBundle can only be full-matched or none.
The HardBundle contains 2 subscriptions, 1 for SLES11 product
and 1 for an addon product, so matching the host[100] system with the
unlimited_virtualization policy for SLES11[1298] will match guest[101] and guest[102] for SLES,
then the HardBundle can be applied on host[100] to inherit the unlimited virtualization for guest[101]
and guest[102] for product [1299] and [1300], but they must have installed all products on all guests.

System[100]
 - Subscription[1] --> SLES11[1298], CONFIRMED
 - Subscription[2] --> addon product[1299], CONFIRMED
 - Subscription[3] --> addon product[1300], CONFIRMED
 
System[101]
 - Subscription[1] --> SLES11[1298], CONFIRMED
 - Subscription[2] --> addon product[1299], CONFIRMED
 - Subscription[3] --> addon product[1300], CONFIRMED
 
System[102]
 - Subscription[1] --> SLES11[1298], CONFIRMED
 - Subscription[2] --> addon product[1299], CONFIRMED
 - Subscription[3] --> addon product[1300], CONFIRMED


The pin on the system[103] will not be satisfied because it breaks the HardBundle.
