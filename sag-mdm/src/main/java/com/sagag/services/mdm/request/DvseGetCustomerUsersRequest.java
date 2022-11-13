package com.sagag.services.mdm.request;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class to provide DVSE XML get the list of customer users request.
 *
 */
@XmlRootElement(name = "GetCustomerUsers_V1")
public class DvseGetCustomerUsersRequest extends DvseCustomerRequest {
}
