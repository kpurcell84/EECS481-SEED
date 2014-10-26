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
 * (build: 2014-07-22 21:53:01 UTC)
 * on 2014-10-17 at 19:02:29 UTC 
 * Modify at your own risk.
 */

package com.appspot.umichseed.seed.model;

/**
 * ProtoRPC message definition to represent manual data to be inserted in the datastore
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the seed. For a detailed explanation see:
 * <a href="http://code.google.com/p/google-http-java-client/wiki/JSON">http://code.google.com/p/google-http-java-client/wiki/JSON</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class SeedApiMessagesPatientManualDataPut extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key("blood_pressure")
  private java.lang.String bloodPressure;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key("body_temp")
  private java.lang.Double bodyTemp;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String email;

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getBloodPressure() {
    return bloodPressure;
  }

  /**
   * @param bloodPressure bloodPressure or {@code null} for none
   */
  public SeedApiMessagesPatientManualDataPut setBloodPressure(java.lang.String bloodPressure) {
    this.bloodPressure = bloodPressure;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Double getBodyTemp() {
    return bodyTemp;
  }

  /**
   * @param bodyTemp bodyTemp or {@code null} for none
   */
  public SeedApiMessagesPatientManualDataPut setBodyTemp(java.lang.Double bodyTemp) {
    this.bodyTemp = bodyTemp;
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
  public SeedApiMessagesPatientManualDataPut setEmail(java.lang.String email) {
    this.email = email;
    return this;
  }

  @Override
  public SeedApiMessagesPatientManualDataPut set(String fieldName, Object value) {
    return (SeedApiMessagesPatientManualDataPut) super.set(fieldName, value);
  }

  @Override
  public SeedApiMessagesPatientManualDataPut clone() {
    return (SeedApiMessagesPatientManualDataPut) super.clone();
  }

}
