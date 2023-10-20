Scenario 31 - 2 Virtual Host Managers
=====================================

...with insuficcient subscriptions. Tests that the algorithm does not consider
Virtual Guests under different Virtual Host Managers to be in the same
virtualization group. Same results should be achieved without the code for
matching in virtualization groups.

* 2 Virtual Host Managers (1000, 2000), 2 Virtual Hosts, 2 Virtual Guests.

Scenario diagram:
```
VHM 1000 <-- VH 100 <-- VG 10
VHM 2000 <-- VH 200 <-- VG 20
```
* 1 Product is installed on both VGs
* Subscription 1 - for the product 814, quantity = 1

Result
------
One VG is matched, the other is unmatched (pin used for reproducibility).

