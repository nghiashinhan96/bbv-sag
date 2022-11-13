
package com.sagag.services.stakis.erp.wsdl.tmconnect;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Vehicle complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Vehicle">
 *   &lt;complexContent>
 *     &lt;extension base="{http://topmotive.eu/TMConnect}BaseDto">
 *       &lt;sequence>
 *         &lt;element name="KTypeId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="ModelId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="MakeId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="Make" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ModelGroup" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Model" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Type" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MotorCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PlateId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Vin" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TypeCertificateNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Mileage" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="MileageShortIndicator" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VehicleOwner" type="{http://topmotive.eu/TMConnect}EntityLink" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Vehicle", propOrder = {
    "kTypeId",
    "modelId",
    "makeId",
    "make",
    "modelGroup",
    "model",
    "type",
    "motorCode",
    "plateId",
    "vin",
    "typeCertificateNumber",
    "mileage",
    "mileageShortIndicator",
    "vehicleOwner"
})
public class Vehicle
    extends BaseDto
{

    @XmlElement(name = "KTypeId")
    protected long kTypeId;
    @XmlElement(name = "ModelId")
    protected long modelId;
    @XmlElement(name = "MakeId")
    protected long makeId;
    @XmlElementRef(name = "Make", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<String> make;
    @XmlElementRef(name = "ModelGroup", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<String> modelGroup;
    @XmlElementRef(name = "Model", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<String> model;
    @XmlElementRef(name = "Type", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<String> type;
    @XmlElementRef(name = "MotorCode", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<String> motorCode;
    @XmlElementRef(name = "PlateId", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<String> plateId;
    @XmlElementRef(name = "Vin", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<String> vin;
    @XmlElementRef(name = "TypeCertificateNumber", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<String> typeCertificateNumber;
    @XmlElement(name = "Mileage")
    protected long mileage;
    @XmlElementRef(name = "MileageShortIndicator", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<String> mileageShortIndicator;
    @XmlElementRef(name = "VehicleOwner", namespace = "http://topmotive.eu/TMConnect", type = JAXBElement.class, required = false)
    protected JAXBElement<EntityLink> vehicleOwner;

    /**
     * Gets the value of the kTypeId property.
     * 
     */
    public long getKTypeId() {
        return kTypeId;
    }

    /**
     * Sets the value of the kTypeId property.
     * 
     */
    public void setKTypeId(long value) {
        this.kTypeId = value;
    }

    /**
     * Gets the value of the modelId property.
     * 
     */
    public long getModelId() {
        return modelId;
    }

    /**
     * Sets the value of the modelId property.
     * 
     */
    public void setModelId(long value) {
        this.modelId = value;
    }

    /**
     * Gets the value of the makeId property.
     * 
     */
    public long getMakeId() {
        return makeId;
    }

    /**
     * Sets the value of the makeId property.
     * 
     */
    public void setMakeId(long value) {
        this.makeId = value;
    }

    /**
     * Gets the value of the make property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getMake() {
        return make;
    }

    /**
     * Sets the value of the make property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setMake(JAXBElement<String> value) {
        this.make = value;
    }

    /**
     * Gets the value of the modelGroup property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getModelGroup() {
        return modelGroup;
    }

    /**
     * Sets the value of the modelGroup property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setModelGroup(JAXBElement<String> value) {
        this.modelGroup = value;
    }

    /**
     * Gets the value of the model property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getModel() {
        return model;
    }

    /**
     * Sets the value of the model property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setModel(JAXBElement<String> value) {
        this.model = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setType(JAXBElement<String> value) {
        this.type = value;
    }

    /**
     * Gets the value of the motorCode property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getMotorCode() {
        return motorCode;
    }

    /**
     * Sets the value of the motorCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setMotorCode(JAXBElement<String> value) {
        this.motorCode = value;
    }

    /**
     * Gets the value of the plateId property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getPlateId() {
        return plateId;
    }

    /**
     * Sets the value of the plateId property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setPlateId(JAXBElement<String> value) {
        this.plateId = value;
    }

    /**
     * Gets the value of the vin property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getVin() {
        return vin;
    }

    /**
     * Sets the value of the vin property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setVin(JAXBElement<String> value) {
        this.vin = value;
    }

    /**
     * Gets the value of the typeCertificateNumber property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getTypeCertificateNumber() {
        return typeCertificateNumber;
    }

    /**
     * Sets the value of the typeCertificateNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setTypeCertificateNumber(JAXBElement<String> value) {
        this.typeCertificateNumber = value;
    }

    /**
     * Gets the value of the mileage property.
     * 
     */
    public long getMileage() {
        return mileage;
    }

    /**
     * Sets the value of the mileage property.
     * 
     */
    public void setMileage(long value) {
        this.mileage = value;
    }

    /**
     * Gets the value of the mileageShortIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getMileageShortIndicator() {
        return mileageShortIndicator;
    }

    /**
     * Sets the value of the mileageShortIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setMileageShortIndicator(JAXBElement<String> value) {
        this.mileageShortIndicator = value;
    }

    /**
     * Gets the value of the vehicleOwner property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link EntityLink }{@code >}
     *     
     */
    public JAXBElement<EntityLink> getVehicleOwner() {
        return vehicleOwner;
    }

    /**
     * Sets the value of the vehicleOwner property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link EntityLink }{@code >}
     *     
     */
    public void setVehicleOwner(JAXBElement<EntityLink> value) {
        this.vehicleOwner = value;
    }

}
