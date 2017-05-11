Scenario 24 - HardBundle applied on full-matched for INSTANCE POLICY
====================================================================

SUBSCRIPTIONS:
- 1 subscription for SLES11 (product id 1298)
- 1 subscription for SUSE Manager Server (product id 1224)

SYSTEMS:
- 1 physical system with only SUMa2.1 installed (system id 100)
- 1 physical system with only SLES11 installed (system id 101)
- 1 physical system with both products installed (system id 102)

PRODUCTS:
- id 1298: base SLES11 SP4 i686
- id 1224: add-on SUSE Manager Server 2.1

PINS:
- 1 pin for system[100] on subscription[2] for product SUMa2.1[1224]

HARD-BUNDLE:
- 1 with part number 051-004063 [INSTANCE]


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

The HardBundle contains 2 subscriptions (with quantity=1), 1 for SLES base
product and 1 for an add-on product. These subscriptions will be merged into
one subscription with quantity=1 and with 2 products.

The matcher finds the solution, such that the only matching system is the
systemId=102, and it will be full-matched:

System[102]
 - Subscription[1] --> SLES11[1298]
 - Subscription[1] --> SUMa2.1[1224]

The pin on the system[100] will not be satisfied because covering system 102 by matches that
share one cent group is cheaper and leads to higher matched products coverage.
