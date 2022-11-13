export class ReturnBasketPart {
  art_nr: string;
  art_id: number;
  total_price_gross_with_vat: string;
  total_price_gross_no_vat: string;
  price_gross_no_vat: string;
  art_description: string;
  quantity: number;

  total_price_net_with_vat: string;
  total_price_net_no_vat: string;
  price_net_no_vat: string;
  total_price_uvpe_with_vat: string; // Column 10. total of Suggested Retail Price include vat
  total_price_uvpe_no_vat: string; // Column 11. total of Single price UVPE
  price_uvpe_no_vat: string; // Column 12. single UVPE price
  vin: string; // Just for next step
  brand: string;
  art_additional_description: string;
}
