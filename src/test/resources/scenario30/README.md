Scenario 30 - Virtual Host Manager infrastructure with 2 subscriptions
======================================================================

A Virtual Host Manager, two Virtual Hosts, three Virtual Guests.

Setup diagram:
```
VHM (1000) ---> VH (100) ---> VG (101), installed products: 814, 815
           |             \--> VG (102), installed products: 815
           \--> VH (110) ---> VG (111), installed products: 814, 815
```
* 2 Subscriptions
** Subscription 1 - for the product 814, quantity = 1
** Subscription 2 - for the product 815, quantity = 1

Result
------
Subscription 1 has enough quantity to cover all installations (this wouldn't be
the case when there was no VHM since the matcher would penalize odd matches
count).

Subscription 2 has not enough quantity to cover all three installations (we'd
need 2 subscriptions for that) and one system will be left w/o a subscription
(we used pin for reproducibility).

