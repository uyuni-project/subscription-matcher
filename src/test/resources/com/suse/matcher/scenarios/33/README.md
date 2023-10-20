Scenario 33 - 2 Virtual Host Managers, more complex setup
=========================================================

Sligtly more complex version of the scenario 31. It tests tha the matcher
separates counting of 1-2 subscriptions under 2 different virtual host managers.
(Same results should be achieved without the code for matching in
virtualization groups.)

* We have 2 symmetric Virtual Host Managers:
```
VHM (1000) ---> VH (100) ---> VG (10)
           |
           \--> VH (101) ---> VG (11)
                         \--> VG (12)

VHM (2000) ---> VH (200) ---> VG (20)
           |
           \--> VH (201) ---> VG (21)
                         \--> VG (22)
```
* Virtual Guests have product 814 installed.
* Subscription 1, quantity = 3, product = 814.

Result
------
The subscription quantity is not enough to cover all installations (pins used
for reproducibility).

