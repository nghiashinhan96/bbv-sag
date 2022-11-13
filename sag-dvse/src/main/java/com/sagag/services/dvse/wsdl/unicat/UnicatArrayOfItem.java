package com.sagag.services.dvse.wsdl.unicat;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UnicatArrayOfItem", propOrder = {
    "item"
})
public class UnicatArrayOfItem {

    @XmlElement(name = "UnicatItem", nillable = true)
    protected List<UnicatItem> item;

    public List<UnicatItem> getItem() {
        if (item == null) {
            item = new ArrayList<UnicatItem>();
        }
        return this.item;
    }

}
