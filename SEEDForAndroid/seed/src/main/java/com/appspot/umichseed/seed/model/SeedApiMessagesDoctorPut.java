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
 * (build: 2014-10-28 17:08:27 UTC)
 * on 2014-11-17 at 19:11:08 UTC 
 * Modify at your own risk.
 */

package com.appspot.umichseed.seed.model;

/**
 * ProtoRPC message definition to represent a doctor
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the seed. For a detailed explanation see:
 * <a href="http://code.google.com/p/google-http-java-client/wiki/JSON">http://code.google.com/p/google-http-java-client/wiki/JSON</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class SeedApiMessagesDoctorPut extends com.google.api.client.json.GenericJson {

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
  @com.google.api.client.util.Key
  private java.lang.String hospital;

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
   * @return value or {@code null} for none
   */
  public java.lang.String getEmail() {
    return email;
  }

  /**
   * @param email email or {@code null} for none
   */
  public SeedApiMessagesDoctorPut setEmail(java.lang.String email) {
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
  public SeedApiMessagesDoctorPut setFirstName(java.lang.String firstName) {
    this.firstName = firstName;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getHospital() {
    return hospital;
  }

  /**
   * @param hospital hospital or {@code null} for none
   */
  public SeedApiMessagesDoctorPut setHospital(java.lang.String hospital) {
    this.hospital = hospital;
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
  public SeedApiMessagesDoctorPut setLastName(java.lang.String lastName) {
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
  public SeedApiMessagesDoctorPut setPhone(java.lang.String phone) {
    this.phone = phone;
    return this;
  }

  @Override
  public SeedApiMessagesDoctorPut set(String fieldName, Object value) {
    return (SeedApiMessagesDoctorPut) super.set(fieldName, value);
  }

  @Override
  public SeedApiMessagesDoctorPut clone() {
    return (SeedApiMessagesDoctorPut) super.clone();
  }

}
