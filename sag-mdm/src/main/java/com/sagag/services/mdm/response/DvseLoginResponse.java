package com.sagag.services.mdm.response;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class to receive DVSE XML login response.
 *
 */
@XmlRootElement(name = "ResponseDataOfLoginCustomerUser_V2")
public class DvseLoginResponse {
    @XmlElement(name = "Data")
    private LoginData data;

    public String getSessionId() {
        return data.getSessionId();
    }
}

@XmlRootElement(name = "Data")
class LoginData {
    private String sessionId;

    @XmlElement(name = "SessionID")
    String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
