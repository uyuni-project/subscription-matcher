Scenario 22 - UNLIMITED VIRTUALIZATION & INHERITED VIRTUALIZATION & PHYSICAL
============================================================================

(Note: INHERITED VIRTUALIZATION subscription inherits the policy dynamically
from other subscriptions.)

Systems:
- 4 physical systems (1000-1003)
- 1 virtual host (100) with 2 guests (101, 102)

Subscriptions:
- id 1: Unlimited virtualization, covering product 1298
- id 2: Physical policy, covering product 1299
- id 3: Inherited virtualization, covering product 1300

Installed products:
- Systems 1000 & 1001: 1299 and 1300 (both "physical and IV" products)
- System 1002: 1299
- System 1003: 1300
- System 100: nothing (virt. host)
- Systems 101 & 102: 1299 & 1300 ("UV + IV products")

Result
------
Systems 1000 & 1001 covered with the PHYSICAL subscription and
INHERITED_VIRTUALIZATION subscription (it inherits the policy from the PHYSICAL
subscription).

Systems 1002 is not covered (not enough subscriptions for covering 1299).

Systems 1003 is not covered (its product could only be covered by the
INHERITED_VIRTUALIZATION subscription, which has sufficient quantity, but this
subscription can't be assigned on its own).

Systems 101 & 102 are covered with the UNLIMITED_VIRTUALIZATION &
INHERITED_VIRTUALIZATION subscription.


```
{
  "system_id": 100,
  "subscription_id": 1,
  "product_id": 1298,
  "cents": 100,
  "confirmed": true
}
{
  "system_id": 100,
  "subscription_id": 3,
  "product_id": 1300,
  "cents": 100,
  "confirmed": true
}
{
  "system_id": 101,
  "subscription_id": 1,
  "product_id": 1298,
  "cents": 0,
  "confirmed": true
}
{
  "system_id": 101,
  "subscription_id": 3,
  "product_id": 1300,
  "cents": 0,
  "confirmed": true
}
{
  "system_id": 102,
  "subscription_id": 1,
  "product_id": 1298,
  "cents": 0,
  "confirmed": true
}
{
  "system_id": 102,
  "subscription_id": 3,
  "product_id": 1300,
  "cents": 0,
  "confirmed": true
}
{
  "system_id": 1000,
  "subscription_id": 2,
  "product_id": 1299,
  "cents": 100,
  "confirmed": true
}
{
  "system_id": 1000,
  "subscription_id": 3,
  "product_id": 1300,
  "cents": 100,
  "confirmed": true
}
{
  "system_id": 1001,
  "subscription_id": 2,
  "product_id": 1299,
  "cents": 100,
  "confirmed": true
}
{
  "system_id": 1001,
  "subscription_id": 3,
  "product_id": 1300,
  "cents": 100,
  "confirmed": true
}
