Scenario 38 - 2 sets of HardBundles each with 2 subscriptions applied on full-matched physical systems
======================================================================================================

SUBSCRIPTIONS:
- 2 subscriptions for SLES11 (product id 1298)
- 2 subscriptions for SUSE Manager Server (product id 1224)

SYSTEMS:
- 3 physical systems with all products installed

PRODUCTS:
- id 1298: base SLES11 SP4 i686
- id 1224: add-on SUSE Manager Server 2.1

PINS:
- Subscription 4 - System 100
- Subscription 2 - System 101

HARD-BUNDLE:
- 2 x the same bundle, part number 874-006260

Result
------
The test is about merging old style hard bundles into a single subscriptions
hard bundles. Here we have 2 sets of subscriptions as follows:

All subscriptions have equal part number, start/end date and quantity
Subscription 1 - Product 1298
Subscription 2 - Product 1224
Subscription 3 - Product 1298
Subscription 4 - Product 1224

There are 2 interchangeable hard bundle configurations:
1. HB1: Merged subscription 1 + 2
   HB2: Merged subscription 3 + 4
2. HB1: Merged subscription 1 + 4
   HB2: Merged subscription 3 + 2

After merge, we get 2 subscriptions, both with quantity = 1. Two systems should
be covered.

Pinned matches are added to test their correct merging.

