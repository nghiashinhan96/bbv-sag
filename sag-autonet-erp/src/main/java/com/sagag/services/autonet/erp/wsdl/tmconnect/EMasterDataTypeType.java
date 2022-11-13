
package com.sagag.services.autonet.erp.wsdl.tmconnect;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EMasterDataType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="EMasterDataType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Undefined"/>
 *     &lt;enumeration value="Article"/>
 *     &lt;enumeration value="ArticleTmf"/>
 *     &lt;enumeration value="RepairTime"/>
 *     &lt;enumeration value="ItemsCollection"/>
 *     &lt;enumeration value="Tour"/>
 *     &lt;enumeration value="Vehicle"/>
 *     &lt;enumeration value="Customer"/>
 *     &lt;enumeration value="TextBlock"/>
 *     &lt;enumeration value="DispatchType"/>
 *     &lt;enumeration value="SelectionItem"/>
 *     &lt;enumeration value="AvailabilitySate"/>
 *     &lt;enumeration value="Icon"/>
 *     &lt;enumeration value="Address"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "EMasterDataType")
@XmlEnum
public enum EMasterDataTypeType {

    @XmlEnumValue("Undefined")
    UNDEFINED("Undefined"),
    @XmlEnumValue("Article")
    ARTICLE("Article"),
    @XmlEnumValue("ArticleTmf")
    ARTICLE_TMF("ArticleTmf"),
    @XmlEnumValue("RepairTime")
    REPAIR_TIME("RepairTime"),
    @XmlEnumValue("ItemsCollection")
    ITEMS_COLLECTION("ItemsCollection"),
    @XmlEnumValue("Tour")
    TOUR("Tour"),
    @XmlEnumValue("Vehicle")
    VEHICLE("Vehicle"),
    @XmlEnumValue("Customer")
    CUSTOMER("Customer"),
    @XmlEnumValue("TextBlock")
    TEXT_BLOCK("TextBlock"),
    @XmlEnumValue("DispatchType")
    DISPATCH_TYPE("DispatchType"),
    @XmlEnumValue("SelectionItem")
    SELECTION_ITEM("SelectionItem"),
    @XmlEnumValue("AvailabilitySate")
    AVAILABILITY_SATE("AvailabilitySate"),
    @XmlEnumValue("Icon")
    ICON("Icon"),
    @XmlEnumValue("Address")
    ADDRESS("Address");
    private final String value;

    EMasterDataTypeType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EMasterDataTypeType fromValue(String v) {
        for (EMasterDataTypeType c: EMasterDataTypeType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
