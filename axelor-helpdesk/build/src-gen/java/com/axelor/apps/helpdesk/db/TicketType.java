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
import com.google.common.base.MoreObjects;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Type;

@Entity
@Table(
  name = "HELPDESK_TICKET_TYPE",
  indexes = {@Index(columnList = "name")}
)
public class TicketType extends AuditableModel {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HELPDESK_TICKET_TYPE_SEQ")
  @SequenceGenerator(
    name = "HELPDESK_TICKET_TYPE_SEQ",
    sequenceName = "HELPDESK_TICKET_TYPE_SEQ",
    allocationSize = 1
  )
  private Long id;

  @NotNull private String name;

  @Widget(title = "Attributes")
  @Basic(fetch = FetchType.LAZY)
  @Type(type = "json")
  private String attrs;

  public TicketType() {}

  public TicketType(String name) {
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
    if (!(obj instanceof TicketType)) return false;

    final TicketType other = (TicketType) obj;
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
        .omitNullValues()
        .toString();
  }
}
