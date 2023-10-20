Scenario 25 - HardBundle applied on full-matched system for 1:2 POLICY
======================================================================

SUBSCRIPTIONS:
- 2 subscription[1] for SLES11-Pool (product id 814)
- 2 subscription[2] for SLES11-Extras (product id 689)

SYSTEMS:
- 1 physical system with only SLES11-Extras installed (system id 100) and 3 cpus
- 1 physical system with only SLES11-Pool installed (system id 101) and 3 cpus
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
The HardBundle is a set of subscriptions sharing certain attributes. Each
subscription owns products from the same product class. Matcher merges such
subscriptions into a single subscription (Pinned matches are also adjusted so
that they correspond to the merged subscription.).

Two matches on such subscription, on one system and on 2 products of various product class
share the same CentGroup (with N cents).

If one match is confirmed, N cents of the subscription are consumed.
If both matches are confirmed, still only N cents of the subscription are consumed.

The HardBundle contains 2 subscriptions (with quantity=2), 2 for SLES11-Pool product
and 2 for SLES11-Extras product. These subscriptions will be merged into one
subscription with quantity=2 and with 2 products.

The matcher finds the solution, such that the only matching system is the
systemId=102, and it will be full-matched:

System[102]
 - Subscription[1] --> SLES11-Pool[814], quantity = 1, CONFIRMED
 - Subscription[1] --> SLES11-Extras[689], quantity = 1, CONFIRMED

The pin on the system[101] will not be satisfied because covering system 102 by matches that
share one cent group is cheaper and leads to higher matched products coverage.
