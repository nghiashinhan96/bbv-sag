package com.sagag.services.mdm.request;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class to provide DVSE XML header.
 *
 */
@XmlRootElement(name = "DataHeader")
public class DvseRequestHeader {
    private String sid;
    private String timestamp;
    private String function;

    @XmlElement(name = "Sid")
    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    @XmlElement(name = "TimeStamp")
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @XmlElement(name = "Function")
    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }
}
