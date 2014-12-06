/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * This code was generated by https://code.google.com/p/google-apis-client-generator/
 * (build: 2014-11-17 18:43:33 UTC)
 * on 2014-12-04 at 22:24:58 UTC 
 * Modify at your own risk.
 */

package com.appspot.umichseed.seed.model;

/**
 * ProtoRPC message definition to represent a patient
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the seed. For a detailed explanation see:
 * <a href="http://code.google.com/p/google-http-java-client/wiki/JSON">http://code.google.com/p/google-http-java-client/wiki/JSON</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class MessagesPatientPut extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key("basis_pass")
  private java.lang.String basisPass;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String diagnosis;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key("doctor_email")
  private java.lang.String doctorEmail;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String email;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key("first_name")
  private java.lang.String firstName;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key("last_name")
  private java.lang.String lastName;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String phone;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key("septic_risk")
  private java.lang.Double septicRisk;

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getBasisPass() {
    return basisPass;
  }

  /**
   * @param basisPass basisPass or {@code null} for none
   */
  public MessagesPatientPut setBasisPass(java.lang.String basisPass) {
    this.basisPass = basisPass;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getDiagnosis() {
    return diagnosis;
  }

  /**
   * @param diagnosis diagnosis or {@code null} for none
   */
  public MessagesPatientPut setDiagnosis(java.lang.String diagnosis) {
    this.diagnosis = diagnosis;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getDoctorEmail() {
    return doctorEmail;
  }

  /**
   * @param doctorEmail doctorEmail or {@code null} for none
   */
  public MessagesPatientPut setDoctorEmail(java.lang.String doctorEmail) {
    this.doctorEmail = doctorEmail;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getEmail() {
    return email;
  }

  /**
   * @param email email or {@code null} for none
   */
  public MessagesPatientPut setEmail(java.lang.String email) {
    this.email = email;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getFirstName() {
    return firstName;
  }

  /**
   * @param firstName firstName or {@code null} for none
   */
  public MessagesPatientPut setFirstName(java.lang.String firstName) {
    this.firstName = firstName;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getLastName() {
    return lastName;
  }

  /**
   * @param lastName lastName or {@code null} for none
   */
  public MessagesPatientPut setLastName(java.lang.String lastName) {
    this.lastName = lastName;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getPhone() {
    return phone;
  }

  /**
   * @param phone phone or {@code null} for none
   */
  public MessagesPatientPut setPhone(java.lang.String phone) {
    this.phone = phone;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Double getSepticRisk() {
    return septicRisk;
  }

  /**
   * @param septicRisk septicRisk or {@code null} for none
   */
  public MessagesPatientPut setSepticRisk(java.lang.Double septicRisk) {
    this.septicRisk = septicRisk;
    return this;
  }

  @Override
  public MessagesPatientPut set(String fieldName, Object value) {
    return (MessagesPatientPut) super.set(fieldName, value);
  }

  @Override
  public MessagesPatientPut clone() {
    return (MessagesPatientPut) super.clone();
  }

}
