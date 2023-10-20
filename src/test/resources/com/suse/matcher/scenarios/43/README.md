Scenario 43 - HardBundle applied for INHERITED_VIRTUALIZATION with UNLIMITED_VIRTUALIZATION POLICY
==================================================================================================

Same as Scenario 28, but the guests have different SLE versions installed. This
should still be matchable.

SUBSCRIPTIONS:
- 1 subscription[1] for SLES 12 SP4 & SP5 (product ids 1625, 1878)
- 1 subscription[2] for an inherited virtualization product (product id 1299)
- 1 subscription[3] for an inherited virtualization product (product id 1300)

SYSTEMS:
- 1 host system with no product installed (system id 100)
- 1 guest system with SLE12 SP4 and all other products installed (system id 101)
- 1 guest system with SLE12 SP5 and all other products installed (system id 102)
- 1 physical system with SLE12 SP5[1878] installed (system id 103)

PRODUCTS:
- id 1625: SLES12 SP4
- id 1878: SLES12 SP5
- id 1299: addon product
- id 1300: addon product

PINS:
1 pin for system[103] on subscription[1] for product SLE [1878]

HARD-BUNDLE:
- 1 with part number 662644477479 [INHERITED_VIRTUALIZATION] for subscriptions [2] and [3]


Result
------
The HardBundle is a set of subscriptions sharing certain attributes. Each
subscription owns products from the same product class. Matcher merges such
subscriptions into a single subscription (Pinned matches are also adjusted so
that they correspond to the merged subscription.).

Two matches on such subscription, on one system and on 2 products of various product class
share the same CentGroup (with N cents).

If one match is confirmed, N cents of the subscription are consumed.
If both matches are confirmed, still only N cents of the subscription are consumed.

The HardBundle contains 2 subscriptions, 1 for SLES11 product and 1 for an
addon product. These subscriptions will be merged into one subscription with
quantity=1 and with 2 products.

The matcher finds the solution, such that matching the host[100] system with
the UNLIMITED_VIRTUALIZATION policy for SLE 12 SP4 & SP5 will match guest[101]
and guest[102] for SLES, then the HardBundle can be applied on host[100] to
inherit the unlimited virtualization for guest[101] and guest[102] for product
[1299] and [1300].

System[100]
 - Subscription[1] --> both SLE 12 SP4[1625] AND SP5[1878], CONFIRMED
 - Subscription[2] --> addon product[1299], CONFIRMED
 - Subscription[3] --> addon product[1300], CONFIRMED
 
System[101]
 - Subscription[1] --> SLE 12 SP4[1625], CONFIRMED
 - Subscription[2] --> addon product[1299], CONFIRMED
 - Subscription[3] --> addon product[1300], CONFIRMED
 
System[102]
 - Subscription[1] --> SLE 12 SP5[1878], CONFIRMED
 - Subscription[2] --> addon product[1299], CONFIRMED
 - Subscription[3] --> addon product[1300], CONFIRMED


The pin on the system[103] will not be satisfied because there is not enough
subscriptions.

