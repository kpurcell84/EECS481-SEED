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
 * on 2014-10-26 at 18:49:56 UTC 
 * Modify at your own risk.
 */

package com.appspot.umichseed.seed.model;

/**
 * ProtoRPC message definition to represent a single piece of patient data
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the seed. For a detailed explanation see:
 * <a href="http://code.google.com/p/google-http-java-client/wiki/JSON">http://code.google.com/p/google-http-java-client/wiki/JSON</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class SeedApiMessagesPQuantDataResponse extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key("activity_type")
  private java.lang.String activityType;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key("air_temp")
  private java.lang.Double airTemp;

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
  private java.lang.Double gsr;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key("heart_rate") @com.google.api.client.json.JsonString
  private java.lang.Long heartRate;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key("skin_temp")
  private java.lang.Double skinTemp;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key("time_taken")
  private com.google.api.client.util.DateTime timeTaken;

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getActivityType() {
    return activityType;
  }

  /**
   * @param activityType activityType or {@code null} for none
   */
  public SeedApiMessagesPQuantDataResponse setActivityType(java.lang.String activityType) {
    this.activityType = activityType;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Double getAirTemp() {
    return airTemp;
  }

  /**
   * @param airTemp airTemp or {@code null} for none
   */
  public SeedApiMessagesPQuantDataResponse setAirTemp(java.lang.Double airTemp) {
    this.airTemp = airTemp;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getBloodPressure() {
    return bloodPressure;
  }

  /**
   * @param bloodPressure bloodPressure or {@code null} for none
   */
  public SeedApiMessagesPQuantDataResponse setBloodPressure(java.lang.String bloodPressure) {
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
  public SeedApiMessagesPQuantDataResponse setBodyTemp(java.lang.Double bodyTemp) {
    this.bodyTemp = bodyTemp;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Double getGsr() {
    return gsr;
  }

  /**
   * @param gsr gsr or {@code null} for none
   */
  public SeedApiMessagesPQuantDataResponse setGsr(java.lang.Double gsr) {
    this.gsr = gsr;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Long getHeartRate() {
    return heartRate;
  }

  /**
   * @param heartRate heartRate or {@code null} for none
   */
  public SeedApiMessagesPQuantDataResponse setHeartRate(java.lang.Long heartRate) {
    this.heartRate = heartRate;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Double getSkinTemp() {
    return skinTemp;
  }

  /**
   * @param skinTemp skinTemp or {@code null} for none
   */
  public SeedApiMessagesPQuantDataResponse setSkinTemp(java.lang.Double skinTemp) {
    this.skinTemp = skinTemp;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public com.google.api.client.util.DateTime getTimeTaken() {
    return timeTaken;
  }

  /**
   * @param timeTaken timeTaken or {@code null} for none
   */
  public SeedApiMessagesPQuantDataResponse setTimeTaken(com.google.api.client.util.DateTime timeTaken) {
    this.timeTaken = timeTaken;
    return this;
  }

  @Override
  public SeedApiMessagesPQuantDataResponse set(String fieldName, Object value) {
    return (SeedApiMessagesPQuantDataResponse) super.set(fieldName, value);
  }

  @Override
  public SeedApiMessagesPQuantDataResponse clone() {
    return (SeedApiMessagesPQuantDataResponse) super.clone();
  }

}
