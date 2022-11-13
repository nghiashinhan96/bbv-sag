import { ReturnBasketPart } from './return-basket-part.model';
import { ReturnBasketTime } from './return-basket-time.model';

export class ReturnBasket {
    parts: ReturnBasketPart[];
    times: ReturnBasketTime[];
    customer_nr: string;
    order_type: string;
    order_number: string;
    order_date: string;
    order_total_with_vat: string;
    order_total_no_vat: string;
    delivery_type: string; // pickup or tour
    payment_type: string; // cash or credit
    address_company: string; // LAdresseFirma
    address_street: string; // LAdresseStrasse
    address_postcode: string; // LAdressePLZ
    address_city: string; // LAdresseOrt
    order_note: string; // Bemerkung
}
