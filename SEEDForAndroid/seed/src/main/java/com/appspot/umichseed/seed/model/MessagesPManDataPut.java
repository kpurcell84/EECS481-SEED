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
 * on 2014-11-23 at 22:06:32 UTC 
 * Modify at your own risk.
 */

package com.appspot.umichseed.seed.model;

/**
 * ProtoRPC message definition to represent a set of survey responses
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the seed. For a detailed explanation see:
 * <a href="http://code.google.com/p/google-http-java-client/wiki/JSON">http://code.google.com/p/google-http-java-client/wiki/JSON</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class MessagesPManDataPut extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String a1;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String a10;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String a2;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String a3;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String a4;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String a5;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String a6;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String a7;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String a8;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String a9;

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
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key("time_taken")
  private com.google.api.client.util.DateTime timeTaken;

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getA1() {
    return a1;
  }

  /**
   * @param a1 a1 or {@code null} for none
   */
  public MessagesPManDataPut setA1(java.lang.String a1) {
    this.a1 = a1;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getA10() {
    return a10;
  }

  /**
   * @param a10 a10 or {@code null} for none
   */
  public MessagesPManDataPut setA10(java.lang.String a10) {
    this.a10 = a10;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getA2() {
    return a2;
  }

  /**
   * @param a2 a2 or {@code null} for none
   */
  public MessagesPManDataPut setA2(java.lang.String a2) {
    this.a2 = a2;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getA3() {
    return a3;
  }

  /**
   * @param a3 a3 or {@code null} for none
   */
  public MessagesPManDataPut setA3(java.lang.String a3) {
    this.a3 = a3;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getA4() {
    return a4;
  }

  /**
   * @param a4 a4 or {@code null} for none
   */
  public MessagesPManDataPut setA4(java.lang.String a4) {
    this.a4 = a4;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getA5() {
    return a5;
  }

  /**
   * @param a5 a5 or {@code null} for none
   */
  public MessagesPManDataPut setA5(java.lang.String a5) {
    this.a5 = a5;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getA6() {
    return a6;
  }

  /**
   * @param a6 a6 or {@code null} for none
   */
  public MessagesPManDataPut setA6(java.lang.String a6) {
    this.a6 = a6;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getA7() {
    return a7;
  }

  /**
   * @param a7 a7 or {@code null} for none
   */
  public MessagesPManDataPut setA7(java.lang.String a7) {
    this.a7 = a7;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getA8() {
    return a8;
  }

  /**
   * @param a8 a8 or {@code null} for none
   */
  public MessagesPManDataPut setA8(java.lang.String a8) {
    this.a8 = a8;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getA9() {
    return a9;
  }

  /**
   * @param a9 a9 or {@code null} for none
   */
  public MessagesPManDataPut setA9(java.lang.String a9) {
    this.a9 = a9;
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
  public MessagesPManDataPut setBloodPressure(java.lang.String bloodPressure) {
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
  public MessagesPManDataPut setBodyTemp(java.lang.Double bodyTemp) {
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
  public MessagesPManDataPut setEmail(java.lang.String email) {
    this.email = email;
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
  public MessagesPManDataPut setTimeTaken(com.google.api.client.util.DateTime timeTaken) {
    this.timeTaken = timeTaken;
    return this;
  }

  @Override
  public MessagesPManDataPut set(String fieldName, Object value) {
    return (MessagesPManDataPut) super.set(fieldName, value);
  }

  @Override
  public MessagesPManDataPut clone() {
    return (MessagesPManDataPut) super.clone();
  }

}
