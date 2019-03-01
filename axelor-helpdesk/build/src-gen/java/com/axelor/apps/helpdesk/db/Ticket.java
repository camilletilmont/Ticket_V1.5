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
import com.axelor.auth.db.AuditableModel;
import com.axelor.auth.db.User;
import com.axelor.db.annotations.Track;
import com.axelor.db.annotations.TrackEvent;
import com.axelor.db.annotations.TrackField;
import com.axelor.db.annotations.TrackMessage;
import com.axelor.db.annotations.Widget;
import com.google.common.base.MoreObjects;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Type;

@Entity
@Table(
  name = "HELPDESK_TICKET",
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
    @TrackField(name = "sujetSelect"),
    @TrackField(name = "statusSelect", on = TrackEvent.UPDATE)
  },
  messages = {
    @TrackMessage(message = "Ticket created", condition = "true", on = TrackEvent.CREATE),
    @TrackMessage(message = "Ticket In Progress", condition = "statusSelect == 1", tag = "info"),
    @TrackMessage(message = "Ticket In Resolved", condition = "statusSelect == 2", tag = "success"),
    @TrackMessage(message = "Ticket Closed", condition = "statusSelect == 3", tag = "info")
  }
)
public class Ticket extends AuditableModel {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HELPDESK_TICKET_SEQ")
  @SequenceGenerator(
    name = "HELPDESK_TICKET_SEQ",
    sequenceName = "HELPDESK_TICKET_SEQ",
    allocationSize = 1
  )
  private Long id;

  @Widget(title = "Sujet", selection = "helpdesk.ticket.sujet.select")
  private String sujetSelect;

  @Widget(title = "Type Sujet", selection = "helpdesk.ticket.type.sujet.select")
  private String typeSujet;

  @Widget(title = "N° de Contrat :")
  @Size(max = 11)
  private String numCtr;

  @Widget(title = "Commercial En Charge :")
  @Size(max = 30)
  private String nomCommercial;

  @Widget(title = "Materiel", selection = "helpdesk.ticket.materiel.domicile.select")
  private String typeMaterielCtr;

  @Widget(title = "Utilisateur :")
  private String utilisateurCtr;

  @Widget(title = "Souscripteur :")
  private String souscripteurCtr;

  @Widget(title = "Test")
  private Boolean testCtr = Boolean.FALSE;

  @Widget(title = "Demo")
  private Boolean demoCtr = Boolean.FALSE;

  @Widget(title = "Pret")
  private Boolean pretCtr = Boolean.FALSE;

  @Widget(title = "Debut Pret")
  private LocalDateTime debutPret;

  @Widget(title = "Fin Pret")
  private LocalDateTime finPret;

  @Widget(title = "EFS", selection = "helpdesk.ticket.efs.select")
  private String efsSelect;

  @Widget(title = "Groupama", selection = "helpdesk.ticket.groupama.select")
  private String groupamaSelect;

  @Widget(title = "Type Contrat", selection = "helpdesk.ticket.type.contrat.select")
  private String typeContrat;

  @Widget(title = "Partenaire", selection = "helpdesk.ticket.partenaire.select")
  private String partenaireSelect;

  @Widget(title = "N° IMEI :")
  private String numIMEI;

  @Widget(title = "N° Sim :")
  private String numSim;

  @Widget(title = "Modele :")
  private String modele;

  @Widget(title = "N° du Transmetteur :")
  private String numTransmetteur;

  @Widget(title = "N° Serie :")
  private String numSerie;

  @Widget(title = "Motif SAV : ")
  private String motifSAV;

  @Widget(title = "Detecteur de Chute")
  private Boolean detecteurChute = Boolean.FALSE;

  @Widget(title = "Quantité")
  private Integer qteDetecteur = 0;

  @Widget(title = "A Facturer")
  private Boolean facturationDetec = Boolean.FALSE;

  @Widget(title = "Bon de Retour")
  private Boolean bonRetour = Boolean.FALSE;

  @Widget(title = "Bouton")
  private Boolean bouton = Boolean.FALSE;

  @Widget(title = "Quantité")
  private Integer qteBouton = 0;

  @Widget(title = "A Facturer")
  private Boolean facturationBouton = Boolean.FALSE;

  @Widget(title = "Boite à Clé")
  private Boolean boiteCle = Boolean.FALSE;

  @Widget(title = "A Facturer")
  private Boolean facturationBoite = Boolean.FALSE;

  @Widget(title = "Carte Sim", selection = "helpdesk.ticket.sim.select")
  private String carteSim;

  @Widget(title = "Carte Sim", selection = "helpdesk.ticket.sim.select")
  private String carteSimMontre;

  @Widget(title = "Option Aidant")
  private Boolean optAidantMontre = Boolean.FALSE;

  @Widget(title = "Option Sortie de périmètre")
  private Boolean sortiePerimMontre = Boolean.FALSE;

  @Widget(title = "Rayon de périmètre (en mètres) :")
  private Integer rayonPerimMontre = 0;

  @Widget(title = "Modele", selection = "helpdesk.ticket.model.montre.select")
  private String modelMontre;

  @Widget(title = "Destinataire SMS 1 :")
  @Size(max = 14)
  private String desti1Montre;

  @Widget(title = "Destinataire SMS 2 :")
  @Size(max = 14)
  private String desti2Montre;

  @Widget(title = "Destinataire SMS 3 :")
  @Size(max = 14)
  private String desti3Montre;

  @Widget(title = "SMS Odyssey :")
  private String smsOdyssey;

  @Widget(title = "Livraison", selection = "helpdesk.ticket.mode.livraison.select")
  private String modeLivraison;

  @Widget(title = "Adresse :")
  private String adressCtr;

  @Widget(title = "Date Expedition")
  private LocalDate exped;

  private Boolean checkCustom1 = Boolean.FALSE;

  private Boolean checkCustom2 = Boolean.FALSE;

  private Boolean checkCustom3 = Boolean.FALSE;

  private Boolean checkCustom4 = Boolean.FALSE;

  private Boolean checkCustom5 = Boolean.FALSE;

  private Boolean checkCustom6 = Boolean.FALSE;

  private Boolean checkCustom7 = Boolean.FALSE;

  private Boolean checkCustom8 = Boolean.FALSE;

  private Boolean checkCustom9 = Boolean.FALSE;

  private Boolean checkCustom10 = Boolean.FALSE;

  private Boolean checkCustom11 = Boolean.FALSE;

  private Boolean checkCustom12 = Boolean.FALSE;

  private Boolean checkCustom13 = Boolean.FALSE;

  private Boolean checkCustom14 = Boolean.FALSE;

  private Boolean checkCustom15 = Boolean.FALSE;

  private Boolean checkCustom16 = Boolean.FALSE;

  private Boolean checkCustom17 = Boolean.FALSE;

  private Boolean checkCustom18 = Boolean.FALSE;

  private Boolean checkCustom19 = Boolean.FALSE;

  private Boolean checkCustom20 = Boolean.FALSE;

  private String stringCustom1;

  private String stringCustom2;

  private String stringCustom3;

  private String stringCustom4;

  private String stringCustom5;

  private String stringCustom6;

  private String stringCustom7;

  private String stringCustom8;

  private String stringCustom9;

  private String stringCustom10;

  private String stringCustom11;

  private String stringCustom12;

  private String stringCustom13;

  private String stringCustom14;

  private String stringCustom15;

  private String stringCustom16;

  private String stringCustom17;

  private String stringCustom18;

  private String stringCustom19;

  private String stringCustom20;

  @Widget(title = "Neuf")
  private Boolean checkNeuf = Boolean.FALSE;

  @Widget(title = "Reconditionné")
  private Boolean checkRecon = Boolean.FALSE;

  @Widget(title = "A Facturer")
  private Boolean checkFacturation1 = Boolean.FALSE;

  @Widget(title = "Contre-Appel :")
  private String contreAppel1;

  @Widget(title = "Contre-Appel Montre :")
  private String contreAppelMontre;

  @Widget(title = "N° Sim :")
  private String numSimMontre;

  @Widget(title = "Ticket Number", readonly = true)
  private String ticketSeq;

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

  @Widget(title = "Status", selection = "helpdesk.status.select")
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

  public Ticket() {}

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  public String getSujetSelect() {
    return sujetSelect;
  }

  public void setSujetSelect(String sujetSelect) {
    this.sujetSelect = sujetSelect;
  }

  public String getTypeSujet() {
    return typeSujet;
  }

  public void setTypeSujet(String typeSujet) {
    this.typeSujet = typeSujet;
  }

  public String getNumCtr() {
    return numCtr;
  }

  public void setNumCtr(String numCtr) {
    this.numCtr = numCtr;
  }

  public String getNomCommercial() {
    return nomCommercial;
  }

  public void setNomCommercial(String nomCommercial) {
    this.nomCommercial = nomCommercial;
  }

  public String getTypeMaterielCtr() {
    return typeMaterielCtr;
  }

  public void setTypeMaterielCtr(String typeMaterielCtr) {
    this.typeMaterielCtr = typeMaterielCtr;
  }

  public String getUtilisateurCtr() {
    return utilisateurCtr;
  }

  public void setUtilisateurCtr(String utilisateurCtr) {
    this.utilisateurCtr = utilisateurCtr;
  }

  public String getSouscripteurCtr() {
    return souscripteurCtr;
  }

  public void setSouscripteurCtr(String souscripteurCtr) {
    this.souscripteurCtr = souscripteurCtr;
  }

  public Boolean getTestCtr() {
    return testCtr == null ? Boolean.FALSE : testCtr;
  }

  public void setTestCtr(Boolean testCtr) {
    this.testCtr = testCtr;
  }

  public Boolean getDemoCtr() {
    return demoCtr == null ? Boolean.FALSE : demoCtr;
  }

  public void setDemoCtr(Boolean demoCtr) {
    this.demoCtr = demoCtr;
  }

  public Boolean getPretCtr() {
    return pretCtr == null ? Boolean.FALSE : pretCtr;
  }

  public void setPretCtr(Boolean pretCtr) {
    this.pretCtr = pretCtr;
  }

  public LocalDateTime getDebutPret() {
    return debutPret;
  }

  public void setDebutPret(LocalDateTime debutPret) {
    this.debutPret = debutPret;
  }

  public LocalDateTime getFinPret() {
    return finPret;
  }

  public void setFinPret(LocalDateTime finPret) {
    this.finPret = finPret;
  }

  public String getEfsSelect() {
    return efsSelect;
  }

  public void setEfsSelect(String efsSelect) {
    this.efsSelect = efsSelect;
  }

  public String getGroupamaSelect() {
    return groupamaSelect;
  }

  public void setGroupamaSelect(String groupamaSelect) {
    this.groupamaSelect = groupamaSelect;
  }

  public String getTypeContrat() {
    return typeContrat;
  }

  public void setTypeContrat(String typeContrat) {
    this.typeContrat = typeContrat;
  }

  public String getPartenaireSelect() {
    return partenaireSelect;
  }

  public void setPartenaireSelect(String partenaireSelect) {
    this.partenaireSelect = partenaireSelect;
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

  public String getMotifSAV() {
    return motifSAV;
  }

  public void setMotifSAV(String motifSAV) {
    this.motifSAV = motifSAV;
  }

  public Boolean getDetecteurChute() {
    return detecteurChute == null ? Boolean.FALSE : detecteurChute;
  }

  public void setDetecteurChute(Boolean detecteurChute) {
    this.detecteurChute = detecteurChute;
  }

  public Integer getQteDetecteur() {
    return qteDetecteur == null ? 0 : qteDetecteur;
  }

  public void setQteDetecteur(Integer qteDetecteur) {
    this.qteDetecteur = qteDetecteur;
  }

  public Boolean getFacturationDetec() {
    return facturationDetec == null ? Boolean.FALSE : facturationDetec;
  }

  public void setFacturationDetec(Boolean facturationDetec) {
    this.facturationDetec = facturationDetec;
  }

  public Boolean getBonRetour() {
    return bonRetour == null ? Boolean.FALSE : bonRetour;
  }

  public void setBonRetour(Boolean bonRetour) {
    this.bonRetour = bonRetour;
  }

  public Boolean getBouton() {
    return bouton == null ? Boolean.FALSE : bouton;
  }

  public void setBouton(Boolean bouton) {
    this.bouton = bouton;
  }

  public Integer getQteBouton() {
    return qteBouton == null ? 0 : qteBouton;
  }

  public void setQteBouton(Integer qteBouton) {
    this.qteBouton = qteBouton;
  }

  public Boolean getFacturationBouton() {
    return facturationBouton == null ? Boolean.FALSE : facturationBouton;
  }

  public void setFacturationBouton(Boolean facturationBouton) {
    this.facturationBouton = facturationBouton;
  }

  public Boolean getBoiteCle() {
    return boiteCle == null ? Boolean.FALSE : boiteCle;
  }

  public void setBoiteCle(Boolean boiteCle) {
    this.boiteCle = boiteCle;
  }

  public Boolean getFacturationBoite() {
    return facturationBoite == null ? Boolean.FALSE : facturationBoite;
  }

  public void setFacturationBoite(Boolean facturationBoite) {
    this.facturationBoite = facturationBoite;
  }

  public String getCarteSim() {
    return carteSim;
  }

  public void setCarteSim(String carteSim) {
    this.carteSim = carteSim;
  }

  public String getCarteSimMontre() {
    return carteSimMontre;
  }

  public void setCarteSimMontre(String carteSimMontre) {
    this.carteSimMontre = carteSimMontre;
  }

  public Boolean getOptAidantMontre() {
    return optAidantMontre == null ? Boolean.FALSE : optAidantMontre;
  }

  public void setOptAidantMontre(Boolean optAidantMontre) {
    this.optAidantMontre = optAidantMontre;
  }

  public Boolean getSortiePerimMontre() {
    return sortiePerimMontre == null ? Boolean.FALSE : sortiePerimMontre;
  }

  public void setSortiePerimMontre(Boolean sortiePerimMontre) {
    this.sortiePerimMontre = sortiePerimMontre;
  }

  public Integer getRayonPerimMontre() {
    return rayonPerimMontre == null ? 0 : rayonPerimMontre;
  }

  public void setRayonPerimMontre(Integer rayonPerimMontre) {
    this.rayonPerimMontre = rayonPerimMontre;
  }

  public String getModelMontre() {
    return modelMontre;
  }

  public void setModelMontre(String modelMontre) {
    this.modelMontre = modelMontre;
  }

  public String getDesti1Montre() {
    return desti1Montre;
  }

  public void setDesti1Montre(String desti1Montre) {
    this.desti1Montre = desti1Montre;
  }

  public String getDesti2Montre() {
    return desti2Montre;
  }

  public void setDesti2Montre(String desti2Montre) {
    this.desti2Montre = desti2Montre;
  }

  public String getDesti3Montre() {
    return desti3Montre;
  }

  public void setDesti3Montre(String desti3Montre) {
    this.desti3Montre = desti3Montre;
  }

  public String getSmsOdyssey() {
    return smsOdyssey;
  }

  public void setSmsOdyssey(String smsOdyssey) {
    this.smsOdyssey = smsOdyssey;
  }

  public String getModeLivraison() {
    return modeLivraison;
  }

  public void setModeLivraison(String modeLivraison) {
    this.modeLivraison = modeLivraison;
  }

  public String getAdressCtr() {
    return adressCtr;
  }

  public void setAdressCtr(String adressCtr) {
    this.adressCtr = adressCtr;
  }

  public LocalDate getExped() {
    return exped;
  }

  public void setExped(LocalDate exped) {
    this.exped = exped;
  }

  public Boolean getCheckCustom1() {
    return checkCustom1 == null ? Boolean.FALSE : checkCustom1;
  }

  public void setCheckCustom1(Boolean checkCustom1) {
    this.checkCustom1 = checkCustom1;
  }

  public Boolean getCheckCustom2() {
    return checkCustom2 == null ? Boolean.FALSE : checkCustom2;
  }

  public void setCheckCustom2(Boolean checkCustom2) {
    this.checkCustom2 = checkCustom2;
  }

  public Boolean getCheckCustom3() {
    return checkCustom3 == null ? Boolean.FALSE : checkCustom3;
  }

  public void setCheckCustom3(Boolean checkCustom3) {
    this.checkCustom3 = checkCustom3;
  }

  public Boolean getCheckCustom4() {
    return checkCustom4 == null ? Boolean.FALSE : checkCustom4;
  }

  public void setCheckCustom4(Boolean checkCustom4) {
    this.checkCustom4 = checkCustom4;
  }

  public Boolean getCheckCustom5() {
    return checkCustom5 == null ? Boolean.FALSE : checkCustom5;
  }

  public void setCheckCustom5(Boolean checkCustom5) {
    this.checkCustom5 = checkCustom5;
  }

  public Boolean getCheckCustom6() {
    return checkCustom6 == null ? Boolean.FALSE : checkCustom6;
  }

  public void setCheckCustom6(Boolean checkCustom6) {
    this.checkCustom6 = checkCustom6;
  }

  public Boolean getCheckCustom7() {
    return checkCustom7 == null ? Boolean.FALSE : checkCustom7;
  }

  public void setCheckCustom7(Boolean checkCustom7) {
    this.checkCustom7 = checkCustom7;
  }

  public Boolean getCheckCustom8() {
    return checkCustom8 == null ? Boolean.FALSE : checkCustom8;
  }

  public void setCheckCustom8(Boolean checkCustom8) {
    this.checkCustom8 = checkCustom8;
  }

  public Boolean getCheckCustom9() {
    return checkCustom9 == null ? Boolean.FALSE : checkCustom9;
  }

  public void setCheckCustom9(Boolean checkCustom9) {
    this.checkCustom9 = checkCustom9;
  }

  public Boolean getCheckCustom10() {
    return checkCustom10 == null ? Boolean.FALSE : checkCustom10;
  }

  public void setCheckCustom10(Boolean checkCustom10) {
    this.checkCustom10 = checkCustom10;
  }

  public Boolean getCheckCustom11() {
    return checkCustom11 == null ? Boolean.FALSE : checkCustom11;
  }

  public void setCheckCustom11(Boolean checkCustom11) {
    this.checkCustom11 = checkCustom11;
  }

  public Boolean getCheckCustom12() {
    return checkCustom12 == null ? Boolean.FALSE : checkCustom12;
  }

  public void setCheckCustom12(Boolean checkCustom12) {
    this.checkCustom12 = checkCustom12;
  }

  public Boolean getCheckCustom13() {
    return checkCustom13 == null ? Boolean.FALSE : checkCustom13;
  }

  public void setCheckCustom13(Boolean checkCustom13) {
    this.checkCustom13 = checkCustom13;
  }

  public Boolean getCheckCustom14() {
    return checkCustom14 == null ? Boolean.FALSE : checkCustom14;
  }

  public void setCheckCustom14(Boolean checkCustom14) {
    this.checkCustom14 = checkCustom14;
  }

  public Boolean getCheckCustom15() {
    return checkCustom15 == null ? Boolean.FALSE : checkCustom15;
  }

  public void setCheckCustom15(Boolean checkCustom15) {
    this.checkCustom15 = checkCustom15;
  }

  public Boolean getCheckCustom16() {
    return checkCustom16 == null ? Boolean.FALSE : checkCustom16;
  }

  public void setCheckCustom16(Boolean checkCustom16) {
    this.checkCustom16 = checkCustom16;
  }

  public Boolean getCheckCustom17() {
    return checkCustom17 == null ? Boolean.FALSE : checkCustom17;
  }

  public void setCheckCustom17(Boolean checkCustom17) {
    this.checkCustom17 = checkCustom17;
  }

  public Boolean getCheckCustom18() {
    return checkCustom18 == null ? Boolean.FALSE : checkCustom18;
  }

  public void setCheckCustom18(Boolean checkCustom18) {
    this.checkCustom18 = checkCustom18;
  }

  public Boolean getCheckCustom19() {
    return checkCustom19 == null ? Boolean.FALSE : checkCustom19;
  }

  public void setCheckCustom19(Boolean checkCustom19) {
    this.checkCustom19 = checkCustom19;
  }

  public Boolean getCheckCustom20() {
    return checkCustom20 == null ? Boolean.FALSE : checkCustom20;
  }

  public void setCheckCustom20(Boolean checkCustom20) {
    this.checkCustom20 = checkCustom20;
  }

  public String getStringCustom1() {
    return stringCustom1;
  }

  public void setStringCustom1(String stringCustom1) {
    this.stringCustom1 = stringCustom1;
  }

  public String getStringCustom2() {
    return stringCustom2;
  }

  public void setStringCustom2(String stringCustom2) {
    this.stringCustom2 = stringCustom2;
  }

  public String getStringCustom3() {
    return stringCustom3;
  }

  public void setStringCustom3(String stringCustom3) {
    this.stringCustom3 = stringCustom3;
  }

  public String getStringCustom4() {
    return stringCustom4;
  }

  public void setStringCustom4(String stringCustom4) {
    this.stringCustom4 = stringCustom4;
  }

  public String getStringCustom5() {
    return stringCustom5;
  }

  public void setStringCustom5(String stringCustom5) {
    this.stringCustom5 = stringCustom5;
  }

  public String getStringCustom6() {
    return stringCustom6;
  }

  public void setStringCustom6(String stringCustom6) {
    this.stringCustom6 = stringCustom6;
  }

  public String getStringCustom7() {
    return stringCustom7;
  }

  public void setStringCustom7(String stringCustom7) {
    this.stringCustom7 = stringCustom7;
  }

  public String getStringCustom8() {
    return stringCustom8;
  }

  public void setStringCustom8(String stringCustom8) {
    this.stringCustom8 = stringCustom8;
  }

  public String getStringCustom9() {
    return stringCustom9;
  }

  public void setStringCustom9(String stringCustom9) {
    this.stringCustom9 = stringCustom9;
  }

  public String getStringCustom10() {
    return stringCustom10;
  }

  public void setStringCustom10(String stringCustom10) {
    this.stringCustom10 = stringCustom10;
  }

  public String getStringCustom11() {
    return stringCustom11;
  }

  public void setStringCustom11(String stringCustom11) {
    this.stringCustom11 = stringCustom11;
  }

  public String getStringCustom12() {
    return stringCustom12;
  }

  public void setStringCustom12(String stringCustom12) {
    this.stringCustom12 = stringCustom12;
  }

  public String getStringCustom13() {
    return stringCustom13;
  }

  public void setStringCustom13(String stringCustom13) {
    this.stringCustom13 = stringCustom13;
  }

  public String getStringCustom14() {
    return stringCustom14;
  }

  public void setStringCustom14(String stringCustom14) {
    this.stringCustom14 = stringCustom14;
  }

  public String getStringCustom15() {
    return stringCustom15;
  }

  public void setStringCustom15(String stringCustom15) {
    this.stringCustom15 = stringCustom15;
  }

  public String getStringCustom16() {
    return stringCustom16;
  }

  public void setStringCustom16(String stringCustom16) {
    this.stringCustom16 = stringCustom16;
  }

  public String getStringCustom17() {
    return stringCustom17;
  }

  public void setStringCustom17(String stringCustom17) {
    this.stringCustom17 = stringCustom17;
  }

  public String getStringCustom18() {
    return stringCustom18;
  }

  public void setStringCustom18(String stringCustom18) {
    this.stringCustom18 = stringCustom18;
  }

  public String getStringCustom19() {
    return stringCustom19;
  }

  public void setStringCustom19(String stringCustom19) {
    this.stringCustom19 = stringCustom19;
  }

  public String getStringCustom20() {
    return stringCustom20;
  }

  public void setStringCustom20(String stringCustom20) {
    this.stringCustom20 = stringCustom20;
  }

  public Boolean getCheckNeuf() {
    return checkNeuf == null ? Boolean.FALSE : checkNeuf;
  }

  public void setCheckNeuf(Boolean checkNeuf) {
    this.checkNeuf = checkNeuf;
  }

  public Boolean getCheckRecon() {
    return checkRecon == null ? Boolean.FALSE : checkRecon;
  }

  public void setCheckRecon(Boolean checkRecon) {
    this.checkRecon = checkRecon;
  }

  public Boolean getCheckFacturation1() {
    return checkFacturation1 == null ? Boolean.FALSE : checkFacturation1;
  }

  public void setCheckFacturation1(Boolean checkFacturation1) {
    this.checkFacturation1 = checkFacturation1;
  }

  public String getContreAppel1() {
    return contreAppel1;
  }

  public void setContreAppel1(String contreAppel1) {
    this.contreAppel1 = contreAppel1;
  }

  public String getContreAppelMontre() {
    return contreAppelMontre;
  }

  public void setContreAppelMontre(String contreAppelMontre) {
    this.contreAppelMontre = contreAppelMontre;
  }

  public String getNumSimMontre() {
    return numSimMontre;
  }

  public void setNumSimMontre(String numSimMontre) {
    this.numSimMontre = numSimMontre;
  }

  public String getTicketSeq() {
    return ticketSeq;
  }

  public void setTicketSeq(String ticketSeq) {
    this.ticketSeq = ticketSeq;
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
    if (obj == null) return false;
    if (this == obj) return true;
    if (!(obj instanceof Ticket)) return false;

    final Ticket other = (Ticket) obj;
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
        .add("sujetSelect", getSujetSelect())
        .add("typeSujet", getTypeSujet())
        .add("numCtr", getNumCtr())
        .add("nomCommercial", getNomCommercial())
        .add("typeMaterielCtr", getTypeMaterielCtr())
        .add("utilisateurCtr", getUtilisateurCtr())
        .add("souscripteurCtr", getSouscripteurCtr())
        .add("testCtr", getTestCtr())
        .add("demoCtr", getDemoCtr())
        .add("pretCtr", getPretCtr())
        .add("debutPret", getDebutPret())
        .omitNullValues()
        .toString();
  }
}
