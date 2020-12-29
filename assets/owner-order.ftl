${order.receiver} hat "${order.items}" bestellt.

Die Kontaktdaten sind:

Rechnungsadresse: ${order.address}
Lieferadresse: ${order.deliveryAddress!"siehe Rechnungsadresse"}
E-Mail: ${order.email}
Firma/Verein: ${order.company!"/"}
Telefon: ${order.telephone}