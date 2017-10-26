#!/usr/bin/env ruby
# encoding: UTF-8

require 'json'

def resolve_products(system, products)
  system["product_ids"]
    .map { |id| products.select { |p| p["id"] == id }.first }
    .sort {|a, b| a["name"] <=> b["name"]}
end

input = JSON.parse(File.read(ARGV.first))

systems = input["systems"]
products = input["products"]
subscriptions = input["subscriptions"]
system_count = systems.count
systems_with_products = systems.select {|s| s["product_ids"].any? }


hosts = systems
  .select {|s| s["virtual_system_ids"].any? }
  .sort {|a, b| a["name"] <=> b["name"]}
  .map do |host|
    host["products"] = resolve_products(host, products)
    host["virtual_systems"] = systems
      .select {|guest| host["virtual_system_ids"].include?(guest["id"]) }
      .sort {|a, b| a["name"] <=> b["name"]}
      .map do |guest|
        guest["products"] = resolve_products(guest, products)
        guest
      end
    host
  end

guests = hosts.map { |h| h["virtual_systems"] }.flatten(1)
hosts_and_guests = hosts + guests
guests_lacking_host = systems.select { |s| !s["physical"] && !hosts_and_guests.include?(s) }
guests_lacking_host_with_products = systems_with_products & guests_lacking_host

hosts_with_products = systems_with_products & hosts
guests_with_products = systems_with_products & guests


physical = systems.select { |s| s["physical"] }
physical_with_products = systems_with_products & physical

hosts.each do |host|
  puts "#{host['name']} (#{host['id']})"
  host["products"].each do |product|
      puts "    +#{product['name']} (#{product['id']})"
  end
  host["virtual_systems"].each do |guest|
      puts "    -#{guest['name']} (#{guest['id']})"
      guest["products"].each do |product|
        puts "        +#{product['name']} (#{product['id']})"
      end
  end
end

puts
puts "systems with at least one SUSE product"
puts "#{systems_with_products.count} systems"
puts "  #{physical_with_products.count} physical systems"
puts "  #{hosts_with_products.count} virtual hosts"
puts "  #{guests_with_products.count} guests in known hosts"
puts "  #{guests_lacking_host_with_products.count} guests without a matching host"

puts
puts "all systems"
puts "#{systems.count} systems"
puts "  #{physical.count} physical systems"
puts "  #{hosts.count} virtual hosts"
puts "  #{guests.count} guests in known hosts"
puts "  #{guests_lacking_host.count} guests without a matching host"
