Scenario 33 - A Guest without a Host but in a Virtualization Group
===================================================================

A Guest (a System remarked as virtual) is managed as physical only
if it's not related neither to a Host nor to a Virtualization Group.

```

VirtualizationGroup (1000)  ----> Host (100) ---> Guest (10)
                            \---> Guest (1001)

```
* Guests have product 814 installed.
* Subscription 1, quantity = 1, product = 814.

Result
------
The 1:2 subscription quantity is enough to cover both installations on Guests only if Guest (1001)
is still treated as a virtual system in the Virtualization Group instead to be managed as a physical one. 

