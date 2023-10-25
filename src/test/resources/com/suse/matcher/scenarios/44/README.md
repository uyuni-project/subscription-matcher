Scenario 44 - Subscriptions without products
============================================

This scenario defines a system with a matching subscription. The user has also 
two additional subscriptions:

- a known subscription for a service which is not associated with any product.
  This subscription has a part number starting with 051.
- an unknown part number describing a subscription without any associated product

Result
------

The system will get its subscription. The two subscriptions without product will be 
both ignored, but for the unknown part number an info message will be reported
