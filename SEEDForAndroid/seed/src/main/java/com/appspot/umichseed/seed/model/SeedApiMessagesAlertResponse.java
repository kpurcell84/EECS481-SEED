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
 * ProtoRPC message definition to represent a previously triggered alert
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the seed. For a detailed explanation see:
 * <a href="http://code.google.com/p/google-http-java-client/wiki/JSON">http://code.google.com/p/google-http-java-client/wiki/JSON</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class SeedApiMessagesAlertResponse extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String message;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key("patient_email")
  private java.lang.String patientEmail;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String priority;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key("time_alerted")
  private com.google.api.client.util.DateTime timeAlerted;

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getMessage() {
    return message;
  }

  /**
   * @param message message or {@code null} for none
   */
  public SeedApiMessagesAlertResponse setMessage(java.lang.String message) {
    this.message = message;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getPatientEmail() {
    return patientEmail;
  }

  /**
   * @param patientEmail patientEmail or {@code null} for none
   */
  public SeedApiMessagesAlertResponse setPatientEmail(java.lang.String patientEmail) {
    this.patientEmail = patientEmail;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getPriority() {
    return priority;
  }

  /**
   * @param priority priority or {@code null} for none
   */
  public SeedApiMessagesAlertResponse setPriority(java.lang.String priority) {
    this.priority = priority;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public com.google.api.client.util.DateTime getTimeAlerted() {
    return timeAlerted;
  }

  /**
   * @param timeAlerted timeAlerted or {@code null} for none
   */
  public SeedApiMessagesAlertResponse setTimeAlerted(com.google.api.client.util.DateTime timeAlerted) {
    this.timeAlerted = timeAlerted;
    return this;
  }

  @Override
  public SeedApiMessagesAlertResponse set(String fieldName, Object value) {
    return (SeedApiMessagesAlertResponse) super.set(fieldName, value);
  }

  @Override
  public SeedApiMessagesAlertResponse clone() {
    return (SeedApiMessagesAlertResponse) super.clone();
  }

}
