Scenario 40 - Equal OEM Subscriptions merging
=============================================

OEM Subscriptions have quantity = 1 (customers typically buy multiple ones)
which makes them unmatchable to systems that have higher number of CPUs in
certain cases (e.g. 1-2 subscription).

In such cases, matcher merges equal subscriptions together to make sure the
systems with higher number of CPUs can be covered.

Pinned matches are present to test their correct merging.

Scenario
--------
- 2 systems with 4 CPUs
- 4 1-2 stackable OEM subscriptions with quantity = 1 covering the installed
  product
- 2 pinned matches

Result
------
Both systems are covered.

