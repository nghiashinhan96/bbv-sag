package com.sagag.services.dvse.wsdl.unicat;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfQuantity", propOrder = {
    "quantity"
})
public class ArrayOfQuantity {

    @XmlElement(name = "Quantity", nillable = true)
    protected List<Quantity> quantity;

    /**
     * Gets the value of the quantity property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the quantity property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getQuantity().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Quantity }
     *
     *
     */
    public List<Quantity> getQuantity() {
        if (quantity == null) {
            quantity = new ArrayList<Quantity>();
        }
        return this.quantity;
    }

}
