echo adjusting pinned matches
cat $1 |   jq --indent 4 '{timestamp: .timestamp, pinned_matches: .pinned_matches | map({subscription_id: {scc_subscription_id: .subscription_id, scc_order_item_id: .subscription_id}, system_id:.system_id}), products: .products, subscriptions: .subscriptions, virtualization_groups: .virtualization_groups, systems: .systems }' > $1.new
