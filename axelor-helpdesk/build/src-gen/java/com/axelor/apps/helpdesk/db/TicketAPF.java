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

import com.axelor.apps.base.db.Partner;
import com.axelor.apps.base.db.Timer;
import com.axelor.apps.project.db.Project;
import com.axelor.auth.db.User;
import com.axelor.db.EntityHelper;
import com.axelor.db.annotations.Sequence;
import com.axelor.db.annotations.Track;
import com.axelor.db.annotations.TrackEvent;
import com.axelor.db.annotations.TrackField;
import com.axelor.db.annotations.TrackMessage;
import com.axelor.db.annotations.Widget;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import org.hibernate.annotations.Type;

/** " */
@Entity
@Table(
  name = "HELPDESK_TICKET_APF",
  indexes = {
    @Index(columnList = "project"),
    @Index(columnList = "customer"),
    @Index(columnList = "contact_partner"),
    @Index(columnList = "sla_policy"),
    @Index(columnList = "assigned_to_user"),
    @Index(columnList = "responsible_user"),
    @Index(columnList = "ticket_type")
  }
)
@Track(
  fields = {
    @TrackField(name = "ticketSeq"),
    @TrackField(name = "sujetAPF"),
    @TrackField(name = "statusSelect", on = TrackEvent.UPDATE)
  },
  messages = {
    @TrackMessage(message = "Ticket created", condition = "true", on = TrackEvent.CREATE),
    @TrackMessage(message = "Ticket In Progress", condition = "statusSelect == 1", tag = "info"),
    @TrackMessage(message = "Ticket In Resolved", condition = "statusSelect == 2", tag = "info"),
    @TrackMessage(message = "Ticket Closed", condition = "statusSelect == 3", tag = "success")
  }
)
public class TicketAPF extends Ticket {

  @Widget(title = "Ticket Number", readonly = true)
  @Sequence("ticket.apf.order.seq")
  private String ticketSeq;

  @Widget(title = "Sujet")
  private String sujetAPF = "APF";

  @Widget(title = "Materiel", selection = "helpdesk.ticket.materiel.apf.select")
  private String matosAPF;

  @Widget(title = "N° IMEI :")
  private String numIMEI;

  @Widget(title = "N° Sim :")
  private String numSim;

  @Widget(title = "Modele : ", selection = "helpdesk.ticket.model.montre.select")
  private String modelMontre;

  @Widget(title = "Modele :")
  private String modele;

  @Widget(title = "N° du Transmetteur :")
  private String numTransmetteur;

  @Widget(title = "N° Serie :")
  private String numSerie;

  @Widget(title = "Transmetteur")
  private Boolean recepTransmetteur = Boolean.FALSE;

  @Widget(title = "Tablette")
  private Boolean recepTab = Boolean.FALSE;

  @Widget(title = "Montre")
  private Boolean recepMontre = Boolean.FALSE;

  @Widget(title = "Boitier Chargeur")
  private Boolean recepBoiterChargeur = Boolean.FALSE;

  @Widget(title = "Chargeur")
  private Boolean recepChargeur = Boolean.FALSE;

  @Widget(title = "Cordon USB")
  private Boolean recepUsb = Boolean.FALSE;

  @Widget(title = "Cordon Téléphone")
  private Boolean recepCordonTel = Boolean.FALSE;

  @Widget(title = "Prise Téléphone Femelle")
  private Boolean recepPriseTel = Boolean.FALSE;

  @Widget(title = "Alimentation")
  private Boolean recepAlim = Boolean.FALSE;

  @Widget(title = "Bouton")
  private Boolean recepBouton = Boolean.FALSE;

  @Widget(title = "Bracelet Anti-chute")
  private Boolean recepBracelet = Boolean.FALSE;

  @Widget(title = "Vis")
  private Boolean recepVis = Boolean.FALSE;

  @Widget(title = "Support Mural")
  private Boolean recepSupportMural = Boolean.FALSE;

  @Widget(title = "Doc. Client")
  private Boolean recepDoc = Boolean.FALSE;

  @Widget(title = "Etat", selection = "helpdesk.ticket.reception.etat.select")
  private String recepEtat;

  @Widget(title = "Motif Etat :", selection = "helpdesk.ticket.apf.etat.tynetec.select")
  private String receptMotifTyn;

  @Widget(title = "Motif Etat :", selection = "helpdesk.ticket.apf.etat.tablette.select")
  private String receptMotifTab;

  @Widget(title = "Motif Etat :", selection = "helpdesk.ticket.apf.etat.montre.select")
  private String receptMotifMontre;

  @Widget(title = "Motif Etat :", selection = "helpdesk.ticket.apf.etat.solem.select")
  private String receptMotifSol;

  @Widget(title = "Autre :")
  private String motifEtatAutre;

  @Widget(title = "Présence Sim")
  private Boolean recepPresenceSim = Boolean.FALSE;

  @Widget(title = "Go Traitement")
  private Boolean checkGoTraitement = Boolean.FALSE;

  @Widget(title = "Config. Solem")
  private Boolean checkSolem = Boolean.FALSE;

  @Widget(title = "Charge")
  private Boolean checkCharge = Boolean.FALSE;

  @Widget(title = "Flashage ROM")
  private Boolean checkFlash = Boolean.FALSE;

  @Widget(title = "Config. Tyn")
  private Boolean checkTyn = Boolean.FALSE;

  @Widget(title = "Config. Bouton")
  private Boolean checkConfigBouton = Boolean.FALSE;

  @Widget(title = "Tests")
  private Boolean checkTest = Boolean.FALSE;

  @Widget(title = "Motif(s) Panne(s) :", selection = "helpdesk.ticket.apf.panne.tynetec.select")
  private String checkMotifPanneTyn;

  @Widget(title = "Motif(s) Panne(s) :", selection = "helpdesk.ticket.apf.panne.tablette.select")
  private String checkMotifPanneTab;

  @Widget(title = "Motif(s) Panne(s) :", selection = "helpdesk.ticket.apf.panne.montre.select")
  private String checkMotifPanneMontre;

  @Widget(title = "Motif(s) Panne(s) :", selection = "helpdesk.ticket.apf.panne.solem.select")
  private String checkMotifPanneSol;

  @Widget(title = "Transmetteur")
  private Boolean reconTransmet = Boolean.FALSE;

  @Widget(title = "Tablette")
  private Boolean reconTablette = Boolean.FALSE;

  @Widget(title = "Montre")
  private Boolean reconMontre = Boolean.FALSE;

  @Widget(title = "Boitier Chargeur")
  private Boolean reconBoitier = Boolean.FALSE;

  @Widget(title = "Chargeur")
  private Boolean reconChargeur = Boolean.FALSE;

  @Widget(title = "Cordon USB")
  private Boolean reconUsb = Boolean.FALSE;

  @Widget(title = "Présence Sim")
  private Boolean reconPresenceSim = Boolean.FALSE;

  @Widget(title = "Cordon Téléphone")
  private Boolean reconCordonTel = Boolean.FALSE;

  @Widget(title = "Prise Téléphone Femelle")
  private Boolean reconPriseTel = Boolean.FALSE;

  @Widget(title = "Alim.")
  private Boolean reconAlim = Boolean.FALSE;

  @Widget(title = "Vis")
  private Boolean reconVis = Boolean.FALSE;

  @Widget(title = "Support Mural")
  private Boolean reconSupportMural = Boolean.FALSE;

  @Widget(title = "Bouton")
  private Boolean reconBouton = Boolean.FALSE;

  @Widget(title = "Bracelet Anti-chute")
  private Boolean reconBraceletChute = Boolean.FALSE;

  @Widget(title = "Sachet Accessoires")
  private Boolean reconSachet = Boolean.FALSE;

  @Widget(title = "Accessoires")
  private Boolean reconAccessoires = Boolean.FALSE;

  @Widget(title = "Notice")
  private Boolean reconNotice = Boolean.FALSE;

  @Widget(title = "Autre :")
  private String reconAutre;

  @Widget(title = "Date Expedition")
  private LocalDate exped;

  @Widget(title = "Arkea Ok")
  private Boolean arkeaOkAPF = Boolean.FALSE;

  private Boolean checkAPFCustom1 = Boolean.FALSE;

  private Boolean checkAPFCustom2 = Boolean.FALSE;

  private Boolean checkAPFCustom3 = Boolean.FALSE;

  private Boolean checkAPFCustom4 = Boolean.FALSE;

  private String stringAPFCustom1;

  private String stringAPFCustom2;

  private String stringAPFCustom3;

  private String stringAPFCustom4;

  @Widget(title = "Project")
  @ManyToOne(
    fetch = FetchType.LAZY,
    cascade = {CascadeType.PERSIST, CascadeType.MERGE}
  )
  private Project project;

  @Widget(title = "Customer")
  @ManyToOne(
    fetch = FetchType.LAZY,
    cascade = {CascadeType.PERSIST, CascadeType.MERGE}
  )
  private Partner customer;

  @Widget(title = "Customer contact")
  @ManyToOne(
    fetch = FetchType.LAZY,
    cascade = {CascadeType.PERSIST, CascadeType.MERGE}
  )
  private Partner contactPartner;

  @Widget(title = "SLA Policy")
  @ManyToOne(
    fetch = FetchType.LAZY,
    cascade = {CascadeType.PERSIST, CascadeType.MERGE}
  )
  private Sla slaPolicy;

  @Widget(title = "Assigned to")
  @ManyToOne(
    fetch = FetchType.LAZY,
    cascade = {CascadeType.PERSIST, CascadeType.MERGE}
  )
  private User assignedToUser;

  @Widget(title = "User in charge of the issue")
  @ManyToOne(
    fetch = FetchType.LAZY,
    cascade = {CascadeType.PERSIST, CascadeType.MERGE}
  )
  private User responsibleUser;

  @Widget(title = "Status", selection = "helpdesk.status.apf.select")
  private Integer statusSelect = 0;

  @Widget(title = "Ticket type")
  @ManyToOne(
    fetch = FetchType.LAZY,
    cascade = {CascadeType.PERSIST, CascadeType.MERGE}
  )
  private TicketType ticketType;

  @Widget(title = "Start date")
  private LocalDateTime startDateT;

  @Widget(title = "End date")
  private LocalDateTime endDateT;

  @Widget(title = "Deadline")
  private LocalDateTime deadlineDateT;

  @Widget(title = "SLA completed")
  private Boolean isSlaCompleted = Boolean.FALSE;

  @Widget(title = "Duration")
  private Long duration = 0L;

  @Widget(title = "Description")
  @Lob
  @Basic(fetch = FetchType.LAZY)
  @Type(type = "text")
  private String description;

  @Widget(title = "Progress (%)", selection = "helpdesk.ticket.progress.select")
  private Integer progressSelect = 0;

  @Widget(title = "Priority", selection = "helpdesk.priority.select")
  private Integer prioritySelect = 2;

  private String mailSubject;

  @OneToMany(
    fetch = FetchType.LAZY,
    cascade = {CascadeType.PERSIST, CascadeType.MERGE}
  )
  private List<Timer> timerList;

  @Widget(title = "Real total duration (Hours)")
  @DecimalMin("0")
  private BigDecimal realTotalDuration = new BigDecimal("0");

  @Widget(readonly = true)
  private Boolean timerState = Boolean.FALSE;

  @Widget(title = "Attributes")
  @Basic(fetch = FetchType.LAZY)
  @Type(type = "json")
  private String attrs;

  public TicketAPF() {}

  public String getTicketSeq() {
    return ticketSeq;
  }

  public void setTicketSeq(String ticketSeq) {
    this.ticketSeq = ticketSeq;
  }

  public String getSujetAPF() {
    return sujetAPF;
  }

  public void setSujetAPF(String sujetAPF) {
    this.sujetAPF = sujetAPF;
  }

  public String getMatosAPF() {
    return matosAPF;
  }

  public void setMatosAPF(String matosAPF) {
    this.matosAPF = matosAPF;
  }

  public String getNumIMEI() {
    return numIMEI;
  }

  public void setNumIMEI(String numIMEI) {
    this.numIMEI = numIMEI;
  }

  public String getNumSim() {
    return numSim;
  }

  public void setNumSim(String numSim) {
    this.numSim = numSim;
  }

  public String getModelMontre() {
    return modelMontre;
  }

  public void setModelMontre(String modelMontre) {
    this.modelMontre = modelMontre;
  }

  public String getModele() {
    return modele;
  }

  public void setModele(String modele) {
    this.modele = modele;
  }

  public String getNumTransmetteur() {
    return numTransmetteur;
  }

  public void setNumTransmetteur(String numTransmetteur) {
    this.numTransmetteur = numTransmetteur;
  }

  public String getNumSerie() {
    return numSerie;
  }

  public void setNumSerie(String numSerie) {
    this.numSerie = numSerie;
  }

  public Boolean getRecepTransmetteur() {
    return recepTransmetteur == null ? Boolean.FALSE : recepTransmetteur;
  }

  public void setRecepTransmetteur(Boolean recepTransmetteur) {
    this.recepTransmetteur = recepTransmetteur;
  }

  public Boolean getRecepTab() {
    return recepTab == null ? Boolean.FALSE : recepTab;
  }

  public void setRecepTab(Boolean recepTab) {
    this.recepTab = recepTab;
  }

  public Boolean getRecepMontre() {
    return recepMontre == null ? Boolean.FALSE : recepMontre;
  }

  public void setRecepMontre(Boolean recepMontre) {
    this.recepMontre = recepMontre;
  }

  public Boolean getRecepBoiterChargeur() {
    return recepBoiterChargeur == null ? Boolean.FALSE : recepBoiterChargeur;
  }

  public void setRecepBoiterChargeur(Boolean recepBoiterChargeur) {
    this.recepBoiterChargeur = recepBoiterChargeur;
  }

  public Boolean getRecepChargeur() {
    return recepChargeur == null ? Boolean.FALSE : recepChargeur;
  }

  public void setRecepChargeur(Boolean recepChargeur) {
    this.recepChargeur = recepChargeur;
  }

  public Boolean getRecepUsb() {
    return recepUsb == null ? Boolean.FALSE : recepUsb;
  }

  public void setRecepUsb(Boolean recepUsb) {
    this.recepUsb = recepUsb;
  }

  public Boolean getRecepCordonTel() {
    return recepCordonTel == null ? Boolean.FALSE : recepCordonTel;
  }

  public void setRecepCordonTel(Boolean recepCordonTel) {
    this.recepCordonTel = recepCordonTel;
  }

  public Boolean getRecepPriseTel() {
    return recepPriseTel == null ? Boolean.FALSE : recepPriseTel;
  }

  public void setRecepPriseTel(Boolean recepPriseTel) {
    this.recepPriseTel = recepPriseTel;
  }

  public Boolean getRecepAlim() {
    return recepAlim == null ? Boolean.FALSE : recepAlim;
  }

  public void setRecepAlim(Boolean recepAlim) {
    this.recepAlim = recepAlim;
  }

  public Boolean getRecepBouton() {
    return recepBouton == null ? Boolean.FALSE : recepBouton;
  }

  public void setRecepBouton(Boolean recepBouton) {
    this.recepBouton = recepBouton;
  }

  public Boolean getRecepBracelet() {
    return recepBracelet == null ? Boolean.FALSE : recepBracelet;
  }

  public void setRecepBracelet(Boolean recepBracelet) {
    this.recepBracelet = recepBracelet;
  }

  public Boolean getRecepVis() {
    return recepVis == null ? Boolean.FALSE : recepVis;
  }

  public void setRecepVis(Boolean recepVis) {
    this.recepVis = recepVis;
  }

  public Boolean getRecepSupportMural() {
    return recepSupportMural == null ? Boolean.FALSE : recepSupportMural;
  }

  public void setRecepSupportMural(Boolean recepSupportMural) {
    this.recepSupportMural = recepSupportMural;
  }

  public Boolean getRecepDoc() {
    return recepDoc == null ? Boolean.FALSE : recepDoc;
  }

  public void setRecepDoc(Boolean recepDoc) {
    this.recepDoc = recepDoc;
  }

  public String getRecepEtat() {
    return recepEtat;
  }

  public void setRecepEtat(String recepEtat) {
    this.recepEtat = recepEtat;
  }

  public String getReceptMotifTyn() {
    return receptMotifTyn;
  }

  public void setReceptMotifTyn(String receptMotifTyn) {
    this.receptMotifTyn = receptMotifTyn;
  }

  public String getReceptMotifTab() {
    return receptMotifTab;
  }

  public void setReceptMotifTab(String receptMotifTab) {
    this.receptMotifTab = receptMotifTab;
  }

  public String getReceptMotifMontre() {
    return receptMotifMontre;
  }

  public void setReceptMotifMontre(String receptMotifMontre) {
    this.receptMotifMontre = receptMotifMontre;
  }

  public String getReceptMotifSol() {
    return receptMotifSol;
  }

  public void setReceptMotifSol(String receptMotifSol) {
    this.receptMotifSol = receptMotifSol;
  }

  public String getMotifEtatAutre() {
    return motifEtatAutre;
  }

  public void setMotifEtatAutre(String motifEtatAutre) {
    this.motifEtatAutre = motifEtatAutre;
  }

  public Boolean getRecepPresenceSim() {
    return recepPresenceSim == null ? Boolean.FALSE : recepPresenceSim;
  }

  public void setRecepPresenceSim(Boolean recepPresenceSim) {
    this.recepPresenceSim = recepPresenceSim;
  }

  public Boolean getCheckGoTraitement() {
    return checkGoTraitement == null ? Boolean.FALSE : checkGoTraitement;
  }

  public void setCheckGoTraitement(Boolean checkGoTraitement) {
    this.checkGoTraitement = checkGoTraitement;
  }

  public Boolean getCheckSolem() {
    return checkSolem == null ? Boolean.FALSE : checkSolem;
  }

  public void setCheckSolem(Boolean checkSolem) {
    this.checkSolem = checkSolem;
  }

  public Boolean getCheckCharge() {
    return checkCharge == null ? Boolean.FALSE : checkCharge;
  }

  public void setCheckCharge(Boolean checkCharge) {
    this.checkCharge = checkCharge;
  }

  public Boolean getCheckFlash() {
    return checkFlash == null ? Boolean.FALSE : checkFlash;
  }

  public void setCheckFlash(Boolean checkFlash) {
    this.checkFlash = checkFlash;
  }

  public Boolean getCheckTyn() {
    return checkTyn == null ? Boolean.FALSE : checkTyn;
  }

  public void setCheckTyn(Boolean checkTyn) {
    this.checkTyn = checkTyn;
  }

  public Boolean getCheckConfigBouton() {
    return checkConfigBouton == null ? Boolean.FALSE : checkConfigBouton;
  }

  public void setCheckConfigBouton(Boolean checkConfigBouton) {
    this.checkConfigBouton = checkConfigBouton;
  }

  public Boolean getCheckTest() {
    return checkTest == null ? Boolean.FALSE : checkTest;
  }

  public void setCheckTest(Boolean checkTest) {
    this.checkTest = checkTest;
  }

  public String getCheckMotifPanneTyn() {
    return checkMotifPanneTyn;
  }

  public void setCheckMotifPanneTyn(String checkMotifPanneTyn) {
    this.checkMotifPanneTyn = checkMotifPanneTyn;
  }

  public String getCheckMotifPanneTab() {
    return checkMotifPanneTab;
  }

  public void setCheckMotifPanneTab(String checkMotifPanneTab) {
    this.checkMotifPanneTab = checkMotifPanneTab;
  }

  public String getCheckMotifPanneMontre() {
    return checkMotifPanneMontre;
  }

  public void setCheckMotifPanneMontre(String checkMotifPanneMontre) {
    this.checkMotifPanneMontre = checkMotifPanneMontre;
  }

  public String getCheckMotifPanneSol() {
    return checkMotifPanneSol;
  }

  public void setCheckMotifPanneSol(String checkMotifPanneSol) {
    this.checkMotifPanneSol = checkMotifPanneSol;
  }

  public Boolean getReconTransmet() {
    return reconTransmet == null ? Boolean.FALSE : reconTransmet;
  }

  public void setReconTransmet(Boolean reconTransmet) {
    this.reconTransmet = reconTransmet;
  }

  public Boolean getReconTablette() {
    return reconTablette == null ? Boolean.FALSE : reconTablette;
  }

  public void setReconTablette(Boolean reconTablette) {
    this.reconTablette = reconTablette;
  }

  public Boolean getReconMontre() {
    return reconMontre == null ? Boolean.FALSE : reconMontre;
  }

  public void setReconMontre(Boolean reconMontre) {
    this.reconMontre = reconMontre;
  }

  public Boolean getReconBoitier() {
    return reconBoitier == null ? Boolean.FALSE : reconBoitier;
  }

  public void setReconBoitier(Boolean reconBoitier) {
    this.reconBoitier = reconBoitier;
  }

  public Boolean getReconChargeur() {
    return reconChargeur == null ? Boolean.FALSE : reconChargeur;
  }

  public void setReconChargeur(Boolean reconChargeur) {
    this.reconChargeur = reconChargeur;
  }

  public Boolean getReconUsb() {
    return reconUsb == null ? Boolean.FALSE : reconUsb;
  }

  public void setReconUsb(Boolean reconUsb) {
    this.reconUsb = reconUsb;
  }

  public Boolean getReconPresenceSim() {
    return reconPresenceSim == null ? Boolean.FALSE : reconPresenceSim;
  }

  public void setReconPresenceSim(Boolean reconPresenceSim) {
    this.reconPresenceSim = reconPresenceSim;
  }

  public Boolean getReconCordonTel() {
    return reconCordonTel == null ? Boolean.FALSE : reconCordonTel;
  }

  public void setReconCordonTel(Boolean reconCordonTel) {
    this.reconCordonTel = reconCordonTel;
  }

  public Boolean getReconPriseTel() {
    return reconPriseTel == null ? Boolean.FALSE : reconPriseTel;
  }

  public void setReconPriseTel(Boolean reconPriseTel) {
    this.reconPriseTel = reconPriseTel;
  }

  public Boolean getReconAlim() {
    return reconAlim == null ? Boolean.FALSE : reconAlim;
  }

  public void setReconAlim(Boolean reconAlim) {
    this.reconAlim = reconAlim;
  }

  public Boolean getReconVis() {
    return reconVis == null ? Boolean.FALSE : reconVis;
  }

  public void setReconVis(Boolean reconVis) {
    this.reconVis = reconVis;
  }

  public Boolean getReconSupportMural() {
    return reconSupportMural == null ? Boolean.FALSE : reconSupportMural;
  }

  public void setReconSupportMural(Boolean reconSupportMural) {
    this.reconSupportMural = reconSupportMural;
  }

  public Boolean getReconBouton() {
    return reconBouton == null ? Boolean.FALSE : reconBouton;
  }

  public void setReconBouton(Boolean reconBouton) {
    this.reconBouton = reconBouton;
  }

  public Boolean getReconBraceletChute() {
    return reconBraceletChute == null ? Boolean.FALSE : reconBraceletChute;
  }

  public void setReconBraceletChute(Boolean reconBraceletChute) {
    this.reconBraceletChute = reconBraceletChute;
  }

  public Boolean getReconSachet() {
    return reconSachet == null ? Boolean.FALSE : reconSachet;
  }

  public void setReconSachet(Boolean reconSachet) {
    this.reconSachet = reconSachet;
  }

  public Boolean getReconAccessoires() {
    return reconAccessoires == null ? Boolean.FALSE : reconAccessoires;
  }

  public void setReconAccessoires(Boolean reconAccessoires) {
    this.reconAccessoires = reconAccessoires;
  }

  public Boolean getReconNotice() {
    return reconNotice == null ? Boolean.FALSE : reconNotice;
  }

  public void setReconNotice(Boolean reconNotice) {
    this.reconNotice = reconNotice;
  }

  public String getReconAutre() {
    return reconAutre;
  }

  public void setReconAutre(String reconAutre) {
    this.reconAutre = reconAutre;
  }

  public LocalDate getExped() {
    return exped;
  }

  public void setExped(LocalDate exped) {
    this.exped = exped;
  }

  public Boolean getArkeaOkAPF() {
    return arkeaOkAPF == null ? Boolean.FALSE : arkeaOkAPF;
  }

  public void setArkeaOkAPF(Boolean arkeaOkAPF) {
    this.arkeaOkAPF = arkeaOkAPF;
  }

  public Boolean getCheckAPFCustom1() {
    return checkAPFCustom1 == null ? Boolean.FALSE : checkAPFCustom1;
  }

  public void setCheckAPFCustom1(Boolean checkAPFCustom1) {
    this.checkAPFCustom1 = checkAPFCustom1;
  }

  public Boolean getCheckAPFCustom2() {
    return checkAPFCustom2 == null ? Boolean.FALSE : checkAPFCustom2;
  }

  public void setCheckAPFCustom2(Boolean checkAPFCustom2) {
    this.checkAPFCustom2 = checkAPFCustom2;
  }

  public Boolean getCheckAPFCustom3() {
    return checkAPFCustom3 == null ? Boolean.FALSE : checkAPFCustom3;
  }

  public void setCheckAPFCustom3(Boolean checkAPFCustom3) {
    this.checkAPFCustom3 = checkAPFCustom3;
  }

  public Boolean getCheckAPFCustom4() {
    return checkAPFCustom4 == null ? Boolean.FALSE : checkAPFCustom4;
  }

  public void setCheckAPFCustom4(Boolean checkAPFCustom4) {
    this.checkAPFCustom4 = checkAPFCustom4;
  }

  public String getStringAPFCustom1() {
    return stringAPFCustom1;
  }

  public void setStringAPFCustom1(String stringAPFCustom1) {
    this.stringAPFCustom1 = stringAPFCustom1;
  }

  public String getStringAPFCustom2() {
    return stringAPFCustom2;
  }

  public void setStringAPFCustom2(String stringAPFCustom2) {
    this.stringAPFCustom2 = stringAPFCustom2;
  }

  public String getStringAPFCustom3() {
    return stringAPFCustom3;
  }

  public void setStringAPFCustom3(String stringAPFCustom3) {
    this.stringAPFCustom3 = stringAPFCustom3;
  }

  public String getStringAPFCustom4() {
    return stringAPFCustom4;
  }

  public void setStringAPFCustom4(String stringAPFCustom4) {
    this.stringAPFCustom4 = stringAPFCustom4;
  }

  public Project getProject() {
    return project;
  }

  public void setProject(Project project) {
    this.project = project;
  }

  public Partner getCustomer() {
    return customer;
  }

  public void setCustomer(Partner customer) {
    this.customer = customer;
  }

  public Partner getContactPartner() {
    return contactPartner;
  }

  public void setContactPartner(Partner contactPartner) {
    this.contactPartner = contactPartner;
  }

  public Sla getSlaPolicy() {
    return slaPolicy;
  }

  public void setSlaPolicy(Sla slaPolicy) {
    this.slaPolicy = slaPolicy;
  }

  public User getAssignedToUser() {
    return assignedToUser;
  }

  public void setAssignedToUser(User assignedToUser) {
    this.assignedToUser = assignedToUser;
  }

  public User getResponsibleUser() {
    return responsibleUser;
  }

  public void setResponsibleUser(User responsibleUser) {
    this.responsibleUser = responsibleUser;
  }

  public Integer getStatusSelect() {
    return statusSelect == null ? 0 : statusSelect;
  }

  public void setStatusSelect(Integer statusSelect) {
    this.statusSelect = statusSelect;
  }

  public TicketType getTicketType() {
    return ticketType;
  }

  public void setTicketType(TicketType ticketType) {
    this.ticketType = ticketType;
  }

  public LocalDateTime getStartDateT() {
    return startDateT;
  }

  public void setStartDateT(LocalDateTime startDateT) {
    this.startDateT = startDateT;
  }

  public LocalDateTime getEndDateT() {
    return endDateT;
  }

  public void setEndDateT(LocalDateTime endDateT) {
    this.endDateT = endDateT;
  }

  public LocalDateTime getDeadlineDateT() {
    return deadlineDateT;
  }

  public void setDeadlineDateT(LocalDateTime deadlineDateT) {
    this.deadlineDateT = deadlineDateT;
  }

  public Boolean getIsSlaCompleted() {
    return isSlaCompleted == null ? Boolean.FALSE : isSlaCompleted;
  }

  public void setIsSlaCompleted(Boolean isSlaCompleted) {
    this.isSlaCompleted = isSlaCompleted;
  }

  public Long getDuration() {
    return duration == null ? 0L : duration;
  }

  public void setDuration(Long duration) {
    this.duration = duration;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Integer getProgressSelect() {
    return progressSelect == null ? 0 : progressSelect;
  }

  public void setProgressSelect(Integer progressSelect) {
    this.progressSelect = progressSelect;
  }

  public Integer getPrioritySelect() {
    return prioritySelect == null ? 0 : prioritySelect;
  }

  public void setPrioritySelect(Integer prioritySelect) {
    this.prioritySelect = prioritySelect;
  }

  public String getMailSubject() {
    return mailSubject;
  }

  public void setMailSubject(String mailSubject) {
    this.mailSubject = mailSubject;
  }

  public List<Timer> getTimerList() {
    return timerList;
  }

  public void setTimerList(List<Timer> timerList) {
    this.timerList = timerList;
  }

  /**
   * Add the given {@link Timer} item to the {@code timerList}.
   *
   * @param item the item to add
   */
  public void addTimerListItem(Timer item) {
    if (getTimerList() == null) {
      setTimerList(new ArrayList<>());
    }
    getTimerList().add(item);
  }

  /**
   * Remove the given {@link Timer} item from the {@code timerList}.
   *
   * <p>It sets {@code item.null = null} to break the relationship.
   *
   * @param item the item to remove
   */
  public void removeTimerListItem(Timer item) {
    if (getTimerList() == null) {
      return;
    }
    getTimerList().remove(item);
  }

  /**
   * Clear the {@code timerList} collection.
   *
   * <p>It sets {@code item.null = null} to break the relationship.
   */
  public void clearTimerList() {
    if (getTimerList() != null) {
      getTimerList().clear();
    }
  }

  public BigDecimal getRealTotalDuration() {
    return realTotalDuration == null ? BigDecimal.ZERO : realTotalDuration;
  }

  public void setRealTotalDuration(BigDecimal realTotalDuration) {
    this.realTotalDuration = realTotalDuration;
  }

  public Boolean getTimerState() {
    return timerState == null ? Boolean.FALSE : timerState;
  }

  public void setTimerState(Boolean timerState) {
    this.timerState = timerState;
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
