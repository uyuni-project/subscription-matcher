Scenario 35 - special HardBundle applied on full-matched for INSTANCE POLICY
============================================================================

SUBSCRIPTIONS:
- 1 subscription for SUSE Manager Proxy (product id 1520)

SYSTEMS:
- 1 physical system with SUMA Proxy 3.1 and SLES12 SP2 installed (system id 100)

PRODUCTS:
- id 1357: base SLES12 SP2 x86_64
- id 1520: add-on SUSE Manager Proxy 3.1

PINS:
- none

HARD-BUNDLE:
- 1 with part number 874-006096 [INSTANCE]


Result
------

The HardBundle has a subscription with 2 products of different product classes.

This scenario should fully match that one system with both products.

System[100]
 - Subscription --> SLES12SP2[1357] + SUMAP3.1[1520]

