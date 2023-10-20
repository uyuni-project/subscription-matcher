Scenario 36 - single Subscription HardBundle with two subscriptions
===================================================================

SUBSCRIPTIONS:
- 1 subscription for SUSE Manager Server (product ids 1518, 1357) singleSubscriptionHardBundle
- 1 subscription for SLES (product id 1357)

SYSTEMS:
- 1 physical system with SUMA Server 3.1 and SLES12 SP2 installed (system id 100)

PRODUCTS:
- id 1357: base SLES12 SP2 x86_64
- id 1518: add-on SUSE Manager Proxy 3.1

PINS:
- none

HARD-BUNDLE:
- 1 with part number 874-005943 [INSTANCE]

Result
------

The HardBundle has a subscription with 2 products of different product classes.

But we got also a SLES subscription with same part number. These subscriptions
won't be merged since they don't have disjoint product sets.
This scenario should fully match that one system with both products using the
subscription 1. The subscription 2 is left unused.

System[100]
 - Subscription --> SLES12SP2[1357] + SUMAP3.1[1520]

