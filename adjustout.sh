# horribly ugly

for f in src/test/resources/*/output.json; do 
echo $f

#    cat $f |  jq --indent 4 '{timestamp: .timestamp, matches: .matches | map({system_id: .system_id, subscription_id: {scc_subscription_id: .subscription_id, scc_order_item_id: .subscription_id}, product_id: .product_id, cents: .cents, confirmed: .confirmed}), subscription_policies: .subscription_policies, messages: .messages}' > $f.new
#    mv $f.new $f
#
#    cat $f | jq --indent 4 '{timestamp: .timestamp, matches: .matches, subscription_policies: .subscription_policies, messages: .messages |  map(if .data.subscription_id!=null then .data += {"subscription_id": "sccSubscriptionId:\(.data.subscription_id),sccOrderItemId:\(.data.subscription_id)"} else . end) }'> $f.new
#    mv $f.new $f
#
#    cat $f | jq --indent 4 '{timestamp: .timestamp, matches: .matches, subscription_policies: .subscription_policies, messages: .messages |  map(if .data.new_subscription_id!=null then .data += {"new_subscription_id": "sccSubscriptionId:\(.data.new_subscription_id),sccOrderItemId:\(.data.new_subscription_id)"} else . end) }'> $f.new
#    mv $f.new $f
#
#    cat $f | jq --indent 4 '{timestamp: .timestamp, matches: .matches, subscription_policies: .subscription_policies, messages: .messages |  map(if .data.old_subscription_id!=null then .data += {"old_subscription_id": "sccSubscriptionId:\(.data.old_subscription_id),sccOrderItemId:\(.data.old_subscription_id)"} else . end) }'> $f.new
#mv $f.new $f


#    cat $f | jq --indent 4 '{timestamp: .timestamp, matches: .matches, subscription_policies: .subscription_policies | [ to_entries | map([{"scc_subscription_id": .key, "scc_order_item_id": .key }, .value]) ] , messages: .messages}' > $f.new
#mv $f.new $f
 cat $f | jq --indent 4 '{timestamp: .timestamp, matches: .matches, subscription_policies: .subscription_policies | to_entries | map([{"scc_subscription_id": .key, "scc_order_item_id": .key }, .value]) , messages: .messages}' > $f.new
mv $f.new $f

done
