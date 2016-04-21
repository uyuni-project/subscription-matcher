Scenario 32 - Smoke test for Virtual Host Manager infrastructure
================================================================

Slightly more complex scenario with 3 Virtual Host Managers.

Scenario diagram:

```
VHM (1000) ---> VH (100) ---> VG (10), installed products: 900
           |             \--> VG (11), installed products: 800
           \--> VH (101) ---> VG (12), installed products: 801, 901
VHM (2000) ---> VH (200) ---> VG (20), installed products: 814, 802
VHM (3000) ---> VH (300) ---> VG (30), installed products: 815
```

* Subscriptions
** id = 1, quantity = 1, products: 900, 901
** id = 2, quantity = 1, products: 800, 801, 802
** id = 3, quantity = 1, products: 814, 815

Result
------
* Subscription 1 covers all installations (installations are under the same VHM)
* Subscription 2 covers only products 801 and 802, not 800 (installations are
  under different VHMs, pins used for reproducibility)
* Subscription 3 covers only product 814, not 815 (same case as the
  Subscription 2, pin used for reproducibility)

