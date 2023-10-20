Scenario 29 - Virtual Host Manager - basic scenario
===================================================

Very simple scenario with a single Virtual Host Manager, two Virtual Hosts and
two Virtual Guests.

Scenario diagram:
```
VHM (1000) ---> VH (100) ---> VG (101)
           |
           \--> VH (110) ---> VG (111)
```
* Product 814 is installed on both virtual guests
* Subscription 1 for product 814, quantity = 1

Result
------
With taking Virtual Host Manager into account the subscription with
quantity = 1 covers all installations. Without considering Virtual Host
Managers, this wouldn't be the case.

