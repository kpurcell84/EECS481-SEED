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
 * on 2014-11-23 at 22:24:20 UTC 
 * Modify at your own risk.
 */

package com.appspot.umichseed.seed.model;

/**
 * ProtoRPC message definition to represent a change of a patients diagnosis
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the seed. For a detailed explanation see:
 * <a href="http://code.google.com/p/google-http-java-client/wiki/JSON">http://code.google.com/p/google-http-java-client/wiki/JSON</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class MessagesPatientDiagnosisPut extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String diagnosis;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String email;

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getDiagnosis() {
    return diagnosis;
  }

  /**
   * @param diagnosis diagnosis or {@code null} for none
   */
  public MessagesPatientDiagnosisPut setDiagnosis(java.lang.String diagnosis) {
    this.diagnosis = diagnosis;
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
  public MessagesPatientDiagnosisPut setEmail(java.lang.String email) {
    this.email = email;
    return this;
  }

  @Override
  public MessagesPatientDiagnosisPut set(String fieldName, Object value) {
    return (MessagesPatientDiagnosisPut) super.set(fieldName, value);
  }

  @Override
  public MessagesPatientDiagnosisPut clone() {
    return (MessagesPatientDiagnosisPut) super.clone();
  }

}
