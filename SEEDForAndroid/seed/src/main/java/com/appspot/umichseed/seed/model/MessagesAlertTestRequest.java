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
 * on 2014-12-24 at 18:36:19 UTC 
 * Modify at your own risk.
 */

package com.appspot.umichseed.seed.model;

/**
 * ProtoRPC message definition to represent a request to trigger a fake alert for testing/demo
 * purposes
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the seed. For a detailed explanation see:
 * <a href="http://code.google.com/p/google-http-java-client/wiki/JSON">http://code.google.com/p/google-http-java-client/wiki/JSON</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class MessagesAlertTestRequest extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key("patient_email")
  private java.lang.String patientEmail;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key("septic_risk")
  private java.lang.Double septicRisk;

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getPatientEmail() {
    return patientEmail;
  }

  /**
   * @param patientEmail patientEmail or {@code null} for none
   */
  public MessagesAlertTestRequest setPatientEmail(java.lang.String patientEmail) {
    this.patientEmail = patientEmail;
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
  public MessagesAlertTestRequest setSepticRisk(java.lang.Double septicRisk) {
    this.septicRisk = septicRisk;
    return this;
  }

  @Override
  public MessagesAlertTestRequest set(String fieldName, Object value) {
    return (MessagesAlertTestRequest) super.set(fieldName, value);
  }

  @Override
  public MessagesAlertTestRequest clone() {
    return (MessagesAlertTestRequest) super.clone();
  }

}
