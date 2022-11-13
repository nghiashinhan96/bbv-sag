package com.sagag.services.common.enums;

import com.sagag.services.common.domain.wss.WssMarginGroupDto;
import com.sagag.services.common.domain.wss.WssMarginGroupValueDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum WssMarginGroupEnum {
  MARGIN_COL_1(1) {
    @Override
    public Double getMarginColValue(WssMarginGroupValueDto marginGroup) {
      return marginGroup.getMargin1();
    }
  },
  MARGIN_COL_2(2) {
    @Override
    public Double getMarginColValue(WssMarginGroupValueDto marginGroup) {
      return marginGroup.getMargin2();
    }
  },
  MARGIN_COL_3(3) {
    @Override
    public Double getMarginColValue(WssMarginGroupValueDto marginGroup) {
      return marginGroup.getMargin3();
    }
  },
  MARGIN_COL_4(4) {
    @Override
    public Double getMarginColValue(WssMarginGroupValueDto marginGroup) {
      return marginGroup.getMargin4();
    }
  },
  MARGIN_COL_5(5) {
    @Override
    public Double getMarginColValue(WssMarginGroupValueDto marginGroup) {
      return marginGroup.getMargin5();
    }
  },
  MARGIN_COL_6(6) {
    @Override
    public Double getMarginColValue(WssMarginGroupValueDto marginGroup) {
      return marginGroup.getMargin6();
    }
  },
  MARGIN_COL_7(7) {
    @Override
    public Double getMarginColValue(WssMarginGroupValueDto marginGroup) {
      return marginGroup.getMargin7();
    }
  };

  private final int value;

  public static WssMarginGroupEnum findByValue(final int value) {
    return Arrays.stream(values()).filter(margin -> margin.value == value).findFirst().orElse(null);
  }

  public static List<WssMarginGroupDto> getAll() {
    return Arrays.stream(values()).map(marginGroup -> new WssMarginGroupDto(marginGroup.getValue(), marginGroup.name())).collect(Collectors.toList());
  }

  public abstract Double getMarginColValue(WssMarginGroupValueDto marginGroup);
}
