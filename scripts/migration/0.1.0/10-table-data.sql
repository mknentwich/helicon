-- WARNING: The application must be run first on the old database to update the DDL schema, then this script can be run
-- This script fills the new shipping column in product_order for existing orderings from the state relation

-- using the shipping from the delivery address for the new shipping column in the product_order table if the delivery address is not null
update product_order p
set shipping = (select z.shipping
                from address a
                         left join state s on s.id = a.state_id
                         left join zone z on z.id = s.zone_id
                where a.id = p.delivery_address_id
                limit 1)
where delivery_address_id is not null;

-- using the shipping from the identity address for the new shipping column in the product_order table if the delivery address is null
update product_order p
set shipping = (select z.shipping
                from identity i
                         left join address a on a.id = i.address_id
                         left join state s on s.id = a.state_id
                         left join zone z on z.id = s.zone_id
                where i.id = p.identity_id
                limit 1)
where delivery_address_id is null;