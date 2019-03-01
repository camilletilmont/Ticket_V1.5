/*
 * Axelor Business Solutions
 *
 * Copyright (C) 2019 Axelor (<http://axelor.com>).
 *
 * This program is free software: you can redistribute it and/or  modify
 * it under the terms of the GNU Affero General Public License, version 3,
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.axelor.apps.helpdesk.db;

import com.axelor.auth.db.AuditableModel;
import com.axelor.db.annotations.Widget;
import com.axelor.team.db.Team;
import com.google.common.base.MoreObjects;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Type;

@Entity
@Table(
  name = "HELPDESK_SLA",
  indexes = {
    @Index(columnList = "name"),
    @Index(columnList = "team"),
    @Index(columnList = "ticket_type")
  }
)
public class Sla extends AuditableModel {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HELPDESK_SLA_SEQ")
  @SequenceGenerator(
    name = "HELPDESK_SLA_SEQ",
    sequenceName = "HELPDESK_SLA_SEQ",
    allocationSize = 1
  )
  private Long id;

  @Widget(title = "SLA policy name")
  @NotNull
  private String name;

  @Widget(title = "Team")
  @ManyToOne(
    fetch = FetchType.LAZY,
    cascade = {CascadeType.PERSIST, CascadeType.MERGE}
  )
  private Team team;

  @Widget(title = "Minimum priority", selection = "helpdesk.priority.select")
  private Integer prioritySelect = 2;

  @Widget(title = "Ticket type")
  @ManyToOne(
    fetch = FetchType.LAZY,
    cascade = {CascadeType.PERSIST, CascadeType.MERGE}
  )
  private TicketType ticketType;

  @Widget(title = "Reach stage", selection = "helpdesk.status.select")
  private Integer reachStageSelect = 0;

  @Widget(title = "days")
  @Min(0)
  private Integer days = 0;

  @Widget(title = "hours")
  @Min(0)
  private Integer hours = 0;

  @Widget(title = "Working Days")
  private Boolean isWorkingDays = Boolean.FALSE;

  @Widget(title = "Description")
  @Lob
  @Basic(fetch = FetchType.LAZY)
  @Type(type = "text")
  private String description;

  @Widget(title = "Attributes")
  @Basic(fetch = FetchType.LAZY)
  @Type(type = "json")
  private String attrs;

  public Sla() {}

  public Sla(String name) {
    this.name = name;
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Team getTeam() {
    return team;
  }

  public void setTeam(Team team) {
    this.team = team;
  }

  public Integer getPrioritySelect() {
    return prioritySelect == null ? 0 : prioritySelect;
  }

  public void setPrioritySelect(Integer prioritySelect) {
    this.prioritySelect = prioritySelect;
  }

  public TicketType getTicketType() {
    return ticketType;
  }

  public void setTicketType(TicketType ticketType) {
    this.ticketType = ticketType;
  }

  public Integer getReachStageSelect() {
    return reachStageSelect == null ? 0 : reachStageSelect;
  }

  public void setReachStageSelect(Integer reachStageSelect) {
    this.reachStageSelect = reachStageSelect;
  }

  public Integer getDays() {
    return days == null ? 0 : days;
  }

  public void setDays(Integer days) {
    this.days = days;
  }

  public Integer getHours() {
    return hours == null ? 0 : hours;
  }

  public void setHours(Integer hours) {
    this.hours = hours;
  }

  public Boolean getIsWorkingDays() {
    return isWorkingDays == null ? Boolean.FALSE : isWorkingDays;
  }

  public void setIsWorkingDays(Boolean isWorkingDays) {
    this.isWorkingDays = isWorkingDays;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getAttrs() {
    return attrs;
  }

  public void setAttrs(String attrs) {
    this.attrs = attrs;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) return false;
    if (this == obj) return true;
    if (!(obj instanceof Sla)) return false;

    final Sla other = (Sla) obj;
    if (this.getId() != null || other.getId() != null) {
      return Objects.equals(this.getId(), other.getId());
    }

    return false;
  }

  @Override
  public int hashCode() {
    return 31;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("id", getId())
        .add("name", getName())
        .add("prioritySelect", getPrioritySelect())
        .add("reachStageSelect", getReachStageSelect())
        .add("days", getDays())
        .add("hours", getHours())
        .add("isWorkingDays", getIsWorkingDays())
        .omitNullValues()
        .toString();
  }
}
