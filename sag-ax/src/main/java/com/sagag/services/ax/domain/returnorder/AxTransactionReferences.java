package com.sagag.services.ax.domain.returnorder;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
public class AxTransactionReferences implements Serializable {

  private static final long serialVersionUID = 5596022880996926607L;

  private List<AxTransactionReference> transactions;

}

