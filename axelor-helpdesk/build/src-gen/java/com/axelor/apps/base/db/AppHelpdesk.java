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
package com.axelor.apps.base.db;

import com.axelor.db.EntityHelper;
import com.axelor.db.annotations.Widget;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;
import org.hibernate.annotations.Type;

@Entity
@Cacheable
@Table(name = "BASE_APP_HELPDESK")
public class AppHelpdesk extends App {

  @Widget(title = "SLA")
  private Boolean isSla = Boolean.FALSE;

  @Widget(title = "Manage Timer")
  private Boolean manageTimer = Boolean.FALSE;

  @Widget(title = "Attributes")
  @Basic(fetch = FetchType.LAZY)
  @Type(type = "json")
  private String attrs;

  public AppHelpdesk() {}

  public Boolean getIsSla() {
    return isSla == null ? Boolean.FALSE : isSla;
  }

  public void setIsSla(Boolean isSla) {
    this.isSla = isSla;
  }

  public Boolean getManageTimer() {
    return manageTimer == null ? Boolean.FALSE : manageTimer;
  }

  public void setManageTimer(Boolean manageTimer) {
    this.manageTimer = manageTimer;
  }

  public String getAttrs() {
    return attrs;
  }

  public void setAttrs(String attrs) {
    this.attrs = attrs;
  }

  @Override
  public boolean equals(Object obj) {
    return EntityHelper.equals(this, obj);
  }

  @Override
  public int hashCode() {
    return EntityHelper.hashCode(this);
  }

  @Override
  public String toString() {
    return EntityHelper.toString(this);
  }
}
