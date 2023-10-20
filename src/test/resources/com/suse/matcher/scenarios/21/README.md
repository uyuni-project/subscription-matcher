Scenario 21 - UNLIMITED VIRTUALIZATION & INHERITED VIRTUALIZATION
=================================================================

1 Host without installed products.
2 Guests under the Host, each with 2 installed products:
- SLE
- a non-base product

Subscriptions (both with quantity = 1):
- SLE subscription with UNLIMITED_VIRTUALIZATION policy
- non-base product subscription with INHERITED_VIRTUALIZATION policy

Result
------

Both products on the guests can be covered with the UNLIMITED_VIRTUALIZATION
and INHERITED_VIRTUALIZATION subscription.

