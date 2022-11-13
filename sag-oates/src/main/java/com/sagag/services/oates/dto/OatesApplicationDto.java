package com.sagag.services.oates.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sagag.services.domain.article.oil.OilUseInterval;
import com.sagag.services.oates.domain.OatesNoteRef;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OatesApplicationDto implements Serializable {

  private static final long serialVersionUID = -980058421870330643L;

  private String id;

  private String guid;

  private String name;

  private String appType;

  private String appTypeOriginal;

  private String displayName;

  private String displayCoption;

  private String displayLubeNoteRefs;

  private String displayCapacity;

  private List<String> axIds;

  private List<OatesProductDto> products;

  private List<OatesAppNoteDto> appNotes;

  private List<OatesNoteRef> noteRefs;

  private List<OatesChangeIntervalDto> changeIntervals;

  @JsonIgnore
  public List<String> getAxIds() {
    return ListUtils.emptyIfNull(this.axIds);
  }

  @JsonIgnore
  public String getValidDisplayName() {
    return StringUtils.defaultIfBlank(this.getDisplayCoption(), this.getDisplayName());
  }

  @JsonIgnore
  public List<String> getPimIds() {
    return this.getAxIds();
  }

  @JsonIgnore
  public List<String> getCapacities() {
    if (StringUtils.isBlank(this.getDisplayCapacity())) {
      return Collections.emptyList();
    }
    return Arrays.asList(this.getDisplayCapacity());
  }

  @JsonIgnore
  public List<OilUseInterval> getIntervals() {
    if (CollectionUtils.isEmpty(this.getChangeIntervals())) {
      return Collections.emptyList();
    }
    return this.getChangeIntervals().stream()
        .filter(interval -> StringUtils.equals(this.id, interval.getApplicationId()))
        .findFirst().map(oilUseIntervalsConverter()).orElseGet(() -> Collections.emptyList());
  }

  private static Function<OatesChangeIntervalDto, List<OilUseInterval>> oilUseIntervalsConverter() {
    return changeInterval -> ListUtils.emptyIfNull(changeInterval.getIntervals()).stream()
        .map(interval -> {
          final OilUseInterval useInterval = new OilUseInterval();
          useInterval.setUseName(interval.getDisplayName());
          useInterval.setInterval(Stream.of(interval.getText(), interval.getDisplayUnit())
              .collect(Collectors.joining(StringUtils.SPACE)));
          return useInterval;
        }).collect(Collectors.toList());
  }

  public List<String> getAppNotes() {
    if (CollectionUtils.isEmpty(this.appNotes) || CollectionUtils.isEmpty(this.noteRefs)) {
      return Collections.emptyList();
    }
    final List<String> appNoteList = new ArrayList<>();

    for (OatesNoteRef noteRef : this.noteRefs) {
      this.appNotes.stream().filter(filterByOatesNoteRef(noteRef)).findFirst()
      .map(OatesAppNoteDto::getText).ifPresent(appNoteList::add);
    }

    return appNoteList;
  }

	public List<OatesAppNoteDto> getOriginAppNotes() {
		return ListUtils.emptyIfNull(this.appNotes);
	}

  private static Predicate<OatesAppNoteDto> filterByOatesNoteRef(OatesNoteRef noteRef) {
    return appNote -> StringUtils.equals(noteRef.getId(), appNote.getId())
        && StringUtils.equals(noteRef.getNoteIndex(), appNote.getNoteIndex())
        && StringUtils.equals(noteRef.getNoteClass(), appNote.getNoteClass());
  }
}
